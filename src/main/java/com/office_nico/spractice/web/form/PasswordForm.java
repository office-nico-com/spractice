package com.office_nico.spractice.web.form;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.office_nico.spractice.constants.Length;

import lombok.Data;

@Data
public class PasswordForm {
	
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9@\\+\\-\\.\\[\\]\\*_<>:!\\|]*$", message = "{javax.validation.constraints.Pattern.alphanummark.message}")
	private String passwd = null;
	
	private String passwdConf = null;

	@AssertFalse(message = "{javax.validation.constraints.Password.Length.message}")
	public boolean isPasswdLen() {
		boolean ret = false;
		if(passwd != null && passwd.length() > 0) {
			if(passwd.length() < Length.passwdMin || passwd.length() > Length.passwd) {
				ret = true;
			}
		}
		return ret;
	}


	@AssertFalse(message = "{javax.validation.constraints.Password.Different.message}")
	public boolean isPasswdDifferent() {
		boolean ret = false;
		
		if(passwd != null && passwd.length() > 0) {
			if(passwdConf == null || passwdConf.length() == 0 || !passwd.equals(passwdConf)) {
				ret = true;
			}
		}
		return ret;
	}

}

