package com.ssafy.authorization.team.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "developer_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperMemberEntity {

	@Id
	@Column(name = "member_seq")
	private Integer MemberSeq;

	@Column(name = "student_id")
	private String id;

	@Column(name = "password")
	private String password;

	@Column(name = "grade")
	private Integer grade;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_number")
	private String phone;

	@Column(name = "gender")
	private Boolean gender;

	@Column(name = "track")
	private String track;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String iamge;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Column(name = "update_date")
	private LocalDateTime modifyDate;

	@Column(name = "delete_date")
	private LocalDateTime deleteDate;

	@Column(name = "is_delete")
	private Boolean isDelete;

}
