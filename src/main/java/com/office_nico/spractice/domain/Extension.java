package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(name = "extensions")
@Data
public class Extension {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String extensionType = null;

	@Column(nullable = false)
	private String extensionName = null;

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
	private VirtualMachine virtualMachine = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "extension")
	@OrderBy("orderNumber ASC")
	private List<ExtensionProperty> extensionProperties = null;
	
	@Transient
	private String icon=null;
	
	@Transient
	private String className=null;

	@Transient
	private String specificProperty =null;
}
