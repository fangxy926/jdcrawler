package com.yangman.crawler.jdcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 使用定时任务，需要先开启定时任务，添加@EnableScheduling注解
@EnableScheduling
public class JdcrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdcrawlerApplication.class, args);
    }

}
