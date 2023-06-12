package com.panghu.housemanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.panghu.housemanage.dao")
public class ManageBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageBooksApplication.class, args);
    }
}
