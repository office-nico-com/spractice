package com.office_nico.spractice.domain;

import java.time.LocalDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Cacheable(false)
@Entity
@Table(name = "clients_users")
@Data
public class ClientUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Short roleCode = null;

	@Column(nullable = false)
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	private Long createdBy = null;

	@Column(nullable = true)
	private Long updatedBy = null;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	private User user = null;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Client.class)
	private Client client = null;

}
