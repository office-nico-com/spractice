package com.office_nico.spractice.repository.client_scenario;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.ClientScenario;



@Repository
@Transactional
public interface ClientScenarioRepository extends JpaRepository<ClientScenario,Long>, ClientScenarioDao<ClientScenario> {

	public List<ClientScenario> findByClientId(Long clientId);

}
