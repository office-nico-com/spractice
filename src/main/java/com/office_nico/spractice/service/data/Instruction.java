package com.office_nico.spractice.service.data;

import lombok.Data;

@Data
public class Instruction {

	public enum Result {
		SUCCESS,			// 取得成功
		INVALID_COURSE,	// 利用できないコース
		INVALID_VM			// 利用できない仮想マシン
	}

	// 結果コード
	private Result result = null;





}
