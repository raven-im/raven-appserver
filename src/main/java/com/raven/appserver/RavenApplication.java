package com.raven.appserver;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@NacosPropertySource(dataId = "raven-appserver.yaml")
@ComponentScan(basePackages = {"com.raven.appserver"})
@MapperScan(basePackages = {"com.raven.appserver.*.mapper"})
public class RavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(RavenApplication.class, args);
    }
}
