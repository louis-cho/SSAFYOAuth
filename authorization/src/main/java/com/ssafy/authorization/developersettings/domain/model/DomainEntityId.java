package com.ssafy.authorization.developersettings.domain.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DomainEntityId implements Serializable {
	private UUID teamId;
	private UUID userId;
	private String domain;
}