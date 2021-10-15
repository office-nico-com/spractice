package com.office_nico.spractice.repository.scenario;

import java.io.Serializable;
import java.util.List;

import com.office_nico.spractice.domain.Scenario;

public interface ScenarioDao <T> extends Serializable {

	List<Scenario> findValidSenarioByClientId(Long clientId);
	
	Scenario findValidSenarioByClientIdAndScenarioKeycode(Long clientId, String scenarioKeycode);

}

