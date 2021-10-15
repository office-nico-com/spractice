package com.office_nico.spractice.web.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {

	@NotBlank
	private String account = null;

	@NotBlank
	private String passwd = null;
}
