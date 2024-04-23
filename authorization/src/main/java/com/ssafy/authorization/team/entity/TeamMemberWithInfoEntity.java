package com.ssafy.authorization.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_member_with_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TeamMemberWithInfoPK.class)
public class TeamMemberWithInfoEntity {

	@Id
	@Column(name = "developer_team_seq", nullable = false)
	private Integer teamSeq;

	@Id
	@Column(name = "member_seq", nullable = false)
	private Integer memberSeq;

	@Column(name = "is_accept", nullable = false)
	private Boolean isAccept;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false)
	private String email;

}
