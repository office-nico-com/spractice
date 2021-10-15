package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Code;

/**
 * 遷移先種別コード
 */
@Component
public class ActionTypeCode extends BaseCode{

	@Code(order=1, message="{code.action.type.guidance}")
	public final Short guidance = 1;
	@Code(order=2, message="{code.action.type.script}")
	public final Short script = 2;
	@Code(order=3, message="{code.action.type.link}")
	public final Short link = 3;
	@Code(order=3, message="{code.action.type.max}")
	public final Short max = 4;
	@Code(order=3, message="{code.action.type.end}")
	public final Short end = 5;

}
