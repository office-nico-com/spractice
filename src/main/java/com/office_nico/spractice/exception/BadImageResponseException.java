package com.office_nico.spractice.exception;

import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class BadImageResponseException extends AppRunnableException {

	private static final long serialVersionUID = -7458135430874643255L;

	@Getter
	@Setter
	private String imagePath = null;
	
	public static BadImageResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName) {
		methodName = "An error occurred in " + methodName + "()";
		BadImageResponseException exception = new BadImageResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		return exception;
	}

	public static BadImageResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName, String imagePath) {
		methodName = "An error occurred in " + methodName + "()";
		BadImageResponseException exception = new BadImageResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		exception.setImagePath(imagePath);
		return exception;
	}

	public BadImageResponseException(Logger logger, Integer uniqueCode, String methodName) {
		super(logger, uniqueCode, methodName);
		this.setLogger(logger);
		this.setUniqueCode(uniqueCode);
		this.setPrintTtrace(false);
	}
}
