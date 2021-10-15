package com.office_nico.spractice.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.repository.user.UserRepository;

@Service
@Transactional
public class AuthService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository = null;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null || username.equals("")) {
			throw new UsernameNotFoundException("");
		}

		User user = userRepository.findFirstByAccountAndIsDeletedFalseAndIsInvalidedFalse(username);
		if (user == null) {
			throw new UsernameNotFoundException("");
		}
		return user;
	}
}
