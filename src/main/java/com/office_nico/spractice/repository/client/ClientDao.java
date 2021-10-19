package com.office_nico.spractice.repository.client;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.office_nico.spractice.domain.Client;

public interface ClientDao <T> extends Serializable {

	public List<Client> findClientByUserId(Long userId);

	Page<Client> findClientsBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit);
	
}

