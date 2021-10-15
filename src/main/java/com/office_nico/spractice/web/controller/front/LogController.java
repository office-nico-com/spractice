package com.office_nico.spractice.web.controller.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.constants.codes.CompletionConditionCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientUser;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.ClientService;
import com.office_nico.spractice.service.CompletionService;
import com.office_nico.spractice.service.UserService;
import com.office_nico.spractice.service.data.SessionData;

@Controller
public class LogController {

	
	private static final String PREFIX = "front/logs";

	private static final Logger logger = LoggerFactory.getLogger(LogController.class);

	@Autowired private ClientService clientService = null;

	@Autowired	private CompletionService completionService = null;

	@Autowired private CompletionConditionCode _completionConditionCode = null;

	@Autowired	private UserService userService = null;

	@Autowired private SessionData sessionData = null;
	
	/**
	 * 履修処理
	 * @param model
	 * @param binaryFileId
	 * @return
	 */
	@GetMapping({ PREFIX + "/completion" })
	@Action(Type.AJAX)
	@ResponseBody
	public Map<String, Object> completion(Model model, String clientKeycode, String completionPointKeycode, Short completionConditionCode, String transaction) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		try {
			if(clientKeycode != null && completionPointKeycode != null && completionConditionCode != null) {

				// ログインチェック
				if (sessionData.getUserId() == null) {
					// 未ログイン
					result.put("result", -1);
					return result;
				}

				Client client = clientService.getValidClientByClientKeycode(logger, sessionData.getUserId(), clientKeycode);
				if (client == null) {
					// クライアントなし
					result.put("result", -2);
					return result;
				}

				// 所属クライアントチェック
				boolean find = false;
				List<ClientUser> clientUsers = userService.getAffiliatedClients(logger, sessionData.getUserId(), sessionData.getUserId());
				for (ClientUser clientUser : clientUsers) {
					if (clientUser.getClient().getId().equals(client.getId())) {
						find = true;
						break;
					}
				}
				if (!find) {
					// 所属していないクライアント
					result.put("result", -3);
					return result;
				}

				if(completionConditionCode.equals(_completionConditionCode.start)) {
					if(!completionService.startCompletion(logger, sessionData.getUserId(), client.getId(), sessionData.getUserId(), completionPointKeycode, transaction)) {
						result.put("result", -4);
						return result;
					}
				}
				else if(completionConditionCode.equals(_completionConditionCode.end)) {
					if(completionService.endCompletion(logger, sessionData.getUserId(),  client.getId(), sessionData.getUserId(), completionPointKeycode, transaction)) {
						result.put("result", -5);
						return result;
					}
				}
			}
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
		return new HashMap<String, Object>();
	}
}

