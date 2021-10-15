package com.office_nico.spractice.web.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class IndexForm {
	
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
