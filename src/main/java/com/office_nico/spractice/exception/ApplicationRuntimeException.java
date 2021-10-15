package com.office_nico.spractice.exception;


public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8930895704157684897L;

	private String applocationMessage = null;


	private Throwable cause = null;

	
	public ApplicationRuntimeException(String message) {
		super();
		this.applocationMessage = message;
	}

	public ApplicationRuntimeException(String message, Throwable e) {
		super(e.getMessage(), e);
		this.applocationMessage = message;
		this.cause = e;
	}

	public String getApplocationMessage() {
		return applocationMessage;
	}

	public Throwable getCause() {
		return cause;
	}

}
