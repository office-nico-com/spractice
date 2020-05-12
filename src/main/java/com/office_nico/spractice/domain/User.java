package com.office_nico.spractice.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String account = null;

	@Column(nullable = false)
	private String passwd = null;
	
	@Column(nullable = false)
	private String familyName = null;
	
	@Column(nullable = false)
	private String givenName = null;
	
	@Column(nullable = false)
	private String familyNameKana = null;
	
	@Column(nullable = false)
	private String givenNameKana = null;
	
	@Column(nullable = false)
	private Short roleCode = null;
	
	@Column(nullable = false)
	private Boolean isInvalided = null;

	@Column(nullable = false)
	private Boolean isDeleted = null;

	@Column(nullable = false)
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	private Long createdBy = null;

	@Column(nullable = true)
	private Long updatedBy = null;

	@ManyToOne(fetch = FetchType.LAZY)
	// @NotFound(action = NotFoundAction.IGNORE)
	private Organization organization = null;
}
