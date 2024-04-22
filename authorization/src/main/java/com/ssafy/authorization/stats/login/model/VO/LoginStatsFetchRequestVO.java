package com.ssafy.authorization.stats.login.model.VO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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

	public LoginStatsFetchRequestVO(JsonNode requestBody) {
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
				// LocalDate로 날짜를 파싱합니다.
				LocalDate localDate = LocalDate.parse(startNode.asText());
				// LocalDate를 Instant로 변환합니다. 여기서는 날짜만 있으므로, 해당 날짜의 시작 시간으로 설정됩니다.
				Instant startTime = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
				this.setStartTime(startTime);
			} catch (DateTimeParseException e) {
				startTime = null;
			}
		}
		if (endNode != null) {
			try {
				// LocalDate로 날짜를 파싱합니다.
				LocalDate localDate = LocalDate.parse(endNode.asText());
				// LocalDate를 Instant로 변환합니다. 여기서는 날짜만 있으므로, 해당 날짜의 시작 시간으로 설정됩니다.
				Instant endTime = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
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
