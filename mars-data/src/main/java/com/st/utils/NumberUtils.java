package com.st.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberUtils {
	
	/**
	 * double类型保留2位小数
	 * 四舍五入
	 * @param num
	 * @return
	 */
	public static double Double2point(double num){
		return new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}



}
