package com.office_nico.spractice.web.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.office_nico.spractice.constants.Length;

import lombok.Data;

@Data
public class ProfileForm {
	
	private String id;

	@NotBlank
	@Email
	@Size(max=Length.email)
	private String email = null;
	
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
}
