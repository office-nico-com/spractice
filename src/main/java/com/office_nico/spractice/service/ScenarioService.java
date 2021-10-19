package com.office_nico.spractice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.constants.codes.ActionTypeCode;
import com.office_nico.spractice.domain.ClientScenario;
import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.domain.Guidance;
import com.office_nico.spractice.domain.GuidanceAction;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.domain.Stock;
import com.office_nico.spractice.repository.client_scenario.ClientScenarioRepository;
import com.office_nico.spractice.repository.completion_point.CompletionPointRepository;
import com.office_nico.spractice.repository.guidance.GuidanceRepository;
import com.office_nico.spractice.repository.guidance_action.GuidanceActionRepository;
import com.office_nico.spractice.repository.scenario.ScenarioRepository;
import com.office_nico.spractice.repository.stock.StockRepository;
import com.office_nico.spractice.util.BeanUtil;

@Service
@Transactional
public class ScenarioService {

	private static final Logger _logger = LoggerFactory.getLogger(ScenarioService.class);

	@Autowired private ScenarioRepository scenarioRepository = null;

	@Autowired private ClientScenarioRepository clientScenarioRepository = null;

	@Autowired private GuidanceRepository guidanceRepository = null;

	@Autowired private GuidanceActionRepository guidanceActionRepository = null;

	@Autowired private ActionTypeCode actionTypeCode = null;

	@Autowired private StockRepository stockRepository = null;

	@Autowired private CompletionPointRepository completionPointRepository = null;

	/**
	 * ページリストの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param search 検索キーワード
	 * @param current 現在のページ
	 * @param pageMax 1ページ当たりの表示数
	 * @return ページオブジェクト
	 */
	public Page<Scenario> page(Logger logger, Long sessionUserId, String search, int start, int length, Sort.Direction dir, String[] orders){
		logger = (logger == null ? logger : _logger);
		
		/*
		int pageNumber = start == 0 ? 0 : (start / length);
		
		Pageable pr = PageRequest.of(pageNumber, length, dir, order);
		Page<Scenario> page = scenarioRepository.findByIsDeletedFalse(pr);
		*/
		
		Page<Scenario> page = scenarioRepository.findScenariosBySearchKeywordAndIsDeletedFalse(search, orders, dir.toString(), start, length);
		
		return page;
	}

	/**
	 * シナリオの一覧を取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 */
	public List<Scenario> list(Logger logger, Long sessionUserId){
		logger = (logger == null ? logger : _logger);
		
		return scenarioRepository.findByIsDeletedFalseOrderByScenarioKeycode();
	}
	
	
	/**
	 * レコードの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @return エンティティ
	 */
	public Scenario get(Logger logger, Long sessionUserId, Long scenarioId) {
		logger = (logger == null ? logger : _logger);
		
		Optional<Scenario> entity = scenarioRepository.findById(scenarioId);

		return entity.get();
	}

	/**
	 * シナリオの登録
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenario エンティティ
	 * @param 登録されたエンティティ
	 */
	@Transactional
	public Scenario create(Logger logger, Long sessionUserId, Scenario scenario) {
		logger = (logger == null ? logger : _logger);

		scenario.setIsDeleted(false);
		scenario.setUpdatedBy(sessionUserId);
		scenario.setCreatedBy(sessionUserId);
		scenario.setUpdatedAt(LocalDateTime.now());
		scenario.setCreatedAt(LocalDateTime.now());
		
		scenarioRepository.save(scenario);
		
		return scenario;
	}

	/**
	 * シナリオの更新
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @param scenario エンティティ
	 * @return 更新されたエンティティ
	 */
	public Scenario update(Logger logger, Long sessionUserId, Long scenarioId, Scenario scenario) {
		logger = (logger == null ? logger : _logger);

		Optional<Scenario> entity = scenarioRepository.findById(scenarioId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setScenarioKeycode(scenario.getScenarioKeycode());
			entity.get().setScenarioName(scenario.getScenarioName());
			entity.get().setDescription(scenario.getDescription());
			entity.get().setNote(scenario.getNote());
			entity.get().setThumbnailBinaryFileId(scenario.getThumbnailBinaryFileId());
			entity.get().setIsInvalided(scenario.getIsInvalided());
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			scenarioRepository.save(entity.get());
		}

		return entity.get();
	}

	/**
	 * シナリオの削除
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオクライアントID
	 */
	public void delete(Logger logger, Long sessionUserId, Long scenarioId) {
		logger = (logger == null ? logger : _logger);

		Optional<Scenario> entity = scenarioRepository.findById(scenarioId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setIsDeleted(true);
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			scenarioRepository.save(entity.get());
		}
	}
	

	/**
	 * クライアントコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオコード 
	 * @return true：重複、false：重複なし
	 */
	public Boolean isDuplicate(Logger logger, Long sessionUserId, String scenarioKeycode) {
		logger = (logger == null ? logger : _logger);
		
		Boolean ret = false;
		if(scenarioRepository.countByIsDeletedFalseAndScenarioKeycode(scenarioKeycode) > 0L) {
			ret = true;
		}
		return ret;
	}

	/**
	 * クライアントコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオコード 
	 * @param exclusionClienId 除外ID
	 * @return true：重複、false：重複なし
	 */
	public Boolean isDuplicate(Logger logger, Long sessionUserId, String scenarioId, Long exclusionClienId) {
		logger = (logger == null ? logger : _logger);
		
		Boolean ret = false;
		if(scenarioRepository.countByIsDeletedFalseAndScenarioKeycodeAndIdNot(scenarioId, exclusionClienId) > 0L) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * ガイダンスの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param senarioId シナリオID
	 * @return 　履修ポイントリスト
	 */
	public List<Guidance> getGuidances(Logger logger, Long sessionUserId, Long scenarioId) {
		logger = (logger == null ? logger : _logger);

		List<Guidance> ret = new ArrayList<>();
		
		List<Map<String, Object>> records = guidanceRepository.findByScenarioIdOrderBySortOrder2(scenarioId);
		for(Map<String, Object> record : records) {
			Guidance guidance = BeanUtil.mapToBean(record, new Guidance());

			List<GuidanceAction> guidanceActions = guidanceActionRepository.findByGuidanceIdOrderBySortOrder(guidance.getId());
			// SortOrderの途中が抜けている場合は追加
			if(guidanceActions.size() > 0 && guidanceActions.get(0).getSortOrder() != 1){
				 int count = 0;
				for(int i=1; i<guidanceActions.get(0).getSortOrder(); i++) {
					GuidanceAction guidanceAction = new GuidanceAction();
					guidanceAction.setSortOrder(i);
					guidanceActions.add(count, guidanceAction);
					count++;
				}
			}
			for(int i = 0; i < guidanceActions.size() - 1; i++) {
				if(guidanceActions.get(i).getSortOrder() + 1 != guidanceActions.get(i + 1).getSortOrder()) {
					 int count = 1;
					for(int j=guidanceActions.get(i).getSortOrder() + 1; j <= guidanceActions.get(i + 1).getSortOrder() - 1; j++) {
						GuidanceAction guidanceAction = new GuidanceAction();
						guidanceAction.setSortOrder(j);
						guidanceActions.add(i + count, guidanceAction);
						count++;
					}
				}
			}
			guidance.setGuidanceActions(guidanceActions);
			ret.add(guidance);
		}
		return ret;
	}

	/**
	 * ガイダンスコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @param guidanceCode ガイダンスコード
	 * @param exclusionGuidanceId 除外ガイダンスID
	 * @return true:重複、false：重複なし
	 */
	public Boolean isDuplicateGuidanceKeycode(Logger logger, Long sessionUserId, Long scenarioId, String guidanceCode, Long exclusionGuidanceId) {
		logger = (logger == null ? logger : _logger);
		
		Boolean ret = false;
		
		if(guidanceRepository.countByGuidanceKeycodeAndIdNot(guidanceCode, exclusionGuidanceId) > 0L) {
			ret = true;
		}
		return ret;
	}

	
	/**
	 * ガイダンスコードの重複チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @param guidanceCode ガイダンスコード
	 * @return true:重複、false：重複なし
	 */
	public Boolean isDuplicateGuidanceKeycode(Logger logger, Long sessionUserId, Long scenarioId, String guidanceCode) {
		logger = (logger == null ? logger : _logger);
		
		Boolean ret = false;
		
		if(guidanceRepository.countByGuidanceKeycode(guidanceCode) > 0L) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * ガイダンスの保存
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param guidances ガイダンス
	 * @param startGuidanceId 開始ガイダンスID
	 */
	public void saveGuidance(Logger logger, Long sessionUserId, Long scenarioId, List<Guidance> guidances, Long startGuidanceId) {
		logger = (logger == null ? logger : _logger);

		Optional<Scenario> scenario = scenarioRepository.findById(scenarioId);
		if(scenario.isEmpty()) {
			throw new NullPointerException("No scenario data !! " + scenarioId);
		}

		// 履修ポイントを登録して、履修ポイントIDを書き換え
		
		for(Guidance guidance : guidances) {
			if(guidance.getStartCompletionPointType() != null && guidance.getStartCompletionPointType() == 1 && guidance.getStartCompletionPointKeycode() != null && guidance.getStartCompletionPointKeycode().length() > 0) {

				Long beforeId = guidance.getStartCompletionPointId();
				CompletionPoint completionPoint = new CompletionPoint();
				completionPoint.setCompletionPointKeycode(guidance.getStartCompletionPointKeycode());
				completionPoint.setDescription(guidance.getStartCompletionPointDescription());
				completionPoint.setIsInvalided(false);
				completionPoint.setIsDeleted(false);
				completionPoint.setUpdatedBy(sessionUserId);
				completionPoint.setCreatedBy(sessionUserId);
				completionPoint.setUpdatedAt(LocalDateTime.now());
				completionPoint.setCreatedAt(LocalDateTime.now());
				completionPointRepository.save(completionPoint);

				for(Guidance guidance2 : guidances) {
					if(guidance2.getStartCompletionPointId() != null && guidance2.getStartCompletionPointId().equals(beforeId)) {
						guidance2.setStartCompletionPointId(completionPoint.getId());
					}
					if(guidance2.getEndCompletionPointId() != null && guidance2.getEndCompletionPointId().equals(beforeId)) {
						guidance2.setEndCompletionPointId(completionPoint.getId());
					}
				}
			}

			if(guidance.getEndCompletionPointType() != null && guidance.getEndCompletionPointType() == 1 && guidance.getEndCompletionPointKeycode() != null && guidance.getEndCompletionPointKeycode().length() > 0) {

				Long beforeId = guidance.getEndCompletionPointId();
				CompletionPoint completionPoint = new CompletionPoint();
				completionPoint.setCompletionPointKeycode(guidance.getEndCompletionPointKeycode());
				completionPoint.setDescription(guidance.getEndCompletionPointDescription());
				completionPoint.setUpdatedBy(sessionUserId);
				completionPoint.setCreatedBy(sessionUserId);
				completionPoint.setIsInvalided(false);
				completionPoint.setIsDeleted(false);
				completionPoint.setUpdatedAt(LocalDateTime.now());
				completionPoint.setCreatedAt(LocalDateTime.now());
				completionPointRepository.save(completionPoint);

				for(Guidance guidance2 : guidances) {
					if(guidance2.getStartCompletionPointId() != null && guidance2.getStartCompletionPointId().equals(beforeId)) {
						guidance2.setStartCompletionPointId(completionPoint.getId());
					}
					if(guidance2.getEndCompletionPointId() != null && guidance2.getEndCompletionPointId().equals(beforeId)) {
						guidance2.setEndCompletionPointId(completionPoint.getId());
					}
				}
			}
		}

		List<Guidance> entities = new ArrayList<>();
		
		for(Guidance guidance : guidances) {
			
			if(guidance.getId() == null) {
				// 新規登録
				if(!guidance.getIsDeleted()) {
					guidance.setScenarioId(scenarioId);
					guidance.setUpdatedBy(sessionUserId);
					guidance.setCreatedBy(sessionUserId);
					guidance.setUpdatedAt(LocalDateTime.now());
					guidance.setCreatedAt(LocalDateTime.now());
					guidanceRepository.save(guidance);
					
					entities.add(guidance);
				}
			}
			else {
				Optional<Guidance> entity = guidanceRepository.findById(guidance.getId());
				if(entity.isEmpty()) {
					// 新規登録
					if(!guidance.getIsDeleted()) {
						guidance.setId(null);
						guidance.setScenarioId(scenarioId);
						guidance.setUpdatedBy(sessionUserId);
						guidance.setCreatedBy(sessionUserId);
						guidance.setUpdatedAt(LocalDateTime.now());
						guidance.setCreatedAt(LocalDateTime.now());
						guidanceRepository.save(guidance);

						entities.add(guidance);
					}
				}
				else {
					// 削除または更新
					if(guidance.getIsDeleted()) {
						guidanceRepository.delete(entity.get());
					}
					else {
						
						BeanUtil.copyFields(guidance, entity.get(), "id", "createdAt", "updatedAt", "createdBy", "updatedBy", "isDeleted");
						entity.get().setScenarioId(scenarioId);
						entity.get().setUpdatedBy(sessionUserId);
						entity.get().setUpdatedAt(LocalDateTime.now());
						guidanceRepository.save(entity.get());

						guidance.setId(entity.get().getId());
						entities.add(entity.get());
					}
				}
			}
		}
		
		// ガイダンスアクションの登録
		for(Guidance guidance : guidances) {
			if(!guidance.getIsDeleted()) {
				
				for(GuidanceAction guidanceAction : guidance.getGuidanceActions()) {
					if(guidanceAction.getSortOrder() != null) {
						
						GuidanceAction record = guidanceActionRepository.findTopByGuidanceIdAndSortOrder(guidance.getId(), guidanceAction.getSortOrder());
						if(record != null) {
							// レコードがあってアクションタイプコードがなければ削除
							// レコードがあってアクションタイプコードがあれば更新
							// ※ アクションタイプコードが、guidanceの場合はガイダンスIDの解決
							if(guidanceAction.getActionTypeCode() == null) {
								guidanceActionRepository.delete(record);
							}
							else {
								BeanUtil.copyFields(guidanceAction, record, "id", "createdAt", "updatedAt", "createdBy", "updatedBy");
								record.setGuidanceId(guidance.getId());
								record.setUpdatedBy(sessionUserId);
								record.setUpdatedAt(LocalDateTime.now());
								resolveGuidanceId(record, entities);
								guidanceActionRepository.save(record);
							}
						}
						else {
							// レコードがなくてアクションタイプコードがなければなにもしない
							// レコードがなくてアクションタイプコードがあれば追加
							// ※ アクションタイプコードが、guidanceの場合はガイダンスIDの解決
							if(guidanceAction.getActionTypeCode() != null) {
								guidanceAction.setGuidanceId(guidance.getId());
								guidanceAction.setUpdatedBy(sessionUserId);
								guidanceAction.setCreatedBy(sessionUserId);
								guidanceAction.setUpdatedAt(LocalDateTime.now());
								guidanceAction.setCreatedAt(LocalDateTime.now());
								resolveGuidanceId(guidanceAction, entities);
								guidanceActionRepository.save(guidanceAction);
							}
						}
					}
				}
			}
			else {
				List<GuidanceAction> records = guidanceActionRepository.findByGuidanceId(guidance.getId());
				for(GuidanceAction guidanceAction : records) {
					guidanceActionRepository.delete(guidanceAction);
					
				}
			}
		}

		
		// 開始ガイダンス番号に変更があった場合は更新
		if(startGuidanceId != null) {
			for(Guidance guidance : entities) {
				if(guidance.getWorkId() != null && guidance.getWorkId().equals(startGuidanceId)) {
					startGuidanceId = guidance.getId();
				}
			}
		}
		if((scenario.get().getStartGuidanceId() == null && startGuidanceId != null) ||
				(scenario.get().getStartGuidanceId() != null && startGuidanceId == null) ||
				(scenario.get().getStartGuidanceId() != null && startGuidanceId != null && !scenario.get().getStartGuidanceId().equals(startGuidanceId))){

			scenario.get().setStartGuidanceId(startGuidanceId);
			scenario.get().setUpdatedBy(sessionUserId);
			scenario.get().setUpdatedAt(LocalDateTime.now());
			scenarioRepository.save(scenario.get());
		}
	}
	
	// アクションタイプがguidanceの場合、遷移先ガイダンス番号の解決をする
	private void resolveGuidanceId(GuidanceAction guidanceAction, List<Guidance> guidances) {
		
		if(guidanceAction != null &&  guidanceAction.getActionTypeCode() != null && guidanceAction.getActionTypeCode() == actionTypeCode.guidance) {
			for(Guidance guidance : guidances) {
				
				if(guidance.getWorkId() != null && guidance.getWorkId().equals(guidanceAction.getNextGuidanceActionId())) {
					guidanceAction.setNextGuidanceActionId(guidance.getId());
				}
			}
		}
	}
	
	/**
	 * コンテンツの更新
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @param scenario エンティティ
	 * @return 更新されたエンティティ
	 */
	public Scenario updateContent(Logger logger, Long sessionUserId, Long scenarioId, Scenario scenario) {
		logger = (logger == null ? logger : _logger);

		Optional<Scenario> entity = scenarioRepository.findById(scenarioId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setContentBody(scenario.getContentBody());
			entity.get().setContentScript(scenario.getContentScript());
			entity.get().setContentCss(scenario.getContentCss());
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			scenarioRepository.save(entity.get());
		}
		return entity.get();
	}

	/**
	 * ストック素材の取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @return ストック素材リスト
	 */
	public List<Stock> getStocks(Logger logger, Long sessionUserId, Long scenarioId){
		logger = (logger == null ? logger : _logger);

		return stockRepository.findByScenarioIdOrderById(scenarioId);
	}

	/**
	 * ストック素材の保存
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @param binaryFileId バイナリファイルID
	 * @return ストック素材
	 */
	public Stock createStock(Logger logger, Long sessionUserId, Long scenarioId, Long binaryFileId){
		logger = (logger == null ? logger : _logger);

		Stock stock = new Stock();
		
		stock.setScenarioId(scenarioId);
		stock.setBinaryFileId(binaryFileId);
		stock.setUpdatedBy(sessionUserId);
		stock.setCreatedBy(sessionUserId);
		stock.setUpdatedAt(LocalDateTime.now());
		stock.setCreatedAt(LocalDateTime.now());

		stockRepository.save(stock);
		
		return stock;
	}

	/**
	 * ストック素材の保存
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId シナリオID
	 * @param binaryFileId バイナリファイルID
	 */
	public void deleteStock(Logger logger, Long sessionUserId, Long scenarioId, Long binaryFileId){
		logger = (logger == null ? logger : _logger);

		Stock stock = stockRepository.findTopByScenarioIdAndBinaryFileId(scenarioId, binaryFileId);
		if(stock != null) {
			stockRepository.delete(stock);
		}
	}

	/**
	 * クライアントIDからシナリオの一覧を取得する
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @return シナリオリスト
	 */
	public List<Scenario> getValidByClientId(Logger logger, Long sessionUserId, Long clientId){
		logger = (logger == null ? logger : _logger);
		
		List<Scenario> scenarios = scenarioRepository.findValidSenarioByClientId(clientId);
		
		return scenarios;
	}
	
	/**
	 * 有効なシナリオ情報の取得（ガイダンス込み）
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param scenarioCode シナリオコード
	 * @return シナリオ
	 */
	public Scenario getValidByScenarioCode(Logger logger, Long sessionUserId, Long clientId, String scenarioKeycode){
		logger = (logger == null ? logger : _logger);

		Scenario scenario = null;

		// シナリオの取得
		if(clientId != null) {
			// 本番用
			scenario = scenarioRepository.findValidSenarioByClientIdAndScenarioKeycode(clientId, scenarioKeycode);
		}
		else {
			scenario = scenarioRepository.findTopByScenarioKeycode(scenarioKeycode);
		}

		// ガイダンスの取得
		List<Guidance> guidances = guidanceRepository.findByScenarioIdOrderBySortOrder(scenario.getId());
		scenario.setGuidances(guidances);

		// 履修ポイントの解決
		for(Guidance guidance : guidances) {
			if(guidance.getStartCompletionPointId() != null) {
				Optional<CompletionPoint> completionPoint = completionPointRepository.findById(guidance.getStartCompletionPointId());
				if(!completionPoint.isEmpty()) {
					guidance.setStartCompletionPointKeycode(completionPoint.get().getCompletionPointKeycode());
				}
			}
			if(guidance.getEndCompletionPointId() != null) {
				Optional<CompletionPoint> completionPoint = completionPointRepository.findById(guidance.getEndCompletionPointId());
				if(!completionPoint.isEmpty()) {
					guidance.setEndCompletionPointKeycode(completionPoint.get().getCompletionPointKeycode());
				}
			}
		}

		// ガイダンスアクションの取得
		List<GuidanceAction> guidanceActions = guidanceActionRepository.findByScenarioId(scenario.getId());

		// 次の遷移先解決
		for(GuidanceAction guidanceAction : guidanceActions) {
			if(guidanceAction.getNextGuidanceActionId() != null) {
				for(Guidance guidance : guidances) {
					if(guidanceAction.getNextGuidanceActionId().equals(guidance.getId())) {
						guidanceAction.setNextGuidanceKeycode(guidance.getGuidanceKeycode());
					}
				}
			}
		}

		for(Guidance guidance : guidances) {
			for(GuidanceAction guidanceAction : guidanceActions) {
				if(guidanceAction.getGuidanceId().equals(guidance.getId())){
					guidance.getGuidanceActions().add(guidanceAction);
				}
			}
		}
		
		if(scenario.getStartGuidanceId() != null) {
			for(Guidance guidance : guidances) {
				if(guidance.getId().equals(scenario.getStartGuidanceId())) {
					scenario.setStartGuidanceKeycode(guidance.getGuidanceKeycode());
				}
			}
		}
		return scenario;
	}
	
	/**
	 * シナリオのコピー
	 * @param logger ロガー 
	 * @param sessionUserId セッションユーザーID
	 * @param sourceScenarioId コピー元シナリオID
	 * @param scenarioKeycode シナリオコード
	 * @param scenarioName シナリオ名
	 * @param copyCompletionPoint 履修ポイントコピーの有無
	 * @return コピー後シナリオ
	 */
	public Scenario copyScenario(Logger logger, Long sessionUserId, Long sourceScenarioId, String scenarioKeycode, String scenarioName, Boolean copyCompletionPoint) {
		logger = (logger == null ? logger : _logger);
		
		Scenario ret = null;
		
		if(scenarioRepository.existsById(sourceScenarioId)) {
			
			Optional<Scenario> sourceScenario = scenarioRepository.findById(sourceScenarioId);
			
			Scenario destScenario = BeanUtil.copyFields(sourceScenario.get(), new Scenario(), "id", "createdAt", "updatedAt", "createdBy", "updatedBy");
			destScenario.setScenarioKeycode(scenarioKeycode);
			destScenario.setScenarioName(scenarioName);

			if(destScenario.getContentScript() != null) {
				destScenario.setContentScript(destScenario.getContentScript().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode));
			}
			if(destScenario.getContentBody() != null) {
				destScenario.setContentBody(destScenario.getContentBody().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode));
			}

			destScenario.setIsDeleted(false);
			destScenario.setUpdatedBy(sessionUserId);
			destScenario.setCreatedBy(sessionUserId);
			destScenario.setUpdatedAt(LocalDateTime.now());
			destScenario.setCreatedAt(LocalDateTime.now());
			scenarioRepository.save(destScenario);
			ret = destScenario;

			Map<Long, Long> completionPointIdMap = new HashMap<>();
			Map<Long, Long> guidanceIdMap = new HashMap<>();
			List<GuidanceAction> destGuidanceActions = new ArrayList<>();
			List<Guidance> destGuidances = new ArrayList<>();

			List<Guidance> guidances = guidanceRepository.findByScenarioIdOrderBySortOrder(sourceScenarioId);
			for(Guidance guidance : guidances) {

				Guidance destGuidance = BeanUtil.copyFields(guidance, new Guidance(), "id", "createdAt", "updatedAt", "createdBy", "updatedBy");
				
				// ユニークなガイダンスコードを生成する
				String destGuidanceKeycode = guidance.getGuidanceKeycode().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode);
				if(guidanceRepository.countByGuidanceKeycode(destGuidanceKeycode) > 0) {
					destGuidanceKeycode = scenarioKeycode + "#" + destGuidanceKeycode;
					if(guidanceRepository.countByGuidanceKeycode(destGuidanceKeycode) > 0) {
						while(true) {
							destGuidanceKeycode += RandomStringUtils.randomNumeric(1);
							if(guidanceRepository.countByGuidanceKeycode(destGuidanceKeycode) == 0) {
								break;
							}
						}
					}
				}
				destGuidance.setGuidanceKeycode(destGuidanceKeycode);

				if(destGuidance.getPreScript() != null) {
					destGuidance.setPreScript(destGuidance.getPreScript().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode));
				}
				if(destGuidance.getPostScript() != null) {
					destGuidance.setPreScript(destGuidance.getPostScript().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode));
				}

				destGuidance.setScenarioId(destScenario.getId());
				destGuidance.setUpdatedBy(sessionUserId);
				destGuidance.setCreatedBy(sessionUserId);
				destGuidance.setUpdatedAt(LocalDateTime.now());
				destGuidance.setCreatedAt(LocalDateTime.now());
				guidanceRepository.save(destGuidance);

				destGuidances.add(destGuidance);
				
				// ガイダンスIDの変換前後を保持
				guidanceIdMap.put(guidance.getId(), destGuidance.getId());
				
				// 開始ガイダンスIDを書き換え
				if(sourceScenario.get().getStartGuidanceId() != null && sourceScenario.get().getStartGuidanceId().equals(guidance.getId())) {
					destScenario.setStartGuidanceId(destGuidance.getId());
				}
				

				List<GuidanceAction> guidanceActions = guidanceActionRepository.findByGuidanceId(guidance.getId());
				for(GuidanceAction guidanceAction : guidanceActions) {
					GuidanceAction destGuidanceAction = BeanUtil.copyFields(guidanceAction, new GuidanceAction(), "id", "createdAt", "updatedAt", "createdBy", "updatedBy");
					destGuidanceAction.setGuidanceId(destGuidance.getId());
					
					if(destGuidanceAction.getBody() != null) {
						destGuidanceAction.setBody(destGuidanceAction.getBody().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode));
					}
					destGuidanceAction.setUpdatedBy(sessionUserId);
					destGuidanceAction.setCreatedBy(sessionUserId);
					destGuidanceAction.setUpdatedAt(LocalDateTime.now());
					destGuidanceAction.setCreatedAt(LocalDateTime.now());
					guidanceActionRepository.save(destGuidanceAction);
					
					// ガイダンスIDを保持
					destGuidanceActions.add(destGuidanceAction);
				}
				
				// 履修ポイントのコピー
				if(copyCompletionPoint) {
					if(guidance.getStartCompletionPointId() != null) {
						completionPointIdMap.put(guidance.getStartCompletionPointId(), null);
					}
					if(guidance.getEndCompletionPointId() != null) {
						completionPointIdMap.put(guidance.getEndCompletionPointId(), null);
					}
				}
			}
			
			// NEXTガイダンスIDの書き換え
			for(GuidanceAction guidanceAction : destGuidanceActions) {
				if(guidanceAction.getNextGuidanceActionId() != null) {
					
					Long newNextGuidanceId = guidanceIdMap.get(guidanceAction.getNextGuidanceActionId());
					if(newNextGuidanceId != null) {
						guidanceAction.setNextGuidanceActionId(newNextGuidanceId);
					}
				}
			}
			
			if(copyCompletionPoint) {
				// 履修ポイントのコピー
				for(Map.Entry<Long, Long> entry : completionPointIdMap.entrySet()){
					if(completionPointRepository.existsById(entry.getKey())) {
						Optional<CompletionPoint> completionPoint = completionPointRepository.findById(entry.getKey());
						CompletionPoint destCompletionPoint = BeanUtil.copyFields(completionPoint.get(), new CompletionPoint(), "id", "createdAt", "updatedAt", "createdBy", "updatedBy");

						// ユニークな履修ポイントを生成する
						String destCompletionPointKeycode = destCompletionPoint.getCompletionPointKeycode().replaceAll(sourceScenario.get().getScenarioKeycode(), scenarioKeycode);
						if(completionPointRepository.countByIsDeletedFalseAndCompletionPointKeycode(destCompletionPointKeycode) > 0) {
							destCompletionPointKeycode = scenarioKeycode + "$" + destCompletionPointKeycode;
							if(completionPointRepository.countByIsDeletedFalseAndCompletionPointKeycode(destCompletionPointKeycode) > 0) {
								while(true) {
									destCompletionPointKeycode += RandomStringUtils.randomNumeric(1);
									if(completionPointRepository.countByIsDeletedFalseAndCompletionPointKeycode(destCompletionPointKeycode) == 0) {
										break;
									}
								}
							}
						}
						destCompletionPoint.setCompletionPointKeycode(destCompletionPointKeycode);
						destCompletionPoint.setIsDeleted(false);
						destCompletionPoint.setUpdatedBy(sessionUserId);
						destCompletionPoint.setCreatedBy(sessionUserId);
						destCompletionPoint.setUpdatedAt(LocalDateTime.now());
						destCompletionPoint.setCreatedAt(LocalDateTime.now());
						completionPointRepository.save(destCompletionPoint);
						entry.setValue(destCompletionPoint.getId());
					}
				}
				// 履修ポイントの書き換え
				for(Guidance destGuidance : destGuidances) {
					if(destGuidance.getStartCompletionPointId() != null) {
						destGuidance.setStartCompletionPointId(completionPointIdMap.get(destGuidance.getStartCompletionPointId()));
					}
					if(destGuidance.getEndCompletionPointId() != null) {
						destGuidance.setEndCompletionPointId(completionPointIdMap.get(destGuidance.getEndCompletionPointId()));
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * 利用しているクライアントの一覧を返す
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param scenarioId        シナリオID
	 * @return クライアントシナリオリスト
	 */
	public List<ClientScenario> getUsedClients(Logger logger, Long sessionUserId, Long scenarioId) {
		logger = (logger == null ? logger : _logger);

		return clientScenarioRepository.findClientByScenarioId(scenarioId);
	}
}
