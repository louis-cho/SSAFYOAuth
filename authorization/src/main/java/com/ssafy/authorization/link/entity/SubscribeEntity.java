package com.ssafy.authorization.link.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ssafy.authorization.team.entity.DeveloperTeamEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscribe")
@IdClass(SubscribePK.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeEntity {
	@Id
	@Column(name = "member_seq")
	private Integer memberSeq;

	@Id
	@Column(name = "developer_team_seq")
	private Integer teamSeq;

	@Column(name = "is_delete")
	private Boolean isDelete;

	@Column(name = "blacklist")
	private Boolean blacklist;

	@Column(name = "create_date")
	@CreationTimestamp
	private LocalDateTime createDate;

	@Column(name = "modify_date")
	@UpdateTimestamp
	private LocalDateTime modifyDate;

	@Column(name = "delete_date")
	private LocalDateTime deleteDate;

	@OneToOne
	@JoinColumn(name = "developer_team_seq")
	DeveloperTeamEntity singedUpServices;
}
