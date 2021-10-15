package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "guidances")
@Data
public class Guidance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long scenarioId = null;
	
	@Column(nullable = false)
	private String guidanceKeycode = null;
	
	@Column(nullable = true)
	private String guidanceText = null;

	@Column(nullable = true)
	private Integer height = null;
	
	@Column(nullable = false)
	private Boolean isFullHeight = null;
	
	@Column(nullable = true)
	private Integer delayMs = null;
	
	@Column(nullable = true)
	private Short positionCode = null;
	
	@Column(nullable = true)
	private String backgroundColor = null;

	@Column(nullable = true)
	private Integer drawingSpeed = null;
	
	@Column(nullable = true)
	private String preScriptName = null;
	
	@Column(nullable = true)
	private String preScript = null;
	
	@Column(nullable = true)
	private Integer preScriptDelayMs = null;
	
	@Column(nullable = true)
	private String postScriptName = null;
	
	@Column(nullable = true)
	private String postScript = null;
	
	@Column(nullable = true)
	private Integer postScriptDelayMs = null;

	@Column(nullable = true)
	private Long startCompletionPointId = null;

	@Column(nullable = true)
	private Long endCompletionPointId = null;

	
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

	@Transient
	private Boolean isDeleted = null;
	
	@Transient
	private List<GuidanceAction> guidanceActions =  new ArrayList<>();

	@Transient
	private Integer startCompletionPointType;
	@Transient
	private Integer endCompletionPointType;
	@Transient
	private String startCompletionPointKeycode;
	@Transient
	private String endCompletionPointKeycode;
	@Transient
	private String startCompletionPointDescription;
	@Transient
	private String endCompletionPointDescription;
	
	@Transient
	private Long workId = null;
	

}
