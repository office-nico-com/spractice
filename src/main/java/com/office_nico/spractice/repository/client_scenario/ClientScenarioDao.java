package com.office_nico.spractice.repository.client_scenario;

import java.io.Serializable;
import java.util.List;

import com.office_nico.spractice.domain.ClientScenario;

public interface ClientScenarioDao <T> extends Serializable {

	List<ClientScenario> findSenarioByClientId(Long clientId);
}

