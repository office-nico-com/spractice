package com.office_nico.spractice.repository.client_scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientScenario;
import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.NativeQueryBuilder;

@Repository
public class ClientScenarioDaoImpl implements ClientScenarioDao<ClientScenario> {

	private static final long serialVersionUID = -2475028364799632325L;

	@PersistenceContext
	private EntityManager entityManager = null;

	
	

	@Override
	public List<ClientScenario> findSenarioByClientId(Long clientId) {

		// List<Scenario> records = entityManager.createQuery("select s from ClientScenario cs join cs.scenario s where cs.client.id = :arg1 order by cs.sortOrder", Scenario.class).setParameter("arg1", clientId).getResultList();

		
		List<ClientScenario> results = new ArrayList<>();
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);

		query.append("SELECT");
		query.appendField("scenarios.id", "id");
		query.appendField("scenarios.scenario_keycode", "scenario_keycode");
		query.appendField("scenarios.scenario_name", "scenario_name");
		query.appendField("scenarios.thumbnail_binary_file_id", "thumbnail_binary_file_id");
		query.appendField("scenarios.description", "description");
		query.appendField("scenarios.note", "note");
		query.appendField("scenarios.is_invalided", "is_invalided");
		query.appendField("scenarios.is_deleted", "is_deleted");
		query.appendField("clients_scenarios.is_invalided", "client_scenario_is_invalided");
		query.appendField("clients_scenarios.sort_order", "sort_order");
		query.append("FROM clients_scenarios");
		query.append("INNER JOIN scenarios ON (scenarios.id = clients_scenarios.scenario_id) ");
		query.append("WHERE clients_scenarios.client_id = ? ", clientId);

		query.addOrder("clients_scenarios.sort_order", "ASC");

		List<Map<String, Object>> records = query.getResults();
		for(Map<String, Object> record : records) {
			Scenario scenario = BeanUtil.mapToBean(record, new Scenario());
			
			ClientScenario clientScenario = new ClientScenario();
			clientScenario.setIsInvalided((Boolean)record.get("client_scenario_is_invalided"));
			clientScenario.setSortOrder((Integer)record.get("sort_order"));
			clientScenario.setScenario(scenario);
			results.add(clientScenario);
		}
		
		
		return results;
	}
	
	@Override
	public List<ClientScenario> findClientByScenarioId(Long scenarioId) {
		
		
		List<ClientScenario> results = new ArrayList<>();
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);
		
		
		query.append("SELECT");
		query.appendField("clients.id");
		query.appendField("clients.client_keycode");
		query.appendField("clients.client_name_ja");
		query.appendField("clients.client_name_ja_kana");
		query.appendField("clients.user_regist_type_code");
		query.appendField("clients.is_invalided");
		query.appendField("clients.is_deleted");
		query.append("FROM clients_scenarios");
		query.append("INNER JOIN clients ON (clients.id = clients_scenarios.client_id AND clients.is_deleted=false)");
		query.append("WHERE clients_scenarios.scenario_id = ?", scenarioId);

		query.addOrder("clients.client_keycode", "ASC");

		List<Map<String, Object>> records = query.getResults();
		for(Map<String, Object> record : records) {
			ClientScenario clientScenario = new ClientScenario();

			Client client = BeanUtil.mapToBean(record, new Client());
			clientScenario.setClient(client);
			results.add(clientScenario);
		}

		return results;

	}
}
