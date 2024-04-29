package com.ssafy.resourceserver.mail.service;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	@Autowired
	private final JavaMailSender emailSender;

	@Autowired
	private final SpringTemplateEngine templateEngine;

	@Autowired
	private final RedisTemplate<String, String> redisTemplate;

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
		return userCode != null && userCode.equals(originCode);
	}
}
