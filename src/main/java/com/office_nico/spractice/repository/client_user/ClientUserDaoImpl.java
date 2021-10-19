package com.office_nico.spractice.repository.client_user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientUser;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.NativeQueryBuilder;


@Repository
public class ClientUserDaoImpl implements ClientUserDao<ClientUser> {

	private static final long serialVersionUID = -4035190059284751587L;

	@PersistenceContext
	private EntityManager entityManager = null;

	@Override
	public List<ClientUser> findClientByUserId(Long userId) {
		
		
		List<ClientUser> results = new ArrayList<>();
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);
		
		
		query.append("SELECT");
		query.appendField("clients.id");
		query.appendField("clients.client_keycode");
		query.appendField("clients.client_name_ja");
		query.appendField("clients.client_name_ja_kana");
		query.appendField("clients.user_regist_type_code");
		query.appendField("clients.is_invalided");
		query.appendField("clients.is_deleted");
		query.appendField("clients_users.role_code");
		query.append("FROM clients_users");
		query.append("INNER JOIN clients ON (clients.id = clients_users.client_id AND clients.is_deleted=false)");
		query.append("WHERE clients_users.user_id = ?", userId);

		query.addOrder("clients.client_keycode", "ASC");

		List<Map<String, Object>> records = query.getResults();
		for(Map<String, Object> record : records) {
			ClientUser clientUser = new ClientUser();
			clientUser.setRoleCode((Short)record.get("role_code"));

			Client client = BeanUtil.mapToBean(record, new Client());
			clientUser.setClient(client);
			results.add(clientUser);
		}

		return results;

		
		
		
		/*
		@SuppressWarnings("rawtypes")
		List<Map> records =  entityManager.createQuery("select new map(c.id as id, c.clientKeycode as client_keycode, c.clientNameJa as client_name_ja, c.clientNameJaKana as client_name_ja_kana, c.userRegistTypeCode as user_regist_type_code, c.description as description, c.isInvalided as is_invalided, c.isDeleted as is_deleted, cu.roleCode as role_code) from ClientUser cu join cu.client c where cu.user.id = :arg1 AND c.isDeleted = false order by c.clientKeycode", Map.class).setParameter("arg1", userId).getResultList();

		List<ClientUser> ret = new ArrayList<>();
		for(@SuppressWarnings("rawtypes") Map map : records) {
			ClientUser clientUser = new ClientUser();
			clientUser.setRoleCode((Short)map.get("role_code"));

			@SuppressWarnings("unchecked")
			Client client = BeanUtil.mapToBean(map, new Client());
			clientUser.setClient(client);
			ret.add(clientUser);
		}
		return ret;
		*/
	}
}
