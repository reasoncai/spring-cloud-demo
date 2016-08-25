package com.nineair.smsgateway.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nineair.smsgateway.domain.SmsBuffer;

public interface SmsBufferRepository extends JpaRepository<SmsBuffer, Long>{

	
	/**
	 * 获取未读的短信
	 * @return
	 */
	@Query(nativeQuery=true,value="SELECT id, mobiles, content, read_flag, send_time FROM sms_buffer WHERE read_flag = 0 and mobiles is not null and content is not null")
	public List<SmsBuffer> getUnRead();

}
