package com.office_nico.spractice.web.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.web.form.IndexForm;

@Controller
public class LoginController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
  PasswordEncoder passwordEncoder;

	@ModelAttribute
	public IndexForm initForm() {
		return new IndexForm();
	}
	

	@GetMapping(value = { "t06o1ny8", "t06o1ny8/login" })
	public String index(Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, @RequestParam(value = "timeout", required = false) String timeout, HttpSession session, @AuthenticationPrincipal User user) {

		// String a = passwordEncoder.encode("passwd");
		
		if (error != null) {
			if (session != null) {
				AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				if (ex != null) {
					model.addAttribute("errorMessage", ex.getMessage());
				}
			}
		}
		else if(logout != null) {
			model.addAttribute("logoutMessage", messageSource.getMessage("authenticate.logouted", null, "dafault message", Locale.JAPAN));
		}
		else if(timeout != null) {
			model.addAttribute("timeoutMessage", messageSource.getMessage("authenticate.timeout", null, "dafault message", Locale.JAPAN));
		}
		else {
			if(user != null && user.getId() != null) {
				return "redirect:/t06o1ny8/my";
			}
		}
		return "admin/login/index";
	}
}
