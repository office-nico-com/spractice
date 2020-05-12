package com.office_nico.spractice.repository.course;

import java.io.Serializable;
import java.util.Optional;

import com.office_nico.spractice.domain.Course;

public interface CourseDao <T> extends Serializable {

	Optional<Course> getX(String courseName);

	Optional<Course> getY(String courseName);
}

