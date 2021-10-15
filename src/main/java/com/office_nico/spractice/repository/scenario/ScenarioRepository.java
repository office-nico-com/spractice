package com.office_nico.spractice.repository.scenario;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.Scenario;



@Repository
@Transactional
public interface ScenarioRepository extends JpaRepository<Scenario,Long>, ScenarioDao<Scenario> {

	// offset limit クエリを書くパターン
	@Query(value="SELECT * FROM clients WHERE is_deleted = false ORDER BY ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
	public List<Client> findByIsDeletedFalse(String order, Integer offset, Integer limit);
	
	// offset limit Pagableを使用するパターン
	public Page<Scenario> findByIsDeletedFalse(Pageable pr);

	public List<Scenario> findByIsDeletedFalseOrderByScenarioKeycode();

	public Long countByIsDeletedFalseAndScenarioKeycode(String senarioKeycode);

	public Long countByIsDeletedFalseAndScenarioKeycodeAndIdNot(String senarioKeycode, Long senarioId);

	// findValidSenarioByClientIdと重複、参考にために残しておく。
	@Query("SELECT s FROM ClientScenario cs join cs.scenario s where s.isDeleted = false AND s.isInvalided = false AND cs.isInvalided = false AND cs.client.id = ?1")
	public List<Scenario> findByClientId(Long clientId);

	public Scenario findTopByScenarioKeycode(String senarioKeycode);

}
