package com.office_nico.spractice.repository.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.User;

@Repository
public class UserDaoImpl implements UserDao<User> {

	private static final long serialVersionUID = -2961860719858399580L;

	@PersistenceContext
	private EntityManager entityManager = null;
}
