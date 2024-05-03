package com.ssafy.authorization.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.authorization.mail.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberFacade {

	private final MemberService memberService;

	private final CustomMemberManager customMemberManager;

	private final EmailService emailService;

	@Transactional
	public Map<String, Boolean> forgotPassword(String userEmail) {
		Map<String, Boolean> response = new HashMap<>();
		if(customMemberManager.userExists(userEmail)) {
			try {
				String tmpPassword = emailService.sendTmpPassword(userEmail);
				customMemberManager.resetTmpPassword(userEmail, tmpPassword);
				response.put("result", true);
				return response;
			} catch (Exception e) {
				response.put("result", false);
				return response;
			}
		} else { // 존재하지 않는 회원
			response.put("result", false);
			return response;
		}
	}
}
