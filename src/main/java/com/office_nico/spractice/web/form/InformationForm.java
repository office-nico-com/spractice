package com.office_nico.spractice.web.form;


import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class InformationForm {
	
	private String id;
	
	@NotBlank
	private String message = null;

	private String isShowDashboard = null;

	private String showStartedOn = null;

	private String showEndedOn = null;

	
}
