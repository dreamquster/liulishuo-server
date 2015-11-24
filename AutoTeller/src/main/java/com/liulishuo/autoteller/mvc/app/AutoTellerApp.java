package com.liulishuo.autoteller.mvc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Administrator on 2015/11/24 0024.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource({
        "classpath*:mvc-dispatcher-servlet.xml"
})
public class AutoTellerApp{

    public static void main(String[] args) {
        SpringApplication.run(AutoTellerApp.class, args);
    }
}
