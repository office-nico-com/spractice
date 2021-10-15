package com.office_nico.spractice.repository.client;

import java.io.Serializable;
import java.util.List;

import com.office_nico.spractice.domain.Client;

public interface ClientDao <T> extends Serializable {

	public List<Client> findClientByUserId(Long userId);
	
}

