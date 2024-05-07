package com.ssafy.authorization.team.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TeamListPK.class)
@Immutable
public class TeamListEntity {

	@Id
	@Column(name = "developer_team_seq", nullable = false)
	private Integer teamSeq;

	@Id
	@Column(name = "member_seq", nullable = false)
	private Integer memberSeq;

	@Column(name = "is_accept", nullable = false)
	private Boolean isAccept;

	@Column(name = "team_name", nullable = false)
	private String teamName;

	@Column(name = "service_key")
	private String serviceKey;

	@Column(name = "serviceName", nullable = false)
	private String serviceName;

	@Column(name = "serviceImage")
	private String serviceImage;

	@Column(name = "leader_member_seq")
	private Integer leader;

	@Column(name = "create_date", nullable = false)
	private LocalDateTime createDate;

	@Column(name = "delete_date")
	private LocalDateTime deleteDate;

	@Column(name = "modify_date")
	private LocalDateTime modifyDate;

	@Column(name = "is_delete", nullable = false)
	private Boolean isDelete;

}
