package com.office_nico.spractice.service.data;

import lombok.Data;

@Data
public class AggregateData {

	private Long id = null;
	private Integer max = 0;
	private Integer min = 0;
	private Integer avg = 0;
	private Integer count = 0;
	
	
}
