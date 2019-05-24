package com.fukun.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.fukun.user.**.client"})
@ServletComponentScan
@EnableEurekaClient
public class FukunDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FukunDemoApplication.class, args);
	}

}
