package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "completion_points")
@Data
public class CompletionPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String completionPointKeycode = null;
	
	@Column(nullable = true)
	private String description = null;

	@Column(nullable = true)
	private String descriptionEn = null;

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

	@Transient
	private LocalDateTime lastCompletedAt = null;
	
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "completionPoint")
	private List<Completion> completions = null;


}
