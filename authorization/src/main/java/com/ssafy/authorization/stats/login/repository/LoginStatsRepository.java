package com.ssafy.authorization.stats.login.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.stats.login.model.LoginStats;
import com.ssafy.authorization.stats.login.model.VO.LoginStatsFetchRequestVO;

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
	public List<LoginStats> fetch(LoginStatsFetchRequestVO requestVO, Pageable pageable) {
		// 필드 값들을 추출합니다.
		UUID userId = requestVO.getUserId();
		UUID teamId = requestVO.getTeamId();
		Instant startTime = requestVO.getStartTime();
		Instant endTime = requestVO.getEndTime();
		Boolean success = requestVO.getSuccess();

		List<Criteria> queryList = new ArrayList<>();

		// userId가 존재하면 userId로 검색합니다.
		if (userId != null) {
			queryList.add(new Criteria("userId").is(userId));
		}

		// teamId가 존재하면 teamId로 검색합니다.
		if (teamId != null) {
			queryList.add(new Criteria("teamId").is(teamId));
		}

		// startTime과 endTime이 존재하면 범위로 검색합니다.
		if (startTime != null && endTime != null) {
			queryList.add(new Criteria("createdAt").between(startTime, endTime.plus(1, ChronoUnit.DAYS)));
		} else if (startTime != null) {
			queryList.add(new Criteria("createdAt").greaterThanEqual(startTime));
		} else if (endTime != null) {
			queryList.add(new Criteria("createdAt").lessThanEqual(endTime.plus(1, ChronoUnit.DAYS)));
		}

		// success가 존재하면 success 여부로 검색합니다.
		if (success != null) {
			queryList.add(new Criteria("success").is(success));
		}

		Criteria criteria = queryList.getFirst();
		for (int i = 1; i < queryList.size(); i++) {
			criteria.and(queryList.get(i));
		}

		if (criteria == null) {
			criteria = new Criteria();
		}
		// 생성된 Criteria를 이용하여 Query를 생성합니다.
		Query query = new CriteriaQuery(criteria);
		if (pageable != null) {
			query.setPageable(pageable);
		}
		// Elasticsearch에서 검색을 수행합니다.
		SearchHits<LoginStats> searchHits = elasticsearchOperations.search(query, LoginStats.class);

		// 검색 결과를 List<LoginStats> 형태로 변환하여 반환합니다.
		List<LoginStats> loginStatsList = searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
		return loginStatsList;
	}
}