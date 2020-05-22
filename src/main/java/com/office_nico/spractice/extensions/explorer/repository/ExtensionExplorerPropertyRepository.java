package com.office_nico.spractice.extensions.explorer.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Extension;
import com.office_nico.spractice.extensions.explorer.domain.ExtensionExplorerProperty;

@Repository
@Transactional
public interface ExtensionExplorerPropertyRepository extends JpaRepository<ExtensionExplorerProperty,Long>, ExtensionExplorerPropertyDao<ExtensionExplorerProperty> {

	List<ExtensionExplorerProperty> findByExtensionIdAndParentIdIsNullOrderByOrderNumber(Long extensionId);

	List<ExtensionExplorerProperty> findByParentIdOrderByOrderNumber(Long parentId);

	
}
