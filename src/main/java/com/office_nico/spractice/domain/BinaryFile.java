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
@Table(name = "binary_files")
@Data
public class BinaryFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id=null;

	@Column(nullable = false)
	private String originalFileName = null;
	
	@Column(nullable = false)
	private String saveFileName = null;

	@Column(nullable = false)
	private String subPath = null;
	
	@Column(nullable = false)
	private String mimeType = null;

	@Column(nullable = false)
	private Long fileSize = null;

	
	@Column(nullable = false)
	private LocalDateTime lastModifiedAt = null;
	
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
	private BinaryFileCategory binaryFileCategory = null;
	
}
