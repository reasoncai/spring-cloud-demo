package com.nineair.smsgateway.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 *短信发送记录表 实体类
 * @author cai
 *
 */
@Entity
public class SmsReply {
	@Id
	@GenericGenerator(name="id",strategy="uuid")
	@GeneratedValue(generator="id")
	private String smid;	   	//主键唯一标识
	private String smagent;		//代理商发送通道记录
	private String smcontent;	//发送短信内容
	private Date smsendtime;	//发送时间
	private Integer smstate;	//发送状态0 已提交1发送成功2 发送失败
	private String mobile;		//手机号码
	private String msgid;		//流水号
	public SmsReply() {
		super();
	}
	
	public SmsReply(String smid, String smagent, String smcontent, Date smsendtime, Integer smstate, String mobile,
			String msgid) {
		super();
		this.smid = smid;
		this.smagent = smagent;
		this.smcontent = smcontent;
		this.smsendtime = smsendtime;
		this.smstate = smstate;
		this.mobile = mobile;
		this.msgid = msgid;
	}

	public String getSmid() {
		return smid;
	}
	public void setSmid(String smid) {
		this.smid = smid;
	}
	public String getSmagent() {
		return smagent;
	}
	public void setSmagent(String smagent) {
		this.smagent = smagent;
	}
	public String getSmcontent() {
		return smcontent;
	}
	public void setSmcontent(String smcontent) {
		this.smcontent = smcontent;
	}
	public Date getSmsendtime() {
		return smsendtime;
	}
	public void setSmsendtime(Date smsendtime) {
		this.smsendtime = smsendtime;
	}
	public Integer getSmstate() {
		return smstate;
	}
	public void setSmstate(Integer smstate) {
		this.smstate = smstate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	@Override
	public String toString() {
		return "SmsReply [smid=" + smid + ", smagent=" + smagent + ", smcontent=" + smcontent + ", smsendtime="
				+ smsendtime + ", smstate=" + smstate + ", mobile=" + mobile + ", msgid=" + msgid + "]";
	}

	
}
