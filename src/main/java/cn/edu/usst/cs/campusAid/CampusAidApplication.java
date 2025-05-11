package cn.edu.usst.cs.campusAid;

import cn.edu.usst.cs.campusAid.config.SecurityProperties;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.concurrent.ExecutorService;

@MapperScan("cn.edu.usst.cs.campusAid.mapper.db") // 只扫描真正的数据库 Mapper
@SpringBootApplication
@EnableConfigurationProperties({SecurityProperties.class})
public class CampusAidApplication {

    public static void main(String[] args) {

        SpringApplication.run(CampusAidApplication.class, args);
    }


    private ExecutorService executorService;

    @Bean
    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = java.util.concurrent.Executors.newFixedThreadPool(10);
        }
        return executorService;
    }

    @Bean
    public UploadFileSystemService getUploadFileSystemService() {
        return new UploadFileSystemService() {
            final String uploadRootDir = "uploads/";

            @Override
            public File getUploadRootDir() {
                File dir = new File(uploadRootDir);
                if (!dir.exists() && !dir.mkdirs())
                    throw new CampusAidException("Failed to create upload directory");
                return dir;
            }

        };
    }


}
