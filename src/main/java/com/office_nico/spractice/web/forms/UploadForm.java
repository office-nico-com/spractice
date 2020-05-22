package com.office_nico.spractice.web.forms;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadForm {
	private MultipartFile file = null;;
	private String lastModifiedDate = null;
	private String lastModifiedTime = null;
	private String mimeType = null;
	private Long binaryFileCategoryId = null;
}

