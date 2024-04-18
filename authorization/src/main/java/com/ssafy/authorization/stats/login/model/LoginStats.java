package com.ssafy.authorization.stats.login.model;

import java.time.Instant;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
@Document(indexName = "login_stats", createIndex = true)
public class LoginStats {

	public LoginStats(String userId, String teamId, Instant createdAt) {
		this.userId = userId;
		this.teamId = teamId;
		this.createdAt = createdAt;
	}

	@Field(type = FieldType.Keyword, name = "userId")
	private String userId;

	@Field(type = FieldType.Keyword, name = "userId")
	private String teamId;

	@Field(type = FieldType.Date, name = "createdAt")
	private Instant createdAt;

	@Field(type = FieldType.Boolean, name = "success")
	private Boolean success;
}
