package com.ssafy.authorization.developersettings.redirect.model;

import java.io.Serializable;

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
public class RedirectEntityId implements Serializable {
	private int teamId;
	private int userId;
	private String redirect;
}