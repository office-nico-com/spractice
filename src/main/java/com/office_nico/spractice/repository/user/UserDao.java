package com.office_nico.spractice.repository.user;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import com.office_nico.spractice.domain.User;

public interface UserDao <T> extends Serializable {

	Page<User> findUsersBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] order, String direction, Integer offset, Integer limit);

}

