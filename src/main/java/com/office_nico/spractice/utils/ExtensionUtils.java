package com.office_nico.spractice.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ExtensionUtils {

	private static String STATIC_FILE_ROOT_PATH="static";
	private static String EXTENTION_FILE_PATH= "/extensions";
	
	
	/**
	 * 拡張機能のCSSファイルパスを取得する
	 * @return CSSファイルのパスリスト
	 */
	public static List<String> getCssPaths(ResourceLoader resourceLoader){
		List<String> files = dumpResources(resourceLoader, "(?i).+\\.css");
		
		return files;
	}

	/**
	 * 拡張機能のJSファイルパスを取得する
	 * @return JSファイルのパスリスト
	 */
	public static List<String> getJsPaths(ResourceLoader resourceLoader){
		List<String> files = dumpResources(resourceLoader, "(?i).+\\.js");
		return files;
	}
	
	
	private static List<String> dumpResources(ResourceLoader resourceLoader, String filterRegExp) {
		List<String> stack = new ArrayList<>();
		Pattern p = null;
		if(filterRegExp != null) {
			p = Pattern.compile(filterRegExp);
		}
		_dumpResources(resourceLoader, STATIC_FILE_ROOT_PATH + EXTENTION_FILE_PATH, EXTENTION_FILE_PATH, stack, p);
		return stack;
	}

	private static void _dumpResources(ResourceLoader resourceLoader, String fullPath, String excludeRootPath, List<String> stack, Pattern p) {

		Resource resource = resourceLoader.getResource("classpath:" + fullPath);
		try (InputStream in = resource.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				
				String pathA = fullPath + "/" + line;
				String pathB = excludeRootPath + "/" + line;

				Resource resource2 = resourceLoader.getResource("classpath:" + pathA);
				
				if(!resource2.isReadable()) {
					_dumpResources(resourceLoader, pathA, pathB, stack, p);
				}
				else {
					if(p == null) {
						stack.add(pathB);
					}
					else {
						Matcher m = p.matcher(line);
						if (m.find()){
							stack.add(pathB);
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
