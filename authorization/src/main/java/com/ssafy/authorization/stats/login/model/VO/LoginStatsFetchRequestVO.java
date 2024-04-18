package com.ssafy.authorization.stats.login.model.VO;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginStatsFetchRequestVO {

	public UUID userId = null;
	public UUID teamId = null;
	public Instant startTime = null;
	public Instant endTime = null;
	public Boolean success = null;

	LoginStatsFetchRequestVO(JsonNode requestBody) {
		JsonNode userNode, teamNode, startNode, endNode, successNode;

		userNode = requestBody.get("userId");
		teamNode = requestBody.get("teamId");
		startNode = requestBody.get("startTime");
		endNode = requestBody.get("endTime");
		successNode = requestBody.get("success");

		if (userNode != null) {
			try {
				userId = UUID.fromString(userNode.asText());
				this.setUserId(userId);
			} catch (IllegalArgumentException e) {
				userId = null;
			}
		}

		if (teamNode != null) {
			try {
				teamId = UUID.fromString(teamNode.asText());
				this.setTeamId(teamId);
			} catch (IllegalArgumentException e) {
				teamId = null;
			}
		}
		if (startNode != null) {
			try {
				startTime = Instant.parse(startNode.asText());
				this.setStartTime(startTime);
			} catch (DateTimeParseException e) {
				startTime = null;
			}
		}
		if (endNode != null) {
			try {
				endTime = Instant.parse(endNode.asText());
				this.setEndTime(endTime);
			} catch (DateTimeParseException e) {
				endTime = null;
			}
		}
		if (successNode != null) {
			success = Boolean.parseBoolean(successNode.asText());
			this.setSuccess(success);
		}

	}
}
