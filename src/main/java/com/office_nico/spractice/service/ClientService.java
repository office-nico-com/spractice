package com.office_nico.spractice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientScenario;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.repository.client.ClientRepository;
import com.office_nico.spractice.repository.client_scenario.ClientScenarioRepository;
import com.office_nico.spractice.repository.scenario.ScenarioRepository;
import com.office_nico.spractice.util.BeanUtil;

@Service
@Transactional
public class ClientService {

	@Autowired ClientRepository clientRepository = null;

	@Autowired ClientScenarioRepository clientScenarioRepository = null;

	@Autowired ScenarioRepository scenarioRepository = null;

	private static final Logger _logger = LoggerFactory.getLogger(ClientService.class);
	
	/**
	 * ページリストの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param String serach 検索条件
	 * @param current 現在のページ
	 * @param pageMax 1ページ当たりの表示数
	 * @return ページオブジェクト
	 */
	public Page<Client> page(Logger logger, Long sessionUserId, String serach, int start, int length, Sort.Direction dir, String[] order){
		logger = (logger == null ? logger : _logger);

		int pageNumber = start == 0 ? 0 : (start / length);
		Pageable pr = PageRequest.of(pageNumber, length, dir, order);
		
		Page<Client> page = null;
		if(serach != null && serach.length() > 0) {
			serach = "%" + serach + "%";
			page = clientRepository.findByIsDeletedFalseAndClientNameOrClientKeycode(serach, pr);
		}
		else {
			page = clientRepository.findByIsDeletedFalse(pr);
		}
		return page;
	}

	
	/**
	 * クライアントの一覧を取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 */
	public List<Client> list(Logger logger, Long sessionUserId){
		logger = (logger == null ? logger : _logger);

		return clientRepository.findByIsDeletedFalseOrderByClientNameJaKana();
	}
	
	
	/**
	 * レコードの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @return エンティティ
	 */
	public Client get(Logger logger, Long sessionUserId, Long clientId) {
		logger = (logger == null ? logger : _logger);

		Optional<Client> entity = clientRepository.findById(clientId);

		return entity.get();
	}

	/**
	 * クライアントの登録
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param client エンティティ
	 * @param 登録されたエンティティ
	 */
	@Transactional
	public Client create(Logger logger, Long sessionUserId, Client client) {
		logger = (logger == null ? logger : _logger);

		client.setSecret(RandomStringUtils.randomAlphanumeric(8));
		client.setIsDeleted(false);
		client.setUpdatedBy(sessionUserId);
		client.setCreatedBy(sessionUserId);
		client.setUpdatedAt(LocalDateTime.now());
		client.setCreatedAt(LocalDateTime.now());
		
		clientRepository.save(client);
		
		return client;
	}

	/**
	 * クライアントの更新
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param client エンティティ
	 * @return 更新されたエンティティ
	 */
	public Client update(Logger logger, Long sessionUserId, Long clientId, Client client) {
		logger = (logger == null ? logger : _logger);

		Optional<Client> entity = clientRepository.findById(clientId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			BeanUtil.copyFields(entity.get(), client, "id", "isDeleted", "createdAt", "updatedAt", "createdBy", "updatedBy", "secret");
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			clientRepository.save(entity.get());
		}

		return entity.get();
	}

	/**
	 * クライアントの削除
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 */
	public void delete(Logger logger, Long sessionUserId, Long clientId) {
		logger = (logger == null ? logger : _logger);

		Optional<Client> entity = clientRepository.findById(clientId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setIsDeleted(true);
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			clientRepository.save(entity.get());
		}
	}
	

	/**
	 * クライアントコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientKeycode クライアントコード 
	 * @return true：重複、false：重複なし
	 */
	public Boolean isDuplidate(Logger logger, Long sessionUserId, String clientKeycode) {
		logger = (logger == null ? logger : _logger);

		Boolean ret = false;
		if(clientRepository.countByIsDeletedFalseAndClientKeycode(clientKeycode) > 0L) {
			ret = true;
		}
		return ret;
	}

	/**
	 * クライアントコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientKeycode クライアントコード 
	 * @param exclusionClienId 除外ID
	 * @return true：重複、false：重複なし
	 */
	public Boolean isDuplidate(Logger logger, Long sessionUserId, String clientKeycode, Long exclusionClienId) {
		logger = (logger == null ? logger : _logger);

		Boolean ret = false;
		if(clientRepository.countByIsDeletedFalseAndClientKeycodeAndIdNot(clientKeycode, exclusionClienId) > 0L) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * ポータルのシナリオ一覧を取得する
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @return シナリオリスト
	 */
	public List<ClientScenario> getPortalScenario(Logger logger, Long sessionUserId, Long clientId){
		logger = (logger == null ? logger : _logger);
	
		// List<Scenario> ret = clientScenarioRepository.findBySenarioByClientId(clientId);
		
		List<ClientScenario> ret = clientScenarioRepository.findSenarioByClientId(clientId);
		
		return ret;
	}
	
	/**
	 * ポータルのシナリオを保存する
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPoints
	 */
	public void savePortalSecnarios(Logger logger, Long sessionUserId, Long clientId, List<ClientScenario> clientScenarios) {
		logger = (logger == null ? logger : _logger);

		List<ClientScenario> records = clientScenarioRepository.findByClientId(clientId);

		Optional<Client> client = clientRepository.findById(clientId);
		if(client.isEmpty()) {
			throw new NullPointerException("No client data ! [" + clientId + "]");
		}
		
		
		for(ClientScenario record : records) {
			boolean find = false;
			for(ClientScenario clientScenario : clientScenarios) {
				if(record.getScenario().getId().equals(clientScenario.getScenarioId())) {
					find = true;
					break;
				}
			}
			if(!find) {
				// 削除
				clientScenarioRepository.delete(record);
			}
		}

		
		for(ClientScenario clientScenario : clientScenarios) {
			boolean find = false;
			for(ClientScenario record : records) {
				if(record.getScenario().getId().equals(clientScenario.getScenarioId())) {
					find = true;

					// 更新
					record.setSortOrder(clientScenario.getSortOrder());
					record.setIsInvalided(clientScenario.getIsInvalided());
					record.setUpdatedBy(sessionUserId);
					record.setUpdatedAt(LocalDateTime.now());
					clientScenarioRepository.save(record);

					break;
				}
			}
			
			// 追加
			if(!find) {
				Optional<Scenario> scenario = scenarioRepository.findById(clientScenario.getScenarioId());
				if(!scenario.isEmpty()) {
					clientScenario.setClient(client.get());
					clientScenario.setScenario(scenario.get());
					clientScenario.setUpdatedBy(sessionUserId);
					clientScenario.setCreatedBy(sessionUserId);
					clientScenario.setUpdatedAt(LocalDateTime.now());
					clientScenario.setCreatedAt(LocalDateTime.now());

					clientScenarioRepository.save(clientScenario);
				}
			}
		}
	}
	
	/**
	 * クライアントコードから有効なクライアント情報を取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientKeycode クライアントコード
	 * @return クライアント情報
	 */
	public Client getValidClientByClientKeycode(Logger logger, Long sessionUserId, String clientKeycode) {
		logger = (logger == null ? logger : _logger);

		Client client = clientRepository.findTopByClientKeycodeAndIsDeletedFalseAndIsInvalidedFalse(clientKeycode);
		return client;
	}
	/*
	 * クライアントコードからクライアント情報を取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientKeycode クライアントコード
	 * @return クライアント情報
	 */
	public Client getClientByClientKeycode(Logger logger, Long sessionUserId, String clientKeycode) {
		logger = (logger == null ? logger : _logger);

		Client client = clientRepository.findTopByClientKeycodeAndIsDeletedFalse(clientKeycode);
		return client;
	}
}
