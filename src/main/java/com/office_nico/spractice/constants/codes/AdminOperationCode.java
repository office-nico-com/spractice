package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Code;

/**
 * ユーザー登録方法
 */
@Component
public class AdminOperationCode extends BaseCode{

	@Code(order=1, message="{code.admin.operation.create}")
	public final Short create = 1;
	@Code(order=1, message="{code.admin.operation.update}")
	public final Short update = 2;
	@Code(order=1, message="{code.admin.operation.delete}")
	public final Short delete = 3;
	@Code(order=1, message="{code.admin.operation.copy}")
	public final Short copy = 4;
	@Code(order=1, message="{code.admin.operation.download}")
	public final Short download = 5;
	@Code(order=1, message="{code.admin.operation.other}")
	public final Short other = 99;
	
}
