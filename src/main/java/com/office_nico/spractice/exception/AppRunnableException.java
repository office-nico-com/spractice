package com.office_nico.spractice.exception;

import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;

// アプリケーション実行例外
public class AppRunnableException extends RuntimeException {

	private static final long serialVersionUID = -2428988131670780013L;

	public static final int DEFAULT_ERROR_CODE = 9001;
	
	@Getter
	@Setter
	private Logger logger = null;
	@Getter
	@Setter
	private Integer uniqueCode = null;
	@Getter
	@Setter
	private Throwable e = null;
	@Getter
	@Setter
	private boolean printTtrace = true;

	
	public AppRunnableException(Logger logger, Integer uniqueCode){
		super();
		this.logger = logger;
		this.uniqueCode = uniqueCode;
	}

	public AppRunnableException(Logger logger, Integer uniqueCode, Throwable e){
		super();
		this.logger = logger;
		this.uniqueCode = uniqueCode;
		this.e = e;
	}

	public AppRunnableException(Logger logger, Integer uniqueCode, Throwable e, String logMessage){
		super(logMessage);
		this.logger = logger;
		this.uniqueCode = uniqueCode;
		this.e = e;
	}
	
	public AppRunnableException(Logger logger, Integer uniqueCode, String logMessage){
		super(logMessage);
		this.logger = logger;
		this.uniqueCode = uniqueCode;
	}

}
