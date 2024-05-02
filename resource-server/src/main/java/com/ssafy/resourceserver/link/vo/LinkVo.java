package com.ssafy.resourceserver.link.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkVo {
	private Integer teamSeq;
	private String serviceName;
	private String serviceImage;
	private Boolean isDelete;
}
