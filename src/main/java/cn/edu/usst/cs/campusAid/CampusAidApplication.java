package cn.edu.usst.cs.campusAid;

import cn.edu.usst.cs.campusAid.config.SecurityProperties;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@MapperScan(
        basePackages =
                {
                        "cn.edu.usst.cs.campusAid.mapper.db",
                        "cn.edu.usst.cs.campusAid.mapper.db.forum",
                        "cn.edu.usst.cs.campusAid.mapper.db.shop",
                        "cn.edu.usst.cs.campusAid.mapper.db.errand",
                        "cn.edu.usst.cs.campusAid.mapper.db.complaint"
                }
) // 只扫描真正的数据库 Mapper
@SpringBootApplication


@EnableConfigurationProperties({SecurityProperties.class})
@ConfigurationProperties()
public class CampusAidApplication {


    @Value("${campus_aid.delay_threads}")
    private int threads;
    private ScheduledExecutorService executorService;

    public static void main(String[] args) {

        SpringApplication.run(CampusAidApplication.class, args);
    }



    @Bean
    public ScheduledExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newScheduledThreadPool(threads);
        }
        return executorService;
    }



}
