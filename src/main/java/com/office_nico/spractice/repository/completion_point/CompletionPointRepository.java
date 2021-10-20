package com.office_nico.spractice.repository.completion_point;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.CompletionPoint;



@Repository
@Transactional
public interface CompletionPointRepository extends JpaRepository<CompletionPoint,Long>, CompletionPointDao<CompletionPoint> {

	
	public Page<CompletionPoint> findByIsDeletedFalse(Pageable pr);

	public List<CompletionPoint> findByIsDeletedFalseOrderByCompletionPointKeycode();

	public CompletionPoint findTopByIsDeletedFalseAndIsInvalidedFalseAndCompletionPointKeycode(String completionPointKeycode);


	public Long countByIsDeletedFalseAndCompletionPointKeycode(String completionPointKeycode);

	public Long countByIsDeletedFalseAndCompletionPointKeycodeAndIdNot(String senarioKeycode, Long completionId);

	@Query("SELECT cp FROM CompletionPoint cp where cp.isDeleted = false AND cp.isInvalided = false AND cp.id IN (?1)")
	public List<CompletionPoint> findByIsDeletedFalseAndIsInvalidedFalseAndId(List<Long> completionPointIds);

	@Query("SELECT cp FROM CompletionPoint cp where cp.isDeleted = false AND cp.id IN (?1)")
	public List<CompletionPoint> findByIsDeletedFalseAndId(List<Long> completionPointIds);

}
