package com.office_nico.spractice.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {

	private static final String PREFIX = "course";

	// Viewファイルの定義
	public class View{
		public static final String INDEX = PREFIX + "/index";
		public static final String SHOW = PREFIX + "/show";
	}

	@GetMapping({"/courses"})
	public String  index(Model model) {

		return View.INDEX;
	}

	@GetMapping({"/course/{courseName}"})
	public String  show(Model model) {

		return View.SHOW;
	}

}
