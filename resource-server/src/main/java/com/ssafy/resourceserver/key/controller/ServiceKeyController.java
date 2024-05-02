package com.ssafy.resourceserver.key.controller;

import com.ssafy.resourceserver.key.service.ServiceKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
