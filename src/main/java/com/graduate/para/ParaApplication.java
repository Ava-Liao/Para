package com.graduate.para;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.graduate.para.mapper")
public class ParaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParaApplication.class, args);
    }

}
