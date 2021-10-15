package com.office_nico.spractice.web.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import lombok.Data;

@Data
public class DataTableForm {
	
	private Integer start = null;
	private Integer length = null;
	private Map<String, String> search = new HashMap<>();
	private List<Map<String, String>> order = new ArrayList<>();
	// private List<String>search = new ArrayList<>();

	
	public int getOrderColumn() {
		int ret = 0;
		if(order != null && order.size() > 0) {
			String _ret = (String)order.get(0).get("column");
			ret = Integer.parseInt(_ret);
		}
		return ret;
	}
	public Sort.Direction getOrderDirecton() {
		Sort.Direction ret = Sort.Direction.ASC;
		if(order != null && order.size() > 0 && ((String)order.get(0).get("dir")).equals("desc")) {
			ret = Sort.Direction.DESC;
		}
		return ret;
	}
	
	
	public String[] getOrderString(Object...orderIndexs) {
		
		if(orderIndexs == null || orderIndexs.length ==0) {
			throw new NullPointerException("There is no order index.");
		}
		
		if(orderIndexs.length < getOrderColumn() + 1) {
			throw new NullPointerException("The order index is insufficient.");
		}
		
		List<String> ret = new ArrayList<>();

		Object o = orderIndexs[getOrderColumn()];
		if(o instanceof String[]) {
			for(String order : (String[])o) {
				ret.add(order);
			}
		}
		else if(o instanceof String) {
			ret.add((String)o);
		}
		return ret.toArray(new String[] {});
	}
	
}
