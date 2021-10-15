package com.office_nico.spractice.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "admin_operations")
@Data
public class AdminOperation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime operatedAt = null;

	@Column(nullable = true)
	private Short functionCode = null;

	@Column(nullable = true)
	private Short operationCode = null;
	
	@Column(nullable = true)
	private Long targetId = null;

	@Column(nullable = true)
	private String message = null;

	@Column(nullable = false)
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	private Long createdBy = null;

	@Column(nullable = true)
	private Long updatedBy = null;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private User user = null;

}
