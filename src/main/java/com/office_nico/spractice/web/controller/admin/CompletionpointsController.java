package com.office_nico.spractice.web.controller.admin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
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
import com.office_nico.spractice.constants.InvalidStatus;
import com.office_nico.spractice.constants.codes.AdminFunctionCode;
import com.office_nico.spractice.constants.codes.AdminOperationCode;
import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.CompletionService;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.web.form.CompletionPointForm;
import com.office_nico.spractice.web.form.DataTableForm;

/**
 * 履修ポイント管理
 */
@Controller
public class CompletionpointsController {

	private static final String PREFIX = "t06o1ny8/completionpoints";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/completionpoints/index";
		public static final String SHOW = "admin/completionpoints/show";
		public static final String NEW = "admin/completionpoints/new";
		public static final String EDIT = "admin/completionpoints/edit";
	}

	private static final Logger logger = LoggerFactory.getLogger(CompletionpointsController.class);

	
	@Autowired
	private MessageSource messageSource = null;
	
	@Autowired
	private InvalidStatus invalidStatus = null;
	
	
	@Autowired
	private CompletionService completionService = null;

	@Autowired
	private AdminOperationService adminOperationService = null;

	@Autowired
	private AdminFunctionCode adminFunctionCode = null;

	@Autowired
	private AdminOperationCode adminOperationCode = null;

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
	 * 履修ポイント管理トップ
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
	public Map<String, Object> list(Model model, HttpServletRequest  req  , @ModelAttribute DataTableForm form) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = new HashMap<>();
		
		try {
			String[] order = form.getOrderString("id", "completionPointKeycode");
			Page<CompletionPoint> page = completionService.page(logger, sessionuser.getId(), form.getStart(), form.getLength(), form.getOrderDirecton(), order);
			map.put("data", page.toList());
			map.put("recordsFiltered", page.getTotalElements());
			map.put("recordsTotal", page.getTotalElements());
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		
		return map;
	}
	
	
	/**
	 * 履修ポイント登録画面
	 * @param model
	 * @param form
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/new" })
	public String newItem(Model model, @ModelAttribute("obj") CompletionPointForm form) {

		try {
			form.setIsInvalided(String.valueOf(invalidStatus.Valid));
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		
		return View.NEW;
	}

	
	/**
	 * 履修ポイント登録処理
	 * @param model
	 * @param form
	 * @param result
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/new" })
	public String create(Model model, @Validated @ModelAttribute("obj") CompletionPointForm form, BindingResult result) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			// 履修ポイントコードの重複チェック
			if(form.getCompletionPointKeycode() != null && form.getCompletionPointKeycode() .length() > 0 && completionService.isDuplicate(logger, sessionuser.getId(), form.getCompletionPointKeycode())) {
				String fieldName = messageSource.getMessage("completionPointKeycode", null, Locale.getDefault());
				result.rejectValue("scenarioKeycode", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				return View.NEW;
			}
	
			// 登録処理
			CompletionPoint completionPoint = BeanUtil.copyFields(form, new CompletionPoint());
			completionPoint = completionService.create(logger, sessionuser.getId(), completionPoint);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.completionpoints, adminOperationCode.create, completionPoint.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return "redirect:/" + PREFIX;
	}

	/**
	 * 履修ポイント照会画面
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/show/{completionPointId}" })
	public String show(Model model, @PathVariable(name = "completionPointId", required = true) Long completionPointId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			CompletionPoint completionPoint = completionService.get(logger, sessionuser.getId(), completionPointId);
			if(completionPoint == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			model.addAttribute("completionPoint", completionPoint);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.SHOW;
	}

	
	/**
	 * 履修ポイント編集画面
	 * @param model
	 * @param form
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/edit/{completionPointId}" })
	public String edit(Model model, @ModelAttribute("obj") CompletionPointForm form,  @PathVariable(name = "completionPointId", required = true) Long completionPointId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			CompletionPoint completionPoint = completionService.get(logger, sessionuser.getId(), completionPointId);
			if(completionPoint == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			BeanUtil.copyFields(completionPoint, form);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.EDIT;

	}

	/**
	 *履修ポイント編集処理
	 * @param model
	 * @param form
	 * @param result
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/edit/{completionPointId}" })
	public String update(Model model, @Validated @ModelAttribute("obj") CompletionPointForm form,  BindingResult result, @PathVariable(name = "completionPointId", required = true) Long completionPointId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			
			// シナリオコードの重複チェック
			if(form.getCompletionPointKeycode() != null && form.getCompletionPointKeycode() .length() > 0 && completionService.isDuplicate(logger, sessionuser.getId(), form.getCompletionPointKeycode(), completionPointId)) {
				String fieldName = messageSource.getMessage("completionPointKeycode", null, Locale.getDefault());
				result.rejectValue("completionPointKeycode", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				return View.EDIT;
			}

			CompletionPoint completionPoint = BeanUtil.copyFields(form, new CompletionPoint());
			completionPoint = completionService.update(logger, sessionuser.getId(), completionPointId, completionPoint);

			if(completionPoint == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.completionpoints, adminOperationCode.update, completionPoint.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX + "/show/" + completionPointId;
	}

	/**
	 *履修ポイント削除処理
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/destroy/{completionPointId}" })
	public String destroy(Model model, @PathVariable(name = "completionPointId", required = true) Long completionPointId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			completionService.delete(logger, sessionuser.getId(), completionPointId);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.completionpoints, adminOperationCode.delete, completionPointId, "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX;
	}
}


