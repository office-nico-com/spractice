package com.office_nico.spractice.service.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CsvImportResult {
	
	public static final Integer RESULT_SUCCESS = 1;
	public static final Integer RESULT_ERROR = 2;
	public static final Integer RESULT_FAIL = 3;
	
	private Integer result = RESULT_SUCCESS;
	
	private List<String> messages = new ArrayList<>();
	
	public void addMessage(String message) {
		messages.add(message);
		result = RESULT_ERROR;
	}
	
	public boolean hasError() {
		boolean ret = false;
		if(result != RESULT_SUCCESS) {
			ret = true;
		}
		return ret;
	}

}
