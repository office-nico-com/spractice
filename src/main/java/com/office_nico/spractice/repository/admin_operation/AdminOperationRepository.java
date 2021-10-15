package com.office_nico.spractice.repository.admin_operation;


import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.AdminOperation;

@Repository
@Transactional
public interface AdminOperationRepository extends JpaRepository<AdminOperation,Long>, AdminOperationDao<AdminOperation> {

	@Query(value="SELECT "
			+ "users.account, users.family_name, users.given_name,"
			+ "clients.client_keycode, clients.client_name_ja,"
			+ "scenarios.scenario_keycode, scenarios.scenario_name,"
			+ "completion_points.completion_point_keycode,"
			+ "u2.family_name AS operator_family_name, "
			+ "u2.given_name AS operator_given_name, "
			+ "admin_operations.* FROM admin_operations "
			+ "LEFT OUTER JOIN users ON (users.id  = admin_operations.target_id AND admin_operations.function_code = 2) "
			+ "LEFT OUTER JOIN clients ON (clients.id  = admin_operations.target_id AND (admin_operations.function_code = 4 OR admin_operations.function_code = 8)) "
			+ "LEFT OUTER JOIN scenarios ON (scenarios.id  = admin_operations.target_id AND (admin_operations.function_code = 5 OR admin_operations.function_code = 6 OR admin_operations.function_code = 7)) "
			+ "LEFT OUTER JOIN completion_points ON (completion_points.id  = admin_operations.target_id AND admin_operations.function_code = 9) "
			+ "LEFT OUTER JOIN users AS u2 ON (u2.id  = admin_operations.user_id) "
			+ "WHERE admin_operations.user_id = ?1 "
			+ "ORDER BY operated_at desc", 
			countQuery = "SELECT count(*) FROM admin_operations WHERE admin_operations.user_id = ?1",
			nativeQuery = true)
	public Page<Map<String, Object>> findByUserId(Long userId, Pageable pr);

	
	@Query(value="SELECT "
			+ "users.account, users.family_name, users.given_name,"
			+ "clients.client_keycode, clients.client_name_ja,"
			+ "scenarios.scenario_keycode, scenarios.scenario_name,"
			+ "completion_points.completion_point_keycode,"
			+ "u2.family_name AS operator_family_name, "
			+ "u2.given_name AS operator_given_name, "
			+ "admin_operations.* FROM admin_operations "
			+ "LEFT OUTER JOIN users ON (users.id  = admin_operations.target_id AND admin_operations.function_code = 2) "
			+ "LEFT OUTER JOIN clients ON (clients.id  = admin_operations.target_id AND (admin_operations.function_code = 4 OR admin_operations.function_code = 8)) "
			+ "LEFT OUTER JOIN scenarios ON (scenarios.id  = admin_operations.target_id AND (admin_operations.function_code = 5 OR admin_operations.function_code = 6 OR admin_operations.function_code = 7)) "
			+ "LEFT OUTER JOIN completion_points ON (completion_points.id  = admin_operations.target_id AND admin_operations.function_code = 9) "
			+ "LEFT OUTER JOIN users AS u2 ON (u2.id  = admin_operations.user_id) "
			+ "ORDER BY operated_at desc", 
			countQuery = "SELECT count(*) FROM admin_operations",
			nativeQuery = true)
	public Page<Map<String, Object>> findAllRecords(Pageable pr);


}
