package com.office_nico.spractice.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SndboxController {

	@Autowired
	ConfigurableApplicationContext ctx = null;
	
	@GetMapping({ "/sandbox" })
	public String index(Model model) {

		InputStream in = this.getClass().getResourceAsStream("../../extensions/explorer/extension.properties");
		if (in == null) {
			throw new IllegalArgumentException(" not found.");
		}
		Properties props = new Properties();
		try {
			props.load(in);
			System.out.println(props.getProperty("icon"));
			
			/*
			ExtensionManager a;
			try {
				a = (ExtensionManager)ctx.getBean(Class.forName("com.office_nico.spractice.extensions.explorer.ExplorerExtension"));
				System.out.println(a.getSpecificProperty(1L, "hoge"));
			}
			catch (ClassNotFoundException | BeansException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			**/
			
		}
		catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return "sandbox/index";
	}
}
