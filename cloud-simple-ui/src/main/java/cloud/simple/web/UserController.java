/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */

package cloud.simple.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.simple.service.UserService;



@RestController
public class UserController {
		
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/demo")
	public String readUserInfo(){
		System.out.println(userService.readUserInfo());
		return userService.readUserInfo();
		 
	}
}
