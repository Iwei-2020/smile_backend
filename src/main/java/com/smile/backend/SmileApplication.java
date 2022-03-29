package com.smile.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.smile.backend.mapper")
public class SmileApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmileApplication.class, args);
    }

}
