package com.office_nico.spractice.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SndboxController {
	@GetMapping({"/sandbox"})
	public String  index(Model model) {


		return "sandbox/index";
	}
}
