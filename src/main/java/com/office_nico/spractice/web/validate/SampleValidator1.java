package com.office_nico.spractice.web.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.office_nico.spractice.service.ClientService;


// ClientFormのところがStringなら入力値がかえってくる
// その場合はフィールドにアノテーションをセット
// 複合チェックする場合はクラスにアノテーションをセット
public class SampleValidator1 implements ConstraintValidator<SampleValid, Object>{

	// ここでサービスの呼出も可能、と
	@Autowired ClientService clientService = null;

	private String message;

	@Override
	public void initialize(SampleValid constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		this.message = constraintAnnotation.message();
	}
	
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		// これで特定のフィールドにエラーを出力できます
		// まぁ、普通ならアノテーションから取得するんだろう
		context.buildConstraintViolationWithTemplate(message).addPropertyNode("clientKeycode").addConstraintViolation();
		return false;
	}
}
