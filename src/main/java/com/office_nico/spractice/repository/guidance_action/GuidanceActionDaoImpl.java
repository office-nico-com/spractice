package com.office_nico.spractice.repository.guidance_action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.GuidanceAction;

@Repository
public class GuidanceActionDaoImpl implements GuidanceActionDao<GuidanceAction> {

	private static final long serialVersionUID = -1244163499164773825L;

	@PersistenceContext
	private EntityManager entityManager = null;


	@Override
	public List<GuidanceAction> findByScenarioId(Long scenarioId) {

		List<GuidanceAction> ret = new ArrayList<>();

		@SuppressWarnings("unchecked")
		List<Object[]> records = entityManager.createNativeQuery(
				"SELECT "
				+ "guidance_actions.id, "
				+ "guidance_actions.guidance_id, "
				+ "guidance_actions.label, "
				+ "guidance_actions.action_type_code, "
				+ "guidance_actions.next_guidance_action_id, "
				+ "guidance_actions.title, "
				+ "guidance_actions.body, "
				+ "guidance_actions.open_window_flag, "
				+ "guidance_actions.sort_order "
				+ "FROM guidance_actions "
				+ "INNER JOIN guidances ON (guidances.id = guidance_actions.guidance_id) "
				+ "INNER JOIN scenarios ON (scenarios.id = guidances.scenario_id) "
				+ "WHERE scenarios.id = :arg1 "
				+ "ORDER BY guidance_actions.sort_order")
				.setParameter("arg1", scenarioId).getResultList();;
		
		for(Object[] record : records) {
			GuidanceAction guidanceAction = new GuidanceAction();
			guidanceAction.setId(Long.valueOf(String.valueOf((BigInteger)record[0])));
			guidanceAction.setGuidanceId(Long.valueOf(String.valueOf((BigInteger)record[1])));
			guidanceAction.setLabel((String)record[2]);
			guidanceAction.setActionTypeCode((Short)record[3]);
			if(record[4] != null) {
				guidanceAction.setNextGuidanceActionId(Long.valueOf(String.valueOf((BigInteger)record[4])));
			}
			guidanceAction.setTitle((String)record[5]);
			guidanceAction.setBody((String)record[6]);
			guidanceAction.setOpenWindowFlag((Boolean)record[7]);
			guidanceAction.setSortOrder((Integer)record[8]);
			ret.add(guidanceAction);
		}
		return ret;
	}
}
