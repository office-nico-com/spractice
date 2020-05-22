package com.office_nico.spractice.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.exceptions.AppRunnableException;

@Service
@Transactional
public class LogService {

	private static int STACKTRACE_MAX=20;

	public void error(Logger logger, String account, Throwable e) {
		log(1, logger, account, e);
	}

	public void warn(Logger logger, String account, Throwable e) {
		log(2, logger, account, e);
	}

	public void error(Logger logger, String account, Integer uniqueCode, String _message) {
		log(1, logger, account,uniqueCode,  _message);
	}

	public void warn(Logger logger, String account, String _message) {
		log(2, logger, account, _message);
	}

	public void info(Logger logger, String account, String _message) {
		log(3, logger, account, _message);
	}

	public void debug(Logger logger, String account, String _message) {
		log(4, logger, account, _message);
	}
	
	private void log(int logLevel, Logger logger, String account, Throwable e) {

		if(e == null) {
			return;
		}

		String message = "";
		int uniqueCode = 9000;

		if(account == null || account.length() == 0){
			account = "unknown";
		}

		if(e instanceof AppRunnableException){
			message += "[" + account + "] ";

			AppRunnableException appRunnableException = ((AppRunnableException)e);
			log(logLevel, appRunnableException.getLogger(), account, appRunnableException.getE());
			
			uniqueCode = ((AppRunnableException)e).getUniqueCode();
			message += "[" + String.format("%04d", uniqueCode) + "] ";
			if(appRunnableException.getMessage() != null){
				message +=appRunnableException.getMessage();
			}
			else{
				message += "There is a problem in the system.";
			}
			int traceCount = STACKTRACE_MAX;
			if(!appRunnableException.isPrintTtrace()) {
				traceCount = 0;
			}
			message += System.getProperty("line.separator");
			message += conversion(appRunnableException, traceCount);

		}
		else {

			if(e.getMessage() != null){
				message +=e.getMessage();
			}
			else{
				message += "There is a problem in the system.";
			}
			message += System.getProperty("line.separator");
			message += conversion(e, STACKTRACE_MAX);
		}
		
		if(logLevel == 1) {
			logger.error(message);
		}
		else if(logLevel == 2){
			logger.warn(message);
		}
		else if(logLevel == 3) {
			logger.info(message);
		}
		else {
			logger.debug(message);
		}
	}

	private void log(int logLevel, Logger logger, String account, Integer uniqueCode, String _message) {
		String message = "";
		if(account == null || account.length() == 0){
			account = "unknown";
		}
		message += "[" + account + "]   ";
		message += "[" + String.format("%04d", uniqueCode) + "]";
		message +=_message;
		if(logLevel == 1) {
			logger.error(message);
		}
		else if(logLevel == 2){
			logger.warn(message);
		}
		else if(logLevel == 3) {
			logger.info(message);
		}
		else {
			logger.debug(message);
		}

	}

	private void log(int logLevel, Logger logger, String account, String _message) {
		String message = "";
		if(account == null || account.length() == 0){
			account = "unknown";
		}
		message += "[" + account + "]   ";
		message +=_message;

		if(logLevel == 1) {
			logger.error(message);
		}
		else if(logLevel == 2){
			logger.warn(message);
		}
		else if(logLevel == 3) {
			logger.info(message);
		}
		else {
			logger.debug(message);
		}
	}
	
	/**
	 * スタックトレースを文字列にして返す
	 * @param e 例外
	 * @return スタックトレース
	 */
	public static final String conversion(Throwable e, int count) {
		String message = "";
		message += e.getClass().toString();
		message += System.getProperty("line.separator");
		StackTraceElement[] elements = e.getStackTrace();
		for (int i = 0; i < count; i++) {
			message += "    at " + elements[i].toString();
			message += System.getProperty("line.separator");
		}
		return message;
	}
	
}
