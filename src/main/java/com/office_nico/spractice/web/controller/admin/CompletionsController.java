package com.office_nico.spractice.web.controller.admin;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.constants.codes.AdminFunctionCode;
import com.office_nico.spractice.constants.codes.AdminOperationCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.AdminOperationService;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.ReportService;
import com.office_nico.spractice.service.data.CompletionHistory;
import com.office_nico.spractice.service.data.CompletionUser;
import com.office_nico.spractice.service.data.ReportData;
import com.office_nico.spractice.util.DateTimeUtil;
import com.opencsv.CSVWriter;

/**
 * 履修履歴
 */
@Controller
public class CompletionsController {

	private static final String PREFIX = "t06o1ny8/completions";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/completions/index";
		public static final String SHOW = "admin/completions/show";
		public static final String UPLOAD = "admin/completions/upload";
	}

	private static final Logger logger = LoggerFactory.getLogger(CompletionsController.class);
	
	@Autowired
	private ClientService clientService = null;
	
	@Autowired
	private ReportService reportService = null;

	@Autowired
	private MessageSource messageSource = null;

	@Autowired
	private AdminOperationService adminOperationService = null;

	@Autowired
	private AdminFunctionCode adminFunctionCode = null;

	@Autowired
	private AdminOperationCode adminOperationCode = null;

	/**
	 * 履修履歴トップ
	 * @param model
	 * @param binaryFileCategoryId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX })
	public String index(Model model, Long clientId, String fd, String td) {

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {

			// クライアントの一覧を取得
			List<Client> clients = clientService.list(logger, sessionuser.getId());
			model.addAttribute("clients", clients);


			model.addAttribute("selectedClientId", clientId);
			
			// 初期日付
			LocalDateTime now = LocalDateTime.now();
			
			if(td == null || td.length() == 0) {
				LocalDateTime _td = now.minusDays(1);
				td = DateTimeUtil.format(_td, DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_SUPPRESS);
			}
			if(fd == null || fd.length() == 0) {
				LocalDateTime _fd = now.minusDays(7);
				fd = DateTimeUtil.format(_fd, DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_SUPPRESS);
			}
			model.addAttribute("toDate", td);
			model.addAttribute("fromDate", fd);

			
			List<ReportData> reports = new ArrayList<>();
			if(clientId != null) {
				// 総受講者数
				Integer totalCount = reportService.getTotalEntryCount(logger, sessionuser.getId(), clientId);
				
				// データ集計
				reports = reportService.getReports(logger, sessionuser.getId(), clientId, totalCount, true);
			}
			model.addAttribute("reports", reports);

			
			return View.INDEX;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	
	// ダウンロード
	@Action(Type.CSV)
	@GetMapping({ PREFIX + "/download/{completionPointId}" })
	public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable(name = "completionPointId", required = true) Long completionPointId, Long clientId, String fd, String td) {
		
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
			
			Client client = clientService.get(logger, sessionuser.getId(), clientId);
			
			CompletionHistory completionHistory = reportService.getCompletionHistories(logger, sessionuser.getId(), completionPointId, clientId, fromDate, toDate);

			String fileName = client.getClientNameJa() + "_" + completionHistory.getCompletionPoint().getDescription() + "_" + DateTimeUtil.format(fromDate, DateTimeUtil.DateTimeFormat.DATE_NUMBER) + "-" + DateTimeUtil.format(toDate, DateTimeUtil.DateTimeFormat.DATE_NUMBER) + ".csv";
			fileName = fileName.replaceAll(" ", "_");
			fileName = fileName.replaceAll("　", "_");

			Cookie cookie = new Cookie("fileDownload", "true");
			cookie.setPath("/");
			response.addCookie(cookie);
			
			// response.setHeader("Set-Cookie", "fileDownload=true; path=/");
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
			
			// オペレーションログ出力
			adminOperationService.record(logger, sessionuser.getId(), adminFunctionCode.completions	, adminOperationCode.download, null, fileName);
			
		}
		catch (Exception e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
}
