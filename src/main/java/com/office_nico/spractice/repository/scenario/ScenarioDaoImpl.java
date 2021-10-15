package com.office_nico.spractice.repository.scenario;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.util.BeanUtil;

@Repository
public class ScenarioDaoImpl implements ScenarioDao<Scenario> {

	private static final long serialVersionUID = 8952628824116520223L;

	@PersistenceContext
	private EntityManager entityManager = null;

	@Override
	public List<Scenario> findValidSenarioByClientId(Long clientId) {

		List<Scenario> ret = new ArrayList<>();

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
				+ "clients_scenarios.sort_order AS sort_order "
				+ "FROM clients_scenarios "
				+ "INNER JOIN scenarios ON (scenarios.id = clients_scenarios.scenario_id AND scenarios.is_invalided=false AND scenarios.is_deleted = false AND clients_scenarios.is_invalided = false) "
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
			ret.add(scenario);
		}
		
		return ret;

	}

	@Override
	public Scenario findValidSenarioByClientIdAndScenarioKeycode(Long clientId, String scenarioKeycode) {

		Scenario scenario = null;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> record =  entityManager.createQuery(
				"select new map("
				+ "s.id as id, "
				+ "s.scenarioKeycode as scenario_keycode, "
				+ "s.scenarioName as scenario_name, "
				+ "s.thumbnailBinaryFileId as thumbnail_binary_file_id, "
				+ "s.description as description, "
				+ "s.note as note, "
				+ "s.startGuidanceId as start_guidance_id, "
				+ "s.contentBody as content_body, "
				+ "s.contentScript as content_script, "
				+ "s.contentCss as content_css, "
				+ "s.isInvalided as is_invalided, "
				+ "s.isDeleted as is_deleted "
				+ ") from ClientScenario cs join cs.scenario s where cs.client.id = :arg1 "
				+ "AND s.scenarioKeycode = :arg2 "
				+ "AND s.isDeleted = false "
				+ "AND s.isInvalided = false "
				+ "AND cs.isInvalided = false ", Map.class)
		.setParameter("arg1", clientId)
		.setParameter("arg2", scenarioKeycode)
		.getSingleResult();

		if(record != null) {
			scenario = BeanUtil.mapToBean(record, new Scenario());
		}
		
		return scenario;
	}
}
