package com.ssafy.authorization.cleanup;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssafy.authorization.stats.login.model.LoginStats;

@Component
public class ElasticCleanUpTask {

	private final ElasticsearchOperations elasticsearchOperations;

	@Autowired
	ElasticCleanUpTask(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@Scheduled(cron = "0 0 1 * * ?") // 매달 첫째 날 새벽 1시에 실행
	public void cleanupOldData() {
		// 현재 날짜에서 한 달을 빼서 계산
		Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);

		// 쿼리 작성
		String query = String.format(
			"{\"query\": {\"range\": {\"createdAt\": {\"lt\": \"%s\"}}}}",
			oneMonthAgo.toString()
		);

		// 쿼리 실행
		elasticsearchOperations.delete(query, LoginStats.class, IndexCoordinates.of("login_stats"));
		System.out.println("1달 이상된 데이터 삭제 완료");
	}
}
