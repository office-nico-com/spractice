package com.office_nico.spractice.web.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.domain.Information;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.InformationService;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.DateTimeUtil;
import com.office_nico.spractice.web.form.DataTableForm;
import com.office_nico.spractice.web.form.InformationForm;

/**
 * 共有情報
 */
@Controller
public class InformationsController {

	private static final String PREFIX = "t06o1ny8/informations";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/informations/index";
		public static final String SHOW = "admin/informations/show";
		public static final String NEW = "admin/informations/new";
		public static final String EDIT = "admin/informations/edit";
	}

	private static final Logger logger = LoggerFactory.getLogger(InformationsController.class);

	@Autowired
	private InformationService informationService = null;

	/**
	 * Datatableの初期化
	 * @param res
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/init" })
	public String init(HttpServletResponse res) {
		// CookieをセットしてDatatabaleのリセットを行う
		// View側でDatatableのclearを実行する
		Cookie cookie = new Cookie("datatableclear", "true");
		cookie.setPath("/");
		res.addCookie(cookie);
		return "redirect:/" + PREFIX;
	}
	
	/**
	 * 情報共有管理トップ
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX })
	public String index(Model model) {
		try {
			
			return View.INDEX;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * Datatableリスト出力
	 * @param model
	 * @param req
	 * @param form
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/list" })
	@ResponseBody
	public Map<String, Object> list(Model model, HttpServletRequest req , @ModelAttribute DataTableForm form) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = new HashMap<>();

		try {
			String[] order = new String[] {"postedAt"};
			Page<Map<String, Object>> page = informationService.page(logger, sessionuser.getId(),  form.getStart(), form.getLength(), Sort.Direction.DESC, order);
			map.put("data", page.toList());
			map.put("recordsFiltered", page.getTotalElements());
			map.put("recordsTotal", page.getTotalElements());
		}
		catch (Exception e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		
		return map;
	}

	/**
	 * 情報共有登録画面
	 * @param model
	 * @param form
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/new" })
	public String newItem(Model model, @ModelAttribute("obj") InformationForm form) {
		
		
		return View.NEW;
	}

	
	/**
	 * 情報共有登録処理
	 * @param model
	 * @param form
	 * @param result
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/new" })
	public String create(Model model, @Validated @ModelAttribute("obj") InformationForm form, BindingResult result) {
		
		try {
			User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// 入力チェック
			if(result.hasErrors()) {
				return View.NEW;
			}
	
			// 登録処理
			if(form.getIsShowDashboard() == null) {
				form.setIsShowDashboard(Boolean.TRUE.toString());
			}
			Information information = BeanUtil.copyFields(form, new Information());
			information = informationService.create(logger, sessionuser.getId(), sessionuser.getId(), information);

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return "redirect:/" + PREFIX;
	}

	/**
	 * 情報共有照会画面
	 * @param model
	 * @param informationId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/show/{informationId}" })
	public String show(Model model, @PathVariable(name = "informationId", required = true) Long informationId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			Information information = informationService.get(logger, sessionuser.getId(), informationId);
			
			if(information == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			model.addAttribute("information", information);
			model.addAttribute("sessionUserId", sessionuser.getId());
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.SHOW;
	}

	
	/**
	 * 情報共有編集画面
	 * @param model
	 * @param form
	 * @param informationId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/edit/{informationId}" })
	public String edit(Model model, @ModelAttribute("obj") InformationForm form,  @PathVariable(name = "informationId", required = true) Long informationId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			Information information = informationService.get(logger, sessionuser.getId(), informationId);
			if(information == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			model.addAttribute("information", information);
			BeanUtil.copyFields(information, form);
			if(information.getShowStartedOn() != null) {
				form.setShowStartedOn(DateTimeUtil.format(information.getShowStartedOn(), DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_SUPPRESS));
			}
			if(information.getShowEndedOn() != null) {
				form.setShowEndedOn(DateTimeUtil.format(information.getShowEndedOn(), DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_SUPPRESS));
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.EDIT;

	}

	/**
	 * 情報共有編集処理
	 * @param model
	 * @param form
	 * @param result
	 * @param informationId
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/edit/{informationId}" })
	public String update(Model model, @Validated @ModelAttribute("obj") InformationForm form,  BindingResult result, @PathVariable(name = "informationId", required = true) Long informationId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			
			// 入力チェック
			if(result.hasErrors()) {
				return View.EDIT;
			}

			if(form.getIsShowDashboard() == null) {
				form.setIsShowDashboard(Boolean.TRUE.toString());
			}
			Information information = BeanUtil.copyFields(form, new Information());
			information = informationService.update(logger, sessionuser.getId(), informationId, information);

			if(information == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX + "/show/" + informationId;
	}

	/**
	 * 情報共有削除処理
	 * @param model
	 * @param informationId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/destroy/{informationId}" })
	public String destroy(Model model, @PathVariable(name = "informationId", required = true) Long informationId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			informationService.delete(logger, sessionuser.getId(), informationId);

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX;
	}
}
