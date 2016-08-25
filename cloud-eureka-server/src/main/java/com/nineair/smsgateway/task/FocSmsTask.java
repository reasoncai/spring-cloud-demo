package com.nineair.smsgateway.task;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nineair.smsgateway.dao.SmsBufferRepository;
import com.nineair.smsgateway.dao.SmsReplyRepository;
import com.nineair.smsgateway.domain.SmsBuffer;
import com.nineair.smsgateway.domain.SmsReply;
import com.nineair.smsgateway.service.SmsService;

/**
 * FOC短信定时任务
 * 
 * @author cai
 *
 */
@Component
public class FocSmsTask {
	private static final Logger logger = LoggerFactory.getLogger(FocSmsTask.class);
	@Autowired
	SmsService smsService;
	@Autowired
	SmsBufferRepository smsBufferRepository;
	@Autowired
	SmsReplyRepository smsReplyRepository;

	/**
	 * 定时任务：发送短信
	 * 
	 * @throws InterruptedException
	 */
	/**
	 * @throws InterruptedException
	 */
	@Scheduled(fixedRate = 30000)//每30秒执行一次
	public void sendMessage() throws InterruptedException {
		logger.info("【定时任务--发送短信】：开始");
		// 读取需要发送的短信
		List<SmsBuffer> unReadList = smsBufferRepository.getUnRead();
		if(unReadList ==null || unReadList.size()<1){
			logger.info("【定时任务--发送短信】：没有需要发送的短信，直接退出");
			return;
		}
		logger.info("准备发送的信息："+unReadList.toString());
		for (SmsBuffer smsBuffer : unReadList) {
			String content = smsBuffer.getContent();
			String mobiles = smsBuffer.getMobiles();
			String msgid = String.valueOf(smsBuffer.getId());
			//去掉号码串中的所有空格
			mobiles = StringUtils.deleteWhitespace(mobiles);
			String[] mobilesarr = mobiles.split("/");
			mobiles = mobiles.replace("/", ",");
			
			//调用短信接口发送短信
			smsService.send(mobiles, content, msgid);
			
			// 标记已读
			smsBuffer.setReadFlag(1);
			smsBufferRepository.save(smsBuffer);


			// 记录已提交到短信发送记录表sms_reply
			for (int i = 0; i < mobilesarr.length; i++) {
				SmsReply smsreply = new SmsReply();
				smsreply.setMobile(mobilesarr[i]);
				smsreply.setSmcontent(content);
				smsreply.setSmstate(0);
				smsreply.setMsgid(msgid);
				smsReplyRepository.save(smsreply);
			}
		
			}
		// 休眠10s再调用查询短信状态接口
		Thread.sleep(10000);
		// 查询并回写短信发送状态
		logger.info("【定时任务--发送短信】：查询并回写短信发送状态");
		smsService.queryAndRecord();
		logger.info("【定时任务--发送短信】：完成");
	}

	/**
	 * 定时任务：查询并回写短信发送状态
	 */
	@Scheduled(fixedRate = 300000)//每5分钟执行一次
	public void getMessageStatus() {
		logger.info("【定时任务--查询并回写短信发送状态】：开始");
		//查询短信发送状态并更新短信记录表
		smsService.queryAndRecord();
		logger.info("【定时任务--查询并回写短信发送状态】：完成");
	}
	
	
}
