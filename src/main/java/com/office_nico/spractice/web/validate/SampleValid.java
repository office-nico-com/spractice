package com.office_nico.spractice.web.validate;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = SampleValidator1.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface SampleValid {
	String message() default "{com.office_nico.spractice.web.validate.TelNumber.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};
}

