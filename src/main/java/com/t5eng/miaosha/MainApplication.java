package com.t5eng.miaosha;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan(basePackages = "com.t5eng.miaosha")

@SpringBootApplication //等价于@Configuration @EnableAutoConfiguration @ComponentScan 三个配置
public class MainApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);

    }
}
