package com.office_nico.spractice.repository.client_user;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.ClientUser;



@Repository
@Transactional
public interface ClientUserRepository extends JpaRepository<ClientUser,Long>, ClientUserDao<ClientUser> {

	public ClientUser findTopByClientIdAndUserId(Long clientId, Long userId);

	public List<ClientUser> findByUserId(Long userId);
	

}
