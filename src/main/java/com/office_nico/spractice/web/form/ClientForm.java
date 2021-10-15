package com.office_nico.spractice.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.office_nico.spractice.constants.Length;

import lombok.Data;

@Data
public class ClientForm {
	
	private String id;
	
	@Size(min=Length.clientKeycodeMin, max=Length.clientKeycode, message = "{javax.validation.constraints.Size.range.message}")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{javax.validation.constraints.Pattern.alphanum.message}")
	private String clientKeycode = null;
	
	@NotBlank
	@Size(max=Length.clientNameJa)
	private String clientNameJa = null;
	
	@NotBlank
	@Size(max=Length.clientNameJaKana)
	@Pattern(regexp = "^[ァ-タダ-ヶー０-９]*$", message = "{javax.validation.constraints.Pattern.zenkana.message}")
	private String clientNameJaKana = null;
	
	@Size(max=Length.securityMangementTeam)
	private String securityMangementTeam = null;

	@Size(max=Length.securityMangementTel)
	private String securityMangementTel = null;

	@Size(max=Length.securityMangementEmail)
	private String securityMangementEmail = null;

	@NotBlank
	private String userRegistTypeCode = null;
	
	@Size(max=Length.description)
	private String description = null;
	
	private String isInvalided = null;

	@Size(max=Length.logoutUrl)
	private String logoutUrl = null;

	private List<ScenarioForm> scenarios = new ArrayList<>();
	
}
