package com.ssafy.authorization.member.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.ssafy.authorization.member.model.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(@Param("email") String email);
	void deleteByEmail(String email);

	Optional<Member> findByNameAndPhoneNumber(String userName, String phoneNumber);
}