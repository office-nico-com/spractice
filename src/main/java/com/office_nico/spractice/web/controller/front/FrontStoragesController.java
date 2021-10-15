package com.office_nico.spractice.web.controller.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office_nico.spractice.annotation.Action;
import com.office_nico.spractice.annotation.Action.Type;
import com.office_nico.spractice.exception.ApplicationRuntimeException;
import com.office_nico.spractice.service.StorageFileService;
import com.office_nico.spractice.service.data.SessionData;
import com.office_nico.spractice.service.data.StorageFile;

@Controller
public class FrontStoragesController {

	
	private static final String PREFIX = "front/storages";

	private static final Logger logger = LoggerFactory.getLogger(FrontStoragesController.class);

	@Autowired
	private StorageFileService storageFileService = null;

	@Autowired
	private SessionData sessionData = null;
	
	/**
	 * 画像表示処理
	 * @param model
	 * @param binaryFileId
	 * @return
	 */
	@GetMapping({ PREFIX + "/{binaryFileId}" })
	@Action(Type.AJAX)
	@ResponseBody
	public HttpEntity<byte[]> image(Model model, @PathVariable(name = "binaryFileId", required = true) Long binaryFileId) {

		try {

			if(sessionData.getUserId() == null) {
				return null;
			}

			StorageFile storageFile = storageFileService.get(logger, sessionData.getUserId(), binaryFileId);
			if (storageFile.getResult() != StorageFile.Result.SUCCESS) {
				return null;
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
}

