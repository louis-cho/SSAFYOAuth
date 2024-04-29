package com.ssafy.authorization.stats.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ssafy.authorization.stats.system.model.Metric;

@Service
public class MetricBroadcastService {
	private SimpMessagingTemplate template;
	private SystemMetricsService systemMetricsService;

	@Autowired
	MetricBroadcastService(SimpMessagingTemplate template, SystemMetricsService systemMetricsService) {
		this.template = template;
		this.systemMetricsService = systemMetricsService;
	}

	@Scheduled(fixedRate = 1000)
	public void broadcastMetrics() {
		template.convertAndSend("/topic/metrics",
			new Metric(systemMetricsService.getCpuLoad(), systemMetricsService.getMemoryUsage()));
	}
}
