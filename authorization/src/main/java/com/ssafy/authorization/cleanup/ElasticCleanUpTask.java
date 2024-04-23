package com.ssafy.authorization.cleanup;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
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

	// @Scheduled(cron = "0 0 1 * * ?") // 매달 첫째 날 새벽 1시에 실행
	@Scheduled(cron = "*/1 * * * * ?") // 매 초마다 실행

	public void cleanupOldData() {
		// 현재 날짜에서 한 달을 빼서 계산
		Instant oneMonthAgo = Instant.now().minus(3, ChronoUnit.DAYS);
		// Query 객체 생성
		Criteria criteria = new Criteria("createdAt").lessThan(oneMonthAgo);
		Query query = new CriteriaQuery(criteria);
		// 쿼리 실행을 통한 데이터 삭제
		elasticsearchOperations.delete(query, LoginStats.class, IndexCoordinates.of("login_stats"));
	}
}
