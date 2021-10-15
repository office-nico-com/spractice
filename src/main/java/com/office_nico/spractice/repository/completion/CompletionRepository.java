package com.office_nico.spractice.repository.completion;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Completion;



@Repository
@Transactional
public interface CompletionRepository extends JpaRepository<Completion,Long>, CompletionDao<Completion> {

	
	public Completion findTopByClientIdAndUserIdAndCompletionPointIdAndTransaction(Long clientId, Long userId, Long completionPointId, String transaction);
	

}
