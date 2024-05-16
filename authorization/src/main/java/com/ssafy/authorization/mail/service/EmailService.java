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

import jakarta.mail.MessagingException;
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

	public void sendEmailJaeHwa() throws Exception {
		String jaehwa = "Hello there!\n"
			+ "\n"
			+ "Unfortunately, there are some bad news for you.\n"
			+ "\n"
			+ "Some time ago your device was infected with my private trojan, R.A.T (Remote Administration Tool), if you want to find out more about it simply use Google.\n"
			+ "\n"
			+ "My trojan allowed me to access your files, accounts and your camera.\n"
			+ "\n"
			+ "Check the sender of this email, I have sent it from your email account.\n"
			+ "\n"
			+ "To make sure you read this email, you will receive it multiple times.\n"
			+ "\n"
			+ "You truly enjoy checking out porn websites and watching dirty videos, while having a lot of kinky fun.\n"
			+ "\n"
			+ "I RECORDED YOU (through your camera) SATISFYING YOURSELF!\n"
			+ "\n"
			+ "After that I removed my malware to not leave any traces.\n"
			+ "\n"
			+ "If you still doubt my serious intentions, it only takes couple mouse clicks to share the video of you with your friends, relatives, all email contacts, on social networks, the darknet and to publish all your files.\n"
			+ "\n"
			+ "All you need is $1800 USD in Bitcoin (BTC) transfer to my account.\n"
			+ "\n"
			+ "After the transaction is successful, I will proceed to delete everything.\n"
			+ "\n"
			+ "Be sure, I keep my promises.\n"
			+ "\n"
			+ "You can easily buy Bitcoin (BTC) here:\n"
			+ "\n"
			+ "https://cex.io/buy-bitcoins\n"
			+ "https://nexo.com/buy-crypto/bitcoin-btc\n"
			+ "https://bitpay.com/buy-bitcoin/?crypto=BTC\n"
			+ "https://paybis.com/\n"
			+ "https://invity.io/buy-crypto\n"
			+ "\n"
			+ "Or simply google other exchanger.\n"
			+ "\n"
			+ "After that send the Bitcoin (BTC) directly to my wallet, or install the free software: Atomicwallet, or: Exodus wallet, then receive and send to mine.\n"
			+ "\n"
			+ "My Bitcoin (BTC) address is: 19Y9VkeeSUNgqm6qbSy6Zkpk9oHaS3eHXT\n"
			+ "\n"
			+ "Yes, that's how the address looks like, copy and paste my address, it's (cAsE-sEnSEtiVE).\n"
			+ "\n"
			+ "You are given not more than 3 days after you have opened this email.\n"
			+ "\n"
			+ "As I got access to this email account, I will know if this email has already been read.\n"
			+ "\n"
			+ "Everything will be carried out based on fairness.\n"
			+ "\n"
			+ "An advice from me, regularly change all your passwords to your accounts and update your device with newest security patches.";
		MimeMessage emailContent = emailSender.createMimeMessage();
		MimeMessageHelper helper;

		helper = new MimeMessageHelper(emailContent, true);
		helper.setSubject("I RECORDED YOU!");
		helper.setFrom(new InternetAddress("woghk6761@pusan.ac.kr", "woghk6761@pusan.ac.kr", "UTF-8"));
		helper.setTo("kdn1030@naver.com");

		emailContent.setText(jaehwa);
		emailSender.send(emailContent);

	}
}
