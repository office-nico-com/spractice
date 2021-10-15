package com.office_nico.spractice.web.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.domain.Information;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.InformationService;
import com.office_nico.spractice.service.data.AdminOperationData;


@Controller
public class MyController {

	private static final String PREFIX = "t06o1ny8/my";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/my/index";
	}


	private static final Logger logger = LoggerFactory.getLogger(MyController.class);

	@Autowired
	private AdminOperationService adminOperationService = null;

	@Autowired
	private InformationService informationService = null;

	/**
	 * ダッシュボードトップ
	 * @param model
	 * @param binaryFileCategoryId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX })
	public String index(Model model) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			List<Information> informations = informationService.list(logger, sessionuser.getId());
			model.addAttribute("informations", informations);
			
			return View.INDEX;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	

	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/list1"})
	@ResponseBody
	public List<AdminOperationData> list1(Model model, Integer p) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			List<AdminOperationData> messages = adminOperationService.list(logger, sessionuser.getId(), sessionuser.getId(), p, 10);
			return messages;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/list2"})
	@ResponseBody
	public List<AdminOperationData> list2(Model model, Integer p) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			List<AdminOperationData> messages = adminOperationService.list(logger, sessionuser.getId(), null, p, 10);
			return messages;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
}
