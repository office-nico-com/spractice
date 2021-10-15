package com.office_nico.spractice.repository.information;


import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Information;

@Repository
@Transactional
public interface InformationRepository extends JpaRepository<Information,Long>, InformationDao<Information> {

	// offset limit Pagableを使用するパターン
	@Query(value="SELECT new map(i.id as id, i.message as message, to_char(i.postedAt, 'yyyy/MM/DD HH24:mm') as postedAt, i.isShowDashboard as isShowDashboard, to_char(i.showStartedOn , 'yyyy/MM/DD') as showStartedOn, to_char(i.showEndedOn , 'yyyy/MM/DD') as showEndedOn, u.familyName as familyName, u.givenName as givenName) FROM Information i join i.user u where i.isDeleted = false order by postedAt",
			countQuery="SELECT COUNT(*) FROM Information i where i.isDeleted = false order by postedAt")
	public Page<Map<String, Object>> findByIsDeletedFalse(Pageable pr);

	/*
	@Query("SELECT i FROM Information i where i.isShowDashboard = true and i.isDeleted = false and (i.showStartedOn is null or i.showStartedOn >= ?1) and (i.showEndedOn is null or i.showEndedOn <= ?1) order by postedAt")
	public List<Information> findByIsShowDashboardANdIsDeletedFalseAndShowStartedGteTodayOnAndShowEndedOnLteToday(Date today);
	*/
}
