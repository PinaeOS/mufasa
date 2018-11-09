package org.pinae.mufasa.client.utils;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Java基本类型转换
 * 
 * @author Huiyugeng
 *
 */
public class GenericClassUtils {

	/*
	 * 构建基本数据类型
	 */
	@SuppressWarnings("rawtypes")
	private static final Class[] genericClasses = {
		int.class, 
		boolean.class, 
		float.class, 
		double.class, 
		short.class, 
		char.class, 
		long.class, 
		byte.class,
		java.lang.String.class, 
		java.lang.Integer.class, 
		java.lang.Boolean.class,
		java.lang.Double.class, 
		java.lang.Short.class, 
		java.lang.Character.class,
		java.lang.Long.class, 
		java.lang.Byte.class, 
		java.math.BigDecimal.class, 
		java.math.BigInteger.class
	};
	
	/**
	 * 判断类是否为基本数据类型
	 * 
	 * @param cls 需要判定的类
	 * 
	 * @return 是否基本数据类型
	 */
	public static boolean isGenericClass(Class<?> cls){
		if (cls == null) return false;
		
		boolean match = false;
		for (Class<?> genericClass : genericClasses) {
			match = isTypeMatch(cls, genericClass);
			if (match) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据字段类型将字符串转换为对象值
	 * 
	 * @param type 字段类型
	 * @param str 字符串
	 * 
	 * @return 对象值
	 */
	public static Object toObject(Class<?> srcCls, String str){
		if (str == null) {
			return null;
		}
		
		if(isTypeMatch(srcCls, java.lang.String.class)){
			return str;
		} else if(isTypeMatch(srcCls, int.class) || isTypeMatch(srcCls, java.lang.Integer.class)){
			return (int)Integer.parseInt(str);
		} else if(isTypeMatch(srcCls, boolean.class) || isTypeMatch(srcCls, java.lang.Boolean.class)){
			return (boolean)Boolean.parseBoolean(str);
		} else if(isTypeMatch(srcCls, float.class) || isTypeMatch(srcCls, java.lang.Float.class)){
			return (float)Float.parseFloat(str);
		} else if(isTypeMatch(srcCls, double.class) || isTypeMatch(srcCls, java.lang.Double.class)){
			return (double)Double.parseDouble(str);
		} else if(isTypeMatch(srcCls, short.class) || isTypeMatch(srcCls, java.lang.Short.class)){
			return (short)Short.parseShort(str);
		} else if(isTypeMatch(srcCls, char.class) || isTypeMatch(srcCls, java.lang.Character.class)){
			if(str.length()>0){
				return str.charAt(0);
			}else{
				return null;
			}
		} else if(isTypeMatch(srcCls, long.class) || isTypeMatch(srcCls, java.lang.Long.class)){
			return (long)Long.parseLong(str);
		} else if(isTypeMatch(srcCls, byte.class) || isTypeMatch(srcCls, java.lang.Byte.class)){
			return (byte)Byte.parseByte(str);
		} else if (isTypeMatch(srcCls, java.math.BigDecimal.class)){
			return new BigDecimal(str);
		} else if (isTypeMatch(srcCls, java.math.BigInteger.class)){
			return new BigInteger(str);
		} else{
			return null;
		}
	}
	
	/**
	 * 根据字段类型将对象值转换为字符串
	 * 
	 * @param type 字段类型
	 * @param value 对象值
	 * 
	 * @return 转换后字符串
	 */
	public static String toString(Class<?> srcCls, Object value){
		if (value == null) {
			return null;
		}
		
		if (isGenericClass(srcCls)) {
			return value.toString();
		}
		return null;
	}
	
	private static boolean isTypeMatch(Class<?> srcCls, Class<?> dstCls) {
		return srcCls.equals(dstCls) || dstCls.isAssignableFrom(srcCls);
	}
}
