package com.office_nico.spractice.repository.binary_file_category;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.BinaryFileCategory;

@Repository
public class BinaryFileCategoryDaoImpl implements BinaryFileCategoryDao<BinaryFileCategory> {

	private static final long serialVersionUID = 1259619843968581069L;

	@PersistenceContext
	private EntityManager entityManager = null;

	public Optional<BinaryFileCategory> getByOrganizationIdOrderByFirstOrderNumber(Long organizationId) {
		
		BinaryFileCategory binaryFileCategory = null;

		binaryFileCategory = entityManager.createQuery("select t1 from BinaryFileCategory t1 where organizationId = :arg1 order by orderNumber asc", BinaryFileCategory.class).setParameter("arg1", organizationId).setMaxResults(1).getSingleResult();
		
		return Optional.ofNullable(binaryFileCategory);
	}

}
