package com.office_nico.spractice.service;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.office_nico.spractice.exceptions.AppRunnableException;
import com.office_nico.spractice.service.annotation.ValidateSession;
import com.office_nico.spractice.service.data.SessionData;




@Aspect
@Component
public class ServiceInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ServiceInterceptor.class);

	@Autowired
	private SessionData session = null;
	
	
	
	@Before("execution(* com.office_nico.spractice.service.*.*(..))")
	public void startLog(JoinPoint jp) {
			
		// セッションの有無チェック
		// メソッドに設定されている方を優先する
		
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Class<?> clazz = jp.getTarget().getClass();
		Method method = signature.getMethod();
		ValidateSession classAnnotation = clazz.getAnnotation(ValidateSession.class);
		ValidateSession methodAnnotation = method.getAnnotation(ValidateSession.class);

		if(classAnnotation != null && classAnnotation.value()) {
			if(methodAnnotation == null || methodAnnotation.value()) {
				// チェックする
				if(session.getOrganizationId() == null || session.getAccount() == null || session.getUserId() == null) {
					String message = "The session is empty. class=" + clazz.getName() + " method=" + method.getName() + "()";
					throw new AppRunnableException(logger, AppRunnableException.DEFAULT_ERROR_CODE, message);
				}
			}
		}
		else {
			if(methodAnnotation != null && methodAnnotation.value()) {
				// チェックする
				String message = "The session is empty. class=" + clazz.getName() + " method=" + method.getName() + "()";
				throw new AppRunnableException(logger, AppRunnableException.DEFAULT_ERROR_CODE, message);
			}
		}
	}
}
