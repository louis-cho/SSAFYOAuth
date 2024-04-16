package com.ssafy.authorization.developersettings.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.authorization.developersettings.domain.model.DomainEntity;

public interface DomainEntityRepository extends JpaRepository<DomainEntity, UUID> {
}
