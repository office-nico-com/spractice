package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(name = "virtual_machines")
@Data
public class VirtualMachine {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String virtualMachineName = null;

	@Column(nullable = false)
	private String virtualMachineNameJa = null;

	@Column(nullable = true)
	private byte[] thumbnail = null;

	@Column(nullable = true)
	private String description = null;

	@Column(nullable = true)
	private String startupScript = null;

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
	private Course course = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "virtualMachine")
	@OrderBy("orderNumber ASC")
	@Where(clause = "is_deleted = false")
	private List<Extension> extensions = null;
	
}
