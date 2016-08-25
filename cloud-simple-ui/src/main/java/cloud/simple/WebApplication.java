/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */

package cloud.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients
public class WebApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebApplication.class, args);
	}
	
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	


}
