package com.ssafy.authorization.stats.system.model;

public class Metric {
	private double cpuLoad;
	private double memoryUsage;

	public Metric(double cpuLoad, double memoryUsage) {
		this.cpuLoad = cpuLoad;
		this.memoryUsage = memoryUsage;
	}

	// Getter 메소드 추가
	public double getCpuLoad() {
		return cpuLoad;
	}

	public double getMemoryUsage() {
		return memoryUsage;
	}
}