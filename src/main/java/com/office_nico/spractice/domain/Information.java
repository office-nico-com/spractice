package com.office_nico.spractice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "informations")
@Data
public class Information {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String message = null;

	@Column(nullable = false)
	private LocalDateTime postedAt = null;

	@Column(nullable = false)
	private Boolean isShowDashboard = null;

	@Column(nullable = true)
	private LocalDate showStartedOn = null;

	@Column(nullable = true)
	private LocalDate showEndedOn = null;

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
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private User user = null;
	
}

