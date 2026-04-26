package com.dj.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dj.mall.mapper")
public class DjMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(DjMallApplication.class, args);
    }

}
