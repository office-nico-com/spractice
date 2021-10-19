package com.office_nico.spractice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.Completion;
import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.domain.Guidance;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.repository.completion.CompletionRepository;
import com.office_nico.spractice.repository.completion_point.CompletionPointRepository;
import com.office_nico.spractice.repository.guidance.GuidanceRepository;
import com.office_nico.spractice.repository.scenario.ScenarioRepository;

@Service
@Transactional
public class CompletionService {

	private static final Logger _logger = LoggerFactory.getLogger(CompletionService.class);
	
	@Autowired private CompletionPointRepository completionPointRepository = null;

	@Autowired private CompletionRepository completionRepository = null;

	@Autowired private GuidanceRepository guidanceRepository = null;
	
	@Autowired private ScenarioRepository scenarioRepository = null;


	/**
	 * ページリストの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param current 現在のページ
	 * @param pageMax 1ページ当たりの表示数
	 * @return ページオブジェクト
	 */
	public Page<CompletionPoint> page(Logger logger, Long sessionUserId, String search, int start, int length, Sort.Direction dir, String[] orders){
		logger = (logger == null ? logger : _logger);
		/*
		int pageNumber = start == 0 ? 0 : (start / length);
		
		Pageable pr = PageRequest.of(pageNumber, length, dir, order);
		Page<CompletionPoint> page = completionPointRepository.findByIsDeletedFalse(pr);
		*/
		Page<CompletionPoint> page = completionPointRepository.findCompletionPointsBySearchKeywordAndIsDeletedFalse(search, orders, dir.toString(), start, length);
		
		return page;
	}
	
	/**
	 * 有効な修了ポイントを取得する
	 * @param logger ロガー
	 * @return 修了ポイントリスト
	 */
	public List<CompletionPoint> getValidCompletionPoints(Logger logger, Long sessionUserId){
		return completionPointRepository.findByIsDeletedFalseAndIsInvalidedFalseOrderByCompletionPointKeycode();
	}
	
	
	/**
	 * レコードの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPointId 履修ポイントID
	 * @return エンティティ
	 */
	public CompletionPoint get(Logger logger, Long sessionUserId, Long completionPointId) {
		logger = (logger == null ? logger : _logger);

		Optional<CompletionPoint> entity = completionPointRepository.findById(completionPointId);

		return entity.get();
	}

	/**
	 * 履修ポイントの登録
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPoint エンティティ
	 * @param 登録されたエンティティ
	 */
	@Transactional
	public CompletionPoint create(Logger logger, Long sessionUserId, CompletionPoint completionPoint) {
		logger = (logger == null ? logger : _logger);

		completionPoint.setIsDeleted(false);
		completionPoint.setUpdatedBy(sessionUserId);
		completionPoint.setCreatedBy(sessionUserId);
		completionPoint.setUpdatedAt(LocalDateTime.now());
		completionPoint.setCreatedAt(LocalDateTime.now());
		
		completionPointRepository.save(completionPoint);
		
		return completionPoint;
	}

	/**
	 * 履修ポイントの更新
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPointId 履修ポイントID
	 * @param completionPoint エンティティ
	 * @return 更新されたエンティティ
	 */
	public CompletionPoint update(Logger logger, Long sessionUserId, Long completionPointId,CompletionPoint completionPoint) {
		logger = (logger == null ? logger : _logger);

		Optional<CompletionPoint> entity = completionPointRepository.findById(completionPointId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setCompletionPointKeycode(completionPoint.getCompletionPointKeycode());
			entity.get().setDescription(completionPoint.getDescription());
			entity.get().setDescriptionEn(completionPoint.getDescriptionEn());
			entity.get().setIsInvalided(completionPoint.getIsInvalided());
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			completionPointRepository.save(entity.get());
		}

		return entity.get();
	}

	/**
	 * 履修ポイントの削除
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPointId 履修ポイントID
	 */
	public void delete(Logger logger, Long sessionUserId, Long completionPointId) {
		logger = (logger == null ? logger : _logger);

		Optional<CompletionPoint> entity = completionPointRepository.findById(completionPointId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setIsDeleted(true);
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			completionPointRepository.save(entity.get());
		}
	}

	/**
	 * 履修ポイントキーコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPointKeycode 履修ポイントキーコード
	 * @param exclusionCompletionPointId 除外履修ポイントID
	 * @return true:重複、false：重複なし
	 */
	public Boolean isDuplicate(Logger logger, Long sessionUserId, String completionPointKeycode, Long exclusionCompletionPointId) {
		logger = (logger == null ? logger : _logger);
		
		Boolean ret = false;
		
		if(completionPointRepository.countByIsDeletedFalseAndCompletionPointKeycodeAndIdNot(completionPointKeycode, exclusionCompletionPointId) > 0L) {
			ret = true;
		}
		return ret;
	}

	
	/**
	 * 履修ポイントキーコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPointKeycode 履修ポイントキーコード
	 * @return true:重複、false：重複なし
	 */
	public Boolean isDuplicate(Logger logger, Long sessionUserId, String completionPointKeycode) {
		logger = (logger == null ? logger : _logger);
		
		Boolean ret = false;
		
		if(completionPointRepository.countByIsDeletedFalseAndCompletionPointKeycode(completionPointKeycode) > 0L) {
			ret = true;
		}
		return ret;
	}

	
	/**
	 * 履修開始
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param userId  ユーザーID
	 * @param completionPointKeycode 履修コード
	 * @param transaction トランザクション情報
	 * @return true：成功、false：失敗
	 */
	public Boolean startCompletion(Logger logger, Long sessionUserId, Long clientId, Long userId, String completionPointKeycode, String transaction) {
		logger = (logger == null ? logger : _logger);

		Boolean ret = false;
		CompletionPoint completionPoint = completionPointRepository.findTopByIsDeletedFalseAndIsInvalidedFalseAndCompletionPointKeycode(completionPointKeycode);
		if(completionPoint != null) {
			Completion completion = completionRepository.findTopByClientIdAndUserIdAndCompletionPointIdAndTransaction(clientId, sessionUserId, completionPoint.getId(), transaction);
			if(completion != null) {
				completion.setStartedAt(LocalDateTime.now());
				completion.setUpdatedAt(LocalDateTime.now());
				completion.setUpdatedBy(userId);
			}
			else {
				completion = new Completion();
				completion.setClientId(clientId);
				completion.setUserId(userId);
				completion.setCompletionPoint(completionPoint);
				completion.setTransaction(transaction);
				completion.setStartedAt(LocalDateTime.now());
				completion.setUpdatedAt(LocalDateTime.now());
				completion.setUpdatedBy(userId);
				completion.setCreatedBy(userId);
				completion.setUpdatedAt(LocalDateTime.now());
				completion.setCreatedAt(LocalDateTime.now());
			}
			completionRepository.save(completion);
			ret = true;
		}
		return ret;
	}

	/**
	 * 履修終了
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param userId  ユーザーID
	 * @param completionPointKeycode 履修コード
	 * @param transaction トランザクション情報
	 * @return true：成功、false：失敗
	 */
	public Boolean endCompletion(Logger logger, Long sessionUserId, Long clientId, Long userId, String completionPointKeycode, String transaction) {
		logger = (logger == null ? logger : _logger);

		Boolean ret = false;
		
		CompletionPoint completionPoint = completionPointRepository.findTopByIsDeletedFalseAndIsInvalidedFalseAndCompletionPointKeycode(completionPointKeycode);

		if(completionPoint != null) {
			
			Completion completion = completionRepository.findTopByClientIdAndUserIdAndCompletionPointIdAndTransaction(clientId, sessionUserId, completionPoint.getId(), transaction);
			if(completion != null) {
				completion.setEndedAt(LocalDateTime.now());
				completion.setUpdatedAt(LocalDateTime.now());
				completion.setUpdatedBy(userId);
			}
			else {
				completion = new Completion();
				completion.setClientId(clientId);
				completion.setUserId(userId);
				completion.setCompletionPoint(completionPoint);
				completion.setTransaction(transaction);
				completion.setEndedAt(LocalDateTime.now());
				completion.setUpdatedAt(LocalDateTime.now());
				completion.setUpdatedBy(userId);
				completion.setCreatedBy(userId);
				completion.setUpdatedAt(LocalDateTime.now());
				completion.setCreatedAt(LocalDateTime.now());
			}
			completionRepository.save(completion);
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 履修履歴の一覧を取得する
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param userId ユーザーID
	 * @param scenarioId シナリオID
	 * @param maxCount 取得最大数
	 * @return 履修履歴一覧
	 */
	public List<Completion> getLastHistories(Logger logger, Long sessionUserId, Long userId, Long clientId, Long scenarioId){
		logger = (logger == null ? logger : _logger);

		// シナリオ中に含まれる履修ポイントのIDをすべて取得し、該当する履修履歴を一覧にする

		List<Long> completionPointIds = new ArrayList<>();
		List<Guidance> guidances = guidanceRepository.findByScenarioIdAndStartCompletionPointIdIsNotNullAndEndCompletionPointIdIsNotNullOrderBySortOrder(scenarioId);
		for(Guidance guidance : guidances) {
			if(guidance.getStartCompletionPointId() != null && !completionPointIds.contains(guidance.getStartCompletionPointId())) {
				completionPointIds.add(guidance.getStartCompletionPointId());
			}
			if(guidance.getEndCompletionPointId() != null && !completionPointIds.contains(guidance.getEndCompletionPointId())) {
				completionPointIds.add(guidance.getEndCompletionPointId());
			}
		}
		
		// 履歴の一覧を取得
		List<Completion> completions = completionRepository.findByUserIdAndClientIdAndCompletionPointIds(userId, clientId, completionPointIds, 10);
		
		return completions;
	}


	/**
	 * 最終結果を取得する
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param userId ユーザーID
	 * @param scenarioId シナリオID
	 * @param maxCount 取得最大数
	 * @return 履修履歴一覧
	 */
	public List<CompletionPoint> getCompletionResults(Logger logger, Long sessionUserId, Long userId, Long clientId, Long scenarioId){
		logger = (logger == null ? logger : _logger);

		// シナリオ中に含まれる履修ポイントのIDをすべて取得し、該当する履修結果を一覧にする

		List<Long> completionPointIds = new ArrayList<>();
		List<Guidance> guidances = guidanceRepository.findByScenarioIdAndStartCompletionPointIdIsNotNullAndEndCompletionPointIdIsNotNullOrderBySortOrder(scenarioId);
		for(Guidance guidance : guidances) {
			if(guidance.getStartCompletionPointId() != null && !completionPointIds.contains(guidance.getStartCompletionPointId())) {
				completionPointIds.add(guidance.getStartCompletionPointId());
			}
			if(guidance.getEndCompletionPointId() != null && !completionPointIds.contains(guidance.getEndCompletionPointId())) {
				completionPointIds.add(guidance.getEndCompletionPointId());
			}
		}
		
		List<CompletionPoint> completions = completionPointRepository.findByUserIdAndClientIdAndCompletionPointIds(userId, clientId, completionPointIds);
		
		return completions;
	}

	/**
	 * 利用しているシナリオの一覧を返す
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarId        シナリオID
	 * @return クライアントシナリオリスト
	 */
	public List<Scenario> getUsedScenarios(Logger logger, Long sessionUserId, Long completionPointId) {
		logger = (logger == null ? logger : _logger);

		return scenarioRepository.findScenarioByCompletionPointId(completionPointId);
	}
}
