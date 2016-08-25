package com.nineair.smsgateway.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
/**
 * 短信发送缓冲表实体类
 * @author cai
 *
 */
@Entity
public class SmsBuffer {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="sms")
	@SequenceGenerator(name="sms",sequenceName="SEQ_SMS_BUFFER_ID",allocationSize=1)
	private Long id;		//主键唯一标识
	private String mobiles; //手机号码串
	private String content;	//发送短信内容
	private Integer readFlag = 0; //读取标识0未读1已读
	private Date sendTime;	//发送时间
	
	
	public SmsBuffer() {
		super();
	}


	public SmsBuffer(Long id, String mobiles, String content, Integer readFlag, Date sendTime) {
		super();
		this.id = id;
		this.mobiles = mobiles;
		this.content = content;
		this.readFlag = readFlag;
		this.sendTime = sendTime;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getMobiles() {
		return mobiles;
	}


	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Integer getReadFlag() {
		return readFlag;
	}


	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}


	public Date getSendTime() {
		return sendTime;
	}


	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}


	@Override
	public String toString() {
		return "SmsBuffer [id=" + id + ", mobiles=" + mobiles + ", content=" + content + ", readFlag=" + readFlag
				+ ", sendTime=" + sendTime + "]";
	}
	
}
