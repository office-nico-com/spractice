package com.office_nico.spractice.repository.scenario;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.office_nico.spractice.domain.Scenario;

public interface ScenarioDao <T> extends Serializable {

	List<Scenario> findValidSenarioByClientId(Long clientId);

	Scenario findValidSenarioByClientIdAndScenarioKeycode(Long clientId, String scenarioKeycode);

	Page<Scenario> findScenariosBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit);

	List<Scenario> findScenarioByCompletionPointId(Long completionPointId);

	List<Scenario> findSenarioByClientId(Long clientId);

}

