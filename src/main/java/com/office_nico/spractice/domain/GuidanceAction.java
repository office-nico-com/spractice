package com.office_nico.spractice.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "guidance_actions")
@Data
public class GuidanceAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long guidanceId = null;
	
	@Column(nullable = false)
	private String label = null;
	
	@Column(nullable = true)
	private Short actionTypeCode = null;
	
	@Column(nullable = true)
	private Long nextGuidanceActionId = null;

	@Column(nullable = true)
	private String title = null;
	
	@Column(nullable = true)
	private String body = null;
	
	@Column(nullable = true)
	private Boolean openWindowFlag = null;
	
	@Column(nullable = false)
	private Integer sortOrder = null;

	@Column(nullable = false)
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	private Long createdBy = null;

	@Column(nullable = true)
	private Long updatedBy = null;

	@Transient
	private String nextGuidanceKeycode = null;


}
