package com.office_nico.spractice.service.data;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Action;

import lombok.Data;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class SessionData {

	// Use as a whole
	private Action.Type actionType = null;

	
	// Used on the admin site
	private String adminFamilyName = null;
	private String adminGivenName = null;
	private List<String> messages = null;
	
	
	// Used on user site.
	private Long userId = null;
	private String account = null;
	private String email = null;
	private Short registeredFromCode = null;
	private String familyName = null;
	private String givenName = null;
	private List<Long> availableClients = null;
	
}
