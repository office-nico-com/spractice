package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Code;

/**
 * 権限コード
 */
@Component
public class PositionCode extends BaseCode{

	@Code(order=1, message="{code.position.bottom}")
	public final Short bottom = 1;
	@Code(order=2, message="{code.position.top}")
	public final Short top = 2;
	
}
