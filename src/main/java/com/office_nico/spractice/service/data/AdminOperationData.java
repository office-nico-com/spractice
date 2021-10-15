package com.office_nico.spractice.service.data;

import java.time.LocalDateTime;

import com.office_nico.spractice.util.DateTimeUtil;

import lombok.Data;

@Data
public class AdminOperationData {

	private LocalDateTime operatedAt = null;
	
	private String familyName = null;
	
	private String givenName = null;
	
	private String message = null;

	public String getOperationTime() {
		return DateTimeUtil.format(operatedAt, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_SUPPRESS1);
	}
	
}
