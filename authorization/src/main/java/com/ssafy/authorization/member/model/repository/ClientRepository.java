package com.ssafy.authorization.member.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.authorization.member.model.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client,String> {
	Optional<Client> findClientByClientId(String clientId);
}
