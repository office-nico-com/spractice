package com.office_nico.spractice.web.controller.front;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.ScenarioService;
import com.office_nico.spractice.service.UserService;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.web.form.PasswordForm;
import com.office_nico.spractice.web.form.ProfileForm;


@Controller
public class FrontProfileController {

	private static final String PREFIX = "front/profile";

	// Viewファイルの定義
	public class View {
		public static final String PROFILE = PREFIX + "/profile";
		public static final String PASSWD = PREFIX + "/password";
		public static final String NOTFOUND = "front/404";
		public static final String FORBIDDEN = "front/403";
	}

	private static final Logger logger = LoggerFactory.getLogger(FrontProfileController.class);

	@Autowired
	private ClientService clientService = null;

	@Autowired
	private SessionData sessionData = null;

	@Autowired
	private UserService userService = null;

	@Autowired
	private ScenarioService scenarioService = null;



	/**
	 * ユーザー情報編集画面
	 * 
	 * @param model
	 * @param form
	 * @param clientKeycode
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/profile" })
	public String editProfile(Model model, @ModelAttribute("obj") ProfileForm form, @PathVariable(name = "clientKeycode", required = true) String clientKeycode) {


		try {


			model.addAttribute("clientKeycode", clientKeycode);
	
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

			// たぶんだけど、userService.getRoleCodeを先に呼び出すとキャッシュしたuserを使おうとする。
			// そのため、proxyクラスが返ってリフレクションでのフィールドコピーに失敗するんじゃないか、と思われる。
			// ⇒ フィールド名がプロキシクラスになってしまう。
			// とりあえず、権限の取得の前に移動して対処。
			User user = userService.get(logger, sessionData.getUserId(), sessionData.getUserId());
			BeanUtil.copyFields(user, form);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.PROFILE;
	}

	/**
	 * ユーザー情報編集処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/profile" })
	public String updateProfile(Model model, @Validated @ModelAttribute("obj") ProfileForm form,  BindingResult result, @PathVariable(name = "clientKeycode", required = true) String clientKeycode, HttpServletResponse res) {
		
		try {

			// クライアントチェック
			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
			model.addAttribute("client", client);

			// 入力チェック
			if(result.hasErrors()) {

				// シナリオの一覧を取得
				List<Scenario> scenarios = scenarioService.getValidByClientId(logger, sessionData.getUserId(), client.getId());
				model.addAttribute("scenarios", scenarios);

				// 権限を取得
				Short roleCode = userService.getRoleCode(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId());
				model.addAttribute("roleCode", roleCode);

				return View.PROFILE;
			}

			User user = BeanUtil.copyFields(form, new User());
			user = userService.updateProfile(logger, sessionData.getUserId(), sessionData.getUserId(), user);
			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}

			// セッション更新
			sessionData.setFamilyName(user.getFamilyName());
			sessionData.setGivenName(user.getGivenName());
			sessionData.setEmail(user.getEmail());

			// 完了フラグをセット
			Cookie cookie = new Cookie("compleate", "true");
			cookie.setPath("/");
			res.addCookie(cookie);

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + clientKeycode + "/profile";
	}
	
	/**
	 * パスワード編集画面
	 * 
	 * @param model
	 * @param form
	 * @param clientKeycode
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/password" })
	public String editPassword(Model model, @ModelAttribute("obj") PasswordForm form, @PathVariable(name = "clientKeycode", required = true) String clientKeycode) {

		try {

			model.addAttribute("clientKeycode", clientKeycode);
	
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
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.PASSWD;
	}

	/**
	 * パスワード編集処理
	 * @param model
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/password" })
	public String updatePassword(Model model, @Validated @ModelAttribute("obj") PasswordForm form,  BindingResult result, @PathVariable(name = "clientKeycode", required = true) String clientKeycode, HttpServletResponse res) {
		
		try {

			// クライアントチェック
			Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
			if (client == null) {
				// クライアントなし → Not Found画面
				return View.NOTFOUND;
			}
			model.addAttribute("client", client);

			
			// 入力チェック
			if(result.hasErrors()) {

				// シナリオの一覧を取得
				List<Scenario> scenarios = scenarioService.getValidByClientId(logger, sessionData.getUserId(), client.getId());
				model.addAttribute("scenarios", scenarios);

				// 権限を取得
				Short roleCode = userService.getRoleCode(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId());
				model.addAttribute("roleCode", roleCode);

				return View.PASSWD;
			}

			User user = userService.updatePassword(logger, sessionData.getUserId(), sessionData.getUserId(), form.getPasswd());
			if(user == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
						
			// 完了フラグをセット
			Cookie cookie = new Cookie("compleate", "true");
			cookie.setPath("/");
			res.addCookie(cookie);

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + clientKeycode + "/password";
	}
	
}
