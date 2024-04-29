package com.ssafy.authorization.team.vo;


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

	@NotBlank
	@Size(min = 1, max = 300)
	@Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]*$")
	private String teamName;

	@NotBlank
	@Size(min = 1, max = 300)
	@Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]*$")
	private String serviceName;

	@NotBlank
	@Size(min = 0, max = 5)
	// @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	private String[] teamMember;

	@NotBlank
	@Size(min = 0, max = 5)
	private String[] domainUrl;

	@NotBlank
	@Size(min = 0, max = 10)
	// @Pattern(regexp = "^(https?|ftp):\\/\\/(-\\.)?([^\\s/?\\.#-]+\\.?)+(/[^\\s]*)?$")
	private String[] redirectionUrl;
}
