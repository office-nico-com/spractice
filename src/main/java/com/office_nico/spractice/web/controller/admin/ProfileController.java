package com.office_nico.spractice.web.controller.admin;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.constants.codes.AdminFunctionCode;
import com.office_nico.spractice.constants.codes.AdminOperationCode;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.UserService;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.web.form.ProfileForm;
import com.office_nico.spractice.web.form.UserForm;


@Controller
public class ProfileController {

	private static final String PREFIX = "t06o1ny8/profile";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/profile/index";
	}

	private static final Logger logger = LoggerFactory.getLogger(ClientsController.class);

	@Autowired
	private UserService userService = null;

	@Autowired
	private ClientService clientService = null;

	@Autowired
	private SessionData sessionData = null;
	
	@Autowired
	private AdminOperationService adminOperationService = null;

	@Autowired
	private AdminFunctionCode adminFunctionCode = null;

	@Autowired
	private AdminOperationCode adminOperationCode = null;

	/**
	 * ユーザー編集画面
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX})
	public String edit(Model model, @ModelAttribute("obj") UserForm form) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			User user = userService.get(logger, sessionuser.getId(), sessionuser.getId());
			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			BeanUtil.copyFields(user, form);

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.INDEX;

	}

	/**
	 * ユーザー編集処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX })
	public String update(Model model, @Validated @ModelAttribute("obj") ProfileForm form,  BindingResult result, HttpServletResponse res) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {

			// 入力チェック
			if(result.hasErrors()) {
				model.addAttribute("clients", clientService.list(logger, sessionuser.getId()));
				return View.INDEX;
			}

			User user = BeanUtil.copyFields(form, new User());

			user = userService.updateProfile(logger, sessionuser.getId(), sessionuser.getId(), user);
			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}

			// セッション更新
			sessionData.setAdminFamilyName(user.getFamilyName());
			sessionData.setAdminGivenName(user.getGivenName());

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.profile, adminOperationCode.update, sessionuser.getId(), "");

			// 完了フラグをセット
			Cookie cookie = new Cookie("compleate", "true");
			cookie.setPath("/");
			res.addCookie(cookie);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX;
	}
}

