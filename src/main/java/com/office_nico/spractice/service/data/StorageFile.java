package com.office_nico.spractice.service.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.office_nico.spractice.domain.BinaryFile;

import lombok.Data;

@Data
public class StorageFile {
	public enum Result {
		SUCCESS, // 保存成功・取得成功
		ERROR_SAVE, // ファイルの保存に失敗
		ERROR_MKDIR, // ディレクトリの作成に失敗
		ERROR_NOIMAGE, // ファイルなし
	}

	// 結果コード
	private Result result = null;
	private BinaryFile binaryFile = null;
	private String savePath = null;

	/**
	 * 拡張子の取得
	 * @return
	 */
	public String getExtention() {
		String extention = "other";
		if(binaryFile != null) {
			int dot = binaryFile.getOriginalFileName().lastIndexOf(".");
			if (dot > 0) {
				extention = binaryFile.getOriginalFileName().substring(dot + 1).toLowerCase();
			}
		}
		return extention;
	}

	/**
	 * ファイルボディの取得
	 * @return
	 */
	public byte[] getBody() {

		byte[] body = null;
		if (savePath != null) {
			Path file = Paths.get(savePath);
			try {
				body = Files.readAllBytes(file);
			}
			catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return body;
	}
}
