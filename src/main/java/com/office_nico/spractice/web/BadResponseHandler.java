package com.office_nico.spractice.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.office_nico.spractice.exceptions.AppRunnableException;
import com.office_nico.spractice.exceptions.BadDownloadResponseException;
import com.office_nico.spractice.exceptions.BadHtmlResponseException;
import com.office_nico.spractice.exceptions.BadImageResponseException;
import com.office_nico.spractice.exceptions.BadJsonResponseException;
import com.office_nico.spractice.service.LogService;
import com.office_nico.spractice.web.JsonResponse.ResponseCode;

@ControllerAdvice
public class BadResponseHandler {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private LogService logService = null;
	
	@ExceptionHandler(BadJsonResponseException.class)
	public ResponseEntity<JsonResponse> getException(HttpServletRequest req, HttpServletResponse res, BadJsonResponseException e){
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setResult(ResponseCode.ERROR);

		// ログ出力
		logService.error(e.getLogger(), "", e);

		// ローディング画面が出ている可能性があるので停止
		Cookie cookie = new Cookie("stopLoading", "true");
		res.addCookie(cookie);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(BadHtmlResponseException.class)
	public String getException(HttpServletRequest req, HttpServletResponse res, BadHtmlResponseException e){
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setResult(ResponseCode.ERROR);
		
		// ログ出力
		logService.error(e.getLogger(), "", e);
		
		// ローディング画面が出ている可能性があるので停止
		Cookie cookie = new Cookie("stopLoading", "true");
		res.addCookie(cookie);
		// エラーコードをセット
		res.setStatus(500);
		return "/error/500.html";
	}

	@ExceptionHandler(BadDownloadResponseException.class)
	public HttpEntity<byte[]> getException(HttpServletRequest req, HttpServletResponse res, BadDownloadResponseException e){
		
		String errorMessage = "DOWNLOAD ERROR";
		
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setResult(ResponseCode.ERROR);

		// ログ出力
		logService.error(e.getLogger(), "", e);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		headers.add("Content-Disposition", "attachment; filename*=utf-8''error.txt");
		headers.add("Set-Cookie", "stopLoading=true; path=/");
		headers.setContentLength(errorMessage.getBytes().length);
		return new HttpEntity<byte[]>(errorMessage.getBytes(), headers);
	}
	
	@ExceptionHandler(BadImageResponseException.class)
	public HttpEntity<byte[]> getException(HttpServletRequest req, HttpServletResponse res, BadImageResponseException e){
		
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setResult(ResponseCode.ERROR);

		// ログ出力
		logService.error(e.getLogger(), "", e);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "image/png");
		headers.add("Content-Disposition", "attachment; filename*=utf-8''error.png");
		headers.add("Set-Cookie", "stopLoading=true; path=/");
		
		List<byte[]> list = new ArrayList<>();

		Resource resource = resourceLoader.getResource("classpath:static/img/no-image.png");
		try (InputStream in = resource.getInputStream(); BufferedInputStream bis = new BufferedInputStream(in)) {

			int avail;
			// 読み込み可能なバイト数づつ読み込む
			while ((avail = bis.available()) > 0) {
				byte[] bytes = new byte[avail];
				bis.read(bytes);
				list.add(bytes);
			}
			
			int size = 0;

			for (byte[] bytes : list) {
				size += bytes.length;
			}
			byte[] result = new byte[size];

			int i = 0;
			for (byte[] bytes : list) {
				for (byte b : bytes) {
					result[i] = b;
					i++;
				}
			}
			return new HttpEntity<byte[]>(result, headers);
		}
		catch (IOException ie) {
			e.printStackTrace();
			return new HttpEntity<byte[]>(null, headers);
		}
	}
}
