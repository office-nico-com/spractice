package com.office_nico.spractice.service.data;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.office_nico.spractice.util.BeanUtil;

import lombok.Data;

@Data
public class Page<T> {
	
	private List<T> objs = new ArrayList<>();
	private Long totalElements = 0L;
	
	
	public List<T> toList(){
		return objs;
	}

	public void add(Map<String, Object> _obj, T dest) {
		objs.add(BeanUtil.mapToBean(_obj, dest));
	}
	
	public void set(List<Map<String, Object>> targets, Class<T> clazz) {
		for(Map<String, Object> target : targets) {
			T bean;
			try {
				bean = clazz.getDeclaredConstructor().newInstance();
				objs.add(BeanUtil.mapToBean(target, bean));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
