package com.office_nico.spractice.repository.client;


import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Client;



@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client,Long>, ClientDao<Client> {

	// offset limit クエリを書くパターン
	@Query(value="SELECT * FROM clients WHERE is_deleted = false ORDER BY ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
	public List<Client> findByIsDeletedFalse(String order, Integer offset, Integer limit);
	
	// offset limit Pagableを使用するパターン
	public Page<Client> findByIsDeletedFalse(Pageable pr);

	@Query(value="SELECT c FROM Client c WHERE c.isDeleted = false AND (c.clientNameJaKana LIKE ?1 OR c.clientNameJa LIKE ?1 OR c.clientKeycode LIKE ?1)", 
			countQuery="SELECT count(c) FROM Client c WHERE c.isDeleted = false AND (c.clientNameJaKana LIKE ?1 OR c.clientNameJa LIKE ?1 OR c.clientKeycode LIKE ?1)")
	public Page<Client> findByIsDeletedFalseAndClientNameOrClientKeycode(String searchKey, Pageable pr);

	
	// これもいけるらしい、すげー Mapで返すのも可能、すげー
	@Query(value="SELECT * FROM clients WHERE is_deleted = false", nativeQuery = true)
	public Page<Map<String, Object>> findByIsDeletedFalseTest(Pageable pr);

	public Long countByIsDeletedFalseAndClientKeycode(String clientKeycode);

	public Long countByIsDeletedFalseAndClientKeycodeAndIdNot(String clientKeycode, Long clientId);

	public List<Client> findByIsDeletedFalseOrderByClientNameJaKana();

	public Client findTopByClientKeycodeAndIsDeletedFalseAndIsInvalidedFalse(String clientKeycode);

	public Client findTopByClientKeycodeAndIsDeletedFalse(String clientKeycode);

	@Query("SELECT count(cu) FROM ClientUser cu join cu.client c where c.isDeleted = false AND cu.client.id = ?1")
	public Integer countUsersByClientId(Long clientId);

}
