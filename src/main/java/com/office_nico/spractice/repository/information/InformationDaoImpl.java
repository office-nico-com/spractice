package com.office_nico.spractice.repository.information;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.office_nico.spractice.domain.Information;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.util.BeanUtil;


@Repository
public class InformationDaoImpl implements InformationDao<Information> {
	
	private static final long serialVersionUID = -4353538069392940590L;

	@PersistenceContext
	private EntityManager entityManager = null;

	@SuppressWarnings("rawtypes")
	@Override
	public List<Information> findByIsShowDashboardANdIsDeletedFalseAndShowStartedGteTodayOnAndShowEndedOnLteToday(LocalDate today) {

		List<Map> records =  entityManager.createQuery("SELECT new map(i.id as id, i.message as message, i.postedAt as posted_at, i.isShowDashboard as is_show_dashboard, i.showStartedOn as show_started_on, i.showEndedOn as show_ended_on, u.familyName as family_name, u.givenName as given_name) FROM Information i join i.user u where i.isShowDashboard = true and i.isDeleted = false and (i.showStartedOn is null or i.showStartedOn <= :arg1) and (i.showEndedOn is null or i.showEndedOn >= :arg1) order by i.postedAt desc", Map.class).setParameter("arg1", today).getResultList();
		List<Information> ret = new ArrayList<>();
		for(Map map : records) {
			@SuppressWarnings("unchecked")
			Information i = BeanUtil.mapToBean(map, new Information());
			@SuppressWarnings("unchecked")
			User u = BeanUtil.mapToBean(map, new User());
			i.setUser(u);
			ret.add(i);
		}
		return ret;
	}
	
}
