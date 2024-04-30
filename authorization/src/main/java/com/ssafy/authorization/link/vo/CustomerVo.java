package com.ssafy.authorization.link.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVo {
	private Integer memberSeq;
	private String email;
	private String name;
	private String Image;
	private String track;
	private String studentId;
	private Boolean gender;
	private String phoneNumber;
}
