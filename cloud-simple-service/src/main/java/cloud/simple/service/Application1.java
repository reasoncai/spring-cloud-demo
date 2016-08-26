package cloud.simple.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
public class Application1 {
	public static void main(String[] args) {
		SpringApplication.run(Application1.class, args);
	}
}
	@RestController
	class DemoController{
		@Value("${sms.userid}")
		String test;
		@RequestMapping("hello")
		public String test(){
			return test+"okc33";
		}
	}

