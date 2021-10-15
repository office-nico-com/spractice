package com.office_nico.spractice.repository.completion_point;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.util.DateTimeUtil;

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
}
