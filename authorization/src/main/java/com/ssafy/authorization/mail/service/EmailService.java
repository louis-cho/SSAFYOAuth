package com.ssafy.authorization.mail.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String emailSenderAddress;

	public String sendEmail(String userEmail){
		String codeForAuth = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		MimeMessage emailContent = emailSender.createMimeMessage();

		try {
			emailContent.setFrom(new InternetAddress(emailSenderAddress, "SSAFYAuth", "UTF-8"));
			emailContent.setRecipients(MimeMessage.RecipientType.TO, userEmail);
			emailContent.setSubject("SSAFYAuth 인증번호 안내");

			String body = "";
			body += "<h1>" + "인증번호 확인 후 이메일 인증을 완료해주세요." + "</h3>";
			body += "<h3>" + "인증 번호 : " + codeForAuth + "</h1>";
			// body += "<h3>" + "인증번호를 정확하게 입력해주세요." + "</h3>";
			//            body += "<h3>" + "위 인증번호의 유효시간은 30분 입니다." + "</h3>";
			body += "<h3>" + "감사합니다." + "</h3>";
			emailContent.setText(body, "UTF-8", "html");
		} catch (Exception e) {
			// 나중에 삭제해야함
			e.printStackTrace();
		}
		emailSender.send(emailContent);
		return codeForAuth;
	}

}
