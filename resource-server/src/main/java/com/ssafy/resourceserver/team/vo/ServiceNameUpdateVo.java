package com.ssafy.resourceserver.team.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceNameUpdateVo {
	@JsonProperty("service_name")
	@NotBlank
	@Size(min = 1, max = 300)
	@Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]*$")
	private String serviceName;
}
