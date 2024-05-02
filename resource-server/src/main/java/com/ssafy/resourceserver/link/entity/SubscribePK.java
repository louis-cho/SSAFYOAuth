package com.ssafy.resourceserver.link.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribePK implements Serializable {
	private Integer memberSeq;
	private Integer teamSeq;
}
