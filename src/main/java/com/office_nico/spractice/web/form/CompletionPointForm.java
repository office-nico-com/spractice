package com.office_nico.spractice.web.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.office_nico.spractice.constants.Length;

import lombok.Data;

@Data
public class CompletionPointForm {

	private String id = null;
	@Size(min=Length.completionPointKeycodeMin, max=Length.completionPointKeycode, message = "{javax.validation.constraints.Size.range.message}")
	@Pattern(regexp ="^[a-zA-Z0-9@\\#\\$\\%\\*!]*$", message = "{javax.validation.constraints.Pattern.alphanummark2.message}")
	private String completionPointKeycode = null;

	@NotBlank
	@Size(max=Length.description)
	private String description = null;
	@Size(max=Length.description)
	private String descriptionEn = null;
	private String isInvalided = null;
	private String isDeleted = null;
	
}
