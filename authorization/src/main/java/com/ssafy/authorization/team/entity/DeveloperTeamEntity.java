package com.ssafy.authorization.team.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "developer_team")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperTeamEntity {

	@Id
	@Column(name = "developer_team_seq", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer seq;

	@Column(name = "team_name", nullable = false)
	private String teamName;

	@Column(name = "service_key")
	private String serviceKey;

	@Column(name = "service_name", nullable = false)
	private String serviceName;

	@Column(name = "service_image")
	private String serviceImage;

	@Column(name = "create_date", nullable = false)
	@CreationTimestamp
	private LocalDateTime createDate;

	@Column(name = "delete_date")
	private LocalDateTime deleteDate;

	@Column(name = "modify_date")
	@UpdateTimestamp
	private LocalDateTime modifyDate;

	@Column(name = "is_delete", nullable = false)
	private Boolean isDelete;

	@Column(name = "leader_member_seq", nullable = false)
	private Integer leader;
}
