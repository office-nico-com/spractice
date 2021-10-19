package com.office_nico.spractice.repository.scenario;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Scenario;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.NativeQueryBuilder;

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
	
	@Override
	public Page<Scenario> findScenariosBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit){
		
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);

		query.append("SELECT DISTINCT");
		query.appendField("scenarios.id");
		query.appendField("scenarios.scenario_keycode");
		query.appendField("scenarios.scenario_name");
		query.appendField("scenarios.thumbnail_binary_file_id");
		query.appendField("scenarios.description");
		query.appendField("scenarios.start_guidance_id");
		query.appendField("scenarios.note");
		query.appendField("scenarios.is_invalided");
		query.appendField("scenarios.is_deleted");
		query.append("FROM scenarios");
		query.append("LEFT OUTER JOIN clients_scenarios ON (clients_scenarios.scenario_id = scenarios.id)");
		query.append("LEFT OUTER JOIN clients ON (clients.id = clients_scenarios.client_id AND clients.is_deleted = false)");
		query.append("WHERE scenarios.is_deleted = false ");
		if(searchKeyword != null && searchKeyword.length() > 0) {
			searchKeyword = "%" + searchKeyword + "%";
			query.append("AND (");
			query.append("scenarios.scenario_keycode LIKE ?", searchKeyword);
			query.append("OR scenarios.scenario_name LIKE ?", searchKeyword);
			query.append("OR scenarios.note LIKE ?", searchKeyword);
			query.append("OR clients.client_keycode LIKE ?", searchKeyword);
			query.append("OR clients.client_name_ja LIKE ?", searchKeyword);
			query.append("OR clients.client_name_ja_kana LIKE ?", searchKeyword);
			query.append(")");
		}

		for(String order : orders) {
			query.addOrder(order, direction);
		}
		query.offset(offset);
		query.limit(limit);

		List<Map<String, Object>> results = query.getResults();
		Integer count = query.getCount();
		
		
		List<Scenario> scenarios = new ArrayList<>();
		for(Map<String, Object> result : results) {
			Scenario scenario = BeanUtil.mapToBean(result, new Scenario());
			scenarios.add(scenario);
		}
		
		int pageNumber = offset == 0 ? 0 : (offset / limit);
		Pageable pr = PageRequest.of(pageNumber, limit);

		Page<Scenario> p = new PageImpl<Scenario>(scenarios, pr, count);
		return p;
	}
	
	
	@Override
	public List<Scenario> findScenarioByCompletionPointId(Long completionPointId) {
		
		
		List<Scenario> results = new ArrayList<>();
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);
		
		
		query.append("SELECT DISTINCT");
		query.appendField("scenarios.id");
		query.appendField("scenarios.scenario_keycode");
		query.appendField("scenarios.scenario_name");
		query.appendField("scenarios.thumbnail_binary_file_id");
		query.appendField("scenarios.description");
		query.appendField("scenarios.start_guidance_id");
		query.appendField("scenarios.note");
		query.appendField("scenarios.is_invalided");
		query.appendField("scenarios.is_deleted");
		query.append("FROM completion_points");
		query.append("INNER JOIN guidances ON (guidances.start_completion_point_id = completion_points.id OR guidances.end_completion_point_id = completion_points.id)");
		query.append("INNER JOIN scenarios ON (scenarios.id = guidances.scenario_id AND scenarios.is_deleted = false)");
		query.append("WHERE completion_points.id = ?", completionPointId);

		query.addOrder("scenarios.scenario_keycode", "ASC");

		List<Map<String, Object>> records = query.getResults();
		for(Map<String, Object> record : records) {

			Scenario scenario = BeanUtil.mapToBean(record, new Scenario());
			results.add(scenario);
		}

		return results;

	}
	
	
}
