package com.ssafy.resourceserver.key.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.resourceserver.key.model.domain.ServiceKey;

@Repository
public interface ServiceKeyRepository extends JpaRepository<ServiceKey, Long> {
	boolean existsByKey(String key);
}
