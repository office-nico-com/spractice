package com.office_nico.spractice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.LogUtil;

@ControllerAdvice
public class ApplicationExceptionHandler {

	protected final static Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
	
	@Autowired
	private SessionData s = null;
	
	@Autowired
	private MessageSource msg;
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public String getException(HttpServletRequest req, HttpServletResponse res, Throwable e){
		
		String ret = null;
		
		Action.Type actionType = s.getActionType();
		if(actionType == null) {
			actionType = Action.Type.PARENT;
		}

		String exceptioMessage = null;

		StackTraceElement[] _elements = e.getStackTrace();
		Class<?> exceptionClass = e.getClass();
		if(e instanceof ApplicationRuntimeException) {

			if(((ApplicationRuntimeException)e).getCause() != null) {
				_elements =  ((ApplicationRuntimeException)e).getCause().getStackTrace();
				exceptionClass = ((ApplicationRuntimeException)e).getCause().getClass();
				if(((ApplicationRuntimeException)e).getApplocationMessage() != null) {
					exceptioMessage = ((ApplicationRuntimeException)e).getApplocationMessage();
				}
				if(((ApplicationRuntimeException)e).getCause().getMessage() != null) {

					if(exceptioMessage != null) {
						exceptioMessage += " : ";
					}
					exceptioMessage += ((ApplicationRuntimeException)e).getCause().getMessage();
				}
			}
			else {
				if(((ApplicationRuntimeException)e).getApplocationMessage() != null) {
					exceptioMessage = ((ApplicationRuntimeException)e).getApplocationMessage();
				}
			}
		}
		else{
			exceptioMessage = e.getMessage();
		}
		List<StackTraceElement> stackTraceElements =  Arrays.asList(_elements);
		
		
		if(actionType == Action.Type.PARENT) {
			ret = "error/500";
			List<String> messages = new ArrayList<>();
			if(exceptioMessage != null){
				messages.add(exceptioMessage);
			}
			messages.add(exceptionClass.toString());
			for (StackTraceElement stackTraceElement : stackTraceElements) {
				messages.add("　　at　" + stackTraceElement.toString());
			}
			req.setAttribute("errors", messages);
		}
		else if(actionType == Action.Type.AJAX) {
			// ajaxのエラーはクライアント側でHTTPエラーをハンドリングして処理すること。
			// 念の為jsonは返すが、ちゃんとクライアント側でエラーハンドリングすること。
			// 403とかは処理できないので。
			try {
				res.setHeader("Content-Type", "application/javascript; charset=UTF-8");
				String json = "{";
				json += "\"result\":-1, ";
				json += "\"trace\": [";
				boolean first = true;
				int count = 0;

				for (StackTraceElement stackTraceElement : stackTraceElements) {
					if(!first) {
						json += ",";
					}
					
					if(exceptioMessage != null){
						json += "\"" + exceptioMessage.replaceAll("\"", "") + "\"";
					}
					
					json += "\"" + exceptionClass.toString() + "\"";
					String c = stackTraceElement.getClassName();
					int dot = c.lastIndexOf(".");
					if(dot >= 0) {
						c = c.substring(dot + 1);
					}
					String m = stackTraceElement.getMethodName();
					json += "\"" + c + "." + m + "(" +  stackTraceElement.getLineNumber() + ")" + "\"";
					first = false;
					if(count > 10) {
						break;
					}
					count++;
				}
				json += "]";
				json += "}";
				res.getWriter().write(json);
			} catch (IOException e1) {
				LogUtil.error(logger, null, "json output error");
			}
			ret = null;
		}
		else if(actionType == Action.Type.DOWNLOAD) {
			
			String message = msg.getMessage("message.system.error.output.file", null, "dafault message", Locale.JAPAN);
			res.setContentLength(message.getBytes().length);
			res.setContentType("text/html; charset=UTF-8");
			res.setHeader("Content-Disposition","attachment;filename=\"error-download.txt\"");
			 try (PrintWriter pw = res.getWriter()) {
				res.getWriter().write(message);
			} catch (IOException e1) {
				LogUtil.error(logger, null, "text output error");
			}
			ret = null;
		}
		else if(actionType == Action.Type.CSV) {
			// CSVはライブラリの中でヘッダーを出力しているので処理しない
			ret = null;
		}


		s.setActionType(null);
		
		return ret;
	}
}

