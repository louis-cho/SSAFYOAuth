package com.ssafy.authorization.team.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamAddVo {

	@JsonProperty("team-name")
	@NotBlank
	@Size(min = 1, max = 300)
	@Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]*$")
	private String teamName;

	@JsonProperty("service-name")
	@NotBlank
	@Size(min = 1, max = 300)
	@Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]*$")
	private String serviceName;

	@JsonProperty("team-members")
	@NotBlank
	@Size(min = 0, max = 5)
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	private String[] teamMember;

	@JsonProperty("domain-url")
	@NotBlank
	@Size(min = 0, max = 5)
	private String[] domainUrl;

	@JsonProperty("redirection-url")
	@NotBlank
	@Size(min = 0, max = 10)
	@Pattern(regexp = "^(https?|ftp):\\/\\/(-\\.)?([^\\s/?\\.#-]+\\.?)+(/[^\\s]*)?$")
	private String[] redirectionUrl;
}
