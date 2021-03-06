package com.office_nico.spractice.repository.course;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Course;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course,Long>, CourseDao<Course> {

	public List<Course> findByCourseName(String courseName);

	public List<Course> findByOrganizationIdAndCourseNameAndIsDeletedFalseAndIsInvalidedFalse(Long organizationId, String courseName);

	
	public Integer countByCourseName(String courseName);

}
