package com.office_nico.spractice.repository.admin_operation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.AdminOperation;

@Repository
public class AdminOperationDaoImpl implements AdminOperationDao<AdminOperation> {

	private static final long serialVersionUID = 1689865552719472929L;

	@PersistenceContext
	private EntityManager entityManager = null;
}
