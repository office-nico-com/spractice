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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "clients")
@Data
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String clientKeycode = null;

	@Column(nullable = false)
	private String clientNameJa = null;

	@Column(nullable = true)
	private String clientNameJaKana = null;

	@Column(nullable = false)
	private Short userRegistTypeCode = null;

	@Column(nullable = true)
	private String description = null;

	@Column(nullable = false)
	private String secret = null;
	
	@Column(nullable = true)
	private String logoutUrl = null;
	
	@Column(nullable = true)
	private String securityMangementTeam = null;

	@Column(nullable = true)
	private String securityMangementTel = null;
	
	@Column(nullable = true)
	private String securityMangementEmail = null;
	
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
	private List<ClientUser> clientUsers = null;
	

}
