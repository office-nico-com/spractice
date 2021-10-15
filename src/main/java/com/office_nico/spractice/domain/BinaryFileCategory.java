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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "binary_file_categories")
@Data
public class BinaryFileCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id=null;

	@Column(nullable = false)
	String categoryName = null;
	
	@Column(nullable = false)
	Boolean canDelete = null;

	@Column(nullable = false)
	Integer sortOrder = null;
	
	@Column(nullable = false)
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	private Long createdBy = null;

	@Column(nullable = true)
	private Long updatedBy = null;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "binaryFileCategory")
	@OrderBy("id DESC")
	private List<BinaryFile> binaryFiles = null;

	@Transient
	private Boolean isDelete = false;
	
}
