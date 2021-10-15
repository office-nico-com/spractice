package com.office_nico.spractice.web.form;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GuidanceForm {

	private String id = null;
	private String guidanceKeycode = null;
	private String guidanceText = null;
	private String isDeleted = null;
	private String sortOrder = null;

	
	private String height = null;
	private String delayMs = null;
	private String positionCode = null;
	private String backgroundColor = null;
	private String drawingSpeed = null;
	
	
	private String preScriptName = null;
	private String preScript = null;
	private String preScriptDelayMs = null;
	private String postScriptName = null;
	private String postScript = null;
	private String postScriptDelayMs = null;

	private String startCompletionPointId = null;
	private String endCompletionPointId = null;

	private String startCompletionPointKeycode = null;
	private String endCompletionPointKeycode = null;
	private String startCompletionPointDescription = null;
	private String endCompletionPointDescription = null;
	private String startCompletionPointType = null;
	private String endCompletionPointType = null;
	
	private String isFullHeight = null;
	
	public List<GuidanceActionForm> guidanceActions = new ArrayList<>();
}
