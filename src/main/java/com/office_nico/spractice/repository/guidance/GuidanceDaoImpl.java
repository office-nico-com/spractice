package com.office_nico.spractice.repository.guidance;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Guidance;

@Repository
public class GuidanceDaoImpl implements GuidanceDao<Guidance> {

	private static final long serialVersionUID = 4856353996006103831L;

	@PersistenceContext
	private EntityManager entityManager = null;
}
