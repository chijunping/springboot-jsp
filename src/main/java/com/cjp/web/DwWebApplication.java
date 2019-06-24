package com.cjp.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Auther: junping.chi@luckincoffee.com
 * @Date: 2019/5/23 20:49
 * @Description:
 */

@SpringBootApplication
public class DwWebApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(DwWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DwWebApplication.class);
    }
}
