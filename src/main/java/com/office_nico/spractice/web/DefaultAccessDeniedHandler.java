package com.office_nico.spractice.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import com.office_nico.spractice.util.RequestUtil;

public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		if (response.isCommitted()) {
			return;
		}

		// csrfによるアクセス拒否の場合は、ステータスコード403を返してシンプルなエラーページを表示する
		// それ以外（おそらく権限エラー）の場合は、権限エラーページにリダイレクトする
		if (accessDeniedException instanceof MissingCsrfTokenException || accessDeniedException instanceof InvalidCsrfTokenException) {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			if (!RequestUtil.isAjaxRequest(request)) {
				try (PrintWriter writer = response.getWriter()) {
					writer.println("<!DOCTYPE html>");
					writer.println("<html lang=\"ja\">");
					writer.println("<head><title>Access Denied</title></head>");
					writer.println("<body>");
					writer.println("Access Denied!!");
					writer.println("</body>");
					writer.println("</html>");
				}
			}
			else {
				// TODO:
				redirectStrategy.sendRedirect(request, response, "error");
			}
		}
	}
}
