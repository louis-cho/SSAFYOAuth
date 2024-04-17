package com.ssafy.authorization.developer.settings.key.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.developer.settings.key.model.domain.ServiceKey;

@Repository
public interface ServiceKeyRepository extends JpaRepository<ServiceKey, Long> {
	boolean existsByKey(String key);
}
