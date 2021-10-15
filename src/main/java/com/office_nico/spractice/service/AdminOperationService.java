package com.office_nico.spractice.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.constants.codes.AdminFunctionCode;
import com.office_nico.spractice.constants.codes.AdminOperationCode;
import com.office_nico.spractice.domain.AdminOperation;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.repository.admin_operation.AdminOperationRepository;
import com.office_nico.spractice.repository.scenario.ScenarioRepository;
import com.office_nico.spractice.repository.user.UserRepository;
import com.office_nico.spractice.service.data.AdminOperationData;
import com.office_nico.spractice.util.DateTimeUtil;

@Service
@Transactional
public class AdminOperationService {
	
	private static final Logger _logger = LoggerFactory.getLogger(AdminOperationService.class);

	@Autowired
	private UserRepository userRepository = null;

	@Autowired
	private ScenarioRepository scenarioRepository = null;

	@Autowired
	private AdminOperationRepository adminOperationRepository = null;


	@Autowired
	private AdminFunctionCode adminFunctionCode = null;

	@Autowired
	private AdminOperationCode adminOperationCode = null;

	@Autowired
	private MessageSource messageSource = null;

	/**
	 * 管理者操作の記録
	 */
	public void record(Logger logger, Long sessionUserId, Short adminFunctionCode, Short adminOperationCode, Long targetId, String message) {
		logger = (logger == null ? logger : _logger);
		
		try {
			AdminOperation adminOperation = new AdminOperation();
			LocalDateTime now = LocalDateTime.now();
			Optional<User> user = userRepository.findById(sessionUserId);
			adminOperation.setOperatedAt(now);
			adminOperation.setUser(user.get());
			adminOperation.setFunctionCode(adminFunctionCode);
			adminOperation.setOperationCode(adminOperationCode);
			adminOperation.setTargetId(targetId);
			adminOperation.setMessage(message);
			adminOperation.setCreatedBy(sessionUserId);
			adminOperation.setCreatedAt(now);
			adminOperation.setUpdatedBy(sessionUserId);
			adminOperation.setUpdatedAt(now);
			adminOperationRepository.save(adminOperation);
		}
		catch(Exception e) {
			// 無視
		}
	}
	
	/**
	 * 管理者操作履歴の取得
	 * @param logger
	 * @param sessionUserId
	 * @param targetUserId
	 * @param page
	 * @param length
	 * @return
	 */
	public List<AdminOperationData> list(Logger logger, Long sessionUserId, Long targetUserId, Integer page, Integer length){
		List<AdminOperationData> ret = new ArrayList<>();
		
		Page<Map<String, Object>> results = null;
		
		Pageable pr = PageRequest.of(page, length);
		
		if(targetUserId != null) {
			results = adminOperationRepository.findByUserId(targetUserId, pr);
		}
		else {
			results = adminOperationRepository.findAllRecords(pr);
		}

		if(results != null) {
			for(Map<String, Object> result : results.toList()) {
				String message = null;
				Short functionCode = (Short)result.get("function_code");
				Short operationCode = (Short)result.get("operation_code");
				if(functionCode == adminFunctionCode.users) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.user.1", new String[] { (String)result.get("account"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						message = messageSource.getMessage("admin.operation.message.user.2", new String[] { String.valueOf(result.get("target_id")), (String)result.get("account"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.importUser) {
					message = messageSource.getMessage("admin.operation.message.import.1", new String[] {(String)result.get("message")}, Locale.getDefault());
				}
				else if(functionCode == adminFunctionCode.clients) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.client.1", new String[] { (String)result.get("client_name_ja"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						message = messageSource.getMessage("admin.operation.message.client.2", new String[] { String.valueOf(result.get("target_id")), (String)result.get("client_name_ja"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.scenarios) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.scenario.1", new String[] {(String)result.get("scenario_name"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else if(operationCode == adminOperationCode.copy) {
						Optional<Scenario> s = scenarioRepository.findById(Long.valueOf((String)result.get("message")));
						message = messageSource.getMessage("admin.operation.message.scenario.3", new String[] {String.valueOf(s.get().getId()), s.get().getScenarioName(),  String.valueOf(result.get("target_id")), (String)result.get("scenario_name"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						message = messageSource.getMessage("admin.operation.message.scenario.2", new String[] { String.valueOf(result.get("target_id")), (String)result.get("scenario_name"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.content) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.content.1", new String[] { (String)result.get("scenario_name"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						message = messageSource.getMessage("admin.operation.message.content.2", new String[] { (String)result.get("scenario_name"), String.valueOf(result.get("target_id")), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.guidance) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.guidance.1", new String[] { (String)result.get("scenario_name"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						message = messageSource.getMessage("admin.operation.message.guidance.2", new String[] { (String)result.get("scenario_name"), String.valueOf(result.get("target_id")), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.portals) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.potal.1", new String[] { (String)result.get("client_name_ja"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						
						message = messageSource.getMessage("admin.operation.message.potal.2", new String[] { (String)result.get("client_name_ja"), String.valueOf(result.get("target_id")), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.completionpoints) {
					if(operationCode == adminOperationCode.delete) {
						message = messageSource.getMessage("admin.operation.message.completionpoints.1", new String[] { (String)result.get("completion_point_keycode"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
					else {
						message = messageSource.getMessage("admin.operation.message.completionpoints.2", new String[] { String.valueOf(result.get("target_id")), (String)result.get("completion_point_keycode"), adminOperationCode.label(operationCode)}, Locale.getDefault());
					}
				}
				else if(functionCode == adminFunctionCode.completions) {
					message = messageSource.getMessage("admin.operation.message.download.1", new String[] {(String)result.get("message")}, Locale.getDefault());
				}
				else if(functionCode == adminFunctionCode.other) {
					message = (String)result.get("message");
				}
				if(message != null) {
					
					AdminOperationData adminOperationData = new AdminOperationData();
					if(targetUserId == null) {
						message = messageSource.getMessage("admin.operation.message.other.user.operation", new String[] {(String)result.get("operator_family_name"), (String)result.get("operator_given_name"), message}, Locale.getDefault());
					}
					adminOperationData.setMessage(message);
					adminOperationData.setOperatedAt(DateTimeUtil.toLocalDateTime((Timestamp)result.get("operated_at")));
					adminOperationData.setFamilyName((String)result.get("operator_family_name"));
					adminOperationData.setGivenName((String)result.get("operator_given_name"));
					ret.add(adminOperationData);
				}
			}
		}
		return ret;
	}
	
}
