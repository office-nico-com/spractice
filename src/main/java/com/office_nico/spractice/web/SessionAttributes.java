package com.office_nico.spractice.web;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionAttributes implements Serializable  {

	// ユーザID
	Long userId = null;
	// 組織ID
	Long organizationId = null;
	// 権限コード
	Short roleCode = null;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Short getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(Short roleCode) {
		this.roleCode = roleCode;
	}
}
