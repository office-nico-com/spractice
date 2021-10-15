package com.office_nico.spractice.service.data;

import java.util.ArrayList;
import java.util.List;

import com.office_nico.spractice.domain.CompletionPoint;

import lombok.Data;

@Data
public class CompletionHistory {

	public CompletionPoint completionPoint = null;
	List<CompletionUser> completionUsers = new ArrayList<>();
	
}
