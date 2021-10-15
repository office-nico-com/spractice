package com.office_nico.spractice.web.form;

import lombok.Data;

@Data
public class GuidanceActionForm {
	private String id = null;
	private String label = null;
	private String actionTypeCode = null;
	private String nextGuidanceActionId = null;
	private String title = null;
	private String body = null;
	private String openWindowFlag = null;
	private String sortOrder = null;
}
