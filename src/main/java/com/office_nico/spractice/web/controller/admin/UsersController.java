package com.office_nico.spractice.web.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.office_nico.spractice.constants.codes.UserRegistTypeCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientUser;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.LogService;
import com.office_nico.spractice.service.UserService;
import com.office_nico.spractice.service.data.CsvImportResult;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.web.form.DataTableForm;
import com.office_nico.spractice.web.form.UploadForm;
import com.office_nico.spractice.web.form.UserClient;
import com.office_nico.spractice.web.form.UserForm;


@Controller
public class UsersController {

	private static final String PREFIX = "t06o1ny8/users";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/users/index";
		public static final String SHOW = "admin/users/show";
		public static final String NEW = "admin/users/new";
		public static final String EDIT = "admin/users/edit";
		public static final String IMPORT = "admin/users/import";
	}

	private static final Logger logger = LoggerFactory.getLogger(ClientsController.class);

	@Autowired
	private MessageSource messageSource = null;
	
	@Autowired
	private UserService userService = null;

	@Autowired
	private ClientService clientService = null;

	@Autowired
	private InvalidStatus invalidStatus = null;

	@Autowired
	private UserRegistTypeCode userRegistTypeCode = null;

	@Autowired
	private LogService logService = null;
	
	@Autowired
	private SessionData sessionData = null;
	
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
	 * ユーザー管理トップ
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
			String[] order = form.getOrderString("id", "account", new String[] {"family_name_kana", "given_name_kana"}, "", new String[] {"registered_from_code","family_name_kana", "given_name_kana"} , new String[] {"is_admin","family_name_kana", "given_name_kana"} , new String[] {"is_invalided","family_name_kana", "given_name_kana"});

			String search = null;
			if(form.getSearch() != null && form.getSearch().get("value") != null) {
				search = form.getSearch().get("value");
			}
			Page<User> page = userService.page(logger, sessionuser.getId(), search, form.getStart(), form.getLength(), form.getOrderDirecton(), order);
			List<User> users = page.toList();
			map.put("data", users);
			map.put("recordsFiltered", page.getTotalElements());
			map.put("recordsTotal", page.getTotalElements());
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return map;
	}
	
	
	/**
	 * ユーザー登録画面
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/new" })
	public String newItem(Model model, @ModelAttribute("obj") UserForm form) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		form.setIsAdmin(String.valueOf(false));
		form.setIsInvalided(String.valueOf(invalidStatus.Valid));
		
		model.addAttribute("clients", clientService.list(logger, sessionuser.getId()));
		return View.NEW;
	}

	/**
	 * ユーザー登録処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/new" })
	public String create(Model model, @Validated @ModelAttribute("obj") UserForm form, BindingResult result) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {

			// 重複チェック
			if(form.getAccount() != null && form.getAccount() .length() > 0 && userService.isDuplidate(logger, sessionuser.getId(), form.getAccount())) {
				String fieldName = messageSource.getMessage("account", null, Locale.getDefault());
				result.rejectValue("account", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				model.addAttribute("clients", clientService.list(logger, sessionuser.getId()));
				return View.NEW;
			}
			
			// 登録処理
			User user = BeanUtil.copyFields(form, new User());
			user.setRegisteredFromCode(userRegistTypeCode.manual);

			List<ClientUser> clientUserList = new ArrayList<>();
			for(UserClient client : form.getClients()) {
				if(client.getClientId() != null && client.getClientId().length() > 0) {
					ClientUser clientUser = new ClientUser();
					clientUser.setUser(user);
					Client _client = new Client();
					_client.setId(Long.parseLong(client.getClientId()));
					clientUser.setClient(_client);
					clientUser.setRoleCode(Short.valueOf(client.getRoleCode()));
					clientUserList.add(clientUser);
				}
			}
			user = userService.create(logger, sessionuser.getId(), user, clientUserList);
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.users, adminOperationCode.create, user.getId(), "");
			
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return "redirect:/" + PREFIX;
	}

	/**
	 * ユーザー照会画面
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/show/{userId}" })
	public String show(Model model, @PathVariable(name = "userId", required = true) Long userId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			User user = userService.get(logger, sessionuser.getId(), userId);
			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			model.addAttribute("user", user);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.SHOW;
	}

	
	/**
	 * ユーザー編集画面
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/edit/{userId}" })
	public String edit(Model model, @ModelAttribute("obj") UserForm form,  @PathVariable(name = "userId", required = true) Long userId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 照会
			User user = userService.get(logger, sessionuser.getId(), userId);
			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			BeanUtil.copyFields(user, form);

			List<Client> clients = clientService.list(logger, sessionuser.getId());
			for(Client client  : clients) {
				UserClient userClient = new UserClient();
				form.getClients().add(userClient);
				
				for(ClientUser clientUser  : user.getClientUsers()) {
					if(client.getId().equals(clientUser.getClient().getId())) {
						userClient.setClientId(String.valueOf(client.getId()));
						userClient.setRoleCode(String.valueOf(clientUser.getRoleCode()));
					}
				}
			}
			model.addAttribute("clients", clients);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.EDIT;

	}

	/**
	 * ユーザー編集処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/edit/{userId}" })
	public String update(Model model, @Validated @ModelAttribute("obj") UserForm form,  BindingResult result, @PathVariable(name = "userId", required = true) Long userId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {

			// アカウントの重複チェック
			if(form.getAccount() != null && form.getAccount() .length() > 0 && userService.isDuplidate(logger, sessionuser.getId(), form.getAccount(), userId)) {
				String fieldName = messageSource.getMessage("account", null, Locale.getDefault());
				result.rejectValue("account", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				model.addAttribute("clients", clientService.list(logger, sessionuser.getId()));
				return View.EDIT;
			}

			User user = BeanUtil.copyFields(form, new User());
			List<ClientUser> clientUserList = new ArrayList<>();
			for(UserClient client : form.getClients()) {
				if(client.getClientId() != null && client.getClientId().length() > 0) {
					ClientUser clientUser = new ClientUser();
					clientUser.setUser(user);
					Client _client = new Client();
					_client.setId(Long.parseLong(client.getClientId()));
					clientUser.setClient(_client);
					clientUser.setRoleCode(Short.valueOf(client.getRoleCode()));
					clientUserList.add(clientUser);
				}
			}
			user = userService.update(logger, sessionuser.getId(), userId, user, clientUserList);

			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.users, adminOperationCode.update, user.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX + "/show/" + userId;
	}

	/**
	 * ユーザー削除処理
	 * @param model
	 * @param clientId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/destroy/{userId}" })
	public String destroy(Model model, @PathVariable(name = "userId", required = true) Long userId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			userService.delete(logger, sessionuser.getId(), userId);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.users, adminOperationCode.delete, userId, "");
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX;
	}
	
	
	/**
	 * 所属している組織の一覧を返す
	 * @param model
	 * @param userId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/client/list/{userId}" })
	@ResponseBody
	public List<Map<String, Object>> clientList(Model model, @PathVariable(name = "userId", required = true) Long userId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<Map<String, Object>> ret = new ArrayList<>();

		try {
			List<ClientUser> clientUsers =  userService.getAffiliatedClients(logger, sessionuser.getId(), userId);
			for(ClientUser clientUser : clientUsers) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", clientUser.getClient().getId());
				map.put("clientNameJa", clientUser.getClient().getClientNameJa());
				map.put("clientNameJaKana", clientUser.getClient().getClientNameJaKana());
				map.put("clientKeycode", clientUser.getClient().getClientKeycode());
				map.put("userRegistTypeCode", clientUser.getClient().getUserRegistTypeCode());
				map.put("description", clientUser.getClient().getDescription());
				map.put("isInvalided", clientUser.getClient().getIsInvalided());
				map.put("isDeleted", clientUser.getClient().getIsDeleted());
				map.put("roleCode", clientUser.getRoleCode());
				ret.add(map);
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return ret;
	}
	
	
	/**
	 * ユーザー一括登録
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/import" })
	public String importCsv(Model model, @ModelAttribute("obj") UploadForm uploadForm) {
		try {

			if(sessionData.getMessages() != null) {
				model.addAttribute("messages", sessionData.getMessages());
			}
			sessionData.setMessages(null);
			
			return View.IMPORT;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * ユーザー一括登録処理
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/import" })
	public String uplopadCsv(Model model, UploadForm uploadForm, HttpServletResponse res) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// エラー ファイル未指定
			if (uploadForm.getFile().isEmpty()) {
				Cookie cookie = new Cookie("result", "nofile");
				cookie.setPath("/");
				res.addCookie(cookie);
				return "redirect:/" + PREFIX + "/import";
			}
			
			String fileName = uploadForm.getFile().getOriginalFilename();
			byte[] body = uploadForm.getFile().getBytes();
			String uploadDate = new String(body, "MS932");

			CsvImportResult csvImportResult = userService.bulkSave(logger, sessionuser.getId(), fileName, uploadDate);
			
			for(String result : csvImportResult.getMessages()) {
				logService.warn(logger, null, result);
			}
			if(csvImportResult.getResult() == CsvImportResult.RESULT_ERROR) {
				Cookie cookie = new Cookie("result", "error");
				cookie.setPath("/");
				sessionData.setMessages(csvImportResult.getMessages());
				res.addCookie(cookie);
			}
			else if(csvImportResult.getResult() == CsvImportResult.RESULT_FAIL) {
				Cookie cookie = new Cookie("result", "fail");
				cookie.setPath("/");
				res.addCookie(cookie);
			}
			else {
				Cookie cookie = new Cookie("result", "success");
				cookie.setPath("/");
				res.addCookie(cookie);

			
				// オペレーションログ出力
				adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.importUser, adminOperationCode.create, null, fileName);
			}
			
			return "redirect:/" + PREFIX + "/import";
		}
		catch(Throwable e) {
			logService.error(logger, null, e);
			Cookie cookie = new Cookie("result", "fail");
			cookie.setPath("/");
			res.addCookie(cookie);
			return "redirect:/" + PREFIX + "/import";
		}
	}
}
