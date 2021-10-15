package com.office_nico.spractice.constants.codes;

import org.springframework.stereotype.Component;

/**
 * ユーザー登録方法
 */
@Component
public class AdminFunctionCode extends BaseCode{

	public final Short login = 1;
	public final Short users = 2;
	public final Short importUser = 3;
	public final Short clients = 4;
	public final Short scenarios = 5;
	public final Short content = 6;
	public final Short guidance = 7;
	public final Short portals = 8;
	public final Short completionpoints = 9;
	public final Short completions = 10;
	public final Short password = 11;
	public final Short profile = 12;
	public final Short other = 99;
	
}
