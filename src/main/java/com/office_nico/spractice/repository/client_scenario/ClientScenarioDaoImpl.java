package com.office_nico.spractice.repository.client_scenario;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.ClientScenario;
import com.office_nico.spractice.domain.Scenario;

@Repository
public class ClientScenarioDaoImpl implements ClientScenarioDao<ClientScenario> {

	private static final long serialVersionUID = -2475028364799632325L;

	@PersistenceContext
	private EntityManager entityManager = null;

	@Override
	public List<ClientScenario> findSenarioByClientId(Long clientId) {

		// List<Scenario> records = entityManager.createQuery("select s from ClientScenario cs join cs.scenario s where cs.client.id = :arg1 order by cs.sortOrder", Scenario.class).setParameter("arg1", clientId).getResultList();

		List<ClientScenario> ret = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<Object[]> records = entityManager.createNativeQuery(
				"SELECT "
				
				+ "scenarios.id, "
				+ "scenarios.scenario_keycode, "
				+ "scenarios.scenario_name, "
				+ "scenarios.thumbnail_binary_file_id, "
				+ "scenarios.description, "
				+ "scenarios.note, "
				+ "scenarios.is_invalided, "
				+ "scenarios.is_deleted, "
				+ "clients_scenarios.is_invalided AS client_scenario_is_invalided, "
				+ "clients_scenarios.sort_order AS sort_order FROM clients_scenarios "

				+ "INNER JOIN scenarios ON (scenarios.id = clients_scenarios.scenario_id) "
				+ "WHERE clients_scenarios.client_id = :arg1 "
				+ "ORDER BY clients_scenarios.sort_order")
				.setParameter("arg1", clientId).getResultList();;
		
		for(Object[] record : records) {
			
			Scenario scenario = new Scenario();

			scenario.setId(Long.valueOf(String.valueOf((BigInteger)record[0])));
			scenario.setScenarioKeycode((String)record[1]);
			scenario.setScenarioName((String)record[2]);
			scenario.setThumbnailBinaryFileId(Long.valueOf(String.valueOf((BigInteger)record[3])));
			scenario.setDescription((String)record[4]);
			scenario.setNote((String)record[5]);
			scenario.setIsInvalided((Boolean)record[6]);
			scenario.setIsDeleted((Boolean)record[7]);
			ClientScenario clientScenario = new ClientScenario();
			clientScenario.setIsInvalided((Boolean)record[8]);
			clientScenario.setSortOrder((Integer)record[9]);
			clientScenario.setScenario(scenario);
			ret.add(clientScenario);
		}
		
		return ret;
	}



}
