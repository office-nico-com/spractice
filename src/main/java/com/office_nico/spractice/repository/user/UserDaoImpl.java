package com.office_nico.spractice.repository.user;

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

import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.NativeQueryBuilder;


@Repository
public class UserDaoImpl implements UserDao<User> {

	private static final long serialVersionUID = -2961860719858399580L;

	@PersistenceContext
	private EntityManager entityManager = null;
	
	@Override
	public Page<User> findUsersBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit){
		
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);

		query.append("SELECT DISTINCT");
		query.appendField("users.id", "id");
		query.appendField("users.account", "account");
		query.appendField("users.family_name", "family_name");
		query.appendField("users.given_name", "given_name");
		query.appendField("users.family_name_kana", "family_name_kana");
		query.appendField("users.given_name", "given_name_kana");
		query.appendField("users.email", "email");
		query.appendField("users.registered_from_code", "registered_from_code");
		query.appendField("users.is_admin", "is_admin");
		query.appendField("users.is_invalided", "is_invalided");
		query.append("FROM users");
		query.append("LEFT OUTER JOIN clients_users ON (clients_users.user_id = users.id)");
		query.append("LEFT OUTER JOIN clients ON (clients.id = clients_users.client_id AND clients.is_deleted = false)");
		query.append("WHERE users.is_deleted = false ");
		if(searchKeyword != null && searchKeyword.length() > 0) {
			searchKeyword = "%" + searchKeyword + "%";
			query.append("AND (");
			query.append("users.account LIKE ?", searchKeyword);
			query.append("OR users.family_name LIKE ?", searchKeyword);
			query.append("OR users.family_name_kana LIKE ?", searchKeyword);
			query.append("OR users.given_name LIKE ?", searchKeyword);
			query.append("OR users.given_name_kana LIKE ?", searchKeyword);
			query.append("OR users.email LIKE ?", searchKeyword);
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
		
		
		List<User> users = new ArrayList<>();
		for(Map<String, Object> result : results) {
			User user = BeanUtil.mapToBean(result, new User());
			users.add(user);
		}
		
		int pageNumber = offset == 0 ? 0 : (offset / limit);
		Pageable pr = PageRequest.of(pageNumber, limit);

		Page<User> p = new PageImpl<User>(users, pr, count);
		return p;
	}
}
