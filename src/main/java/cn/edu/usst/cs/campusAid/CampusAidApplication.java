package cn.edu.usst.cs.campusAid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;

@SpringBootApplication
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
}
