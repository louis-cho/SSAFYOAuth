package com.ssafy.authorization.stats.login.model;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;

@Document(indexName = "login_stats", createIndex = true)
public class LoginDocument {

	public LoginDocument(UUID userId, UUID teamId, Instant createdAt) {
		this.userId = userId.toString();
		this.teamId = teamId.toString();
		this.createdAt = createdAt;
	}

	@Id
	private Long id;
	@Field(type = FieldType.Text, name = "userId")
	private String userId;

	@Field(type = FieldType.Text, name = "userId")
	private String teamId;

	@Field(type = FieldType.Date, name = "createdAt")
	private Instant createdAt;

	@Field(type = FieldType.Boolean, name = "success")
	private Boolean success;

}
