package com.smile.backend;

import com.smile.backend.controller.WebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.smile.backend.mapper")
@EnableTransactionManagement
public class SmileApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SmileApplication.class, args);
        WebSocketServer.setApplicationContext(context);
    }
}
