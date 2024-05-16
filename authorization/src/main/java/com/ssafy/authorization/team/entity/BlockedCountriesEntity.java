package com.ssafy.authorization.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blocked_country")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedCountriesEntity {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer seq;

	@Column(name = "country_list", nullable = false)
	private String countryList;

	@Column(name = "team_id")
	private Integer teamId;

}
