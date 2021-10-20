package com.office_nico.spractice.repository.completion;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Completion;
import com.office_nico.spractice.domain.CompletionPoint;
import com.office_nico.spractice.service.data.AggregateData;
import com.office_nico.spractice.service.data.CompletionUser;
import com.office_nico.spractice.util.BeanUtil;
import com.office_nico.spractice.util.DateTimeUtil;

@Repository
public class CompletionDaoImpl implements CompletionDao<Completion> {

	private static final long serialVersionUID = 2228807948595494597L;

	@PersistenceContext
	private EntityManager entityManager = null;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Completion> findByUserIdAndClientIdAndCompletionPointIds(Long userId, Long clientId, List<Long> completionPointIds, Integer limit) {

		List<Completion> ret = new ArrayList<>();;
		
		if(completionPointIds == null || completionPointIds.size() == 0) {
			return ret;
		}
		
		List<Map> records =  entityManager.createQuery(
				"select new map("
				+ "c.id as id, "
				+ "c.clientId as client_id, "
				+ "c.userId as user_id, "
				+ "c.completionPoint.id as completion_point_id, "
				+ "c.completionPoint.description as description, "
				+ "c.completionPoint.descriptionEn as description_en, "
				+ "c.transaction as transaction, "
				+ "c.startedAt as started_at, "
				+ "c.endedAt as ended_at, "
				+ "c.createdAt as created_at, "
				+ "c.updatedAt as updated_at "
				+ ") "
				+ "from Completion c join c.completionPoint cp "
				+ "where c.completionPoint.id in (:arg1) "
				+ "and c.userId = :arg2 "
				+ "and c.clientId = :arg3 "
				+ "and cp.isDeleted = false "			// TODO:無効の場合は履歴がみれてもいい。削除の場合はみせない、でいいか。
				+ "order by c.createdAt desc "
				, Map.class)
		.setParameter("arg1", completionPointIds)
		.setParameter("arg2", userId)
		.setParameter("arg3", clientId)
		.setMaxResults(limit)
		.getResultList();


		for(Map<String, Object> map : records) {
			
			Completion completion = BeanUtil.mapToBean(map, new Completion(), "completionPointId", "description", "descriptionEn");
			CompletionPoint completionPoint = new CompletionPoint();
			completionPoint.setId((Long)map.get("completion_point_id"));
			completionPoint.setDescription((String)map.get("description"));
			completionPoint.setDescriptionEn((String)map.get("description_en"));
			completion.setCompletionPoint(completionPoint);

			ret.add(completion);
		}
		return ret;
	}
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<AggregateData> calcWorkTimesByCompletionPointIds(List<Long> completionPointIds, Long clientId) {
		List<AggregateData> aggregates = new ArrayList<>();
		List<Object[]> records = entityManager.createNativeQuery(
			"SELECT "
			+ "completions.completion_point_id as id, "
			+ "cast(max(extract(epoch from completions.ended_at)-extract(epoch from completions.started_at)) as integer) as max, "
			+ "cast(min(extract(epoch from completions.ended_at)-extract(epoch from completions.started_at)) as integer) as min, "
			+ "cast(avg(extract(epoch from completions.ended_at)-extract(epoch from completions.started_at)) as integer) as avg "
			+ "FROM completions "
			+ "INNER JOIN users ON (users.id = completions.user_id AND users.is_deleted = false) "		// 削除は含めない、無効は含める
			+ "INNER JOIN clients_users ON (clients_users.user_id = users.id AND clients_users.client_id = :arg3 ) "
			+ "WHERE completions.completion_point_id in ( :arg1 ) "
			+ "AND completions.client_id = :arg2 "
			+ "AND (completions.started_at IS NOT NULL) "
			+ "AND (completions.ended_at IS NOT NULL) "
			+ "GROUP BY completions.completion_point_id " )
			.setParameter("arg1", completionPointIds)
			.setParameter("arg2", clientId)
			.setParameter("arg3", clientId)
			.getResultList();

		for(Object[] record : records) {
			AggregateData a = new AggregateData();
			a.setId(Long.valueOf(String.valueOf((BigInteger)record[0])));
			a.setMax((Integer)record[1]);
			a.setMin((Integer)record[2]);
			a.setAvg((Integer)record[3]);
			aggregates.add(a);
		}
		return aggregates;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public List<AggregateData> findCompletionCountsByCompletionPointIds(List<Long> completionPointId, Long clientId) {
		List<AggregateData> aggregates = new ArrayList<>();
		List<Object[]> records = entityManager.createNativeQuery(
			"SELECT "
					+ "completion_point_id, cast(count(*) as integer) as count FROM "
					+ "( "
					+ "  SELECT completions.user_id, completion_point_id FROM completions "
					+ "  INNER JOIN users ON (users.id = completions.user_id AND users.is_deleted = false) "  // 削除ユーザーは含めない
					+ "  INNER JOIN clients_users ON (clients_users.user_id = users.id AND clients_users.client_id = :arg3 ) "
					+ "  WHERE completions.completion_point_id in ( :arg1 ) "
					+ "  AND completions.client_id = :arg2 "
					+ "  AND (completions.started_at IS NOT NULL) "
					+ "  AND (completions.ended_at IS NOT NULL) "
					+ "  GROUP BY completions.user_id, completion_point_id "
					+ ") AS t1 "
					+ "GROUP BY t1.completion_point_id "
				)
			.setParameter("arg1", completionPointId)
			.setParameter("arg2", clientId)
			.setParameter("arg3", clientId)
			.getResultList();

		for(Object[] record : records) {
			AggregateData a = new AggregateData();
			a.setId(Long.valueOf(String.valueOf((BigInteger)record[0])));
			a.setCount((Integer)record[1]);
			aggregates.add(a);
		}
		return aggregates;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public Integer countUnworkedUserCountByCompletionPointIdAndClientId(Long completionPointId, Long clientId) {
		
		Integer ret = 0;
		
		List<Object> records = entityManager.createNativeQuery(
				"SELECT cast(count(*) as integer) AS unworked_count FROM ( "
						+ "SELECT t1.user_id, count(completions.*) AS count FROM "
						+ "  (SELECT users.id AS user_id FROM users INNER JOIN clients_users ON (clients_users.user_id = users.id) WHERE users.is_deleted = false AND clients_users.client_id = :arg2 ) t1 "
						+ "LEFT OUTER JOIN completions ON (completions.user_id = t1.user_id AND completions.completion_point_id = :arg1 AND completions.client_id = :arg3) "
						+ "GROUP BY t1.user_id HAVING count(completions.*) = 0 "
						+ ") t2 "
					)
				.setParameter("arg1", completionPointId)
				.setParameter("arg2", clientId)
				.setParameter("arg3", clientId)
				.getResultList();
	
		
		if(records.size() > 0) {
			ret = (Integer)records.get(0);
		}
		return ret;
	}

	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<CompletionUser> findByCompletionPointIdAndClientIdAndStartedAtNotNullAndEndedAtNotNullAndEndedAtFromAndEndedAtTo(Long completionPointId, Long clientId, LocalDate fd, LocalDate td) {

		
		td = td.plusDays(1);
		
		List<CompletionUser> ret = new ArrayList<>();
		List<Object[]> records = entityManager.createNativeQuery(
			"SELECT "
			+ "completions.completion_point_id, "
			+ "completions.started_at, "
			+ "completions.ended_at, "
			+ "users.account, "
			+ "users.email, "
			+ "users.family_name, "
			+ "users.given_name "
			+ "FROM completions "
			+ "INNER JOIN users ON (users.id = completions.user_id AND users.is_deleted = false) "  // 削除ユーザーは除外
			+ "INNER JOIN clients_users ON (clients_users.user_id = users.id AND clients_users.client_id = :arg5 ) "
			+ "WHERE completions.completion_point_id = :arg1 "
			+ "AND completions.client_id = :arg2 "
			+ "AND (completions.started_at IS NOT NULL) "
			+ "AND (completions.ended_at IS NOT NULL) "
			+ "AND (completions.ended_at >= :arg3) "
			+ "AND (completions.ended_at < :arg4) "
			+ "ORDER BY completions.ended_at "
			)
			.setParameter("arg1", completionPointId)
			.setParameter("arg2", clientId)
			.setParameter("arg3", fd)
			.setParameter("arg4", td)
			.setParameter("arg5", clientId)
			.getResultList();

		for(Object[] record : records) {
			CompletionUser completionUser = new CompletionUser();
			completionUser.setAccount((String)record[3]);
			completionUser.setEmail((String)record[4]);
			completionUser.setFamilyName((String)record[5]);
			completionUser.setGivenName((String)record[6]);
			completionUser.setEndedAt(DateTimeUtil.toLocalDateTime((Timestamp)record[2]));
			ret.add(completionUser);
		}
		return ret;
	}

	
}
