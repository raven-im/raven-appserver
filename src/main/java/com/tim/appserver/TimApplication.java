package com.tim.appserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.tim.appserver"})
@MapperScan(basePackages = {"com.tim.appserver.*.mapper"})
public class TimApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimApplication.class, args);
    }
}
