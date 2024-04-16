package com.ssafy.authorization.member.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.member.model.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
