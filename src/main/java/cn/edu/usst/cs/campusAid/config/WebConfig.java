package cn.edu.usst.cs.campusAid.config;

import cn.edu.usst.cs.campusAid.interceptor.api.AuthInterceptor;
import cn.edu.usst.cs.campusAid.interceptor.api.BanApiInterceptor;
import cn.edu.usst.cs.campusAid.interceptor.view.TemplatedErrandInterceptor;
import cn.edu.usst.cs.campusAid.interceptor.view.TemplatedForumInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final BanApiInterceptor banApiInterceptor;


    private TemplatedErrandInterceptor templatedErrandInterceptor;
    private TemplatedForumInterceptor templatedForumInterceptor;


    public WebConfig(
                     AuthInterceptor authInterceptor,
                     BanApiInterceptor banApiInterceptor,
                     TemplatedErrandInterceptor templatedErrandInterceptor,
                     TemplatedForumInterceptor templatedForumInterceptor

    ) {

        this.authInterceptor = authInterceptor;
        this.banApiInterceptor = banApiInterceptor;
        this.templatedErrandInterceptor = templatedErrandInterceptor;
        this.templatedForumInterceptor = templatedForumInterceptor;
    }

    /**
     * campusaid-web/src/main/resources/static里的文件若需要在公网访问，需要在这里注册
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/forum").setViewName("forward:/forum/forum.html");
        registry.addViewController("/forum/post").setViewName("forward:/forum/post.html");
        registry.addViewController("/forum/information").setViewName("forward:/forum/information.html");
        registry.addViewController("/forum/blog").setViewName("forward:/forum/blog.html");
        registry.addViewController("/forum/reply").setViewName("forward:/forum/reply.html");

        registry.addViewController("/charge").setViewName("forward:/charge/charge.html");

        registry.addViewController("/admin").setViewName("forward:/admin/admin.html");

        registry.addViewController("/").setViewName("forward:/auth/index.html");
        registry.addViewController("/register").setViewName("forward:/auth/register.html");
        registry.addViewController("/complaint").setViewName("forward:/complaint/complaint.html");
        registry.addViewController("/admin/complaint").setViewName("forward:/complaint/complaint-admin.html");


    }

    /**
     * 指定用户上传资源的路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的访问路径
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // 配置 shop 目录下的静态文件访问
        registry
                .addResourceHandler("/shop/**")
                .addResourceLocations("classpath:/static/shop/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截器
        registry
                .addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login/**")
                .excludePathPatterns("/api/register/**")
        ;

        registry.addInterceptor(banApiInterceptor)
                .addPathPatterns("/api/**")
        ;
        registry.addInterceptor(templatedErrandInterceptor)
                .addPathPatterns("/errand")
        ;
        registry.addInterceptor(templatedForumInterceptor)
                .addPathPatterns("/forum")
        ;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

}
