package com.office_nico.spractice.constants.codes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.office_nico.spractice.annotation.Code;
import com.office_nico.spractice.service.data.LabelValue;
import com.office_nico.spractice.util.LogUtil;

public class BaseCode {
	
	@Autowired
	protected MessageSource messagesource;

	 public String label(String val) {
		if(val == null) {
			return "";
		}
		return label(Long.valueOf(val));
	}

	
	public String label(Short val) {
		if(val == null) {
			return "";
		}
		return label(Long.valueOf(String.valueOf(val)));
	}

	
	public String label(Integer val) {
		if(val == null) {
			return "";
		}
		return label(Long.valueOf(String.valueOf(val)));
	}

	public String label(Long val) {

		if(val == null) {
			return "";
		}

		Logger logger = LoggerFactory.getLogger(this.getClass());

		String ret = "";
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field filed : fields) {
			filed.setAccessible(true);
			try {
				Object obj = filed.get(this);
				if((obj instanceof Short || obj instanceof Integer || obj instanceof Long) && val == Long.valueOf(String.valueOf(obj))){
					Code c = (Code)filed.getAnnotation(Code.class);
					if(c != null) {
						ret = c.message();
						if(ret.length() > 2 && ret.substring(0, 1).equals("{") && ret.substring(ret.length() - 1, ret.length()).equals("}") ) {
							String msgKey = ret.substring(1, ret.length() - 1);
							// TODO:多国語対応の時はこれでいいのか要チェック
							ret = messagesource.getMessage(msgKey, null, msgKey, Locale.getDefault());
						}
					}
					else {
						logger.debug(LogUtil.format(null, "Code name is not registered. | " + this + " | " + val, null));
					}
					break;
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				logger.debug(LogUtil.format(null, "Code name resolution failed. | " + this + " | " + val , e));
			}
		}
		return ret;
	}
	
	public List<LabelValue> list() {

		Logger logger = LoggerFactory.getLogger(this.getClass());

		List<LabelValue> ret = new ArrayList<>();
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field filed : fields) {
			filed.setAccessible(true);
			
			try {
				
				LabelValue labelValue = new LabelValue();
				
				Object obj = filed.get(this);
				if(obj instanceof Short || obj instanceof Integer || obj instanceof Long) {
					labelValue.setValue(String.valueOf(obj));
					labelValue.setLabel(label(String.valueOf(obj)));

					Code c = (Code)filed.getAnnotation(Code.class);
					if(c != null) {
						Integer order = c.order();
						labelValue.setOrder(order);
					}
					else {
						labelValue.setOrder(99999);
					}
					ret.add(labelValue);
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				logger.debug(LogUtil.format(null, "Code listing failed. | " + this.getClass() , e));
			}
		}
		// sort
		Collections.sort(ret, new Comparator<LabelValue>() {
			@Override
			public int compare(LabelValue personFirst, LabelValue personSecond) {
				return Integer.compare(personFirst.getOrder(), personSecond.getOrder());
			}
		});
		return ret;
	}
}
