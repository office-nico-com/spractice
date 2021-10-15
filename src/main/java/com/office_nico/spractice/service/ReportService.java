package com.office_nico.spractice.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.domain.Guidance;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.repository.client.ClientRepository;
import com.office_nico.spractice.repository.completion.CompletionRepository;
import com.office_nico.spractice.repository.completion_point.CompletionPointRepository;
import com.office_nico.spractice.repository.guidance.GuidanceRepository;
import com.office_nico.spractice.repository.scenario.ScenarioRepository;
import com.office_nico.spractice.service.data.AggregateData;
import com.office_nico.spractice.service.data.CompletionHistory;
import com.office_nico.spractice.service.data.CompletionUser;
import com.office_nico.spractice.service.data.ReportData;

@Service
@Transactional
public class ReportService {

	private static final Logger _logger = LoggerFactory.getLogger(ScenarioService.class);

	@Autowired
	private ClientRepository clientRepository = null;
	
	@Autowired
	private GuidanceRepository guidanceRepository = null; 
	
	@Autowired
	private ScenarioRepository scenarioRepository = null;
	
	@Autowired
	private CompletionPointRepository completionPointRepository = null;

	@Autowired
	private CompletionRepository completionRepository = null;

	/**
	 * 総受講者数の取得、削除ユーザーは除く
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @return 総受講者数
	 */
	public Integer getTotalEntryCount(Logger logger, Long sessionUserId, Long clientId) {
		logger = (logger == null ? logger : _logger);
		
		Integer ret = clientRepository.countUsersByClientId(clientId);
		
		return ret;
	}
	
	
	/**
	 * 集計データの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param totalCount 総受講数
	 * @return 集計データ
	 */
	public List<ReportData> getReports(Logger logger, Long sessionUserId, Long clientId, Integer totalCount){
		logger = (logger == null ? logger : _logger);
		
		List<ReportData> reports = new ArrayList<>();
		
		// シナリオの一覧⇒修了ポイントの一覧を取得する
		List<Long> completionPointIds = new ArrayList<>();
		List<Scenario> scenarios = scenarioRepository.findValidSenarioByClientId(clientId);
		for(Scenario scenario : scenarios) {
			List<Guidance> guidances = guidanceRepository.findByScenarioIdAndStartCompletionPointIdIsNotNullAndEndCompletionPointIdIsNotNullOrderBySortOrder(scenario.getId());
			for(Guidance guidance : guidances) {
				if(guidance.getStartCompletionPointId() != null && !completionPointIds.contains(guidance.getStartCompletionPointId())) {
					completionPointIds.add(guidance.getStartCompletionPointId());
				}
				if(guidance.getEndCompletionPointId() != null && !completionPointIds.contains(guidance.getEndCompletionPointId())) {
					completionPointIds.add(guidance.getEndCompletionPointId());
				}
			}
		}
		List<CompletionPoint> completionPoints = completionPointRepository.findByIsDeletedFalseAndIsInvalidedFalseAndId(completionPointIds);
		for(CompletionPoint completionPoint : completionPoints) {
			ReportData report = new ReportData();
			report.setCompletionPoint(completionPoint);
			reports.add(report);
		}

		// 訓練時間を求める
		List<AggregateData> aggregates = completionRepository.calcWorkTimesByCompletionPointIds(completionPointIds, clientId);

		for(ReportData report : reports) {
			for(AggregateData aggregateData : aggregates) {
				if(report.getCompletionPoint().getId().equals(aggregateData.getId())) {
						int min = aggregateData.getMin() / 60;
						if(aggregateData.getMin() % 60 != 0) {
							min += 1;
						}
						report.setMinMin(min);

						int max = aggregateData.getMax() / 60;
						if(aggregateData.getMax() % 60 != 0) {
							max += 1;
						}
						report.setMaxMin(max);

						int avg = aggregateData.getAvg() / 60;
						if(aggregateData.getAvg() % 60 != 0) {
							avg += 1;
						}
						report.setAvgMin(avg);
				}
			}
		}
		
		// 修了数を求める
		aggregates = completionRepository.findCompletionCountsByCompletionPointIds(completionPointIds, clientId);
		for(ReportData report : reports) {
			for(AggregateData aggregateData : aggregates) {
				if(report.getCompletionPoint().getId().equals(aggregateData.getId())) {
					report.setCompletionCount(aggregateData.getCount());
				}
			}
		}
		
		// 未着手数を求める
		for(ReportData report : reports) {
			Integer unworkedCount = completionRepository.countUnworkedUserCountByCompletionPointIdAndClientId(report.getCompletionPoint().getId(), clientId);
			report.setUnworkedCount(unworkedCount);
		}

		// 訓練中数を求める（総受講数 - 修了数 - 未着手数）
		for(ReportData report : reports) {
			report.setWorikingCount(totalCount - report.getCompletionCount() - report.getUnworkedCount());
		}
		
		return reports;
	}
	
	
	/**
	 * 修了ユーザーを取得する
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param completionPointId 修了ポイントID
	 * @param clientId クライアントID
	 * @param fd 検索開始日
	 * @param td 検索終了日
	 * @return 修了履歴情報
	 */
	public CompletionHistory getCompletionHistories(Logger logger, Long sessionUserId, Long completionPointId, Long clientId, LocalDate fd, LocalDate td) {
		
		CompletionHistory exportData = new CompletionHistory();
		
		Optional<CompletionPoint> completionPoint = completionPointRepository.findById(completionPointId);
		if(!completionPoint.isEmpty()) {
			
			exportData.setCompletionPoint(completionPoint.get());
			List<CompletionUser> completionUsers = completionRepository.findByCompletionPointIdAndClientIdAndStartedAtNotNullAndEndedAtNotNullAndEndedAtFromAndEndedAtTo(completionPointId, clientId, fd, td);
			exportData.setCompletionUsers(completionUsers);
		}
		return exportData;
	}
	
}



