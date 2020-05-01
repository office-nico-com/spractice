package com.office_nico.spractice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.VirtualMachine;

@Repository
@Transactional
public interface VirtualMachineRepository extends JpaRepository<VirtualMachine,Long>, VirtualMachineDao {

	public List<VirtualMachine> findByVirtualMachineName(String virtualMachineName);

	public Integer countByVirtualMachineName(String virtualMachineName);
}
