package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

import com.office_nico.spractice.annotation.Code;

/**
 * 履修状態
 */
@Component
public class CompletionConditionCode extends BaseCode{

	@Code(order=1, message="{code.completion.condition.start}")
	public final Short start = 1;
	@Code(order=2, message="{code.completion.condition.end}")
	public final Short end = 2;

}
