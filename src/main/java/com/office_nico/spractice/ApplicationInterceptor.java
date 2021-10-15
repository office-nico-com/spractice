package com.office_nico.spractice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.LogUtil;

@Aspect
@Component
public class ApplicationInterceptor {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ApplicationInterceptor.class);

	@Autowired
	private SessionData sessionData = null;

	@Value("${build.version:xxxxxxx}")
	String version;

	@Autowired
	private MessageSource msg;

	@Before("execution(* com.office_nico.spractice.web.controller.*..*.*(..))")
	public void handleBeforeWeb(JoinPoint joinpoint) {

		// ログの出力とバージョン情報の出力
		Signature signature = joinpoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		Class<?> z = method.getDeclaringClass();
		Logger logger = LoggerFactory.getLogger(z);

		Object[] methodArgs = joinpoint.getArgs();
		for (Object methodArg : methodArgs) {
			if (methodArg instanceof Model) {
				((Model) methodArg).addAttribute("ver7u2hrvufd", version);
				break;
			}
		}

		String actionName = msg.getMessage("action.name." + z.getSimpleName() + "." + method.getName(), null, z.getSimpleName() + "." + method.getName(), Locale.JAPAN);
		String _message = msg.getMessage("message.action.start.1", new String[] { actionName, z.getSimpleName() + "." + method.getName() }, "dafault message", Locale.JAPAN);
		LogUtil.info(logger, null, _message);

		Object sessionuser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (sessionuser != null && sessionuser instanceof User && sessionData.getAdminFamilyName() == null) {
			sessionData.setAdminFamilyName(((User) sessionuser).getFamilyName());
			sessionData.setAdminGivenName(((User) sessionuser).getGivenName());
		}

	}

	@Before("execution(* com.office_nico.spractice.web.controller.*..*.*(..))")
	public void handleAfterWeb(JoinPoint joinpoint) {

		// ログの出力
		Signature signature = joinpoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		Class<?> z = method.getDeclaringClass();
		Logger logger = LoggerFactory.getLogger(z);

		String actionName = msg.getMessage("action.name." + z.getSimpleName() + "." + method.getName(), null, z.getSimpleName() + "." + method.getName(), Locale.JAPAN);
		String _message = msg.getMessage("message.action.end.1", new String[] { actionName, z.getSimpleName() + "." + method.getName() }, "default message", Locale.JAPAN);
		LogUtil.info(logger, null, _message);

	}

	@AfterThrowing(pointcut = "execution(* com.office_nico.spractice.web.controller.*..*.*(..))", throwing = "e")
	public void handleAfterThrowingWeb(JoinPoint joinpoint, Throwable e) {

		// Note:ログを出すための処理はインターセプターでやった方が情報がとれるし、画面を出す処理はExceptionハンドラーがResponseにタッチできるの処理しやすい。
		// そのため、インタセプター ⇒ ApplicationExceptionHandler と連動して処理している。

		// ページタイプをセッションにセット
		Signature signature = joinpoint.getSignature();
		if (signature instanceof MethodSignature) {

			// アノテーションからページ情報を取得してセッションにセットする
			// 画面出力処理はApplicationExceptionHandlerに任せる
			// ※ このAOPでは出力画面の結果に影響を与えることができないから
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			Class<?> z = method.getDeclaringClass();
			Logger logger = LoggerFactory.getLogger(z);

			String message = "";

			Annotation annotation = method.getDeclaredAnnotation(Action.class);
			if (annotation != null) {
				// セッションに画面タイプをわたして、ApplicationExceptionHandlerで処理する。
				sessionData.setActionType(((Action) annotation).value());
			}

			if (e instanceof ApplicationRuntimeException) {
				message = ((ApplicationRuntimeException) e).getApplocationMessage();
				String _message = LogUtil.format(e.getStackTrace(), 0, null, message, e);
				logger.error(_message);
			}
			else {
				if (annotation != null) {
					String actionName = msg.getMessage("action.name." + z.getSimpleName() + "." + method.getName(), null, z.getSimpleName() + "." + method.getName(), Locale.JAPAN);
					String _message = msg.getMessage("message.action.fail.1", new String[] { actionName }, "dafault message", Locale.JAPAN);
					message = _message;
				}
				else {
					message = msg.getMessage("message.action.fail.2", null, "dafault message", Locale.JAPAN);
				}
				String _message = LogUtil.format(e.getStackTrace(), 0, null, message, e);
				logger.error(_message);
			}
		}
	}
}
