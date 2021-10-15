package com.office_nico.spractice.repository.guidance_action;

import java.io.Serializable;
import java.util.List;

import com.office_nico.spractice.domain.GuidanceAction;

public interface GuidanceActionDao <T> extends Serializable {

	List<GuidanceAction> findByScenarioId(Long scenarioId);

}

