package com.office_nico.spractice.repository.binary_file_category;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.BinaryFileCategory;

@Repository
@Transactional
public interface BinaryFileCategoryRepository extends JpaRepository<BinaryFileCategory,Long>, BinaryFileCategoryDao<BinaryFileCategory> {

	List<BinaryFileCategory> findByOrganizationIdOrderByOrderNumber(Long organizationId);

	Integer countByIdAndOrganizationId(Long id, Long organizationId);

}
