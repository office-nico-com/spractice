package com.office_nico.spractice.web.controller.front;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.constants.codes.RoleCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.ReportService;
import com.office_nico.spractice.service.ScenarioService;
import com.office_nico.spractice.service.UserService;
import com.office_nico.spractice.service.data.CompletionHistory;
import com.office_nico.spractice.service.data.CompletionUser;
import com.office_nico.spractice.service.data.ReportData;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.util.DateTimeUtil;
import com.opencsv.CSVWriter;


@Controller
public class FrontReportController {

	private static final String PREFIX = "front/report";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = PREFIX + "/index";
		public static final String NOTFOUND = "front/404";
		public static final String FORBIDDEN = "front/403";
	}

	private static final Logger logger = LoggerFactory.getLogger(FrontReportController.class);

	@Autowired
	private ClientService clientService = null;

	@Autowired
	private SessionData sessionData = null;

	@Autowired
	private UserService userService = null;

	@Autowired
	private ScenarioService scenarioService = null;

	@Autowired
	private RoleCode roleCode = null;

	@Autowired
	private ReportService reportService = null;

	@Autowired
	private MessageSource messageSource = null;

	/**
	 * レポート画面
	 * 
	 * @param model
	 * @param form
	 * @param clientKeycode
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/report" })
	public String report(Model model, @PathVariable(name = "clientKeycode", required = true) String clientKeycode) {

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
	
			// 権限のチェック
			Short _roleCode = userService.getRoleCode(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId());
			if(_roleCode != roleCode.admin) {
				return View.FORBIDDEN;
			}
			
			
			// シナリオの一覧を取得
			List<Scenario> scenarios = scenarioService.getValidByClientId(logger, sessionData.getUserId(), client.getId());
			model.addAttribute("scenarios", scenarios);
	
			// 権限を取得
			model.addAttribute("roleCode", _roleCode);
	
			// 現在日時
			model.addAttribute("currentTime", LocalDateTime.now());
			
			// 総受講者数
			Integer totalCount = reportService.getTotalEntryCount(logger, sessionData.getUserId(), client.getId());
			model.addAttribute("totalCount", totalCount);
			
			// データ集計
			List<ReportData> reports = reportService.getReports(logger, sessionData.getUserId(), client.getId(), totalCount);
			model.addAttribute("reports", reports);
			
			
			
			// 初期日付
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime td = now.minusDays(1);
			LocalDateTime fd = now.minusDays(7);
			model.addAttribute("toDate", td);
			model.addAttribute("fromDate", fd);
		
		}
		catch (Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		
		// レポート結果
		return View.INDEX;
	}
	
	
	// ダウンロード
	@Action(Type.CSV)
	@GetMapping({ "{clientKeycode:(?!^css$)(?!^img$)(?!^js$)(?!^smile-admin-template$)(?!^t06o1ny8$)(?!^front$)^[a-zA-Z0-9]+$}/download/{completionPointId:^[0-9]+$}" })
	public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable(name = "clientKeycode", required = true) String clientKeycode, @PathVariable(name = "completionPointId", required = true) Long completionPointId, String fd, String td) {
		

		// クライアントチェック
		Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
		if (client == null) {
			// クライアントなし → Not Found画面
			throw new ApplicationRuntimeException("The client does not exist. clientKeycode = " + clientKeycode);
		}

		// ログインチェック
		if (sessionData.getUserId() == null) {
			throw new ApplicationRuntimeException("You are not logged in.");
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
			throw new ApplicationRuntimeException("You do not have permission to operate this client. clientKeycode = " + clientKeycode);
		}

		// 権限のチェック
		Short _roleCode = userService.getRoleCode(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId());
		if(_roleCode != roleCode.admin) {
			throw new ApplicationRuntimeException("You do not have permission to view the report. userId = " + sessionData.getAccount() + " clientKeycode = " + clientKeycode);
		}
		
		// 該当のクライアント中に指定のcompletionPointIdが含まれるチェック

		try(OutputStream out = response.getOutputStream();
				Writer writer = new OutputStreamWriter(out, "Shift_JIS");
				CSVWriter  csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			){
			
			
			LocalDate fromDate = DateTimeUtil.parse(fd, LocalDate.class);
			if(fromDate == null) {
				fromDate = LocalDate.now().minusDays(7);
			}
			LocalDate toDate = DateTimeUtil.parse(td, LocalDate.class);
			if(toDate == null) {
				toDate = LocalDate.now().minusDays(1);
			}
			
			CompletionHistory completionHistory = reportService.getCompletionHistories(logger, sessionData.getUserId(), completionPointId, client.getId(), fromDate, toDate);

			String fileName = client.getClientNameJa() + "_" + completionHistory.getCompletionPoint().getDescription() + "_" + DateTimeUtil.format(fromDate, DateTimeUtil.DateTimeFormat.DATE_NUMBER) + "-" + DateTimeUtil.format(toDate, DateTimeUtil.DateTimeFormat.DATE_NUMBER) + ".csv";
			fileName = fileName.replaceAll(" ", "_");
			fileName = fileName.replaceAll("　", "_");

			// response.setHeader("Set-Cookie", "fileDownload = true; path = /");
			Cookie cookie = new Cookie("fileDownload", "true");
			cookie.setPath("/");
			response.addCookie(cookie);

			response.setContentType("application/octet-stream");

			if (request.getHeader("User-Agent").indexOf("MSIE") == -1) {
				// Firefox, Opera 11
				response.setHeader("Content-Disposition", String.format(Locale.JAPAN, "attachment; filename*=utf-8'jp'%s", URLEncoder.encode(fileName, "utf-8")));
			}
			else {
				// IE7, 8, 9
				response.setHeader("Content-Disposition", String.format(Locale.JAPAN, "attachment; filename=\"%s\"", new String(fileName.getBytes("MS932"), "ISO8859_1")));			
			}
			// out.write(fileBody.toString().getBytes());

			
			List<String> header = new ArrayList<>();
			header.add(messageSource.getMessage("csv.header.ended.at", null, Locale.getDefault()));
			header.add(messageSource.getMessage("csv.header.account", null, Locale.getDefault()));
			header.add(messageSource.getMessage("csv.header.username", null, Locale.getDefault()));
			header.add(messageSource.getMessage("csv.header.email", null, Locale.getDefault()));

			String[] line = new String[header.size()];
			header.toArray(line);
			csvWriter.writeNext(line); 

			for(CompletionUser completionUser : completionHistory.getCompletionUsers()) {
				List<String> body = new ArrayList<>();
				body.add(DateTimeUtil.format(completionUser.getEndedAt(), DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_SUPPRESS1));
				body.add(completionUser.getAccount());
				body.add(completionUser.getFamilyName() + " " + completionUser.getGivenName());
				body.add(completionUser.getEmail());
				line = new String[body.size()];
				body.toArray(line);
				csvWriter.writeNext(line); 
			}
		}
		catch (Exception e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		
	}
}

