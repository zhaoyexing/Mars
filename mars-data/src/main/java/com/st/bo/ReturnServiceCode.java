package com.st.bo;

/**
 * 错误代码
 * 返回错误码
 *
 */
public final class ReturnServiceCode {
	private ReturnServiceCode() {
	}

	/**
	 * 通用1开头，后三位有效：001
	 */
	//参数错误，参数没传的情况下返回这个错误码
	public static final int CHECK_PARAM_ERROR = 1001;
	//程序报错一般对应500错误
	public static final int SERVER_EXCEPTION = 1002;
	//登录失败错误返回码
	public static final int LOGIN_FAILED_ERROR = 1003;
	//没有访问权限
	public static final int NO_ACCESS_PERMISSION = 1004;
	
	
	

	

}
