package cloud.simple.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.simple.api.service.HelloClient;

@RestController
public class DemoController{
	@Autowired
	HelloClient helloClient;
	@RequestMapping("/demo")
	public String test(){
		return helloClient.hello();
	}
	
	
}

