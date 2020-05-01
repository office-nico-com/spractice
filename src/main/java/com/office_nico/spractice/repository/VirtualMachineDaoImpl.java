package com.office_nico.spractice.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.VirtualMachine;

@Repository
public class VirtualMachineDaoImpl implements VirtualMachineDao<VirtualMachine> {

	@PersistenceContext
	private EntityManager entityManager = null;

//	@Autowired
//	private EntityManagerFactory factory  = null;


	@Override
	public VirtualMachine getX(){

//		EntityManager entityManager = factory.createEntityManager();

		VirtualMachine ret = new VirtualMachine();

		List<VirtualMachine> v = entityManager.createQuery("select v from VirtualMachine v order by id desc", VirtualMachine.class).setMaxResults(1).getResultList();

		if(v.size() > 0) {
			ret = v.get(0);
		}


		return ret;
	}
}
