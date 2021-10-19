package com.office_nico.spractice.repository.completion_point;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.DateTimeUtil;
import com.office_nico.spractice.util.NativeQueryBuilder;

@Repository
public class CompletionPointDaoImpl implements CompletionPointDao<CompletionPoint> {

	private static final long serialVersionUID = -5212010669049551184L;

	@PersistenceContext
	private EntityManager entityManager = null;


	@Override
	@SuppressWarnings("unchecked")
	public List<CompletionPoint> findByUserIdAndClientIdAndCompletionPointIds(Long userId, Long clientId, List<Long> completionPointIds) {

		List<CompletionPoint> ret = new ArrayList<>();
		;

		if (completionPointIds == null || completionPointIds.size() == 0) {
			return ret;
		}

		List<Object[]> records = entityManager.createNativeQuery(
						"SELECT " 
								+ "completion_points.id, " 
								+ "completion_points.completion_point_keycode, " 
								+ "completion_points.description, " 
								+ "completion_points.description_en, " 
								+ "t1.last_completed_at " 
								+ "FROM completion_points "
								+ "LEFT OUTER JOIN (SELECT completion_point_id, MAX(ended_at) AS last_completed_at FROM completions WHERE user_id = :arg2 AND client_id = :arg3 AND ended_at IS NOT NULL GROUP BY completion_point_id) AS t1 "
								+ "ON (t1.completion_point_id = completion_points.id) " 
								+ "WHERE completion_points.id IN ( :arg1 ) " 
								+ "AND is_invalided = false "  // TODO:履修履歴の方は無効を見せるが、履修状況の方は無効は表示しない
								+"AND is_deleted = false " + "ORDER BY completion_point_keycode ")
				.setParameter("arg1", completionPointIds)
				.setParameter("arg2", userId)
				.setParameter("arg3", clientId)
				.getResultList();

		for (Object[] obj : records) {
			CompletionPoint completionPoint = new CompletionPoint();
			completionPoint.setId(Long.valueOf(String.valueOf((BigInteger) obj[0])));
			completionPoint.setCompletionPointKeycode((String) obj[1]);
			completionPoint.setDescription((String) obj[2]);
			completionPoint.setDescriptionEn((String) obj[3]);
			Timestamp t = (Timestamp) obj[4];
			if (t != null) {
				completionPoint.setLastCompletedAt(DateTimeUtil.toLocalDateTime(t));
			}
			ret.add(completionPoint);
		}
		return ret;
	}
	
	@Override
	public Page<CompletionPoint> findCompletionPointsBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit){
		
		NativeQueryBuilder query  = NativeQueryBuilder.newSql(entityManager);

		query.append("SELECT DISTINCT");
		query.appendField("completion_points.id");
		query.appendField("completion_points.completion_point_keycode");
		query.appendField("completion_points.description");
		query.appendField("completion_points.description_en");
		query.appendField("completion_points.is_invalided");
		query.appendField("completion_points.is_deleted");
		query.append("FROM completion_points");
		query.append("LEFT OUTER JOIN guidances ON (guidances.start_completion_point_id = completion_points.id OR guidances.end_completion_point_id = completion_points.id)");
		query.append("LEFT OUTER JOIN scenarios ON (scenarios.id = guidances.scenario_id AND scenarios.is_deleted = false)");
		query.append("WHERE completion_points.is_deleted = false ");
		if(searchKeyword != null && searchKeyword.length() > 0) {
			searchKeyword = "%" + searchKeyword + "%";
			query.append("AND (");
			query.append("completion_points.completion_point_keycode LIKE ?", searchKeyword);
			query.append("OR completion_points.description LIKE ?", searchKeyword);
			query.append("OR completion_points.description_en LIKE ?", searchKeyword);
			query.append("OR scenarios.scenario_keycode LIKE ?", searchKeyword);
			query.append("OR scenarios.scenario_name LIKE ?", searchKeyword);
			query.append(")");
		}

		for(String order : orders) {
			query.addOrder(order, direction);
		}
		query.offset(offset);
		query.limit(limit);

		List<Map<String, Object>> results = query.getResults();
		Integer count = query.getCount();
		
		
		List<CompletionPoint> completionPoints = new ArrayList<>();
		for(Map<String, Object> result : results) {
			CompletionPoint completionPoint = BeanUtil.mapToBean(result, new CompletionPoint());
			completionPoints.add(completionPoint);
		}
		
		int pageNumber = offset == 0 ? 0 : (offset / limit);
		Pageable pr = PageRequest.of(pageNumber, limit);

		Page<CompletionPoint> p = new PageImpl<CompletionPoint>(completionPoints, pr, count);
		return p;
	}
}
