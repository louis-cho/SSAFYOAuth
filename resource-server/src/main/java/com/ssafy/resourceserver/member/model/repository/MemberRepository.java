package com.ssafy.resourceserver.member.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.resourceserver.member.model.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(@Param("email") String email);
	void deleteByEmail(String email);
}