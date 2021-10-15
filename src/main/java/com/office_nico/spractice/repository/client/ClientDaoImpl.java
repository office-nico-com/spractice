package com.office_nico.spractice.repository.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Client;


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

}
