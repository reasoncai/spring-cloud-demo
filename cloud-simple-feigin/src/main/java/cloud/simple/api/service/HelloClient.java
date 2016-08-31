package cloud.simple.api.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netflix.ribbon.proxy.annotation.Hystrix;

@FeignClient(value="cloud-simple-service",fallback=HelloClientHystrix.class)
public interface HelloClient {
	@RequestMapping(path="/hello")
	String hello();
	
}
