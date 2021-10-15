package com.office_nico.spractice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Cacheable;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.office_nico.spractice.util.BeanUtil;

import lombok.Data;

@Cacheable(false)
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
	
	private static final long serialVersionUID = 5818287185789008740L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String account = null;

	@Column(nullable = true)
	private String passwd = null;

	@Column(nullable = true)
	private String email = null;

	@Column(nullable = true)
	private String familyName = null;
	
	@Column(nullable = true)
	private String givenName = null;
	
	@Column(nullable = true)
	private String familyNameKana = null;
	
	@Column(nullable = true)
	private String givenNameKana = null;

	@Column(nullable = true)
	private Short registeredFromCode = null;
	
	@Column(nullable = false)
	private Boolean isAdmin = null;

	@Column(nullable = true)
	private String description = null;
	
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	// @JoinColumn(name="userId")
	@OrderBy("id")
	private List<ClientUser> clientUsers = new ArrayList<>();
	
	
	/*
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<OrganizationsUsers> organizationsUsers = null;
	*/
	
	@Transient
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return  AuthorityUtils.createAuthorityList("ROLE_USER");
	}

	@Transient
	@Override
	public String getPassword() {
		return passwd;
	}

	@Transient
	@Override
	public String getUsername() {
		return account;
	}

	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	@Transient
	private String clientKeycodeList = null;
	@Transient
	private String clientNameList = null;
	@Transient
	private String clientNameKanaList = null;
	
	/**
	 * 引数のインスタンスでフィールドを上書きする
	 * 
	 * @param entity 更新元オブジェクト
	 */
	public void overwrite(User entity) {
		BeanUtil.copyFields(entity, this, "id", "passwd", "registeredFromCode", "isDeleted", "createdAt", "updatedAt", "createdBy", "updatedBy");
	}
}

