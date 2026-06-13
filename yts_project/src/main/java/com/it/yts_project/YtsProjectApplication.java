package com.it.yts_project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.it.yts_project.mapper")
public class YtsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtsProjectApplication.class, args);
    }

}
