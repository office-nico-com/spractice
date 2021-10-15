package com.office_nico.spractice.repository.user;


import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long>, UserDao<User> {

	public User findFirstByAccountAndIsDeletedFalseAndIsInvalidedFalse(String email);

	// public Page<User> findByIsDeletedFalse(Pageable pr);
	// @Query(value="SELECT users.*, t2.client_keycode_list, t2.client_name_list, t2.client_name_kana_list FROM users INNER JOIN (SELECT users.id, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_keycode)) ORDER BY 1), '\t') as client_keycode_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_kana_list FROM users LEFT OUTER JOIN  (SELECT clients_users.user_id, clients.client_keycode, clients.client_name_ja, clients.client_name_ja_kana FROM clients_users INNER JOIN clients ON (clients.id = clients_users.client_id) order by clients.id) AS t1 ON t1.user_id = users.id GROUP BY users.id) AS t2  ON (t2.id = users.id) WHERE is_deleted = false ORDER BY :order LIMIT :limit OFFSET :offset ", nativeQuery = true)
	@Query(value="SELECT u.*, t2.client_keycode_list, t2.client_name_list, t2.client_name_kana_list FROM users AS u INNER JOIN (SELECT users.id, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_keycode)) ORDER BY 1), '\t') as client_keycode_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_kana_list FROM users LEFT OUTER JOIN  (SELECT clients_users.user_id, clients.client_keycode, clients.client_name_ja, clients.client_name_ja_kana FROM clients_users INNER JOIN clients ON (clients.id = clients_users.client_id) order by clients.id) AS t1 ON t1.user_id = users.id GROUP BY users.id) AS t2  ON (t2.id = u.id) WHERE is_deleted = false ORDER BY TRUE ", 
			countQuery="SELECT count(*) as count FROM users INNER JOIN (SELECT users.id, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_keycode)) ORDER BY 1), '\t') as client_keycode_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_kana_list FROM users LEFT OUTER JOIN  (SELECT clients_users.user_id, clients.client_keycode, clients.client_name_ja, clients.client_name_ja_kana FROM clients_users INNER JOIN clients ON (clients.id = clients_users.client_id) order by clients.id) AS t1 ON t1.user_id = users.id GROUP BY users.id) AS t2  ON (t2.id = users.id) WHERE is_deleted = false",
			nativeQuery = true)
	public Page<Map<String, Object>> findByIsDeletedFalse2(Pageable pr);

	public Page<User> findByIsDeletedFalse(Pageable pr);

	@Query(value="SELECT count(*) as count FROM users INNER JOIN (SELECT users.id, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_keycode)) ORDER BY 1), '\t') as client_keycode_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_list, array_to_string(ARRAY(SELECT unnest(array_agg(t1.client_name_ja)) ORDER BY 1), '\t') as client_name_kana_list FROM users LEFT OUTER JOIN  (SELECT clients_users.user_id, clients.client_keycode, clients.client_name_ja, clients.client_name_ja_kana FROM clients_users INNER JOIN clients ON (clients.id = clients_users.client_id) order by clients.id) AS t1 ON t1.user_id = users.id GROUP BY users.id) AS t2  ON (t2.id = users.id) WHERE is_deleted = false", nativeQuery = true)
	public Long countByIsDeletedFalse();

	public User findTopByAccountAndIsDeletedFalseAndIsInvalidedFalse(String account);

	public User findTopByAccountAndIsDeletedFalse(String account);

	public Long countByIsDeletedFalseAndAccount(String email);

	public Long countByIsDeletedFalseAndAccountAndIdNot(String email, Long userId);

	public User findTopById(Long userId);
	
}
