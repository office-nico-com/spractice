package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Code;

/**
 * 権限コード
 */
@Component
public class RoleCode extends BaseCode{

	@Code(order=1, message="{code.role.admin}")
	public final Short admin = 1;
	@Code(order=2, message="{code.role.user}")
	public final Short user = 2;
	
}
