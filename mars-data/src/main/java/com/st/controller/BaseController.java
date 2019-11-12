package com.st.controller;


import com.st.bo.FacadeResult;
import com.st.bo.ReturnServiceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


public abstract class BaseController<E> {

	private static Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 异常页面控制
	 * 
	 * @param runtimeException
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public @ResponseBody
	FacadeResult runtimeExceptionHandler(RuntimeException runtimeException) {

		//捕获异常，对不同异常进行处理
		/*if(UnauthenticatedException.class.getName().equals(runtimeException.getClass().getName())){

			return generateException(ReturnServiceCode.NO_ACCESS_PERMISSION);
		}*/
		logger.error("controller runtimeException:" + runtimeException.getLocalizedMessage());
		runtimeException.printStackTrace();
		return generateException(ReturnServiceCode.SERVER_EXCEPTION,runtimeException.getMessage());
	}

	/**
	 * 正常返回数据
	 * 
	 * @return
	 */
	public FacadeResult generateData(E data) {
		return new FacadeResult(data);
	}
	
	public FacadeResult generateData(E data,long size) {
		return new FacadeResult(data,size);
	}
	
	public FacadeResult generateData(Integer result, String message) {
		return new FacadeResult(result, message);
	}

	/**
	 * 返回异常信息
	 *
	 * @return
	 */
	public FacadeResult generateException(Integer result) {
		return new FacadeResult(result);
	}

	/**
	 * 返回异常信息 自定义
	 *
	 * @return
	 */
	public FacadeResult generateException(Integer result, String message) {
		return new FacadeResult(result, message);
	}

}
