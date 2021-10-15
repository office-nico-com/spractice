package com.office_nico.spractice.service.data;

import com.office_nico.spractice.domain.CompletionPoint;

import lombok.Data;

@Data
public class ReportData {

	// private Scenario scenario = null;
	private CompletionPoint completionPoint = null;
	
	// 最小時間
	private Integer minMin = null;
	
	// 平均時間
	private Integer avgMin = null;
	
	// 最大時間
	private Integer maxMin = null;
	
	// 修了数
	private Integer completionCount = 0;
	
	
	// 訓練中数
	private Integer worikingCount = 0;
	
	// 未着手数
	private Integer unworkedCount = 0;
	
	
	
}
