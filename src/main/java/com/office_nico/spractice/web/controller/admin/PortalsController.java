package com.office_nico.spractice.web.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.constants.codes.AdminFunctionCode;
import com.office_nico.spractice.constants.codes.AdminOperationCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientScenario;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.data.KeyValue;
import com.office_nico.spractice.web.form.ClientForm;
import com.office_nico.spractice.web.form.DataTableForm;
import com.office_nico.spractice.web.form.ScenarioForm;


@Controller
public class PortalsController {

	private static final String PREFIX = "t06o1ny8/portals";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/portals/index";
		public static final String SHOW = "admin/portals/show";
		public static final String UPLOAD = "admin/portals/upload";
	}

	private static final Logger logger = LoggerFactory.getLogger(PortalsController.class);

	
	@Autowired
	private ClientService clientService = null;

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
	public Map<String, Object> list(Model model, HttpServletRequest  req  , @ModelAttribute DataTableForm form) {

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
	 * シナリオリスト
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/scenario/list/{clientId}" })
	@ResponseBody
	public List<Map<String, Object>> completionList(Model model, @PathVariable(name = "clientId", required = true) Long clientId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			List<ClientScenario> clientSecnarios = clientService.getPortalScenario(logger, sessionuser.getId(), clientId);
			
			return toJsonResult(clientSecnarios);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}



	@Action(Type.AJAX)
	@PostMapping({ PREFIX + "/scenario/list/{clientId}" })
	@ResponseBody
	public Map<String, Object> updateCompletion(Model model, @ModelAttribute("obj") ClientForm form, @PathVariable(name = "clientId", required = true) Long clientId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Map<String, Object> ret = new HashMap<>();
		
		try {
			List<KeyValue> errors = new ArrayList<>();
			ret.put("errors", errors);
			
			if(errors.size() == 0) {
				List<ClientScenario> clientScenarios = new ArrayList<>();
				for(ScenarioForm scenarioForm : form.getScenarios()) {
					if(scenarioForm.getIsDeleted() != null && !Boolean.valueOf(scenarioForm.getIsDeleted())) {
						ClientScenario clientScenario = new ClientScenario();
						clientScenario.setScenarioId(Long.valueOf(scenarioForm.getId()));
						clientScenario.setSortOrder(Integer.valueOf(scenarioForm.getSortOrder()));
						clientScenario.setIsInvalided(Boolean.valueOf(scenarioForm.getIsInvalided()));
						clientScenarios.add(clientScenario);
					}
				}
				clientService.savePortalSecnarios(logger, sessionuser.getId(), clientId, clientScenarios);

				List<ClientScenario> clientSecnarios = clientService.getPortalScenario(logger, sessionuser.getId(), clientId);
				ret.put("list", toJsonResult(clientSecnarios));
				
				// オペレーションログ出力
				adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.portals, adminOperationCode.update, clientId, "");
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return ret;
	}
	
	/**
	 * シナリオの一覧をJOSNマップ化
	 * なんかイマイチだな、、、
	 * @param clientSecnarios
	 * @return
	 */
	private List<Map<String, Object>> toJsonResult(List<ClientScenario> clientSecnarios){
		
		List<Map<String, Object>> ret = new ArrayList<>();

		for(ClientScenario clientScenario : clientSecnarios) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", clientScenario.getScenario().getId());
			map.put("scenarioKeycode", clientScenario.getScenario().getScenarioKeycode());
			map.put("scenarioName", clientScenario.getScenario().getScenarioName());
			map.put("description", clientScenario.getScenario().getDescription());
			map.put("note", clientScenario.getScenario().getNote());
			map.put("isInvalided", clientScenario.getScenario().getIsInvalided());
			map.put("isDeleted", clientScenario.getScenario().getIsDeleted());
			map.put("isInvisible", clientScenario.getIsInvalided());
			map.put("sortOrder", clientScenario.getSortOrder());
			ret.add(map);
		}
		return ret;
	}
}
