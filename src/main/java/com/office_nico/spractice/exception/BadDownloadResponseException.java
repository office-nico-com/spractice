package com.office_nico.spractice.exception;

import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class BadDownloadResponseException extends AppRunnableException {

	private static final long serialVersionUID = -9126631173780511065L;

	@Getter
	@Setter
	private String downloadMessage = null;
	
	public static BadDownloadResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName) {
		methodName = "An error occurred in " + methodName + "()";
		BadDownloadResponseException exception = new BadDownloadResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		return exception;
	}

	public static BadDownloadResponseException create(Logger logger, Integer uniqueCode, Throwable e, String methodName, String downloadMessage) {
		methodName = "An error occurred in " + methodName + "()";
		BadDownloadResponseException exception = new BadDownloadResponseException(logger, uniqueCode, methodName);
		exception.setE(e);
		exception.setDownloadMessage(downloadMessage);
		return exception;
	}

	public BadDownloadResponseException(Logger logger, Integer uniqueCode, String methodName) {

		
		super(logger, uniqueCode, methodName);
		this.setLogger(logger);
		this.setUniqueCode(uniqueCode);
		this.setPrintTtrace(false);
	}
}
