package com.office_nico.spractice.web.controller.front;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.constants.codes.UserRegistTypeCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientUser;
import com.office_nico.spractice.domain.Completion;
import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.domain.Guidance;
import com.office_nico.spractice.domain.GuidanceAction;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.CompletionService;
import com.office_nico.spractice.service.ScenarioService;
import com.office_nico.spractice.service.UserService;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.DateTimeUtil;
import com.office_nico.spractice.web.form.LoginForm;


@Controller
public class TopController {

	private static final String PREFIX = "front/top";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = PREFIX + "/index";
		public static final String LOGIN = PREFIX + "/login";
		public static final String LANDING = PREFIX + "/landing";
		public static final String DESCRIPTION = PREFIX + "/description";
		public static final String WORK = PREFIX + "/work";
		public static final String LOGOUT = PREFIX + "/logout";
		public static final String NOTFOUND = "front/404";
		public static final String FORBIDDEN = "front/403";
	}

	private static final Logger logger = LoggerFactory.getLogger(TopController.class);

	@Autowired
	private ClientService clientService = null;

	@Autowired
	private SessionData sessionData = null;

	@Autowired
	private UserRegistTypeCode userRegistTypeCode = null;

	@Autowired
	private PasswordEncoder passwordEncoder = null;

	@Autowired
	private UserService userService = null;

	@Autowired
	private ScenarioService scenarioService = null;

	@Autowired
	private CompletionService completionService = null;

	@Value("${ipa.rss.feed.url}") 
	private String rss = null;

	
	/**
	 * サイトトップ
	 * 
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "" })
	public String index(Model model) {
		return View.INDEX;
	}

	/**
	 * ランディングページ
	 * 
	 * @param model
	 * @param form
	 * @param clientKeycode
	 * @param uid
	 * @param em
	 * @param name
	 * @param kana
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}" })
	public String landing(Model model, @ModelAttribute("obj") LoginForm form, @PathVariable(name = "clientKeycode", required = true) String clientKeycode, String s, String uid, String em, String name, String kana) {

		try {
			
			model.addAttribute("clientKeycode", clientKeycode);

			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
			model.addAttribute("client", client);

			/*
			// for debug
			sessionData.setUserId(2L);
			List<Long> _availableClients = new ArrayList<>();
			List<ClientUser> clientUsers = userService.getAffiliatedClients(logger, sessionData.getUserId(), 2L);
			for (ClientUser clientUser : clientUsers) {
				_availableClients.add(clientUser.getClient().getId());
			}
			sessionData.setAvailableClients(_availableClients);
			// for debug
			*/
			
			if (sessionData.getUserId() == null) {

				// 未ログイン
				if (client.getUserRegistTypeCode() == userRegistTypeCode.manual) {
					// → ログイン画面へ
					return View.LOGIN;
				}
				else {
					if (s != null && s.length() > 0 && client.getSecret().equals(s) && uid != null && uid.length() > 0) {
						// すでに登録されている場合→ 更新 → LP
						// 登録されていない場合 → 自動登録 → LP
						User user = userService.autoSave(logger, sessionData.getUserId(), client, uid, em, name, kana);
						sessionData.setUserId(user.getId());
						sessionData.setAccount(user.getAccount());
						sessionData.setEmail(user.getEmail());
						sessionData.setRegisteredFromCode(user.getRegisteredFromCode());
						sessionData.setFamilyName(user.getFamilyName());
						sessionData.setGivenName(user.getGivenName());

						List<Long> availableClients = new ArrayList<>();
						for (ClientUser clientUser : user.getClientUsers()) {
							availableClients.add(clientUser.getClient().getId());
						}
						sessionData.setAvailableClients(availableClients);

						// 接続情報を見せないように自身にリダイレクト
						return "redirect:/" + clientKeycode;
					}
					else {
						// → ログイン画面へ
						return View.LOGIN;
					}
				}
			}

			// 所属クライアントチェック
			boolean find = false;
			for (Long availableClientId : sessionData.getAvailableClients()) {
				if (availableClientId.equals(client.getId())) {
					find = true;
					break;
				}
			}
			if (!find) {
				return View.FORBIDDEN;
			}

			// シナリオの一覧を取得
			List<Scenario> scenarios = scenarioService.getValidByClientId(logger, sessionData.getUserId(), client.getId());
			model.addAttribute("scenarios", scenarios);

			// 権限を取得
			Short roleCode = userService.getRoleCode(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId());
			model.addAttribute("roleCode", roleCode);

			// → LP
			return View.LANDING;

		}
		catch (Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * 詳細説明画面の表示
	 * 
	 * @param model
	 * @param form
	 * @param clientKeycode
	 * @param scenarioKeycode
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/{scenarioKeycode:(?!^logout$)^[a-zA-Z0-9]+$}" })
	public String description(Model model, @ModelAttribute("obj") LoginForm form, @PathVariable(name = "clientKeycode", required = true) String clientKeycode, @PathVariable(name = "scenarioKeycode", required = true) String scenarioKeycode) {

		try {
			model.addAttribute("clientKeycode", clientKeycode);
			model.addAttribute("scenarioKeycode", scenarioKeycode);
	
			// クライアントチェック
			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
			model.addAttribute("client", client);
	
			// ログインチェック
			if (sessionData.getUserId() == null) {
				return "redirect:/" + clientKeycode;
			}
	
			// 所属クライアントチェック
			boolean find = false;
			for (Long availableClientId : sessionData.getAvailableClients()) {
				if (availableClientId.equals(client.getId())) {
					find = true;
					break;
				}
			}
			if (!find) {
				return View.FORBIDDEN;
			}
	
			// シナリオの一覧を取得
			List<Scenario> scenarios = scenarioService.getValidByClientId(logger, sessionData.getUserId(), client.getId());
			model.addAttribute("scenarios", scenarios);
	
			// 権限を取得
			Short roleCode = userService.getRoleCode(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId());
			model.addAttribute("roleCode", roleCode);
	
			Scenario targetScenario = null;
			for (Scenario scenario : scenarios) {
				if (scenario.getScenarioKeycode().equals(scenarioKeycode)) {
					targetScenario = scenario;
					break;
				}
			}
			if (targetScenario == null) {
				return View.FORBIDDEN;
			}
			model.addAttribute("targetScenario", targetScenario);
	
			// 履修履歴を取得
			List<Completion> completions = completionService.getLastHistories(logger, sessionData.getUserId(), sessionData.getUserId(), client.getId(), targetScenario.getId());
			model.addAttribute("completionHistories", completions);
	
			// 最終履修履歴を取得
			List<CompletionPoint> completionResults = completionService.getCompletionResults(logger, sessionData.getUserId(), sessionData.getUserId(), client.getId(), targetScenario.getId());
			model.addAttribute("completionResults", completionResults);
	
			// すべて履修済みか判定
			Boolean allCompletion = true;
			if (completionResults.size() == 0) {
				allCompletion = false;
			}
			else {
				for (CompletionPoint completionPoint : completionResults) {
					if (completionPoint.getLastCompletedAt() == null) {
						allCompletion = false;
						break;
					}
				}
			}
			model.addAttribute("allCompletion", allCompletion);
		}
		catch (Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.DESCRIPTION;
	}

	/**
	 * 受講画面の表示
	 * 
	 * @param model
	 * @param form
	 * @param clientKeycode
	 * @param scenarioKeycode
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/{scenarioKeycode:(?!^logout$)^[a-zA-Z0-9]+$}/work" })
	public String work(Model model, @ModelAttribute("obj") LoginForm form, @PathVariable(name = "clientKeycode", required = true) String clientKeycode, @PathVariable(name = "scenarioKeycode", required = true) String scenarioKeycode) {

		try {
			model.addAttribute("clientKeycode", clientKeycode);
			model.addAttribute("scenarioKeycode", scenarioKeycode);
	
			// クライアントチェック
			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
			model.addAttribute("client", client);
	
			// ログインチェック
			if (sessionData.getUserId() == null) {
				return "redirect:/" + clientKeycode;
			}
	
			// 所属クライアントチェック
			boolean find = false;
			for (Long availableClientId : sessionData.getAvailableClients()) {
				if (availableClientId.equals(client.getId())) {
					find = true;
					break;
				}
			}
			if (!find) {
				return View.FORBIDDEN;
			}
	
			// シナリオを取得
			Scenario targetScenario = scenarioService.getValidByScenarioCode(logger, sessionData.getUserId(), client.getId(), scenarioKeycode);
			if (targetScenario == null) {
				return View.FORBIDDEN;
			}
			
			// 変数を置換
			targetScenario.setContentBody(convertVariables(targetScenario.getContentBody(), client));
			targetScenario.setContentScript(convertVariables(targetScenario.getContentScript(), client));
			for(Guidance guidance : targetScenario.getGuidances()) {
				guidance.setGuidanceText(convertVariables(guidance.getGuidanceText(), client));
				guidance.setPreScript(convertVariables(guidance.getPreScript(), client));
				guidance.setPostScript(convertVariables(guidance.getPostScript(), client));
				for(GuidanceAction guidanceAction : guidance.getGuidanceActions()) {
					guidanceAction.setBody(convertVariables(guidanceAction.getBody(), client));
				}
			}
			model.addAttribute("production", true);
			model.addAttribute("targetScenario", targetScenario);
		}
		catch (Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.WORK;
	}

	/**
	 * ログイン処理
	 * 
	 * @param model
	 * @param form
	 * @param result
	 * @param clientKeycode
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}" })
	public String doLogin(Model model, @Validated @ModelAttribute("obj") LoginForm form, BindingResult result, @PathVariable(name = "clientKeycode", required = true) String clientKeycode) {

		try {
			model.addAttribute("clientKeycode", clientKeycode);
	
			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
	
			// 入力チェック
			if (result.hasErrors()) {
				return View.LOGIN;
			}
	
			User user = userService.getValidByAccount(logger, sessionData.getUserId(), form.getAccount());
	
			// パスワードチェック
			if (user == null || user.getPasswd() == null || !passwordEncoder.matches(form.getPasswd(), user.getPasswd())) {
				result.rejectValue("account", "AbstractUserDetailsAuthenticationProvider.badCredentials", new String[] {}, "");
				return View.LOGIN;
			}
	
			// 所属クライアントチェック
			boolean find = false;
			List<Long> availableClients = new ArrayList<>();
			List<ClientUser> clientUsers = userService.getAffiliatedClients(logger, sessionData.getUserId(), user.getId());
			for (ClientUser clientUser : clientUsers) {
				availableClients.add(clientUser.getClient().getId());
				if (clientUser.getClient().getId().equals(client.getId())) {
					find = true;
				}
			}
			if (!find) {
				result.rejectValue("account", "authenticate.site.not.available", new String[] {}, "");
				return View.LOGIN;
			}
	
			// 認証OK
			sessionData.setUserId(user.getId());
			sessionData.setAccount(user.getAccount());
			sessionData.setEmail(user.getEmail());
			sessionData.setAvailableClients(availableClients);
			sessionData.setRegisteredFromCode(user.getRegisteredFromCode());
			sessionData.setFamilyName(user.getFamilyName());
			sessionData.setGivenName(user.getGivenName());

		}
		catch (Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + clientKeycode;
	}

	@Action(Type.AJAX)
	@ResponseBody
	@GetMapping({ "front/rssread" })
	public List<Map<String,String>> rssread(Model model) {

		List<Map<String,String>> ret = new ArrayList<>();
		String path = this.rss;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(path);
			Element root = document.getDocumentElement();

			/* Get and print Title of RSS Feed. */
			NodeList channel = root.getElementsByTagName("channel");
			NodeList title = ((Element) channel.item(0)).getElementsByTagName("title");
			System.out.println("\nTitle: " + title.item(0).getFirstChild().getNodeValue() + "\n");

			/* Get Node list of RSS items */
			NodeList item_list = root.getElementsByTagName("item");
			for (int i = 0; i < item_list.getLength(); i++) {
				
				Element element = (Element) item_list.item(i);
				NodeList item_title = element.getElementsByTagName("title");
				NodeList item_link = element.getElementsByTagName("link");
				NodeList item_creator = element.getElementsByTagName("dc:creator");
				NodeList item_date = element.getElementsByTagName("dc:date");
				/*
				System.out.println(" title: " + item_title.item(0).getFirstChild().getNodeValue());
				System.out.println(" link:  " + item_link.item(0).getFirstChild().getNodeValue());
				System.out.println(" creator:  " + item_creator.item(0).getFirstChild().getNodeValue());
				System.out.println(" date:  " + item_date.item(0).getFirstChild().getNodeValue());
				*/
				Map<String, String> map = new HashMap<>();
				map.put("title", item_title.item(0).getFirstChild().getNodeValue());
				map.put("link", item_link.item(0).getFirstChild().getNodeValue());
				map.put("creator", item_creator.item(0).getFirstChild().getNodeValue());
				LocalDateTime ld = DateTimeUtil.parse(item_date.item(0).getFirstChild().getNodeValue(), LocalDateTime.class);
				map.put("date", DateTimeUtil.format(ld, DateTimeUtil.DateTimeFormat.DATE_JA_ZERO_SUPPRESS));
				ret.add(map);
			}
		}
		catch (IOException | ParserConfigurationException | SAXException e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return ret;
	}

	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/logout" })
	public String logout(Model model, @PathVariable(name = "clientKeycode", required = true) String clientKeycode) {

		try {
			model.addAttribute("clientKeycode", clientKeycode);
	
			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
	
			// ログインチェック
			if (sessionData.getUserId() == null) {
				return "redirect:/" + clientKeycode;
			}
	
			// 所属クライアントチェック
			boolean find = false;
			for (Long availableClientId : sessionData.getAvailableClients()) {
				if (availableClientId.equals(client.getId())) {
					find = true;
					break;
				}
			}
			if (!find) {
				return View.FORBIDDEN;
			}
	
			sessionData.setUserId(null);
			sessionData.setEmail(null);
			sessionData.setRegisteredFromCode(null);
			sessionData.setFamilyName(null);
			sessionData.setGivenName(null);
	
			if (client.getLogoutUrl() != null && client.getLogoutUrl().length() > 0) {
				return "redirect:" + client.getLogoutUrl();
			}
			else {
				return View.LOGOUT;
			}
		}
		catch (Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	/**
	 * 変数の置換
	 * @param src 変換元文字列
	 * @return 変換後文字列
	 */
	private String convertVariables(String str, Client client) {
		String ret = "";
		if(str != null) {
			str = str.replaceAll("\\{CLIENT_NAME\\}", client.getClientNameJa());
			str = str.replaceAll("\\{SECURITY_TEAM_NAME\\}", client.getSecurityMangementTeam());
			str = str.replaceAll("\\{SECURITY_TEAM_TEL\\}", client.getSecurityMangementTel());
			str = str.replaceAll("\\{SECURITY_TEAM_EMAIL\\}", client.getSecurityMangementEmail());
			str = str.replaceAll("\\{ACCOUNT\\}", sessionData.getAccount());
			str = str.replaceAll("\\{EMAIL\\}", sessionData.getEmail());
			str = str.replaceAll("\\{USER_FULLNAME\\}", sessionData.getFamilyName() + " " + sessionData.getGivenName());
			str = str.replaceAll("\\{USER_FAMILYNAME\\}", sessionData.getFamilyName());
			str = str.replaceAll("\\{USER_GIVENNAME\\}", sessionData.getGivenName());
			ret = str;
		}
		return ret;
	}
}
