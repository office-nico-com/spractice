package com.office_nico.spractice.repository.client;

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

import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.NativeQueryBuilder;


@Repository
public class ClientDaoImpl implements ClientDao<Client> {

	private static final long serialVersionUID = 7638801827070413804L;

	@PersistenceContext
	private EntityManager entityManager = null;
	
	@Override
	public List<Client> findClientByUserId(Long userId) {

		List<Client> records = entityManager.createQuery("select c from ClientUser cu join cu.client c where cu.user.id = :arg1 order by c.clientKeycode", Client.class).setParameter("arg1", userId).getResultList();
		
		return records;
	}
	
	@Override
	public Page<Client> findClientsBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit){
		
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);

		query.append("SELECT DISTINCT");
		query.appendField("clients.id");
		query.appendField("clients.client_keycode");
		query.appendField("clients.client_name_ja");
		query.appendField("clients.client_name_ja_kana");
		query.appendField("clients.user_regist_type_code");
		query.appendField("clients.description");
		query.appendField("clients.secret");
		query.appendField("clients.logout_url");
		query.appendField("clients.security_mangement_team");
		query.appendField("clients.security_mangement_tel");
		query.appendField("clients.security_mangement_email");
		query.appendField("clients.is_invalided");
		query.appendField("clients.is_deleted");
		query.append("FROM clients");
		query.append("LEFT OUTER JOIN clients_scenarios ON (clients_scenarios.client_id = clients.id)");
		query.append("LEFT OUTER JOIN scenarios ON (scenarios.id = clients_scenarios.scenario_id AND scenarios.is_deleted = false)");
		query.append("WHERE clients.is_deleted = false ");
		if(searchKeyword != null && searchKeyword.length() > 0) {
			searchKeyword = "%" + searchKeyword + "%";
			query.append("AND (");
			query.append("clients.client_keycode LIKE ?", searchKeyword);
			query.append("OR clients.client_name_ja LIKE ?", searchKeyword);
			query.append("OR clients.client_name_ja_kana LIKE ?", searchKeyword);
			query.append("OR scenarios.scenario_keycode LIKE ?", searchKeyword);
			query.append("OR scenarios.scenario_name LIKE ?", searchKeyword);
			query.append(")");
		}

		for(String order : orders) {
			query.addOrder(order, direction);
		}
		query.offset(offset);
		query.limit(limit);

		List<Map<String, Object>> results = query.getResults();
		Integer count = query.getCount();
		
		
		List<Client> clients = new ArrayList<>();
		for(Map<String, Object> result : results) {
			Client client = BeanUtil.mapToBean(result, new Client());
			clients.add(client);
		}
		
		int pageNumber = offset == 0 ? 0 : (offset / limit);
		Pageable pr = PageRequest.of(pageNumber, limit);

		Page<Client> p = new PageImpl<Client>(clients, pr, count);
		return p;
	}


}
