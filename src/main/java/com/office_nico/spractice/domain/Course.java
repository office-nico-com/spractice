package com.office_nico.spractice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(name = "courses")
@Data
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String courseName;

	@Column(nullable = false)
	private String courseNameJa;

	@Column(nullable = true)
	private Long thumbnailBinaryFileId;

	@Column(nullable = true)
	private String description = null;

	@Column(nullable = false)
	private LocalDate startDate = null;

	@Column(nullable = false)
	private LocalDate endDate = null;

	@Column(nullable = true)
	private String note = null;

	@Column(nullable = false)
	private Integer orderNumber = null;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	@OrderBy("orderNumber ASC")
	@Where(clause = "is_deleted = false")
	private List<VirtualMachine> virtualMachines = null;

}
