package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Code;

/**
 * ユーザー登録方法
 */
@Component
public class UserRegistTypeCode extends BaseCode{

	@Code(order=1, message="{code.user.regist.type.manual}")
	public final Short manual = 1;
	@Code(order=2, message="{code.user.regist.type.system_link}")
	public final Short systemLink = 2;
	
}
