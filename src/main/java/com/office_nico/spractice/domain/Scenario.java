package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "scenarios")
@Data
public class Scenario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String scenarioKeycode = null;

	@Column(nullable = false)
	private String scenarioName = null;

	@Column(nullable = true)
	private Long thumbnailBinaryFileId = null;

	@Column(nullable = false)
	private String description = null;

	@Column(nullable = true)
	private String note = null;
	
	@Column(nullable = true)
	private String contentBody = null;
	
	@Column(nullable = true)
	private String contentScript = null;

	@Column(nullable = true)
	private String contentCss = null;
	
	@Column(nullable = true)
	private Long startGuidanceId = null;
	
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
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scenario")
	@OrderBy("id")
	private List<ClientScenario> clientScenarios = null;

	@Transient
	private String clientKeycodeList = null;
	@Transient
	private String clientNameList = null;
	@Transient
	private String clientNameKanaList = null;
	
	@Transient
	private List<Guidance> guidances = new ArrayList<>();

	@Transient
	private String startGuidanceKeycode = null;


}
