package com.ssafy.authorization.stats.login.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.authorization.stats.login.model.LoginStats;
import com.ssafy.authorization.stats.login.model.vo.LoginStatsFetchRequestVO;
import com.ssafy.authorization.stats.login.service.LoginStatsService;
import com.sun.management.OperatingSystemMXBean;

@RestController
@RequestMapping("/api/ttt")
public class LoginStatsTestController {

	LoginStatsService loginStatsService;

	@Autowired
	LoginStatsTestController(LoginStatsService loginStatsService) {
		this.loginStatsService = loginStatsService;
	}

	@PostMapping("/save")
	public String testSave(@RequestBody JsonNode requestBody) {
		UUID temaId = UUID.fromString(requestBody.get("teamId").asText());
		UUID userId = UUID.fromString(requestBody.get("userId").asText());

		Instant createdAt = Instant.now();

		LoginStats loginStats = new LoginStats(temaId.toString(), userId.toString(), createdAt);
		loginStatsService.save(loginStats);

		return "finished";

	}

	@PostMapping("/fetch")
	public String testFetch(@RequestBody JsonNode requestBody) {

		LoginStatsFetchRequestVO requestVO = new LoginStatsFetchRequestVO(requestBody);
		Pageable pageable = null;
		if (requestBody.get("page") != null & requestBody.get("size") != null) {
			pageable = PageRequest.of(requestBody.get("page").asInt(0), requestBody.get("size").asInt(30),
				Sort.by("createdAt").descending());
		}

		List<LoginStats> list = null;
		try {
			list = loginStatsService.fetch(requestVO, pageable);
		} catch (Exception e) {
			return null;
		}
		StringBuilder ret = new StringBuilder();
		for (LoginStats loginStats : list) {
			ret.append(loginStats.toString());
		}
		return ret.toString();
	}

	@PostMapping("/test")
	public void testResource() {
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

		// 시스템의 총 메모리 양
		long totalMemory = heapMemoryUsage.getMax();
		// 현재 사용 중인 메모리 양
		long usedMemory = heapMemoryUsage.getUsed();

		// 남은 메모리 양
		long remainingMemory = totalMemory - usedMemory;

		// 메모리 사용량의 퍼센티지 계산
		double usedMemoryPercentage = ((double)usedMemory / totalMemory) * 100;
		double remainingMemoryPercentage = ((double)remainingMemory / totalMemory) * 100;

		System.out.println("사용 중인 메모리 퍼센티지: " + usedMemoryPercentage + "%");
		System.out.println("남은 메모리 퍼센티지: " + remainingMemoryPercentage + "%");

		OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

		// CPU 사용률 가져오기 (0.0 ~ 1.0 사이의 값)
		double cpuUsage = osBean.getCpuLoad();

		// CPU 사용률을 백분율로 변환하여 출력
		double cpuUsagePercentage = cpuUsage * 100;
		System.out.println("CPU 사용률: " + cpuUsagePercentage + "%");

		// 가용한 프로세서 수 출력

	}

	@PostMapping("/fetch-all")
	public String testAllFetch(@RequestBody JsonNode requestBody) {
		LoginStatsFetchRequestVO requestVO = new LoginStatsFetchRequestVO(requestBody);
		Pageable pageable = null;
		if (requestBody.get("page") != null & requestBody.get("size") != null) {
			pageable = PageRequest.of(requestBody.get("page").asInt(0), requestBody.get("size").asInt(30),
				Sort.by("createdAt").descending());
		}

		List<LoginStats> list = null;
		try {
			list = loginStatsService.fetch(requestVO, pageable);
		} catch (Exception e) {
			return null;
		}
		StringBuilder ret = new StringBuilder();
		for (LoginStats loginStats : list) {
			ret.append(loginStats.toString());
		}
		System.out.println(ret);
		return ret.toString();
	}
}
