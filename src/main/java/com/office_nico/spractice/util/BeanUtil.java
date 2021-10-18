package com.office_nico.spractice.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;
import com.ibm.icu.math.BigDecimal;
import com.office_nico.spractice.exception.ApplicationRuntimeException;


public class BeanUtil {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);
	
	public static <T> T copyFields(Object source, T dest, String...exclusions) {
		
		List<String> _exclusions = Arrays.asList(exclusions);
		
		
		Method[] sourceMethods = source.getClass().getMethods();
		Method[] destMethods = dest.getClass().getMethods();
		
		for(Method sourceMethod : sourceMethods) {
			if(sourceMethod.getName().indexOf("get") >= 0
					&& sourceMethod.getParameterCount() == 0 
					&& (sourceMethod.getReturnType().equals(String.class)
							|| sourceMethod.getReturnType().equals(Long.class)
							|| sourceMethod.getReturnType().equals(Integer.class)
							|| sourceMethod.getReturnType().equals(Short.class)
							|| sourceMethod.getReturnType().equals(Float.class)
							|| sourceMethod.getReturnType().equals(Double.class)
							|| sourceMethod.getReturnType().equals(BigDecimal.class)
							|| sourceMethod.getReturnType().equals(Boolean.class)
							|| sourceMethod.getReturnType().equals(Date.class)
							|| sourceMethod.getReturnType().equals(LocalDateTime.class)
							|| sourceMethod.getReturnType().equals(LocalDate.class)
							|| sourceMethod.getReturnType().equals(BigInteger.class)
				)) {
				// System.out.println(sourceMethod.getName());
				try {
					
					Class<?> sourceClass = sourceMethod.getReturnType();
					Object sourceObj = sourceMethod.invoke(source);
					// System.out.println(sourceObj);

					String destMethodName = sourceMethod.getName().replace("get", "set");
					for(Method destMethod : destMethods) {
						
						String str = sourceMethod.getName().replace("get", "");
						String fieldName = str.substring(0, 1).toLowerCase() + str.substring(1);
						
						if(destMethod.getName().equals(destMethodName) && destMethod.getParameterCount() == 1 && !_exclusions.contains(fieldName)) {
							// System.out.println(destMethodName);
							Class<?> destClass =  destMethod.getParameterTypes()[0];
							if(sourceObj == null){
								// nullならnullをセット
								// ただし、コピー先がBooleanの場合、チェックボックスの可能性があるのでfalseをセットする
								// う～んバグの温床になるかなぁ／true or falseで nullってありえなくない？
								if(sourceClass.equals(String.class) && destClass.equals(Boolean.class)) {
//									destField.set(dest, Boolean.FALSE);
									destMethod.invoke(dest, Boolean.FALSE);
								}
								else {
									Object arg = null;
									destMethod.invoke(dest, arg);
								}
							}
							else if(sourceClass.equals(destClass)) {
								// 型が同じ場合は無条件にセット
								destMethod.invoke(dest, sourceObj);
//								System.out.println(destMethodName);
//								System.out.println(sourceObj);
							}
							else if(sourceClass.equals(String.class) && ((String)sourceObj).equals("")) {
								// 空文字ならnullをセット
								// ただし、コピー先がBooleanの場合、チェックボックスの可能性があるのでfalseをセットする
								// う～んバグの温床になるかなぁ／true or falseで nullってありえなくない？
								if(sourceClass.equals(String.class) && destClass.equals(Boolean.class)) {
									destMethod.invoke(dest, Boolean.FALSE);
								}
								else {
									Object arg = null;
									destMethod.invoke(dest, arg);
								}
							}
							// String → Any
							else if(sourceClass.equals(String.class) && destClass.equals(Long.class)) {
								destMethod.invoke(dest, Long.valueOf((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(Integer.class)) {
								destMethod.invoke(dest, Integer.valueOf((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(Short.class)) {
								destMethod.invoke(dest, Short.valueOf((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(Float.class)) {
								destMethod.invoke(dest, Float.valueOf((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(Double.class)) {
								destMethod.invoke(dest, Double.valueOf((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(BigDecimal.class)) {
								destMethod.invoke(dest,new BigDecimal((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(Boolean.class)) {
								destMethod.invoke(dest, Boolean.valueOf((String)sourceObj));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(Date.class)) {
								destMethod.invoke(dest, DateTimeUtil.parse((String)sourceObj, Date.class));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(LocalDateTime.class)) {
								destMethod.invoke(dest, DateTimeUtil.parse((String)sourceObj, LocalDateTime.class));
							}
							else if(sourceClass.equals(String.class) && destClass.equals(LocalDate.class)) {
								destMethod.invoke(dest, DateTimeUtil.parse((String)sourceObj, LocalDate.class));
							}
							// Any → String
							else if(sourceClass.equals(Long.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((Long)sourceObj));
							}
							else if(sourceClass.equals(Integer.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((Integer)sourceObj));
							}
							else if(sourceClass.equals(Short.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((Short)sourceObj));
							}
							else if(sourceClass.equals(Float.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((Float)sourceObj));
							}
							else if(sourceClass.equals(Double.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((Double)sourceObj));
							}
							else if(sourceClass.equals(BigDecimal.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((BigDecimal)sourceObj));
							}
							else if(sourceClass.equals(Boolean.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, String.valueOf((Boolean)sourceObj));
							}
							else if(sourceClass.equals(Date.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, DateTimeUtil.format((Date)sourceObj, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
							}
							else if(sourceClass.equals(LocalDateTime.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, DateTimeUtil.format((LocalDateTime)sourceObj, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
							}
							else if(sourceClass.equals(LocalDate.class) && destClass.equals(String.class)) {
								destMethod.invoke(dest, DateTimeUtil.format((LocalDate)sourceObj, DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_PADDING));
							}
							// Number → Number
							else if((sourceClass.equals(Long.class) ||sourceClass.equals(Integer.class) || sourceClass.equals(Short.class)) && destClass.equals(Long.class)) {
								String val = String.valueOf(sourceObj);
								destMethod.invoke(dest, Long.valueOf(val));
							}
							else if((sourceClass.equals(Long.class) || sourceClass.equals(Integer.class) || sourceClass.equals(Short.class)) && destClass.equals(Integer.class)) {
								String val = String.valueOf(sourceObj);
								destMethod.invoke(dest, Integer.valueOf(val));
							}
							else if((sourceClass.equals(Long.class) || sourceClass.equals(Integer.class) || sourceClass.equals(Short.class)) && destClass.equals(Short.class)) {
								String val = String.valueOf(sourceObj);
								destMethod.invoke(dest, Short.valueOf(val));
							}
							// Floating Point Number → Floating Point Number
							else if((sourceClass.equals(Float.class) || sourceClass.equals(Double.class) || sourceClass.equals(BigDecimal.class)) && destClass.equals(Float.class)) {
								String val = String.valueOf(sourceObj);
								destMethod.invoke(dest, Float.valueOf(val));
							}
							else if((sourceClass.equals(Float.class) || sourceClass.equals(Double.class) || sourceClass.equals(BigDecimal.class)) && destClass.equals(Double.class)) {
								String val = String.valueOf(sourceObj);
								destMethod.invoke(dest, Double.valueOf(val));
							}
							else if((sourceClass.equals(Float.class) || sourceClass.equals(Double.class) || sourceClass.equals(BigDecimal.class)) && destClass.equals(BigDecimal.class)) {
								String val = String.valueOf(sourceObj);
								destMethod.invoke(dest, new BigDecimal(val));
							}
						}
					}
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// 失敗したら処理をとめる
					throw new ApplicationRuntimeException("field copy error", e);
				}
			}
		}
		
		
		/*
		Field[] sourceFields = source.getClass().getDeclaredFields();
		Field[] destFields = dest.getClass().getDeclaredFields();
		for(Field sourceField : sourceFields) {
			
			for(Field destField : destFields) {
			
				// フィールド名が同じで除外リストに含まれていない
				if(sourceField.getName().equals(destField.getName()) && !_exclusions.contains(sourceField.getName()) && !sourceField.getName().equals("serialVersionUID")) {

					Object obj = null;
					try {
						sourceField.setAccessible(true);
						destField.setAccessible(true);
						obj = sourceField.get(source);
					}
					catch (IllegalArgumentException | IllegalAccessException e) {
						throw new ApplicationRuntimeException("An error occurred during the field copy process.", e);
					}

					try {
						
						// 扱える型の制限
						if(sourceField.getType().equals(String.class) 
								|| sourceField.getType().equals(Long.class) 
								|| sourceField.getType().equals(Integer.class)
								|| sourceField.getType().equals(Short.class)
								|| sourceField.getType().equals(Float.class) 
								|| sourceField.getType().equals(Double.class)
								|| sourceField.getType().equals(BigDecimal.class)
								|| sourceField.getType().equals(Boolean.class)
								|| sourceField.getType().equals(Date.class)
								|| sourceField.getType().equals(LocalDateTime.class)
								|| sourceField.getType().equals(LocalDate.class)
								|| sourceField.getType().equals(BigInteger.class)) {
							
							if(obj == null){
								// nullならnullをセット
								// ただし、コピー先がBooleanの場合、チェックボックスの可能性があるのでfalseをセットする
								// う～んバグの温床になるかなぁ／true or falseで nullってありえなくない？
								if(sourceField.getType().equals(String.class) && destField.getType().equals(Boolean.class)) {
									destField.set(dest, Boolean.FALSE);
								}
								else {
									destField.set(dest, null);
								}
							}
							else if(sourceField.getType().equals(destField.getType())) {
								// 型が同じ場合は無条件にセット
									destField.set(dest, obj);
							}
							else if(sourceField.getType().equals(String.class) && ((String)obj).equals("")) {
								// 空文字ならnullをセット
								// ただし、コピー先がBooleanの場合、チェックボックスの可能性があるのでfalseをセットする
								// う～んバグの温床になるかなぁ／true or falseで nullってありえなくない？
								if(sourceField.getType().equals(String.class) && destField.getType().equals(Boolean.class)) {
									destField.set(dest, Boolean.FALSE);
								}
								else {
									destField.set(dest, null);
								}
							}
							// String → Any
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Long.class)) {
								destField.set(dest, Long.valueOf((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Integer.class)) {
								destField.set(dest, Integer.valueOf((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Short.class)) {
								destField.set(dest, Short.valueOf((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Float.class)) {
								destField.set(dest, Float.valueOf((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Double.class)) {
								destField.set(dest, Double.valueOf((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(BigDecimal.class)) {
								destField.set(dest, new BigDecimal((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Boolean.class)) {
								destField.set(dest, Boolean.valueOf((String)obj));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(Date.class)) {
								destField.set(dest, DateTimeUtil.parse((String)obj, Date.class));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(LocalDateTime.class)) {
								destField.set(dest, DateTimeUtil.parse((String)obj, LocalDateTime.class));
							}
							else if(sourceField.getType().equals(String.class) && destField.getType().equals(LocalDate.class)) {
								destField.set(dest, DateTimeUtil.parse((String)obj, LocalDate.class));
							}
							// Any → String
							else if(sourceField.getType().equals(Long.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Long)obj));
							}
							else if(sourceField.getType().equals(Integer.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Integer)obj));
							}
							else if(sourceField.getType().equals(Short.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Short)obj));
							}
							else if(sourceField.getType().equals(Float.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Float)obj));
							}
							else if(sourceField.getType().equals(Double.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Double)obj));
							}
							else if(sourceField.getType().equals(BigDecimal.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((BigDecimal)obj));
							}
							else if(sourceField.getType().equals(Boolean.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Boolean)obj));
							}
							else if(sourceField.getType().equals(Date.class) && destField.getType().equals(String.class)) {
								destField.set(dest, DateTimeUtil.format((Date)obj, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
							}
							else if(sourceField.getType().equals(LocalDateTime.class) && destField.getType().equals(String.class)) {
								destField.set(dest, DateTimeUtil.format((LocalDateTime)obj, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
							}
							else if(sourceField.getType().equals(LocalDate.class) && destField.getType().equals(String.class)) {
								destField.set(dest, DateTimeUtil.format((LocalDate)obj, DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_PADDING));
							}
							// Number → Number
							else if((sourceField.getType().equals(Long.class) || sourceField.getType().equals(Integer.class) || sourceField.getType().equals(Short.class)) && destField.getType().equals(Long.class)) {
								String val = String.valueOf(obj);
								destField.set(dest, Long.valueOf(val));
							}
							else if((sourceField.getType().equals(Long.class) || sourceField.getType().equals(Integer.class) || sourceField.getType().equals(Short.class)) && destField.getType().equals(Integer.class)) {
								String val = String.valueOf(obj);
								destField.set(dest, Long.valueOf(val));
							}
							else if((sourceField.getType().equals(Long.class) || sourceField.getType().equals(Integer.class) || sourceField.getType().equals(Short.class)) && destField.getType().equals(Short.class)) {
								String val = String.valueOf(obj);
								destField.set(dest, Long.valueOf(val));
							}
							// Floating Point Number → Floating Point Number
							else if((sourceField.getType().equals(Float.class) || sourceField.getType().equals(Double.class) || sourceField.getType().equals(BigDecimal.class)) && destField.getType().equals(Float.class)) {
								String val = String.valueOf(obj);
								destField.set(dest, Float.valueOf(val));
							}
							else if((sourceField.getType().equals(Float.class) || sourceField.getType().equals(Double.class) || sourceField.getType().equals(BigDecimal.class)) && destField.getType().equals(Double.class)) {
								String val = String.valueOf(obj);
								destField.set(dest, Double.valueOf(val));
							}
							else if((sourceField.getType().equals(Float.class) || sourceField.getType().equals(Double.class) || sourceField.getType().equals(BigDecimal.class)) && destField.getType().equals(BigDecimal.class)) {
								String val = String.valueOf(obj);
								destField.set(dest, new BigDecimal(val));
							}
						}
					}
					catch (IllegalArgumentException | IllegalAccessException e) {
						logger.debug("An error occurred during the field copy process.Processing will continue.", e);
					}
				}
			}
		}
		*/
		return dest;
	}
	
	public static <T> T mapToBean(Map<String, Object> source, T dest, String...exclusions) {
		
		List<String> _exclusions = Arrays.asList(exclusions);
		
		
		Method[] destMethods = dest.getClass().getMethods();
		source.forEach((k, v) -> {
			
			String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
			String destMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

			for(Method destMethod : destMethods) {
				
				if(destMethod.getName().equals(destMethodName) && destMethod.getParameterCount() == 1 && !_exclusions.contains(fieldName)) {
						try {

							Class<?> destClass =  destMethod.getParameterTypes()[0];
							
							if(v == null){
								// nullならnullをセット
								Object arg = null;
								destMethod.invoke(dest, arg);
							}
							else if(v.getClass().equals(String.class) 
									|| v.getClass().equals(Long.class) 
									|| v.getClass().equals(Integer.class)
									|| v.getClass().equals(Short.class)
									|| v.getClass().equals(Float.class) 
									|| v.getClass().equals(Double.class)
									|| v.getClass().equals(BigDecimal.class)
									|| v.getClass().equals(Boolean.class)
									|| v.getClass().equals(Date.class)
									|| v.getClass().equals(LocalDateTime.class)
									|| v.getClass().equals(LocalDate.class)
									|| v.getClass().equals(BigInteger.class)) {
								// 扱える型の制限
								if(v.getClass().equals(destClass)) {
									// 型が同じ場合は無条件にセット
										destMethod.invoke(dest, v);
								}
								// String → Any
								else if(v.getClass().equals(String.class) && destClass.equals(Long.class)) {
									destMethod.invoke(dest, Long.valueOf((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(Integer.class)) {
									destMethod.invoke(dest, Integer.valueOf((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(Short.class)) {
									destMethod.invoke(dest, Short.valueOf((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(Float.class)) {
									destMethod.invoke(dest, Float.valueOf((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(Double.class)) {
									destMethod.invoke(dest, Double.valueOf((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(BigDecimal.class)) {
									destMethod.invoke(dest, new BigDecimal((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(Boolean.class)) {
									destMethod.invoke(dest, Boolean.valueOf((String)v));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(Date.class)) {
									destMethod.invoke(dest, DateTimeUtil.parse((String)v, Date.class));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(LocalDateTime.class)) {
									destMethod.invoke(dest, DateTimeUtil.parse((String)v, LocalDateTime.class));
								}
								else if(v.getClass().equals(String.class) && destClass.equals(LocalDate.class)) {
									destMethod.invoke(dest, DateTimeUtil.parse((String)v, LocalDate.class));
								}
								// Any → String
								else if(v.getClass().equals(Long.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((Long)v));
								}
								else if(v.getClass().equals(Integer.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((Integer)v));
								}
								else if(v.getClass().equals(Short.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((Short)v));
								}
								else if(v.getClass().equals(Float.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((Float)v));
								}
								else if(v.getClass().equals(Double.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((Double)v));
								}
								else if(v.getClass().equals(BigDecimal.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((BigDecimal)v));
								}
								else if(v.getClass().equals(Boolean.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, String.valueOf((Boolean)v));
								}
								else if(v.getClass().equals(Date.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, DateTimeUtil.format((Date)v, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
								}
								else if(v.getClass().equals(LocalDateTime.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, DateTimeUtil.format((LocalDateTime)v, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
								}
								else if(v.getClass().equals(LocalDate.class) && destClass.equals(String.class)) {
									destMethod.invoke(dest, DateTimeUtil.format((LocalDate)v, DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_PADDING));
								}

								// Number → Number
								else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destClass.equals(Long.class)) {
									String val = String.valueOf(v);
									destMethod.invoke(dest, Long.valueOf(val));
								}
								else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destClass.equals(Integer.class)) {
									String val = String.valueOf(v);
									destMethod.invoke(dest, Integer.valueOf(val));
								}
								else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destClass.equals(Short.class)) {
									String val = String.valueOf(v);
									destMethod.invoke(dest, Short.valueOf(val));
								}
								// Floating Point Number → Floating Point Number
								else if((v.getClass().equals(Float.class) || v.getClass().equals(Double.class) || v.getClass().equals(BigDecimal.class)) && destClass.equals(Float.class)) {
									String val = String.valueOf(v);
									destMethod.invoke(dest, Float.valueOf(val));
								}
								else if((v.getClass().equals(Float.class) || v.getClass().equals(Double.class) || v.getClass().equals(BigDecimal.class)) && destClass.equals(Double.class)) {
									String val = String.valueOf(v);
									destMethod.invoke(dest, Double.valueOf(val));
								}
								else if((v.getClass().equals(Float.class) || v.getClass().equals(Double.class) || v.getClass().equals(BigDecimal.class)) && destClass.equals(BigDecimal.class)) {
									String val = String.valueOf(v);
									destMethod.invoke(dest, new BigDecimal(val));
								}
							}
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// 失敗したら処理をとめる
							throw new ApplicationRuntimeException("field copy error", e);
						}
				}
			}
		});

		
		/*
		Field[] destFields = dest.getClass().getDeclaredFields();

		source.forEach((k, v) -> {
			
			String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
			
			for(Field destField : destFields) {
				
				destField.setAccessible(true);
				
				// フィールド名が同じで除外リストに含まれていない
				if(fieldName.equals(destField.getName()) && !_exclusions.contains(fieldName) ) {
						
					try {

						if(v == null){
							// nullならnullをセット
								destField.set(dest, null);
						}
						else if(v.getClass().equals(String.class) 
								|| v.getClass().equals(Long.class) 
								|| v.getClass().equals(Integer.class)
								|| v.getClass().equals(Short.class)
								|| v.getClass().equals(Float.class) 
								|| v.getClass().equals(Double.class)
								|| v.getClass().equals(BigDecimal.class)
								|| v.getClass().equals(Boolean.class)
								|| v.getClass().equals(Date.class)
								|| v.getClass().equals(LocalDateTime.class)
								|| v.getClass().equals(LocalDate.class)
								|| v.getClass().equals(BigInteger.class)) {
							// 扱える型の制限
							
							if(v.getClass().equals(destField.getType())) {
								// 型が同じ場合は無条件にセット
									destField.set(dest, v);
							}
							// String → Any
							else if(v.getClass().equals(String.class) && destField.getType().equals(Long.class)) {
								destField.set(dest, Long.valueOf((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(Integer.class)) {
								destField.set(dest, Integer.valueOf((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(Short.class)) {
								destField.set(dest, Short.valueOf((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(Float.class)) {
								destField.set(dest, Float.valueOf((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(Double.class)) {
								destField.set(dest, Double.valueOf((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(BigDecimal.class)) {
								destField.set(dest, new BigDecimal((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(Boolean.class)) {
								destField.set(dest, Boolean.valueOf((String)v));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(Date.class)) {
								destField.set(dest, DateTimeUtil.parse((String)v, Date.class));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(LocalDateTime.class)) {
								destField.set(dest, DateTimeUtil.parse((String)v, LocalDateTime.class));
							}
							else if(v.getClass().equals(String.class) && destField.getType().equals(LocalDate.class)) {
								destField.set(dest, DateTimeUtil.parse((String)v, LocalDate.class));
							}
							// Any → String
							else if(v.getClass().equals(Long.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Long)v));
							}
							else if(v.getClass().equals(Integer.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Integer)v));
							}
							else if(v.getClass().equals(Short.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Short)v));
							}
							else if(v.getClass().equals(Float.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Float)v));
							}
							else if(v.getClass().equals(Double.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Double)v));
							}
							else if(v.getClass().equals(BigDecimal.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((BigDecimal)v));
							}
							else if(v.getClass().equals(Boolean.class) && destField.getType().equals(String.class)) {
								destField.set(dest, String.valueOf((Boolean)v));
							}
							else if(v.getClass().equals(Date.class) && destField.getType().equals(String.class)) {
								destField.set(dest, DateTimeUtil.format((Date)v, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
							}
							else if(v.getClass().equals(LocalDateTime.class) && destField.getType().equals(String.class)) {
								destField.set(dest, DateTimeUtil.format((LocalDateTime)v, DateTimeUtil.DateTimeFormat.TIME_MIN_SLASH_ZERO_PADDING));
							}
							else if(v.getClass().equals(LocalDate.class) && destField.getType().equals(String.class)) {
								destField.set(dest, DateTimeUtil.format((LocalDate)v, DateTimeUtil.DateTimeFormat.DATE_SLASH_ZERO_PADDING));
							}
							// Number → Number
							else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destField.getType().equals(Long.class)) {
								String val = String.valueOf(v);
								destField.set(dest, Long.valueOf(val));
							}
							else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destField.getType().equals(Integer.class)) {
								String val = String.valueOf(v);
								destField.set(dest, Long.valueOf(val));
							}
							else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destField.getType().equals(Short.class)) {
								String val = String.valueOf(v);
								destField.set(dest, Long.valueOf(val));
							}
							else if((v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Short.class) || v.getClass().equals(BigInteger.class)) && destField.getType().equals(Long.class)) {
								String val = String.valueOf(v);
								destField.set(dest, Long.valueOf(val));
							}
							// Floating Point Number → Floating Point Number
							else if((v.getClass().equals(Float.class) || v.getClass().equals(Double.class) || v.getClass().equals(BigDecimal.class)) && destField.getType().equals(Float.class)) {
								String val = String.valueOf(v);
								destField.set(dest, Float.valueOf(val));
							}
							else if((v.getClass().equals(Float.class) || v.getClass().equals(Double.class) || v.getClass().equals(BigDecimal.class)) && destField.getType().equals(Double.class)) {
								String val = String.valueOf(v);
								destField.set(dest, Double.valueOf(val));
							}
							else if((v.getClass().equals(Float.class) || v.getClass().equals(Double.class) || v.getClass().equals(BigDecimal.class)) && destField.getType().equals(BigDecimal.class)) {
								String val = String.valueOf(v);
								destField.set(dest, new BigDecimal(val));
							}
						}
					}
					catch (IllegalArgumentException | IllegalAccessException e) {
						logger.debug("An error occurred during the field copy process.Processing will continue.", e);
					}
				}
			}
		});
		*/
		return dest;
	}
}
