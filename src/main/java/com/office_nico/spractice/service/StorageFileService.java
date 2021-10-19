package com.office_nico.spractice.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.BinaryFile;
import com.office_nico.spractice.domain.BinaryFileCategory;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.exception.AppRunnableException;
import com.office_nico.spractice.repository.binary_file.BinaryFileRepository;
import com.office_nico.spractice.repository.binary_file_category.BinaryFileCategoryRepository;
import com.office_nico.spractice.service.annotation.ValidateSession;
import com.office_nico.spractice.service.data.StorageFile;
import com.office_nico.spractice.service.data.StorageFile.Result;

/**
 * ファイルストレージサービス
 */
@Service
@Transactional
@ValidateSession
public class StorageFileService {

	private static final Logger _logger = LoggerFactory.getLogger(StorageFileService.class);
	
	@Value("${upload.file.save.path}") 
	private String savePath = null;

	@Autowired 
	private BinaryFileCategoryRepository binaryFileCategoryRepository = null;

	@Autowired 
	private BinaryFileRepository binaryFileRepository = null;

	@Autowired
	private LogService logService = null;

	
	/**
	 * バイナリファイルカテゴリの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @return BinaryFileCategoryオブジェクト配列
	 */
	public List<BinaryFileCategory> getCategories(Logger logger, Long sessionUserId){
		logger = (logger == null ? logger : _logger);

		List<BinaryFileCategory> list = binaryFileCategoryRepository.findByOrderBySortOrder();

		return list;
	}

	/**
	 * バイナリファイル一覧の取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param binaryFileCategoryId バイナリファイルカテゴリID
	 * @return StorageFileオブジェクト配列
	 */
	public List<StorageFile> list(Logger logger, Long sessionUserId, Long binaryFileCategoryId) {
		logger = (logger == null ? logger : _logger);
		
		Optional<BinaryFileCategory> binaryFileCategory = null;
		
		if(binaryFileCategoryId == null) {
			binaryFileCategory = binaryFileCategoryRepository.getOrderByFirstSortOrder();
		}
		else {
			binaryFileCategory = binaryFileCategoryRepository.findById(binaryFileCategoryId);
		}
		
		if(binaryFileCategory.isEmpty()) {
			throw new NullPointerException();
		}
		
		List<StorageFile> list = new ArrayList<>();
		List<BinaryFile> binaryFiles = binaryFileCategory.get().getBinaryFiles();
		for(BinaryFile binaryFile : binaryFiles) {
			StorageFile storageFile = new StorageFile();
			storageFile.setBinaryFile(binaryFile);
			storageFile.setResult(Result.SUCCESS);
			String saveFullPath = savePath + "/" + binaryFile.getSubPath() + "/" + binaryFile.getSaveFileName();
			storageFile.setSavePath(saveFullPath);
			list.add(storageFile);
		}
		return list;
	}
	
	
	/**
	 * バイナリファイルの保存
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param body ファイルボディ
	 * @param originalFileName ファイル名
	 * @param lastModifiedAt 最終更新日時
	 * @param binaryFileCategoryId バイナリファイルカテゴリID
	 * @param mimeType MIMEタイプ
	 * @return StorageFileオブジェクト
	 */
	public StorageFile save(Logger logger, Long sessionUserId, byte[] body, String originalFileName, LocalDateTime lastModifiedAt, Long binaryFileCategoryId, String mimeType) {
		logger = (logger == null ? logger : _logger);

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		StorageFile storageFile = new StorageFile();
		storageFile.setResult(Result.SUCCESS);
		
		// ローカルディスク上に保存
		String filename = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
		
		// 二桁の乱数を付加
		Random rand = new Random();
		int r = rand.nextInt(90) + 10;
		filename += r;

		int dot = originalFileName.lastIndexOf(".");
		String extention = "";
		if (dot > 0) {
			extention = originalFileName.substring(dot).toLowerCase();
		}

		String subPath = DateTimeFormatter.ofPattern("yyyyMM").format(LocalDateTime.now());
		
		File dir = new File(savePath + "/" + subPath);
		if(!dir.exists()) {
			if(!dir.mkdirs()) {
				throw new AppRunnableException(logger, -9999, "Create directory error. " + dir.getPath());
			}
		}
		
		String saveFileName = filename + extention;
		String saveFullPath = savePath + "/" + subPath + "/" + filename + extention;
		Path uploadfile = Paths.get(saveFullPath);
		try (OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
			os.write(body);
		
			// DB上にレコード作成
			BinaryFile binaryFile = new BinaryFile();
			BinaryFileCategory binaryFileCategory = new BinaryFileCategory();
			binaryFileCategory.setId(binaryFileCategoryId);
			binaryFile.setBinaryFileCategory(binaryFileCategory);
			binaryFile.setOriginalFileName(originalFileName);
			binaryFile.setSaveFileName(saveFileName);
			binaryFile.setSubPath(subPath);
			binaryFile.setFileSize(Integer.valueOf(body.length).longValue());
			binaryFile.setMimeType(mimeType);
			if(lastModifiedAt == null) {
				lastModifiedAt = LocalDateTime.now();
			}
			binaryFile.setLastModifiedAt(lastModifiedAt);
			binaryFile.setUpdatedBy(sessionuser.getId());
			binaryFile.setCreatedBy(sessionuser.getId());
			binaryFile.setUpdatedAt(LocalDateTime.now());
			binaryFile.setCreatedAt(LocalDateTime.now());

			binaryFile = binaryFileRepository.save(binaryFile);
			storageFile.setBinaryFile(binaryFile);
			storageFile.setSavePath(saveFullPath);
		}
		catch (IOException ex) {
			// TODO:
			System.err.println(ex);
			 storageFile.setResult(Result.ERROR_SAVE);
		}
		
		return storageFile;
	}


	
	/**
	 * バイナリファイルの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param binaryFileId バイナリファイルID
	 * @return StorageFileオブジェクト
	 */
	public StorageFile get(Logger logger, Long sessionUserId, Long binaryFileId) {
		logger = (logger == null ? logger : _logger);

		StorageFile storageFile = new StorageFile();
		storageFile.setResult(Result.SUCCESS);
		
		Optional<BinaryFile> binaryFile = binaryFileRepository.findById(binaryFileId);

		storageFile.setBinaryFile(binaryFile.get());
		String saveFullPath = savePath + "/" + binaryFile.get().getSubPath() + "/" + binaryFile.get().getSaveFileName();
		storageFile.setSavePath(saveFullPath);
		
		return storageFile;
	}
	
	/**
	 * バイナリファイルの削除
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param binaryFileIds バイナリファイルID配列
	 * @return 削除したバイナリファイルID配列
	 */
	public List<Long> remove(Logger logger, Long sessionUserId, List<Long> binaryFileIds){
		logger = (logger == null ? logger : _logger);

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<Long> deleteBinaryFileIds = new ArrayList<>();
		
		for(Long binaryFileId : binaryFileIds) {
			StorageFile storageFile = get(logger, sessionUserId, binaryFileId);
			if(storageFile.getResult() == Result.SUCCESS) {
				deleteBinaryFileIds.add(storageFile.getBinaryFile().getId());
				// ファイルを削除
				File f = new File(storageFile.getSavePath());
				if(!f.delete()) {
					logService.warn(logger, sessionuser.getEmail(), "Failed to delete file." + storageFile.getSavePath());
				}
				// データを削除
				binaryFileRepository.delete(storageFile.getBinaryFile());
			}
		}
		return deleteBinaryFileIds;
	}
	
	
	/**
	 * カテゴリの移動
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param moveToBinaryFileCategoryId 移動先バイナリファイルカテゴリID
	 * @param binaryFileIds バイナリファイルID
	 * @return 移動したバイナリファイルID配列
	 */
	public List<Long> moveFiles(Logger logger, Long sessionUserId, Long moveToBinaryFileCategoryId, List<Long> binaryFileIds){
		logger = (logger == null ? logger : _logger);

		List<Long> moveBinaryFileIds = new ArrayList<>();
		
		// 移動先のカテゴリの存在チェック
		if(binaryFileCategoryRepository.countById(moveToBinaryFileCategoryId) == 0) {
			return moveBinaryFileIds;
		}
		
		for(Long binaryFileId : binaryFileIds) {
			StorageFile storageFile = get(logger, sessionUserId, binaryFileId);
			if(storageFile.getResult() == Result.SUCCESS) {
				// カテゴリを移動
				if(!storageFile.getBinaryFile().getBinaryFileCategory().getId().equals(moveToBinaryFileCategoryId)) {
					BinaryFileCategory binaryFileCategory = new BinaryFileCategory();
					binaryFileCategory.setId(moveToBinaryFileCategoryId);
					storageFile.getBinaryFile().setBinaryFileCategory(binaryFileCategory);
					binaryFileRepository.save(storageFile.getBinaryFile());
					moveBinaryFileIds.add(storageFile.getBinaryFile().getId());
				}
			}
		}
		return moveBinaryFileIds;
	}
	
	/**
	 * カテゴリの編集
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param binaryFileCategories バイナリファイルカテゴリオブジェクト配列
	 */
	public void editCategory(Logger logger, Long sessionUserId, List<BinaryFileCategory> binaryFileCategories) {
		logger = (logger == null ? logger : _logger);

		User sessionuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// まずは新規追加
		for(BinaryFileCategory binaryFileCategory: binaryFileCategories) {
			if(binaryFileCategory.getId() == null) {
				// 新規登録
				binaryFileCategory.setCanDelete(true);
				binaryFileCategory.setUpdatedBy(sessionuser.getId());
				binaryFileCategory.setCreatedBy(sessionuser.getId());
				binaryFileCategory.setUpdatedAt(LocalDateTime.now());
				binaryFileCategory.setCreatedAt(LocalDateTime.now());
				binaryFileCategoryRepository.save(binaryFileCategory);
			}
			else if(binaryFileCategory.getIsDelete() != null && binaryFileCategory.getIsDelete().equals(true)) {
				// 削除
				Optional<BinaryFileCategory> _binaryFileCategory = binaryFileCategoryRepository.findById(binaryFileCategory.getId());
				if(!_binaryFileCategory.isEmpty() && _binaryFileCategory.get().getCanDelete().equals(true)) {

					List<Long> deleteFileId = new ArrayList<>();
					for(BinaryFile binaryFile : _binaryFileCategory.get().getBinaryFiles()) {
						deleteFileId.add(binaryFile.getId());
					}
					// ファイルの削除
					remove(logger, sessionUserId, deleteFileId);
					binaryFileCategoryRepository.delete(_binaryFileCategory.get());
				}
			}
			else {
				// 更新
				Optional<BinaryFileCategory> _binaryFileCategory = binaryFileCategoryRepository.findById(binaryFileCategory.getId());
				if(!_binaryFileCategory.isEmpty()) {
					_binaryFileCategory.get().setCategoryName(binaryFileCategory.getCategoryName());
					_binaryFileCategory.get().setSortOrder(binaryFileCategory.getSortOrder());
					binaryFileCategory.setUpdatedBy(sessionuser.getId());
					binaryFileCategory.setUpdatedAt(LocalDateTime.now());
					binaryFileCategoryRepository.save(_binaryFileCategory.get());
				}
			}
		}
	}
	
	/**
	 * バイナリファイルの利用状況チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param binaryFileIds バイナリファイルID
	 * @return 利用されているStorageFileオブジェクト配列
	 */
	public List<StorageFile> isUsedFile(Logger logger, Long sessionUserId, List<Long> binaryFileIds){
		logger = (logger == null ? logger : _logger);

		List<StorageFile> ret = new ArrayList<>();
		
		/*
		for(Long binaryFileId : binaryFileIds) {
			StorageFile storageFile = get(logger, sessionUserId, binaryFileId);
			// TODO:チェック処理
			// ret.add(storageFile);
		}
		*/
		return ret;
	}

	/**
	 * バイナリファイルカテゴリの利用状況チェック
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param binaryFileCategoryIds バイナリファイルカテゴリID配列
	 * @return 利用されているStorageFileオブジェクト配列
	 */
	public List<StorageFile> isUsedCategory(Logger logger, Long sessionUserId, List<Long> binaryFileCategoryIds){
		logger = (logger == null ? logger : _logger);

		List<StorageFile> ret = new ArrayList<>();
		for(Long binaryFileCategoryId : binaryFileCategoryIds) {
				Optional<BinaryFileCategory> binaryFileCategory = binaryFileCategoryRepository.findById(binaryFileCategoryId);
				if(!binaryFileCategory.isEmpty()) {
					
					List<Long> binaryFileIds = new ArrayList<>();
					for(BinaryFile binaryFile : binaryFileCategory.get().getBinaryFiles()) {
						binaryFileIds.add(binaryFile.getId());
					}
					List<StorageFile> storageFiles = isUsedFile(logger, sessionUserId, binaryFileIds);
					ret.addAll(storageFiles);
				}
		}
		return ret;
	}
}
