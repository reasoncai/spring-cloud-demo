package com.nineair.smsgateway.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nineair.smsgateway.domain.SmsReply;

public interface SmsReplyRepository extends JpaRepository<SmsReply, String>{
	/**
	 * 根据短信流水号和手机号码查找状态为已提交的短信
	 * @param msgid 	短信ID
	 * @param mobile	手机号码
	 * @return
	 */
	@Query(value="SELECT smid, smagent, smcontent, smsendtime, smstate, mobile ,msgid FROM sms_reply WHERE msgid=?1 AND mobile=?2 AND smstate = 0",nativeQuery=true)
	public List<SmsReply> findToUpdate(String msgid,String mobile);

}
