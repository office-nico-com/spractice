package com.office_nico.spractice.exception;

import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class BadJsonResponseException extends AppRunnableException {

	private static final long serialVersionUID = 5914534734195951587L;

	@Getter
	@Setter
	private String jsonMessage = null;
	
	public static BadJsonResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName) {
		methodName = "An error occurred in " + methodName + "()";
		BadJsonResponseException exception = new BadJsonResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		return exception;
	}

	public static BadJsonResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName, String jsonMessage) {
		methodName = "An error occurred in " + methodName + "()";
		BadJsonResponseException exception = new BadJsonResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		exception.setJsonMessage(jsonMessage);
			return exception;
	}

	public BadJsonResponseException(Logger logger, Integer uniqueCode, String methodName) {
		super(logger, uniqueCode, methodName);
		this.setLogger(logger);
		this.setUniqueCode(uniqueCode);
		this.setPrintTtrace(false);
	}
}
