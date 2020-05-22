package com.office_nico.spractice.exceptions;

import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;


public class BadHtmlResponseException extends AppRunnableException {

	private static final long serialVersionUID = 4325291983234804295L;

	// 次に利用できるアクション
	public enum NextAction{
		TOP,		// 通常の親画面など
		CLOSE,	// 子画面など
		EXIT		// 復帰不可
	};
	
	@Getter
	@Setter
	private NextAction nextAction = null;
	@Getter
	@Setter
	private String htmlMessage = null;
	
	public static BadHtmlResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName, NextAction nextAction) {
		methodName = "An error occurred in " + methodName + "()";
		BadHtmlResponseException exception = new BadHtmlResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		exception.setNextAction(nextAction);
		return exception;
	}

	public static BadHtmlResponseException create(Logger logger, Integer uniqueCode, Throwable e, NextAction nextAction, String methodName, String htmlMessage) {
		methodName = "An error occurred in " + methodName + "()";
		BadHtmlResponseException exception = new BadHtmlResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		exception.setNextAction(nextAction);
		exception.setHtmlMessage(htmlMessage);
			return exception;
	}

	public BadHtmlResponseException(Logger logger, Integer uniqueCode, String methodName) {
		super(logger, uniqueCode, methodName);
		this.setLogger(logger);
		this.setUniqueCode(uniqueCode);
		this.setPrintTtrace(false);
	}
}
