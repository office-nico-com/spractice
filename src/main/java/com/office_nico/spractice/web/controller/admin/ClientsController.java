package com.office_nico.spractice.web.controller.admin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.office_nico.spractice.constants.codes.UserRegistTypeCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.web.form.ClientForm;
import com.office_nico.spractice.web.form.DataTableForm;


/**
 * クライアント管理
 */
@Controller
public class ClientsController {

	private static final String PREFIX = "t06o1ny8/clients";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/clients/index";
		public static final String SHOW = "admin/clients/show";
		public static final String NEW = "admin/clients/new";
		public static final String EDIT = "admin/clients/edit";
	}

	private static final Logger logger = LoggerFactory.getLogger(ClientsController.class);


	@Autowired
	private ClientService clientService = null;
	
	@Autowired
	private MessageSource messageSource = null;
	
	@Autowired
	private UserRegistTypeCode userRegistTypeCode = null;
	
	@Autowired
	private InvalidStatus invalidStatus = null;

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
	 * クライアント管理トップ
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
			
			String[] order = form.getOrderString("id", "clientKeycode", new String[] {"clientNameJaKana", "clientKeycode"}, new String[] {"userRegistTypeCode","clientNameJaKana", "clientKeycode"}, new String[] {"isInvalided","clientNameJaKana", "clientKeycode"});

			String search = null;
			if(form.getSearch() != null && form.getSearch().get("value") != null) {
				search = form.getSearch().get("value");
			}
			
			Page<Client> page = clientService.page(logger, sessionuser.getId(), search, form.getStart(), form.getLength(), form.getOrderDirecton(), order);
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
	 * クライアント登録画面
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/new" })
	public String newItem(Model model, @ModelAttribute("obj") ClientForm form) {
		
		// User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			form.setClientKeycode(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
			form.setUserRegistTypeCode(String.valueOf(userRegistTypeCode.systemLink));
			form.setIsInvalided(String.valueOf(invalidStatus.Valid));
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.NEW;
	}

	/**
	 * クライアント登録処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/new" })
	public String create(Model model, @Validated @ModelAttribute("obj") ClientForm form, BindingResult result) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			// クライアントコードの重複チェック
			if(form.getClientKeycode() != null && form.getClientKeycode() .length() > 0 && clientService.isDuplidate(logger, sessionuser.getId(), form.getClientKeycode())) {
				String fieldName = messageSource.getMessage("clientKeycode", null, Locale.getDefault());
				result.rejectValue("clientKeycode", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				return View.NEW;
			}
	
			// 登録処理
			Client client = BeanUtil.copyFields(form, new Client());
			client = clientService.create(logger, sessionuser.getId(), client);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.clients, adminOperationCode.create, client.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return "redirect:/" + PREFIX;
	}

	/**
	 * クライアント照会画面
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/show/{clientId}" })
	public String show(Model model, @PathVariable(name = "clientId", required = true) Long clientId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			Client client = clientService.get(logger, sessionuser.getId(), clientId);
			if(client == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			model.addAttribute("client", client);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.SHOW;
	}

	
	/**
	 * クライアント編集画面
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/edit/{clientId}" })
	public String edit(Model model, @ModelAttribute("obj") ClientForm form,  @PathVariable(name = "clientId", required = true) Long clientId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			Client client = clientService.get(logger, sessionuser.getId(), clientId);
			if(client == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			BeanUtil.copyFields(client, form);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.EDIT;

	}

	/**
	 * クライアント編集処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/edit/{clientId}" })
	public String update(Model model, @Validated @ModelAttribute("obj") ClientForm form,  BindingResult result, @PathVariable(name = "clientId", required = true) Long clientId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			
			// クライアントコードの重複チェック
			if(form.getClientKeycode() != null && form.getClientKeycode() .length() > 0 && clientService.isDuplidate(logger, sessionuser.getId(), form.getClientKeycode(), clientId)) {
				String fieldName = messageSource.getMessage("clientKeycode", null, Locale.getDefault());
				result.rejectValue("clientKeycode", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}
			
			// 入力チェック
			if(result.hasErrors()) {
				return View.EDIT;
			}

			Client client = BeanUtil.copyFields(form, new Client());
			client = clientService.update(logger, sessionuser.getId(), clientId, client);

			if(client == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.clients, adminOperationCode.update, client.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX + "/show/" + clientId;
	}

	/**
	 * クライアント削除処理
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/destroy/{clientId}" })
	public String destroy(Model model, @PathVariable(name = "clientId", required = true) Long clientId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			clientService.delete(logger, sessionuser.getId(), clientId);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.clients, adminOperationCode.delete, clientId, "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX;
	}
}
