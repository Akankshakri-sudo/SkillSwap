package com.skillswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SkillSwapApplication – the entry point of the entire application.
 *
 * @SpringBootApplication is a shortcut for three annotations:
 *   - @Configuration      : marks this class as a source of bean definitions
 *   - @EnableAutoConfiguration : tells Spring Boot to auto-configure
 *                               based on the JARs on the classpath
 *   - @ComponentScan      : scans this package (and sub-packages) for
 *                           @Component, @Service, @Repository, @Controller
 */
@SpringBootApplication
public class SkillSwapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillSwapApplication.class, args);
    }
}
