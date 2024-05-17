package com.ssafy.resourceserver.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.resourceserver.team.entity.CountLoginUserEntity;

public interface CountLoginUserRepository extends JpaRepository<CountLoginUserEntity, String> {
	CountLoginUserEntity findByServiceId(String teamSeq);

	@Query(value = "select sum(login_count) from public.count_login_user", nativeQuery = true)
	Integer countAllLoginUser();
}
