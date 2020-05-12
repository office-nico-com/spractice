package com.office_nico.spractice.repository.extension;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Extension;

@Repository
@Transactional
public interface ExtensionRepository extends JpaRepository<Extension,Long>, ExtensionDao<Extension> {

	
	List<Extension> findByVirtualMachineIdAndIsDeletedFalseAndIsInvalidedFalse(Long virtualMachineId);

}
