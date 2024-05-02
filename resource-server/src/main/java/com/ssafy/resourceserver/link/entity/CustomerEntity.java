package com.ssafy.resourceserver.link.entity;

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
@Table(name = "customer")
@Immutable
@IdClass(CustomerPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {
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
	private LocalDateTime createDate;

	@Column(name = "modify_date")
	private LocalDateTime modifyDate;

	@Column(name = "delete_date")
	private LocalDateTime deleteDate;

	@Column(name = "email")
	private String email;

	@Column(name = "student_id")
	private String studentId;

	@Column(name = "name")
	private String name;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "gender")
	private Boolean gender;

	@Column(name = "image")
	private String image;

	@Column(name = "track")
	private String track;

}
