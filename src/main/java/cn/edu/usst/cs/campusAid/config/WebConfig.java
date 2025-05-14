package cn.edu.usst.cs.campusAid.config;

import cn.edu.usst.cs.campusAid.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
        registry.addViewController("/forum/add_blog").setViewName("forward:/forum/add_blog.html");

        registry.addViewController("/").setViewName("forward:/auth/index.html");
        registry.addViewController("/register").setViewName("forward:/auth/register.html");
    }

    /**
     * 指定用户上传资源的路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authInterceptor)

//                论坛页面
                .addPathPatterns("/forum/**")

//                论坛后端支持
                .addPathPatterns("/api/forum/**")
        ;
    }
}