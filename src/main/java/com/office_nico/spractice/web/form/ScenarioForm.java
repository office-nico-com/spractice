package com.office_nico.spractice.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.office_nico.spractice.constants.Length;

import lombok.Data;

@Data
public class ScenarioForm {
	
	private String id;

	@Size(min=Length.scenarioKeycodeMin, max=Length.scenarioKeycode, message = "{javax.validation.constraints.Size.range.message}")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{javax.validation.constraints.Pattern.alphanum.message}")
	private String scenarioKeycode = null;
	
	@NotBlank
	@Size(max=Length.scenarioName)
	private String scenarioName = null;
	
	@NotBlank
	private String thumbnailBinaryFileId = null;
	
	@Size(max=Length.description)
	private String description = null;

	@Size(max=Length.note)
	private String note = null;

	private String isInvalided = null;

	private String isDeleted = null;

	private List<CompletionPointForm> completions = new ArrayList<>();

	private List<GuidanceForm> guidances = new ArrayList<>();

	private String startGuidanceId = null;

	private String contentBody = null;
	
	private String contentScript = null;

	private String contentCss = null;
	
	private String sortOrder = null;


}
