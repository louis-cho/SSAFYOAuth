package com.ssafy.authorization.stats.login.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.stats.login.model.LoginStats;

@Repository
public class LoginStatsRepository {

	private final ElasticsearchOperations elasticsearchOperations;

	@Autowired
	LoginStatsRepository(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	// 저장
	public void create(LoginStats loginStats) {
		elasticsearchOperations.save(loginStats);
	}

	// 삭제

	// 조회
	public List<LoginStats> fetch(String userId, String teamId) {

		System.out.println("userId >> " + userId);
		Criteria criteria = new Criteria("userId").is(UUID.fromString(userId));
		Query query = new CriteriaQuery(criteria);
		SearchHits<LoginStats> searchHIts = elasticsearchOperations.search(query, LoginStats.class);
		List<LoginStats> loginStatsList = searchHIts.getSearchHits().stream().map(SearchHit::getContent).toList();
		return loginStatsList;
	}
}