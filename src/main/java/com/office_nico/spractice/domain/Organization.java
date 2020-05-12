package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(name = "organizations")
@Data
public class Organization {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String organizationName = null;

	@Column(nullable = false)
	private String description = null;
	
	@Column(nullable = true)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	@OrderBy("orderNumber ASC")
	@Where(clause = "is_deleted = false")
	private List<Course> courses = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	@OrderBy("family_name ASC")
	@Where(clause = "is_deleted = false")
	private List<User> users = null;
	
}
