package com.ssafy.resourceserver.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TeamMemberPK.class)
public class TeamMemberEntity {

	@Id
	@Column(name = "developer_team_seq")
	private Integer teamSeq;

	@Id
	@Column(name = "member_seq")
	private Integer memberSeq;

	@Column(name = "is_accept")
	private Boolean isAccept;

}
