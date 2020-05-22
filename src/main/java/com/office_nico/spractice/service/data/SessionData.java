package com.office_nico.spractice.service.data;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class SessionData {

	private Long organizationId = null;
	private Long userId = null;
	private String account = null;

	public boolean isLogin() {
		boolean ret = false;
		if(organizationId != null && userId != null && account != null) {
			ret = true;
		}
		return ret;
	}
}
