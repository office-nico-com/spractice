package com.office_nico.spractice.repository.guidance;


import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Guidance;


@Repository
@Transactional
public interface GuidanceRepository extends JpaRepository<Guidance,Long>, GuidanceDao<Guidance> {

	
	public List<Guidance> findByScenarioIdOrderBySortOrder(Long scenarioId);

	@Query(value="SELECT guidances.*, t1.completion_point_keycode AS start_completion_point_keycode, t2.completion_point_keycode AS end_completion_point_keycode FROM guidances LEFT OUTER JOIN completion_points AS t1 ON (t1.id = guidances.start_completion_point_id) LEFT OUTER JOIN completion_points  AS t2 ON (t2.id = guidances.end_completion_point_id)  WHERE guidances.scenario_id = ?1 ORDER BY guidances.sort_order", nativeQuery = true)
	public List<Map<String, Object>> findByScenarioIdOrderBySortOrder2(Long scenarioId);

	public Long countByGuidanceKeycode(String guidanceKeycode);

	public Long countByGuidanceKeycodeAndIdNot(String guidanceKeycode, Long guidanceId);

	
	@Query("SELECT g FROM Guidance g WHERE g.scenarioId = ?1 AND (g.startCompletionPointId IS NOT NULL OR g.endCompletionPointId IS NOT NULL)")
	public List<Guidance> findByScenarioIdAndStartCompletionPointIdIsNotNullAndEndCompletionPointIdIsNotNullOrderBySortOrder(Long scenarioId);

	
	

}
