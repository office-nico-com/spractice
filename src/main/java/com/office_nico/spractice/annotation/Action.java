package com.office_nico.spractice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action{
	public enum Type{
		PARENT,
		DOWNLOAD,
		CSV,
		AJAX,
		IMAGE
	}
	Type value() default Type.PARENT;
}
