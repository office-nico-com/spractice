package com.office_nico.spractice.repository.guidance_action;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.GuidanceAction;


@Repository
@Transactional
public interface GuidanceActionRepository extends JpaRepository<GuidanceAction,Long>, GuidanceActionDao<GuidanceAction> {

	

	public GuidanceAction findTopByGuidanceIdAndSortOrder(Long guidanceId, Integer sortOrder);
	
	public List<GuidanceAction> findByGuidanceId(Long guidanceId);

	public List<GuidanceAction> findByGuidanceIdOrderBySortOrder(Long guidanceId);

}
