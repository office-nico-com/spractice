package com.office_nico.spractice.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.office_nico.spractice.constants.Length;

import lombok.Data;

@Data
public class UserForm {
	
	private String id;

	@NotBlank
	@Email
	@Size(max=Length.email)
	private String email = null;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9@\\+\\-\\.\\[\\]\\*_<>:!\\|]*$", message = "{javax.validation.constraints.Pattern.alphanummark.message}")
	@Size(max=Length.account)
	private String account = null;

	@Pattern(regexp = "^[a-zA-Z0-9@\\+\\-\\.\\[\\]\\*_<>:!\\|]*$", message = "{javax.validation.constraints.Pattern.alphanummark.message}")
	private String passwd = null;
	
	private String passwdConf = null;
	
	@NotBlank
	@Size(max=Length.familyName)
	private String familyName = null;
	
	@NotBlank
	@Size(max=Length.givenName)
	private String givenName = null;

	@NotBlank
	@Size(max=Length.familyNameKana)
	@Pattern(regexp = "^[ァ-タダ-ヶー０-９]*$", message = "{javax.validation.constraints.Pattern.zenkana.message}")
	private String familyNameKana = null;
	
	@NotBlank
	@Size(max=Length.givenNameKana)
	@Pattern(regexp = "^[ァ-タダ-ヶー０-９]*$", message = "{javax.validation.constraints.Pattern.zenkana.message}")
	private String givenNameKana = null;

	private String registeredFromCode = null;
	
	private String isAdmin = null;

	@Size(max=Length.description)
	private String description = null;
	
	private String isInvalided = null;
	
	private List<UserClient> clients  = new ArrayList<>();
	
	
	@AssertFalse(message = "{javax.validation.constraints.NotBlank.message}")
	public boolean isPasswdBlank() {
		boolean ret = false;
		// 新規の場合は必須
		// 編集の場合は任意
		if((id == null || id.length() == 0) && (passwd == null || passwd.length() == 0)) {
			ret = true;
		}
		return ret;
	}

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
