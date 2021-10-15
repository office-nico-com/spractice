package com.office_nico.spractice.util;

import org.slf4j.Logger;

import com.office_nico.spractice.exception.ApplicationRuntimeException;

public class LogUtil {

	private static final int SOURCE_POINT_LEN = 40;
	private static final int SOURCE_LINE_LEN = 5;
	private static final int SOURCE_ACCOUNT_LEN = 10;
	private static final int STACK_TRACE_MAX = 60;

	/**
	 * デバッグ パス確認用
	 * @param logger ロガー
	 */
	public static void path(Logger logger) {
		String log = format("---", "path", null);
		logger.debug(log);
	}
	
	/**
	 * デバッグ パス確認用
	 * @param logger ロガー
	 * @param message メッセージ
	 */
	public static void path(Logger logger, String message) {
		String log = format("---", message, null);
		logger.debug(log);
	}

	/**
	 * DEBUGログ
	 * @param logger ロガー
	 * @param account アカウント
	 * @param message メッセージ
	 */
	public static void debug(Logger logger, String account, String message) {
		String log = format(account, message, null);
		logger.debug(log);
	}

	/**
	 * INFOログ
	 * @param logger ロガー
	 * @param account アカウント
	 * @param message メッセージ
	 */
	public static void info(Logger logger, String account, String message) {
		String log = format(account, message, null);
		logger.info(log);
	}

	/**
	 * WARNログ
	 * @param logger ロガー
	 * @param account アカウント
	 * @param message メッセージ
	 */
	public static void warn(Logger logger, String account, String message) {
		String log = format(account, message, null);
		logger.warn(log);
	}

	/**
	 * WARNログ
	 * @param logger ロガー
	 * @param account アカウント
	 * @param message メッセージ
	 * @param e 例外
	 */
	public static void warn(Logger logger, String account, String message, Throwable e) {
		String log = format(account, message, e);
		logger.warn(log);
	}

	
	/**
	 * ERRORログ
	 * @param logger ロガー
	 * @param account アカウント
	 * @param message メッセージ
	 */
	public static void error(Logger logger, String account, String message) {
		String log = format(account, message, null);
		logger.error(log);
	}

	/**
	 * ERRORログ
	 * @param logger ロガー
	 * @param account アカウント
	 * @param message メッセージ
	 * @param e 例外
	 */
	public static void error(Logger logger, String account, String message, Throwable e) {
		String log = format(account, message, e);
		logger.error(log);
	}

	/**
	 * ログフォーマット
	 * @param account アカウント 
	 * @param message メッセージ
	 * @param e 例外
	 * @return ログ出力文字列
	 */
	public static String format(String account, String message, Throwable e) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		return  format(elements, 3, account, message, e);
	}
	
	/**
	 * ログフォーマット
	 * @param tracePos ログ出力元のスタックトレース位置
	 * @param account アカウント
	 * @param message メッセージ
	 * @param e 例外
	 * @return ログ出力文字列
	 */
	public static String format(StackTraceElement[] elements, int tracePos, String account, String message, Throwable e) {

		String c = elements[tracePos].getClassName();
		int dot = c.lastIndexOf(".");
		if(dot >= 0) {
			c = c.substring(dot + 1);
		}
		String m = elements[tracePos].getMethodName();

		String sourcePoint = c + "." + m + "()";
		for(int i = 0; i< SOURCE_POINT_LEN + 10; i++) {
			sourcePoint += " ";
		}
		sourcePoint = sourcePoint.substring(0, SOURCE_POINT_LEN);
		
		String sourceLine = String.valueOf(elements[tracePos].getLineNumber());
		for(int i = sourceLine.length(); i< SOURCE_LINE_LEN; i++) {
			sourceLine = " " + sourceLine;
		}

		if(account == null || account.length() == 0) {
			account = "unknown";
		}
		for(int i = 0; i< SOURCE_ACCOUNT_LEN + 10; i++) {
			account += " ";
		}
		account = account.substring(0, SOURCE_ACCOUNT_LEN);

		if(message == null) {
			message = "---";
		}
		String ret =  "[" + account + "] " + sourcePoint + " | " + sourceLine + " | " + message;
		if(e != null) {
			ret += System.getProperty("line.separator");
			
			if(e instanceof ApplicationRuntimeException) {
				if(((ApplicationRuntimeException)e).getCause() == null) {
					if(e.getMessage() != null){
						ret += e.getMessage() + System.getProperty("line.separator");
					}
					ret += toString(e);
				}
				else {
					ret += e.getClass().toString();
					ret += System.getProperty("line.separator");
					
					StackTraceElement[] elements2 = e.getStackTrace();
					for (int i = 0; i < 5; i++) {
						ret +=  "    at " + elements2[i].toString();
						ret +=  System.getProperty("line.separator");
					}
					ret +=  System.getProperty("line.separator");

					if(e.getMessage() != null){
						ret += e.getMessage() + System.getProperty("line.separator");
					}
					ret += toString(((ApplicationRuntimeException)e).getCause());
				}
			}
			else {
				if(e.getMessage() != null){
					ret += e.getMessage() + System.getProperty("line.separator");
				}
				ret += toString(e);
			}
		}
		return ret;
	}

	/**
	 * スタックトレースを文字列にして返す
	 * @param e 例外
	 * @return スタックトレース
	 */
	public static final String toString(Throwable e) {

		String message = "";
		message += e.getClass().toString();
		message += System.getProperty("line.separator");
		StackTraceElement[] elements = e.getStackTrace();
		int max = elements.length;
		if(STACK_TRACE_MAX > 0) {
			if(max > STACK_TRACE_MAX) {
				max = STACK_TRACE_MAX;
			}
		}

		for (int i = 0; i < max; i++) {
			message += "    at " + elements[i].toString();
			message += System.getProperty("line.separator");
		}
		return message;
	}
}
