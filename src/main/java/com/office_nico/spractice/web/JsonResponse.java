package com.office_nico.spractice.web;

import lombok.Data;

public class JsonResponse {

	public enum ResponseCode{
		OK(1),						// 正常
		IMPOSSIBLE(2),		// 処理不可
		ERROR(99);				// その他エラー

		private final int value;
		
		ResponseCode(int value) {
			this.value = value;
		}
		
		public int value() {
			return this.value;
		}
	}

	Integer result = ResponseCode.OK.value();
	
	public void setResult(ResponseCode code) {
		this.result = code.value();
	}
	
	
	public String getResult() {
		return this.result.toString();
	}
	
}
