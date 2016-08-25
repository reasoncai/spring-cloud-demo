package com.nineair.smsgateway.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nineair.smsgateway.dao.SmsReplyRepository;
import com.nineair.smsgateway.domain.SmsReply;
import com.nineair.smsgateway.service.SmsService;

/**
 * 短信服务实现类
 * 
 * @author cai
 *
 */
@Service
public class SmsServiceImpl implements SmsService {
	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
	@Value("${sms.userid}")
	private String userId;
	@Value("${sms.password}")
	private String password;
	@Value("${sms.query.url}")
	private String queryUrl;
	@Value("${sms.send.url}")
	private String sendUrl;
	@Autowired
	SmsReplyRepository smsReplyRepository;

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码串(多个号码以,分隔)
	 * @param content
	 *            内容
	 * @param msgid
	 *            信息ID
	 * @return
	 */
	@Override
	public void send(String mobiles, String content, String msgid) {
		String[] mobilesarr = new String[] {mobiles};
		// 如果超过100个电话号码，分组发送
		if (mobiles.split(",").length > 100) {
			logger.warn("超过100个电话号码,进行分组："+mobiles);
			mobilesarr = spiltMobiles(mobiles);
		}
		for (String mobile : mobilesarr) {
			int count = mobile.split(",").length;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(sendUrl);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("userId", userId));
			nvps.add(new BasicNameValuePair("password", password));
			nvps.add(new BasicNameValuePair("pszMobis", mobile));
			nvps.add(new BasicNameValuePair("pszMsg", content));
			nvps.add(new BasicNameValuePair("iMobiCount", String.valueOf(count)));
			nvps.add(new BasicNameValuePair("MsgId", msgid));
			logger.info("发送短信参数：" + nvps.toString());
			CloseableHttpResponse response = null;
			try {
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(nvps, "utf-8");
				httpPost.setEntity(ent);
				response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				SAXReader saxReader = new SAXReader();
				Document document = saxReader.read(inputStream);
				String result = document.getRootElement().getText();
				EntityUtils.consume(entity);
				if (result.length() < 10) {
					logger.error("短信接口报错，错误码为：" + result);
				}
			} catch (Exception e) {
				logger.error("发送短信异常", e);
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("发送短信异常", e);
				}
			}
		}

	}

	/**
	 * 查询短信发送状态
	 * 
	 * @return
	 */
	@Override
	public List<SmsReply> querySendState() {
		List<SmsReply> list = new ArrayList<SmsReply>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(queryUrl);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userId", userId));
		nvps.add(new BasicNameValuePair("password", password));
		nvps.add(new BasicNameValuePair("iReqType", "0"));
		CloseableHttpResponse response = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(nvps, "utf-8");
			httpPost.setEntity(ent);
			response = httpclient.execute(httpPost);
			// 获取并解析响应的xml
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(inputStream);
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements("string");
			for (Element element : elements) {
				String text = element.getText();
				String[] info = text.split(",");
				SmsReply smsReplyTable = new SmsReply();
				smsReplyTable.setSmsendtime(sdf.parse(info[1]));
				smsReplyTable.setSmagent(info[3]);
				smsReplyTable.setMobile(info[4]);
				smsReplyTable.setMsgid(info[5]);
				// 判断发送状态
				if ("DELIVRD".equals(info[info.length - 1])) {
					// 发送成功
					smsReplyTable.setSmstate(1);
				} else {
					// 发送失败
					smsReplyTable.setSmstate(2);
				}
				list.add(smsReplyTable);
			}
			logger.info("查询结果集："+list.toString());
			EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error("查询短信发送状态接口异常", e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				logger.error("查询短信发送状态接口异常", e);
			}
		}

		return list;
	}

	/**
	 * 查询短信发送状态并更新短信记录表
	 */
	@Override
	public void queryAndRecord() {
		// 查询短信发送状态
		List<SmsReply> stateList = this.querySendState();
		for (SmsReply smsReplyTable : stateList) {
			// 短信发送状态信息
			String msgid = smsReplyTable.getMsgid();
			String mobile = smsReplyTable.getMobile();
			Date smsendtime = smsReplyTable.getSmsendtime();
			Integer smstate = smsReplyTable.getSmstate();
			String smagent = smsReplyTable.getSmagent();
			// 查找本地对应的记录
			List<SmsReply> list = smsReplyRepository.findToUpdate(msgid, mobile);
			if (list != null && list.size() > 0) {
				SmsReply smsReply = list.get(0);
				smsReply.setSmsendtime(smsendtime);
				smsReply.setSmstate(smstate);
				smsReply.setSmagent(smagent);
				// 将查询到的状态更新到本地短信发送记录表
				smsReplyRepository.save(smsReply);
			}
		}
	}

	/**
	 * 分割超过100个电话号码
	 * 
	 * @param mobiles
	 * @return
	 */
	private String[] spiltMobiles(String mobiles) {
		String[] mobilesarr = mobiles.split(",");
		// 划分大小
		int size = 100;
		// 划分组数
		int number = mobilesarr.length / size + 1;
		String result = "";
		int index = 0;
		for (int i = 0; i < number; i++) {
			int ordinalIndexOf = StringUtils.ordinalIndexOf(mobiles, ",", size * (i + 1));
			//找不到，即到了最后
			if (ordinalIndexOf == -1) {
				result = result + StringUtils.substring(mobiles, index);
				break;
			}
			String substring = StringUtils.substring(mobiles, index, ordinalIndexOf);
			index = ordinalIndexOf + 1;
			result = result + substring + "|";
		}
		return result.split("\\|");
	}

}
