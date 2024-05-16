package com.ssafy.resourceserver.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.resourceserver.team.entity.Oauth2AuthorizationConsentEntity;

public interface Oauth2AuthorizationConsentRepository extends JpaRepository<Oauth2AuthorizationConsentEntity, String> {

	@Query(value="select count(principal_name) from public.oauth2_authorization_consent\n" +
		"where  registered_client_id=:teamSeq\n" +
		"group by registered_client_id", nativeQuery = true)
	Integer countServiceUser(String teamSeq);
}
