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
@Table(name = "completions")
@Data
public class Completion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long clientId = null;
	
	@Column(nullable = false)
	private Long userId = null;

	@Column(nullable = false)
	private String transaction = null;

	@Column(nullable = true)
	private LocalDateTime startedAt = null;

	@Column(nullable = true)
	private LocalDateTime endedAt = null;

	
	@Column(nullable = false)
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	private Long createdBy = null;

	@Column(nullable = true)
	private Long updatedBy = null;

	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CompletionPoint.class)
	private CompletionPoint completionPoint = null;


}
