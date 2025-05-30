package com.wuli.badminton;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.wuli.badminton.dao")
@EnableScheduling
public class BadmintonApplication {
    public static void main(String[] args) {
        SpringApplication.run(BadmintonApplication.class, args);
    }

}
