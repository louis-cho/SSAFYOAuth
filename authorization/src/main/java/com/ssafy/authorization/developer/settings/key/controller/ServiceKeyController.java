package com.ssafy.authorization.developer.settings.key.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.authorization.developer.settings.key.service.ServiceKeyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/team/key")
@RequiredArgsConstructor
@Slf4j
public class ServiceKeyController {

	@Autowired
	private final ServiceKeyService serviceKeyService;



	@PostMapping("/claim")
	public void claim(){
		String serviceKey = serviceKeyService.createServiceKey(13L, 13L);
		log.info("발급 받은 키 값 : {}", serviceKey);
	}


	@PostMapping("/reclaim")
	public void reclaim(){
		String serviceKey = serviceKeyService.createServiceKey(13L, 13L);
		log.info("발급 받은 키 값 : {}", serviceKey);
	}
}
