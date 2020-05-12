package com.office_nico.spractice.repository.course;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Course;
import com.office_nico.spractice.domain.VirtualMachine;

@Repository
public class CourseDaoImpl implements CourseDao<Course> {

	private static final long serialVersionUID = -6904187129704834197L;

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
	public Optional<Course> getX(String courseName){

		Course ret = new Course();

		// ret = entityManager.createQuery("select c from Course c order by id desc", Course.class).setMaxResults(1).getSingleResult();

		ret = entityManager.createQuery("select c from Course c where courseName = :arg1 order by id desc", Course.class).setParameter("arg1", courseName).setMaxResults(1).getSingleResult();

		
		return Optional.ofNullable(ret);
	}
	
	
	@Override
	public Optional<Course> getY(String courseName){



		// ① join fetchしないと、オブジェクトを配列で返すことができる。
		Object[] a = entityManager.createQuery("select c,v from Course c join c.virtualMachines v where c.courseName = :arg1 and v.id = 1 order by c.id desc", Object[].class).setParameter("arg1", courseName).setMaxResults(1).getSingleResult();
//		System.out.println(a[0]);
//		System.out.println(a[1]);

		System.out.println(((Course)(a[0])).getCourseName());
		// System.out.println(((Course)(a[0])).getVirtualMachines().get(0).getVirtualMachineName());
		System.out.println(((VirtualMachine)(a[1])).getVirtualMachineName());
		
		// ② join fetchすると、c.virtualMatchnes[0]に結果がはいる。
		Course b = entityManager.createQuery("select c from Course c join fetch c.virtualMachines v where c.courseName = :arg1 and v.id = 1 order by c.id desc", Course.class).setParameter("arg1", courseName).setMaxResults(1).getSingleResult();

		System.out.println(b.getCourseName());
		System.out.println(b.getVirtualMachines().get(0).getVirtualMachineName());

		// うん、、、まぁ、どっちでもいいが①の方が直観的にわかって、②の方が論理的か
		
		
		
//		System.out.println(a.getCourseName());
//		System.out.println(a.getVirtualMachines().get(0).getVirtualMachineName());

		
		
		
		
		return Optional.ofNullable(b);
	}

}
