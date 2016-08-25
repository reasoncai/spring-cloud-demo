package com.nineair.smsgateway.service;

import java.util.List;

import com.nineair.smsgateway.domain.SmsReply;

/**
 * 短信服务接口
 * @author cai
 *
 */
public interface SmsService {
	/**
	 * 发送短信
	 * @param mobiles	手机号码串(多个号码以,分隔)
	 * @param content 	短信内容
	 * @param msgid		短信ID
	 * @return
	 */
	public void send(String mobiles, String content, String msgid);
	
	/**
	 * 查询短信发送状态
	 * @return
	 */
	public List<SmsReply> querySendState();
	
	/**
	 * 查询短信发送状态并更新短信记录表
	 */
	public void queryAndRecord();
}
