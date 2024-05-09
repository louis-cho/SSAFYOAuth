package com.ssafy.authorization.stats.system.service;

import java.lang.management.ManagementFactory;

import com.ssafy.authorization.stats.system.model.Metric;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sun.management.OperatingSystemMXBean;

@Service
public class SystemMetricsService {
	private final OperatingSystemMXBean osBean;

	@Setter
	@Getter
	private double cpuLoad = 0.0;

	@Setter
	@Getter
	private double memoryLoad = 0.0;

	SystemMetricsService() {
		this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	}

	@Scheduled(fixedRate = 1000)
	private void calculateMetrics() {
		this.cpuLoad = osBean.getCpuLoad() * 100;
		this.memoryLoad = (1 - (double)osBean.getFreeMemorySize() / osBean.getTotalMemorySize()) * 100;
	}

}