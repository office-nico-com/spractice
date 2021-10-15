package com.office_nico.spractice.web.controller.admin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.domain.BinaryFileCategory;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.StorageFileService;
import com.office_nico.spractice.service.data.StorageFile;
import com.office_nico.spractice.service.data.StorageFile.Result;
import com.office_nico.spractice.web.controller.admin.JsonResponse.ResponseCode;
import com.office_nico.spractice.web.form.UploadForm;

import lombok.Getter;
import lombok.Setter;


@Controller
public class StoragesController {

	private static final String PREFIX = "t06o1ny8/storages";

	// Viewファイルの定義
	public class View {
		public static final String INDEX = "admin/storages/index";
		public static final String SHOW = "admin/storages/show";
		public static final String UPLOAD = "admin/storages/upload";
	}

	private static final Logger logger = LoggerFactory.getLogger(StoragesController.class);

	@Autowired
	private StorageFileService storageFileService = null;
	
	@Value("${zip.work.path}")
	private String zipWork = null;
	
	/**
	 * ファイルマネージャ画面
	 * @param model
	 * @param binaryFileCategoryId
	 * @return
	 */
	@Action(Type.PARENT)
	@GetMapping({ PREFIX, PREFIX + "/{binaryFileCategoryId}" })
	public String index(Model model, @PathVariable(name = "binaryFileCategoryId", required = false) Long binaryFileCategoryId) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			
			List<BinaryFileCategory> binaryFileCategories = storageFileService.getCategories(logger, sessionuser.getId());
			// カテゴリの一覧をチェックして、利用できないカテゴリを指定されたらトップに飛ばす
			if (binaryFileCategoryId != null) {
				boolean find = false;
				for (BinaryFileCategory binaryFileCategory : binaryFileCategories) {
					if (binaryFileCategory.getId().equals(binaryFileCategoryId)) {
						find = true;
						break;
					}
				}
				if (!find) {
					return "redirect:/" + PREFIX;
				}
			}
			else {
				binaryFileCategoryId = binaryFileCategories.get(0).getId();
			}
			model.addAttribute("binaryFileCategoryId", binaryFileCategoryId);
			model.addAttribute("categories", binaryFileCategories);
			List<StorageFile> storageFiles = storageFileService.list(logger, sessionuser.getId(), binaryFileCategoryId);
			model.addAttribute("storageFiles", storageFiles);

			return View.INDEX;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * ファイルアップロード処理
	 * @param model
	 * @param uploadForm
	 * @return
	 * @throws IOException
	 */
	@PostMapping({ PREFIX })
	@Action(Type.AJAX)
	@ResponseBody
	public JsonResponse upload(Model model, UploadForm uploadForm) throws IOException{
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 結果用クラスオブジェクト
			class UploadResult extends JsonResponse{
				@Getter
				@Setter
				private Long id = null;
			}
			
			UploadResult response = new UploadResult();
			if (uploadForm.getFile().isEmpty()) {
				response.setResult(JsonResponse.ResponseCode.ERROR);
				return response;
			}

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/M/d H:m:s");
			LocalDateTime lastModified = LocalDateTime.parse(uploadForm.getLastModifiedDate() + " " + uploadForm.getLastModifiedTime(), dtf);
			StorageFile storageFile = storageFileService.save(logger, sessionuser.getId(), uploadForm.getFile().getBytes(), uploadForm.getFile().getOriginalFilename(), lastModified, uploadForm.getBinaryFileCategoryId(), uploadForm.getMimeType());
			if (storageFile.getResult() == StorageFile.Result.SUCCESS) {
				response.setId(storageFile.getBinaryFile().getId());
			}
			else {
				response.setResult(JsonResponse.ResponseCode.ERROR);
			}
			return response;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * 画像表示処理
	 * @param model
	 * @param binaryFileId
	 * @return
	 */
	@GetMapping({ PREFIX + "/image/{binaryFileId}" })
	@Action(Type.AJAX)
	@ResponseBody
	public HttpEntity<byte[]> image(Model model, @PathVariable(name = "binaryFileId", required = true) Long binaryFileId) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			
			StorageFile storageFile = storageFileService.get(logger, sessionuser.getId(), binaryFileId);
			if (storageFile.getResult() != StorageFile.Result.SUCCESS) {
				// TODO:
				throw new NullPointerException();
			}
			byte[] body = storageFile.getBody();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", storageFile.getBinaryFile().getMimeType());
			headers.setContentLength(body.length);
			return new HttpEntity<byte[]>(body, headers);
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}

	/**
	 * ファイルダウンロード処理
	 * @param model
	 * @param response
	 * @param binaryFileIds
	 * @return
	 */
	@GetMapping({ PREFIX + "/download" })
	@Action(Type.DOWNLOAD)
	@ResponseBody
	public HttpEntity<byte[]> download(Model model, HttpServletResponse response, @RequestParam(name = "binaryFileIds", required = true) List<Long> binaryFileIds) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		HttpHeaders header = new HttpHeaders();
		try {
			if (binaryFileIds.size() == 1) {
				StorageFile storageFile = storageFileService.get(logger, sessionuser.getId(), binaryFileIds.get(0));
				if (storageFile.getResult() == StorageFile.Result.SUCCESS) {
					header.add("Content-Type", storageFile.getBinaryFile().getMimeType());
					header.add("Content-Disposition", "attachment; filename*=utf-8''" + URLEncoder.encode(storageFile.getBinaryFile().getOriginalFileName(), "UTF-8"));
					header.add("Set-Cookie", "stopLoading=true; path=/");
					return new HttpEntity<byte[]>(storageFile.getBody(), header);
				}
				else {
					throw new Exception("There is no data. id = " + binaryFileIds.get(0));
				}
			}
			else if (binaryFileIds.size() > 1) {

				// zipファイルにローカルディスクに保存
				File dir = new File(zipWork);
				if(!dir.exists()) {
					if(!dir.mkdirs()) {
						throw new Exception("TFailed to create the directory. dir = " + dir);
					}
				}
				
				List<Map<String, Object>> checkFileNames=new ArrayList<>();
				String filename = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
				File fz = new File(zipWork + "/" + filename);
				try (FileOutputStream fos = new FileOutputStream(fz); ZipOutputStream zos = new ZipOutputStream(fos);) {

					for (Long binaryFileId : binaryFileIds) {
						StorageFile storageFile = storageFileService.get(logger, sessionuser.getId(), binaryFileId);
						if(storageFile.getResult() == Result.SUCCESS) {
							// テンポラリファイルに書き出し
							ZipEntry ze = new ZipEntry(getZipFileName(storageFile.getBinaryFile().getOriginalFileName(), checkFileNames));
							zos.putNextEntry(ze);
							zos.write(storageFile.getBody(), 0, storageFile.getBody().length);
							zos.closeEntry();
						}
					}
				}
				catch (Exception e) {

					throw e;
				}

				// ダウンロード処理、zipファイルを読み出しながら出力
				try (FileInputStream fis = new FileInputStream(fz); BufferedInputStream reader = new BufferedInputStream(fis); OutputStream out = response.getOutputStream();) {
					String dlname = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm").format(LocalDateTime.now());
					response.setContentType("application/octet-stream;charset=UTF-8");
					response.setContentLengthLong(fz.length());
					response.setHeader("Content-Disposition", "attachment; filename=\"download_" + dlname + ".zip\"");
					// response.setHeader("Set-Cookie", "stopLoading=true; path=/");		// js側でローディング画面を停止する
					Cookie cookie = new Cookie("stopLoading", "true");
					response.addCookie(cookie);
					
					int data;
					while ((data = reader.read()) != -1) {
						out.write(data);
					}
				}
				catch (Exception e) {
					// TODO
					e.printStackTrace();
					throw new Exception("aa");
				}
				finally {
					@SuppressWarnings("unused")
					boolean ret = fz.delete();
				}
			}
			else {
				throw new Exception("There is no data. id = " + binaryFileIds);
			}
		}
		catch (Exception e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}

		return null;
	}


	/**
	 * ファイル削除処理
	 * @param model
	 * @param binaryFileIds
	 * @return
	 */
	@PostMapping({ PREFIX + "/remove" })
	@Action(Type.AJAX)
	@ResponseBody
	public JsonResponse remove(Model model, @RequestBody List<Long> binaryFileIds) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			
			// 結果用クラスオブジェクト
			class RemoveResult extends JsonResponse{
				@Getter
				@Setter
				private List<Long> binaryFileIds = null;
			}
			RemoveResult result = new RemoveResult();

			List<Long> deleteBinaryFileIds = storageFileService.remove(logger, sessionuser.getId(), binaryFileIds);
			result.setBinaryFileIds(deleteBinaryFileIds);
			return result;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	@PostMapping({ PREFIX + "/move" })
	@Action(Type.AJAX)
	@ResponseBody
	public JsonResponse move(Model model, @RequestParam(name = "binaryFileCategoryId", required = true) Long binaryFileCategoryId, @RequestBody List<Long> binaryFileIds) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// 結果用クラスオブジェクト
			class MoveResult extends JsonResponse{
				@Getter
				@Setter
				private List<Long> binaryFileIds = null;
			}
			MoveResult result = new MoveResult();
			
			List<Long> moveBinaryFileIds = storageFileService.moveFiles(logger, sessionuser.getId(), binaryFileCategoryId, binaryFileIds);
			result.setBinaryFileIds(moveBinaryFileIds);
			return result;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	/**
	 * カテゴリ編集処理
	 * @param model
	 * @param categories
	 * @return
	 */
	@PostMapping({ PREFIX + "/editCategory" })
	@Action(Type.AJAX)
	@ResponseBody
	public JsonResponse editCategory(Model model, @RequestBody List<Map<String, String>> categories) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			JsonResponse result = new JsonResponse();
	
			List<BinaryFileCategory> binaryFileCategories = new ArrayList<>();
			
			for(Map<String, String>category : categories) {
				BinaryFileCategory binaryFileCategory = new BinaryFileCategory();
				if(category.get("id").length()> 0) {
					binaryFileCategory.setId(Long.parseLong(category.get("id")));
				}
	
				if(category.get("category_name") == null || category.get("category_name").length() == 0) {
					binaryFileCategory.setCategoryName(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()));
				}
				else {
					binaryFileCategory.setCategoryName(category.get("category_name"));
				}
	
				if(category.get("sort_order") == null || category.get("sort_order").length() == 0) {
					binaryFileCategory.setSortOrder(999);
				}
				else {
					binaryFileCategory.setSortOrder(Integer.parseInt(category.get("sort_order")));
				}
	
				if(category.get("is_delete") == null || category.get("is_delete").length() == 0) {
					binaryFileCategory.setIsDelete(false);
				}
				else {
					binaryFileCategory.setIsDelete(Boolean.parseBoolean(category.get("is_delete")));
				}
				binaryFileCategories.add(binaryFileCategory);
			}
			storageFileService.editCategory(logger, sessionuser.getId(), binaryFileCategories);
	
			return result;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	

	/**
	 * バイナリファイルの削除確認処理
	 * 他の機能で利用されているファイルは削除できない
	 * @return
	 */
	@GetMapping({ PREFIX + "/canRemoveFile" })
	@Action(Type.AJAX)
	@ResponseBody
	public JsonResponse canRemoveFile(Model model, @RequestParam(name = "binaryFileIds", required = true) List<Long> binaryFileIds) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {

			// 結果用クラスオブジェクト
			class MoveResult extends JsonResponse{
				@Getter
				@Setter
				private List<String> fileNames = null;
			}
			MoveResult result = new MoveResult();
			
			List<StorageFile> storageFiles = storageFileService.isUsedFile(logger, sessionuser.getId(), binaryFileIds);
			if(storageFiles.size() > 0) {
				result.setResult(ResponseCode.IMPOSSIBLE); // 削除不可
				List<String> fileNames = new ArrayList<>();
				for(StorageFile storageFile : storageFiles) {
					fileNames.add(storageFile.getBinaryFile().getOriginalFileName());
				}
				result.setFileNames(fileNames);
			}
			return result;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	/**
	 * カテゴリの削除確認処理
	 * 他の機能で利用されているファイルは削除できない
	 * @return
	 */
	@GetMapping({ PREFIX + "/canRemoveCategory" })
	@Action(Type.AJAX)
	@ResponseBody
	public JsonResponse canRemoveCategory(Model model, @RequestParam(name = "binaryFileCategoryIds", required = true) List<Long> binaryFileCategoryIds) {
		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			// 結果用クラスオブジェクト
			class MoveResult extends JsonResponse{
				@Getter
				@Setter
				private List<String> fileNames = null;
			}
			MoveResult result = new MoveResult();
	
			List<StorageFile> storageFiles = storageFileService.isUsedCategory(logger, sessionuser.getId(), binaryFileCategoryIds);
			if(storageFiles.size() > 0) {
				result.setResult(ResponseCode.IMPOSSIBLE); // 削除不可
				List<String> fileNames = new ArrayList<>();
				for(StorageFile storageFile : storageFiles) {
					fileNames.add(storageFile.getBinaryFile().getOriginalFileName());
				}
				result.setFileNames(fileNames);
			}
			return result;
		}
		catch(Throwable e) {
			throw new ApplicationRuntimeException("An error has occurred in " + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() .", e);
		}
	}
	
	
	/**
	 * ZIP用のファイル名を取得
	 * 重複しているファイル名がある場合は(n)を追加する
	 * @param fileName ファイル名
	 * @param checkFileNames ファイル名リスト
	 * @return ZIP用ファイル名
	 */
	private String getZipFileName(String fileName, List<Map<String, Object>> checkFileNames){
		String ret = fileName;

		boolean find=false;
		for(Map<String, Object> checkFileName : checkFileNames) {
			if(((String)checkFileName.get("name")).equals(fileName)){
				// 一致
				Integer count = (Integer)checkFileName.get("count"); 
				count++;
				checkFileName.put("count", count);
				// 拡張子の前にインデックスを追加
				String prefix = "";
				String extention = "";
				int dot = fileName.lastIndexOf(".");
				if (dot > 0) {
					prefix = fileName.substring(0, dot);
					extention = fileName.substring(dot);
					ret = prefix + "(" + count + ")" + extention;
				}
				else {
					ret = fileName + "(" + count + ")";
				}
				find=true;
			}
		}
		if(!find) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", fileName);
			map.put("count", 1);
			checkFileNames.add(map);
		}
		
		return ret;
	}
}
