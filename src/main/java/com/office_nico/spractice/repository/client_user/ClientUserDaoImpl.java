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


@Repository
public class ClientUserDaoImpl implements ClientUserDao<ClientUser> {

	private static final long serialVersionUID = -4035190059284751587L;

	@PersistenceContext
	private EntityManager entityManager = null;

	@Override
	public List<ClientUser> findClientByUserId(Long userId) {

		
		@SuppressWarnings("rawtypes")
		List<Map> records =  entityManager.createQuery("select new map(c.id as id, c.clientKeycode as client_keycode, c.clientNameJa as client_name_ja, c.clientNameJaKana as client_name_ja_kana, c.userRegistTypeCode as user_regist_type_code, c.description as description, c.isInvalided as is_invalided, c.isDeleted as is_deleted, cu.roleCode as role_code) from ClientUser cu join cu.client c where cu.user.id = :arg1 order by c.clientKeycode", Map.class).setParameter("arg1", userId).getResultList();
		
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
	}
}
