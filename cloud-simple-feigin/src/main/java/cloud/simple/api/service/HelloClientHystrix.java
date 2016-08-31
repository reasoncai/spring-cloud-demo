package cloud.simple.api.service;

import org.springframework.stereotype.Component;

import com.netflix.ribbon.proxy.annotation.Hystrix;

@Component
public class HelloClientHystrix implements HelloClient{
	
	@Override
	public String hello() {
		return "error";
	}

}
