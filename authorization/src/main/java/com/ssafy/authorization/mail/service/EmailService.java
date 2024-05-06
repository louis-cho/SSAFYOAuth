package com.ssafy.authorization.mail.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Value("${spring.mail.username}")
	private String emailSenderAddress;

	public void sendEmail(String userEmail) throws Exception {
		String authCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		MimeMessage emailContent = emailSender.createMimeMessage();
		MimeMessageHelper helper;

		helper = new MimeMessageHelper(emailContent, true);
		helper.setSubject("SSAFYAuth 인증번호 안내");
		helper.setFrom(new InternetAddress(emailSenderAddress, "SSAFYAuth", "UTF-8"));
		helper.setTo(userEmail);

		Context context = new Context();
		context.setVariable("authCode", authCode);

		String html = templateEngine.process("pages/mail", context);
		helper.setText(html, true);

		emailSender.send(emailContent);
		String key = "AuthCode : " + userEmail;
		redisTemplate.opsForValue().set(key, authCode);
		redisTemplate.expire(key, 180, TimeUnit.SECONDS);
	}

	public boolean certify(String userEmail, String userCode) {
		String key = "AuthCode : " + userEmail;
		String originCode = redisTemplate.opsForValue().get(key);
		// 레디스에 있는지 확인후 해야함...
		if (userCode != null && userCode.equals(originCode)) {
			redisTemplate.delete(key);
			redisTemplate.opsForValue().set(userEmail, "true");
			redisTemplate.expire(key, 180, TimeUnit.SECONDS);
			return true;
		} else {
			return false;
		}
	}

	public String sendTmpPassword(String userEmail) throws Exception {
		String tmpPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		MimeMessage emailContent = emailSender.createMimeMessage();
		MimeMessageHelper helper;

		helper = new MimeMessageHelper(emailContent, true);
		helper.setSubject("SSAFYAuth 인증번호 안내");
		helper.setFrom(new InternetAddress(emailSenderAddress, "SSAFYAuth", "UTF-8"));
		helper.setTo(userEmail);

		Context context = new Context();
		context.setVariable("tmpPassword", tmpPassword);

		String html = templateEngine.process("pages/tmpmail", context);
		helper.setText(html, true);

		emailSender.send(emailContent);
		return tmpPassword;
	}
}
