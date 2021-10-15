package com.office_nico.spractice.repository.client_user;

import java.io.Serializable;
import java.util.List;

import com.office_nico.spractice.domain.ClientUser;

public interface ClientUserDao <T> extends Serializable {

	List<ClientUser> findClientByUserId(Long userId);

}

