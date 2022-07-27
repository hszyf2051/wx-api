package com.yif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author admin
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiApplication {
    public static void main(String[] args) {
        System.out.println("启动");
        SpringApplication.run(ApiApplication.class,args);
    }
}
