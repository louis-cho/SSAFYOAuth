package com.ssafy.authorization.member.model.repository;

import com.ssafy.authorization.member.model.domain.Authority;
import com.ssafy.authorization.member.model.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	List<Authority> findByMember(Member member);
}
