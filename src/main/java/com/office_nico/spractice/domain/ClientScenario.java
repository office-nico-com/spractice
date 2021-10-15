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
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "clients_scenarios")
@Data
public class ClientScenario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Scenario.class)
	private Scenario scenario = null;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Client.class)
	private Client client = null;
	
	@Column(nullable = false)
	private Boolean isInvalided = null;

	@Transient
	private Boolean isDeleted = null;

	@Transient
	private Long scenarioId = null;

}
