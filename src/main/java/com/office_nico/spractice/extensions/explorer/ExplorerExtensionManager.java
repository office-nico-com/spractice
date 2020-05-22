package com.office_nico.spractice.extensions.explorer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.office_nico.spractice.extensions.ExtensionManager;
import com.office_nico.spractice.extensions.explorer.domain.ExtensionExplorerProperty;
import com.office_nico.spractice.extensions.explorer.repository.ExtensionExplorerPropertyRepository;
import com.office_nico.spractice.repository.extension.ExtensionRepository;

@Component
public class ExplorerExtensionManager implements ExtensionManager{

	@Autowired
	private ExtensionExplorerPropertyRepository extensionExplorerRepository = null;
	
	@Override
	public String getSpecificProperty(Long extensionId, String extensionName) {
		
		String json = null;
		try {
	
			List<ExtensionExplorerProperty> extensionExplorerProperties = extensionExplorerRepository.findByExtensionIdAndParentIdIsNullOrderByOrderNumber(extensionId);
			for(ExtensionExplorerProperty extensionExplorerProperty : extensionExplorerProperties) {
				_getExplorerProperties(extensionExplorerProperty);
			}
			ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
			json = mapper.writeValueAsString(extensionExplorerProperties);
		}
		catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		String ret = extensionName + ".resources = " + json; 
		
		return ret;
	}
	
	private void _getExplorerProperties(ExtensionExplorerProperty parent){
		List<ExtensionExplorerProperty> extensionExplorerProperties = extensionExplorerRepository.findByParentIdOrderByOrderNumber(parent.getId());
		if(extensionExplorerProperties.size() > 0) {
			for(ExtensionExplorerProperty extensionExplorerProperty : extensionExplorerProperties) {
				_getExplorerProperties(extensionExplorerProperty);
			}
			parent.setExtensionExplorerProperties(extensionExplorerProperties);
		}
	}
}
