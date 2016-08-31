package cloud.simple.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@RestController
public class DemoController{
	@Autowired
	RestTemplate restTemplate;
	@Value("${sms.userid}")
	String test;

	@RequestMapping("/hello")
	public String test(){
		return test;
	}
	@HystrixCommand(fallbackMethod = "demofail")
	@RequestMapping("/demo")
	public String demo(){//http://cloud-simple-service/hello
		return restTemplate.getForEntity("http://cloud-simple-service/hello",String.class).getBody();
	}
	
	public String demofail(){
		return "fail";
	}
}

