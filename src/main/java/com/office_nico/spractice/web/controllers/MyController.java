package com.office_nico.spractice.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

	private static final String PREFIX = "my";

	// Viewファイルの定義
	public class View{
		public static final String INDEX = PREFIX + "/index";
	}

	@GetMapping({PREFIX})
	public String  container(Model model) {
		return View.INDEX;
	}
}
