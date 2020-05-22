package com.office_nico.spractice.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.office_nico.spractice.service.InstructionService;
import com.office_nico.spractice.service.data.Instruction;
import com.office_nico.spractice.util.ExtensionUtil;
import com.office_nico.spractice.web.SessionAttributes;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Controller
public class VmController {

	private static final String PREFIX = "vm";

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	private SessionAttributes session = null;

	@Autowired
	private InstructionService instructionService = null;

	// Viewファイルの定義
	public class View {
		public static final String INDEX = PREFIX + "/index";
	}

	@GetMapping({ "course/{courseName}/{virtualMachineName}" })
	public String index(Model model, @PathVariable("courseName") String courseName, @PathVariable("virtualMachineName") String virtualMachineName) {

		session.setUserId(1L);
		session.setOrganizationId(1L);
		session.setRoleCode((short) 1);

		Instruction instruction = instructionService.get(1L, 1L, courseName, virtualMachineName);
		model.addAttribute("instruction", instruction);

		
		model.addAttribute("extensionCssPaths", ExtensionUtil.getCssPaths(resourceLoader));
		model.addAttribute("extensionJsPaths", ExtensionUtil.getJsPaths(resourceLoader));

		return View.INDEX;
	}

}
