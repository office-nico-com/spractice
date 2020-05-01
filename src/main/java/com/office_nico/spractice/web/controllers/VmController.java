package com.office_nico.spractice.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.office_nico.spractice.service.InstructionService;
import com.office_nico.spractice.web.SessionAttributes;

@Controller
public class VmController {

	private static final String PREFIX = "vm";

	@Autowired
	private SessionAttributes session = null;

	@Autowired
	private InstructionService instructionService = null;

	// Viewファイルの定義
	public class View{
		public static final String INDEX = PREFIX + "/index";
	}

	@GetMapping({"/course/{courseName}/{virtualMachineName}"})
	public String  index(Model model, @PathVariable("courseName") String courseName, @PathVariable("virtualMachineName") String virtualMachineName) {

		session.setUserId(1L);
		session.setOrganizationId(1L);
		session.setRoleCode((short)1);



		instructionService.get(1L, courseName, virtualMachineName);



		return View.INDEX;
	}
}
