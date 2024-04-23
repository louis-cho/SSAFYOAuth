package com.ssafy.authorization.stats.system.service;

import java.lang.management.ManagementFactory;

import org.springframework.stereotype.Service;

import com.sun.management.OperatingSystemMXBean;

@Service
public class SystemMetricsService {
	private final OperatingSystemMXBean osBean;

	SystemMetricsService() {
		this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	}

	public double getCpuLoad() {
		return osBean.getCpuLoad() * 100;
	}

	public double getMemoryUsage() {
		return (1 - (double)osBean.getFreeMemorySize() / osBean.getTotalMemorySize()) * 100;
	}
}