package com.office_nico.spractice.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Course;

@Repository
public class CourseDaoImpl implements CourseDao<Course> {

	@PersistenceContext
	private EntityManager entityManager = null;

	/*
	public CourseDaoImpl() {
		super();
	}

	public CourseDaoImpl(EntityManager em) {
		this();
		entityManager = em;
	}
	*/


	@Override
	public Course getX(){

		Course ret = new Course();

		List<Course> c = entityManager.createQuery("select c from Course c order by id desc", Course.class).setMaxResults(1).getResultList();

		if(c.size() > 0) {
			ret = c.get(0);
		}


		return ret;
	}
}
