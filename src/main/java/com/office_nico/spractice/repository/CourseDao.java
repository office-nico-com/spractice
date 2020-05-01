package com.office_nico.spractice.repository;

import java.io.Serializable;

import com.office_nico.spractice.domain.Course;

public interface CourseDao <T> extends Serializable {
	public Course getX();
}

