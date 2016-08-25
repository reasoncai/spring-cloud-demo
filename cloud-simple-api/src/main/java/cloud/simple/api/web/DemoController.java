package cloud.simple.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
	
	@RequestMapping("/demo")
	public String demo(){//http://cloud-simple-service/hello
		return restTemplate.getForEntity("http://cloud-simple-service/hello",String.class).getBody();
	}
}

