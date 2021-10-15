package com.office_nico.spractice.web.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

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
import com.office_nico.spractice.constants.Length;
import com.office_nico.spractice.constants.codes.AdminFunctionCode;
import com.office_nico.spractice.constants.codes.AdminOperationCode;
import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.domain.Guidance;
import com.office_nico.spractice.domain.GuidanceAction;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.domain.Stock;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.CompletionService;
import com.office_nico.spractice.service.ScenarioService;
import com.office_nico.spractice.service.data.KeyValue;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.web.form.DataTableForm;
import com.office_nico.spractice.web.form.GuidanceActionForm;
import com.office_nico.spractice.web.form.GuidanceForm;
import com.office_nico.spractice.web.form.ScenarioForm;


@Controller
public class ScenariosController {

	private static final String PREFIX = "t06o1ny8/scenarios";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/scenarios/index";
		public static final String SHOW = "admin/scenarios/show";
		public static final String NEW = "admin/scenarios/new";
		public static final String EDIT = "admin/scenarios/edit";
		public static final String COMPLETION = "admin/scenarios/completion";
		public static final String GUIDANCE = "admin/scenarios/guidance";
		public static final String CONTENT = "admin/scenarios/content";
		public static final String WORK = "front/top/work";
	}

	private static final Logger logger = LoggerFactory.getLogger(ScenariosController.class);

	@Autowired
	private ScenarioService scenarioService = null;
	
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
	 * シナリオ管理トップ
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
			String[] order = form.getOrderString("id", "scenarioKeycode", "scenarioName", "id", new String[] {"isInvalided","scenarioName"});
			Page<Scenario> page = scenarioService.page(logger, sessionuser.getId(), form.getStart(), form.getLength(), form.getOrderDirecton(), order);
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
	 * シナリオ登録画面
	 * @param model
	 * @param form
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/new" })
	public String newItem(Model model, @ModelAttribute("obj") ScenarioForm form) {
		
		try {
			form.setIsInvalided(String.valueOf(invalidStatus.Valid));
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		
		return View.NEW;
	}

	
	/**
	 * シナリオ登録処理
	 * @param model
	 * @param form
	 * @param result
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/new" })
	public String create(Model model, @Validated @ModelAttribute("obj") ScenarioForm form, BindingResult result) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			// シナリオコードの重複チェック
			if(form.getScenarioKeycode() != null && form.getScenarioKeycode() .length() > 0 && scenarioService.isDuplicate(logger, sessionuser.getId(), form.getScenarioKeycode())) {
				String fieldName = messageSource.getMessage("scenarioKeycode", null, Locale.getDefault());
				result.rejectValue("scenarioKeycode", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				return View.NEW;
			}
	
			// 登録処理
			Scenario scenario = BeanUtil.copyFields(form, new Scenario());
			scenario.setContentScript("$(function(){\n\t\n});\n");
			scenario = scenarioService.create(logger, sessionuser.getId(), scenario);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.scenarios, adminOperationCode.create, scenario.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return "redirect:/" + PREFIX;
	}

	/**
	 * シナリオ照会画面
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/show/{scenarioId}" })
	public String show(Model model, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// 照会
			Scenario scenario = scenarioService.get(logger, sessionuser.getId(), scenarioId);
			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			model.addAttribute("scenario", scenario);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.SHOW;
	}

	
	/**
	 * シナリオ編集画面
	 * @param model
	 * @param form
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/edit/{scenarioId}" })
	public String edit(Model model, @ModelAttribute("obj") ScenarioForm form,  @PathVariable(name = "scenarioId", required = true) Long scenarioId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// 照会
			Scenario scenario = scenarioService.get(logger, sessionuser.getId(), scenarioId);
			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			BeanUtil.copyFields(scenario, form);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.EDIT;

	}

	/**
	 * シナリオ編集処理
	 * @param model
	 * @param form
	 * @param result
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/edit/{scenarioId}" })
	public String update(Model model, @Validated @ModelAttribute("obj") ScenarioForm form,  BindingResult result, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			
			// シナリオコードの重複チェック
			if(form.getScenarioKeycode() != null && form.getScenarioKeycode() .length() > 0 && scenarioService.isDuplicate(logger, sessionuser.getId(), form.getScenarioKeycode(), scenarioId)) {
				String fieldName = messageSource.getMessage("scenarioKeycode", null, Locale.getDefault());
				result.rejectValue("scenarioKeycode", "javax.validation.constraints.Duplidate.message", new String[] {fieldName}, "");
			}

			// 入力チェック
			if(result.hasErrors()) {
				return View.EDIT;
			}

			Scenario scenario = BeanUtil.copyFields(form, new Scenario());
			scenario = scenarioService.update(logger, sessionuser.getId(), scenarioId, scenario);

			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.scenarios, adminOperationCode.update, scenario.getId(), "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX + "/show/" + scenarioId;
	}

	/**
	 * シナリオ削除処理
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/destroy/{scenarioId}" })
	public String destroy(Model model, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			scenarioService.delete(logger, sessionuser.getId(), scenarioId);

			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.scenarios, adminOperationCode.delete, scenarioId, "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX;
	}
	
	
	/**
	 * 履修ポイント
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/completion/{scenarioId}" })
	public String completion(Model model, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// 照会
			Scenario scenario = scenarioService.get(logger, sessionuser.getId(), scenarioId);
			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			model.addAttribute("scenario", scenario);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return View.COMPLETION;
	}

	/**
	 * 履修ポイントリスト
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/completion/list/{scenarioId}" })
	@ResponseBody
	public List<CompletionPoint> completionList(Model model, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		try {
			User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			List<CompletionPoint> list = completionService.getValidCompletionPoints(logger, sessionuser.getId());
			
			return list;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * ガイダンス
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/guidance/{scenarioId}" })
	public String guidance(Model model, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// 照会
			Scenario scenario = scenarioService.get(logger, sessionuser.getId(), scenarioId);
			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			model.addAttribute("scenario", scenario);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return View.GUIDANCE;
	}

	/**
	 * ガイダンスリスト
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/guidance/list/{scenarioId}" })
	@ResponseBody
	public List<Guidance> guidanceList(Model model, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			List<Guidance> list = scenarioService.getGuidances(logger, sessionuser.getId(), scenarioId);

			return list;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	
	/**
	 * ガイダンス保存
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@PostMapping({ PREFIX + "/guidance/list/{scenarioId}" })
	@ResponseBody
	public Map<String, Object> updateGuidance(Model model, @ModelAttribute("obj") ScenarioForm form, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Map<String, Object> ret = new HashMap<>();

		try {
		
			List<KeyValue> errors = new ArrayList<>();
			ret.put("errors", errors);
			
			// 入力チェック
			for(GuidanceForm guidanceForm : form.getGuidances()) {
				List<KeyValue> keyValues = validateGuidance(model, guidanceForm, scenarioId);
				for(KeyValue kv : keyValues) {
					kv.setValue(messageSource.getMessage("com.office_nico.spractice.web.validate.Global.Error.message", new String[] {guidanceForm.getGuidanceKeycode(),  kv.getValue()}, Locale.getDefault()));
				}
				errors.addAll(keyValues);
			}
			
			Long startGuidanceId = null;
			if(form.getStartGuidanceId() != null && form.getStartGuidanceId().length() > 0) {
				startGuidanceId = Long.valueOf(form.getStartGuidanceId());
			}
	
			if(errors.size() == 0) {
				List<Guidance> guidances = new ArrayList<>();
				for(GuidanceForm guidanceForm : form.getGuidances()) {
					Guidance bean =  new Guidance();
					for(GuidanceActionForm guidanceActionForm : guidanceForm.guidanceActions) {
						GuidanceAction bean2 = BeanUtil.copyFields(guidanceActionForm, new GuidanceAction());
						bean.getGuidanceActions().add(bean2);
					}
					BeanUtil.copyFields(guidanceForm, bean);
					bean.setWorkId(bean.getId());
					guidances.add(bean);
				}
				scenarioService.saveGuidance(logger, sessionuser.getId(), scenarioId, guidances, startGuidanceId);
				
				List<Guidance> list = scenarioService.getGuidances(logger, sessionuser.getId(), scenarioId);
				ret.put("list", list);
	
				Scenario scenario = scenarioService.get(logger, sessionuser.getId(), scenarioId);
				ret.put("startGuidanceId", scenario.getStartGuidanceId());
	
				// オペレーションログ出力
				adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.guidance, adminOperationCode.update, scenarioId, "");
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return ret;
	}

	/**
	 * ガイダンスの入力チェック
	 * @param model
	 * @param form
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@PostMapping({ PREFIX + "/guidance/validate/{scenarioId}" })
	@ResponseBody
	public List<KeyValue> validateGuidance(Model model, @ModelAttribute("obj") GuidanceForm form, @PathVariable(name = "scenarioId", required = true) Long scenarioId){
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<KeyValue> ret = new ArrayList<>();

		try {

			Pattern p = Pattern.compile("^[a-zA-Z0-9@\\#\\$\\%\\*!]*$");
			String fieldGuidanceKeycode = messageSource.getMessage("guidanceKeycode", null, "", Locale.getDefault());
	
			// guidanceKeycode
			//   ブランクチェック
			//   半角英数記号
			//   桁数
			//   重複
			if(form.getGuidanceKeycode() == null || form.getGuidanceKeycode().length() == 0) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.NotBlank.message", new String[] {fieldGuidanceKeycode}, "", Locale.getDefault());
				kv.setKey("guidanceKeycode");
				kv.setValue(message);
				ret.add(kv);
			}
			else if(!p.matcher(form.getGuidanceKeycode()).matches()) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Pattern.alphanummark2.message", new String[] {fieldGuidanceKeycode}, "", Locale.getDefault());
				kv.setKey("guidanceKeycode");
				kv.setValue(message);
				ret.add(kv);
			}
			else if(form.getGuidanceKeycode().length() < Length.guidanceKeycodeMin || form.getGuidanceKeycode().length() > Length.guidanceKeycode) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Size.range.message", null, "", Locale.getDefault());
				kv.setKey("guidanceKeycode");
				kv.setValue(message.replaceAll("\\{min\\}", String.valueOf(Length.guidanceKeycodeMin)).replaceAll("\\{max\\}", String.valueOf(Length.guidanceKeycode)));
				ret.add(kv);
			}
			else if((form.getId() == null || form.getId().length() == 0) && scenarioService.isDuplicateGuidanceKeycode(logger, sessionuser.getId(), scenarioId, form.getGuidanceKeycode())) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Duplidate.message", new String[] {fieldGuidanceKeycode}, "", Locale.getDefault());
				kv.setKey("guidanceKeycode");
				kv.setValue(message);
				ret.add(kv);
			}
			else if((form.getId() != null && form.getId().length() > 0) && scenarioService.isDuplicateGuidanceKeycode(logger, sessionuser.getId(), scenarioId, form.getGuidanceKeycode(), Long.parseLong(form.getId()))) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Duplidate.message", new String[] {fieldGuidanceKeycode}, "", Locale.getDefault());
				kv.setKey("guidanceKeycode");
				kv.setValue(message);
				ret.add(kv);
			}
	
			// guidanceText
			//   桁数
			if(form.getGuidanceText().length() > Length.guidanceText) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Size.message", null, "", Locale.getDefault());
				kv.setKey("guidanceText");
				kv.setValue(message.replaceAll("{max}", String.valueOf(Length.guidanceText)));
				ret.add(kv);
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return ret;
	}
	
	
	/**
	 * 履修ポイントの入力チェック
	 * @param model
	 * @param form
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@PostMapping({ PREFIX + "/guidance/validate/completion/{scenarioId}" })
	@ResponseBody
	public List<KeyValue> validateCompletionPoint(Model model, @ModelAttribute("obj") GuidanceForm form, @PathVariable(name = "scenarioId", required = true) Long scenarioId){
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<KeyValue> ret = new ArrayList<>();
		
		try {

			Pattern p = Pattern.compile("^[a-zA-Z0-9@\\#\\$\\%\\*!]*$");
			String fieldCompletionPointKeycode = messageSource.getMessage("completionPointKeycode", null, "", Locale.getDefault());
			String fieldCompletionPointDescription = messageSource.getMessage("description", null, "", Locale.getDefault());
	
			if(form.getStartCompletionPointKeycode() != null && form.getStartCompletionPointKeycode().length() > 0) {
	
				// startCompletionPointKeycode
				//   半角英数
				//   桁数
				//   重複
				if(!p.matcher(form.getStartCompletionPointKeycode()).matches()) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Pattern.alphanummark2.message", new String[] {fieldCompletionPointKeycode}, "", Locale.getDefault());
					kv.setKey("startCompletionPointKeycode");
					kv.setValue(message);
					ret.add(kv);
				}
				else if(form.getStartCompletionPointKeycode().length() < Length.guidanceKeycodeMin || form.getStartCompletionPointKeycode().length() > Length.guidanceKeycode) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Size.range.message", null, "", Locale.getDefault());
					kv.setKey("startCompletionPointKeycode");
					kv.setValue(message.replaceAll("\\{min\\}", String.valueOf(Length.guidanceKeycodeMin)).replaceAll("\\{max\\}", String.valueOf(Length.guidanceKeycode)));
					ret.add(kv);
				}
				else if(completionService.isDuplicate(logger, sessionuser.getId(), form.getStartCompletionPointKeycode())) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Duplidate.message", new String[] {fieldCompletionPointKeycode}, "", Locale.getDefault());
					kv.setKey("startCompletionPointKeycode");
					kv.setValue(message);
					ret.add(kv);
				}
				
				// startCompletionPointDescription
				//   ブランクチェック
				//   桁数
				if(form.getStartCompletionPointDescription() == null || form.getStartCompletionPointDescription().length() == 0) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.NotBlank.message", new String[] {fieldCompletionPointDescription}, "", Locale.getDefault());
					kv.setKey("startCompletionPointDescription");
					kv.setValue(message);
					ret.add(kv);
				}
				else if(form.getStartCompletionPointDescription().length() > Length.description) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Size.message", null, "", Locale.getDefault());
					kv.setKey("startCompletionPointDescription");
					kv.setValue(message.replaceAll("{max}", String.valueOf(Length.description)));
					ret.add(kv);
				}
			}
	
			
			if(form.getEndCompletionPointKeycode() != null && form.getEndCompletionPointKeycode().length() > 0) {
				// endCompletionPointKeycode
				//   半角英数
				//   桁数
				//   重複
				if(!p.matcher(form.getEndCompletionPointKeycode()).matches()) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Pattern.alphanum.message", new String[] {fieldCompletionPointKeycode}, "", Locale.getDefault());
					kv.setKey("endCompletionPointKeycode");
					kv.setValue(message);
					ret.add(kv);
				}
				else if(form.getEndCompletionPointKeycode().length() < Length.guidanceKeycodeMin || form.getEndCompletionPointKeycode().length() > Length.guidanceKeycode) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Size.range.message", null, "", Locale.getDefault());
					kv.setKey("endCompletionPointKeycode");
					kv.setValue(message.replaceAll("\\{min\\}", String.valueOf(Length.guidanceKeycodeMin)).replaceAll("\\{max\\}", String.valueOf(Length.guidanceKeycode)));
					ret.add(kv);
				}
				else if(completionService.isDuplicate(logger, sessionuser.getId(), form.getEndCompletionPointKeycode())) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Duplidate.message", new String[] {fieldCompletionPointKeycode}, "", Locale.getDefault());
					kv.setKey("endCompletionPointKeycode");
					kv.setValue(message);
					ret.add(kv);
				}
	
				// endCompletionPointDescription
				//   ブランクチェック
				//   桁数
				if(form.getEndCompletionPointKeycode() == null || form.getEndCompletionPointKeycode().length() == 0) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.NotBlank.message", new String[] {fieldCompletionPointDescription}, "", Locale.getDefault());
					kv.setKey("endCompletionPointDescription");
					kv.setValue(message);
					ret.add(kv);
				}
				else if(form.getEndCompletionPointKeycode().length() > Length.description) {
					KeyValue kv = new KeyValue();
					String message = messageSource.getMessage("javax.validation.constraints.Size.message", null, "", Locale.getDefault());
					kv.setKey("endCompletionPointDescription");
					kv.setValue(message.replaceAll("{max}", String.valueOf(Length.description)));
					ret.add(kv);
				}
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return ret;
	}



	/**
	 * コンテンツ
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/content/{scenarioId}" })
	public String content(Model model, @ModelAttribute("obj") ScenarioForm form, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// 照会
			Scenario scenario = scenarioService.get(logger, sessionuser.getId(), scenarioId);
			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			List<Stock> stocks = scenarioService.getStocks(logger, sessionuser.getId(), scenarioId);
			
			model.addAttribute("scenario", scenario);
			model.addAttribute("stocks", stocks);
			BeanUtil.copyFields(scenario, form);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.CONTENT;
	}

	/**
	 * コンテンツ編集
	 * @param model
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@PostMapping({ PREFIX + "/content/{scenarioId}" })
	public String editContent(Model model, @ModelAttribute("obj") ScenarioForm form, @PathVariable(name = "scenarioId", required = true) Long scenarioId) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			
			Scenario scenario = BeanUtil.copyFields(form, new Scenario());
			scenario = scenarioService.updateContent(logger, sessionuser.getId(), scenarioId, scenario);

			if(scenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.content, adminOperationCode.update, scenarioId, "");

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return "redirect:/" + PREFIX + "/content/" + scenarioId;
	}

	/**
	 * 素材ストック
	 * @param model
	 * @param scenarioId
	 * @param binaryFileId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/content/stock/save" })
	@ResponseBody
	public Map<String, String> saveStock(Model model, Long scenarioId, Long binaryFileId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Map<String, String> ret = new HashMap<>();

		try {
			
			scenarioService.createStock(logger, sessionuser.getId(), Long.valueOf(scenarioId), Long.valueOf(binaryFileId));

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return ret;
	}

	/**
	 * 素材削除
	 * @param model
	 * @param scenarioId
	 * @param binaryFileId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/content/stock/destroy" })
	@ResponseBody
	public Map<String, String> destroyStock(Model model, Long scenarioId, Long binaryFileId) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Map<String, String> ret = new HashMap<>();

		try {

			scenarioService.deleteStock(logger, sessionuser.getId(), scenarioId, binaryFileId);

		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		
		return ret;
	}

	/**
	 * シナリオのコピー
	 * @param model
	 * @param form
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.AJAX)
	@GetMapping({ PREFIX + "/copy/{scenarioId}" })
	@ResponseBody
	public Map<String, Object> copyScenario(Model model, @ModelAttribute("obj") GuidanceForm form, @PathVariable(name = "scenarioId", required = true) Long scenarioId, String scenarioKeycode, String scenarioName, String copyCompletionpoint){	

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> ret = new HashMap<String, Object>();

		try {
			List<KeyValue> keyValues = new ArrayList<>();
			Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");
	
			String fieldScenarioKeycode = messageSource.getMessage("scenarioKeycode", null, "", Locale.getDefault());
			String fieldScenarioName = messageSource.getMessage("scenarioName", null, "", Locale.getDefault());
	
			if(scenarioKeycode == null || scenarioKeycode.length() == 0) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.NotBlank.message", new String[] {fieldScenarioKeycode}, "", Locale.getDefault());
				kv.setKey("scenarioKeycode");
				kv.setValue(message);
				keyValues.add(kv);
			}
			else if(scenarioKeycode.length() < Length.scenarioKeycodeMin && scenarioKeycode.length() > Length.scenarioKeycode) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Size.range.message", null, "", Locale.getDefault());
				kv.setKey("scenarioKeycode");
				kv.setValue(message.replaceAll("\\{min\\}", String.valueOf(Length.scenarioKeycodeMin)).replaceAll("\\{max\\}", String.valueOf(Length.scenarioKeycode)));
				keyValues.add(kv);
			}
			else if(!p.matcher(scenarioKeycode).matches()) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Pattern.alphanum.message", new String[] {}, "", Locale.getDefault());
				kv.setKey("scenarioKeycode");
				kv.setValue(message);
				keyValues.add(kv);
			}
			else if(scenarioService.isDuplicate(logger, sessionuser.getId(), scenarioKeycode)) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Duplidate.message", new String[] {fieldScenarioKeycode}, "", Locale.getDefault());
				kv.setKey("scenarioKeycode");
				kv.setValue(message);
				keyValues.add(kv);
			}
	
			if(scenarioName == null || scenarioName.length() == 0) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.NotBlank.message", new String[] {fieldScenarioName}, "", Locale.getDefault());
				kv.setKey("scenarioName");
				kv.setValue(message);
				keyValues.add(kv);
			}
			else if(scenarioName.length() > Length.scenarioName) {
				KeyValue kv = new KeyValue();
				String message = messageSource.getMessage("javax.validation.constraints.Size.message", null, "", Locale.getDefault());
				kv.setKey("scenarioKeycode");
				kv.setValue(message.replaceAll("\\{max\\}", String.valueOf(Length.scenarioName)));
				keyValues.add(kv);
			}
	
			// エラーがなければ実行
			if(ret.size() == 0) {
				Scenario scenario = scenarioService.copyScenario(logger, sessionuser.getId(), scenarioId, scenarioKeycode, scenarioName, Boolean.valueOf(copyCompletionpoint));
				ret.put("result", "ok");
				ret.put("scenario", scenario);
				adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.scenarios, adminOperationCode.copy, scenario.getId(), String.valueOf(scenarioId));
			}
			else {
				ret.put("result", "error");
				ret.put("errors", keyValues);
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return ret;
	}
	
	/**
	 * プレビュー
	 * @param model
	 * @param form
	 * @param scenarioId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX + "/preview/{scenarioKeycode}" })
	public String preview(Model model, @ModelAttribute("obj") ScenarioForm form,  @PathVariable(name = "scenarioKeycode", required = true) String scenarioKeycode) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// シナリオを取得
			Scenario targetScenario = scenarioService.getValidByScenarioCode(logger, sessionuser.getId(), null, scenarioKeycode);
			if (targetScenario == null) {
				throw new ApplicationRuntimeException("Data does not exist.");
			}
			// 変数を置換
			targetScenario.setContentBody(convertVariables(targetScenario.getContentBody()));
			targetScenario.setContentScript(convertVariables(targetScenario.getContentScript()));
			for(Guidance guidance : targetScenario.getGuidances()) {
				guidance.setGuidanceText(convertVariables(guidance.getGuidanceText()));
				guidance.setPreScript(convertVariables(guidance.getPreScript()));
				guidance.setPostScript(convertVariables(guidance.getPostScript()));
				for(GuidanceAction guidanceAction : guidance.getGuidanceActions()) {
					guidanceAction.setBody(convertVariables(guidanceAction.getBody()));
				}
			}
			model.addAttribute("production", false);
			model.addAttribute("targetScenario", targetScenario);
		
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return View.WORK;

	}
	
	/**
	 * 変数の置換
	 * @param src 変換元文字列
	 * @return 変換後文字列
	 */
	private String convertVariables(String str) {
		String ret = "";
		if(str != null) {
			str = str.replaceAll("\\{CLIENT_NAME\\}", "[クライアント名]");
			str = str.replaceAll("\\{SECURITY_TEAM_NAME\\}", "[セキュリティ対策組織名]");
			str = str.replaceAll("\\{SECURITY_TEAM_TEL\\}", "[000-1111-2222]");
			str = str.replaceAll("\\{SECURITY_TEAM_EMAIL\\}", "[mailaddress@spractice.com]");
			str = str.replaceAll("\\{ACCOUNT\\}", "[youraccount]");
			str = str.replaceAll("\\{EMAIL\\}", "[youraccount@spractice.com]");
			str = str.replaceAll("\\{USER_FULLNAME\\}", "[テスト 太郎]");
			str = str.replaceAll("\\{USER_FAMILYNAME\\}", "[テスト]");
			str = str.replaceAll("\\{USER_GIVENNAME\\}", "[太郎]");
			ret = str;
		}
		return ret;
	}
}

