package com.ssafy.authorization.member.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.authorization.member.model.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT user FROM Member user JOIN FETCH user.authorities WHERE user.username=:username")
	Member findByUsername(String username);

}
