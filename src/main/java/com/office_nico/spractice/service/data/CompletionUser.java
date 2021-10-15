package com.office_nico.spractice.service.data;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CompletionUser {
	private String account = null;
	private String email = null;
	private String 	familyName = null;
	private String givenName = null;
	private LocalDateTime endedAt = null;
}

