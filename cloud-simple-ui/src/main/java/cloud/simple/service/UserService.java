/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */
package cloud.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserService {
	 @Autowired	 
	 RestTemplate restTemplate;
	
	 
	 final String SERVICE_NAME="cloud-simple-service";
	 
	 public String readUserInfo() {
		 	System.out.println("http://"+SERVICE_NAME+"/hello");
	        return restTemplate.getForEntity("http://"+SERVICE_NAME+"/hello", String.class).getBody();
		 //return feignUserService.readUserInfo();
	 }	 
	
}
