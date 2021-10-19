package com.office_nico.spractice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeQueryBuilder {
	
	
	private static final Logger logger = LoggerFactory.getLogger(NativeQueryBuilder.class);

	private boolean firstSelect = true;
	private String query = "";
	private List<Map<String, String>> select = new ArrayList<>();
	private List<Object> args = new ArrayList<>();
	private EntityManager em = null;
	private List<Map<String, String >> orders = new ArrayList<Map<String, String>>();
	private Integer offset = null;
	private Integer limit = null;
	
	
	private NativeQueryBuilder(EntityManager _em) {
		em = _em;
	}
	
	public static NativeQueryBuilder newSql(EntityManager em) {
		return new NativeQueryBuilder(em);
	}

	public NativeQueryBuilder append(String _query){
		query += _query + " \n";
		return this;
	}

	/**
	 * 検索フィールドの設定
	 * .までを検索してaliasに設定する。なければ、検索フィールド名=alias名
	 * @param field 検索フィールド
	 * @return NativeQueryBuilder
	 */
	public NativeQueryBuilder appendField(String field){
		int i = field.indexOf(".");
		if(i < 0) {
			appendField(field, field);
		}
		else {
			String alias = field.substring(i + 1, field.length());
			appendField(field, alias);
		}
		return this;
	}

	
	/**
	 * 検索フィールドの設定
	 * .までを検索してaliasに設定する
	 * @param field 検索フィールド
	 * @param alias alias
	 * @return NativeQueryBuilder
	 */
	public NativeQueryBuilder appendField(String field, String alias){
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("field", field);
		fieldMap.put("alias", alias);
		select.add(fieldMap);

		if(!firstSelect) {
			query += ", ";
		}
		else {
			query += "  ";
		}
		query += field + " AS " + alias;
		query += " \n";
		firstSelect = false;
		return this;
	}
	
	public NativeQueryBuilder append(String _query, Object...params){

		_query += " ";

		String[] queries = StringUtils.split(_query, "\\?");
		if(params.length != queries.length - 1) {
			throw new RuntimeException("The number of parameter data did not match. \"?\" is " + (queries.length - 1) + ". args is " + params.length + ".");
		}
		int queryCount = 0;
		int argCount = 0;
		for(String _q : queries) {
			if(queryCount > 0) {
				query += " ?" + (args.size() + argCount);
				argCount++;
			}
			query += _q;
			queryCount++;
		}
		query += " \n";

		for(Object param : params) {
			args.add(param);
		}
		return this;
	}

	public NativeQueryBuilder addOrder(String key, String dir) {
		Map<String, String> order = new HashMap<>();
		order.put("key", key);
		order.put("dir", dir);
		orders.add(order);
		return this;
	}
	
	public NativeQueryBuilder offset(Integer offset) {
		this.offset = offset;
		return this;
	}

	public NativeQueryBuilder limit(Integer limit) {
		this.limit = limit;
		return this;
	}
	
	public Map<String, Object> getSingleResult() {
		Map<String, Object> result = null;
		int bkLimit = limit;
		limit = 1;
		List<Map<String, Object>> records = getResults();
		if(records.size() > 0) {
			result = records.get(0);
		}
		limit = bkLimit;
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getCount() {
		Integer count = 0;
		String countQuery = "SELECT count(*) from (" + this.query + ") as fuhweu";
		Query query = em.createNativeQuery(countQuery);
		
		int i = 0;
		for(Object arg : args) {
			query = query.setParameter(i, arg);
			i++;
		}

		logger.debug("build native count query ===================> \n" + countQuery);

		List<Object> results = query.getResultList();
		if(results.size() > 0) {
			String _c = String.valueOf(results.get(0));
			count = Integer.valueOf(_c);
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResults() {

		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		String execQuery = this.query ;
		
		
		int count = 0;
		for(Map<String, String> order : orders) {
			if(count == 0) {
				execQuery += "\n ORDER BY ";
			}
			else {
				execQuery += ", ";
			}
			execQuery += order.get("key");
			execQuery += " ";
			execQuery += order.get("dir");
			count++;
		}

		logger.debug("build native query ===================> \n" + execQuery);

		Query query = em.createNativeQuery(execQuery);

		int i = 0;
		for(Object arg : args) {
			query = query.setParameter(i, arg);
			i++;
		}

		if(offset != null) {
			query.setFirstResult(offset);
		}
		if(limit != null) {
			query.setMaxResults(limit);
		}
	
		List<Object[]> results = query.getResultList();
		for(Object[] objs : results) {
			
			Map<String, Object> record = new HashMap<>();

			for(int j=0; j<objs.length; j++) {
				if(select.size() > j) {
					Object val = objs[j];
					Map<String, String> field = select.get(j);
					record.put(field.get("alias"), val);
				}
			}
			records.add(record);
		}
		return records;
	}
}
