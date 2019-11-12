package com.st.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * http接口返回数据包装对象
 * @author admin
 *
 */
@ApiModel(value = "返回值",description = "返回值")
public class FacadeResult<E> {
	//返回状态码
	@ApiModelProperty(value = "状态码",required = true)
	private Integer status;
	//返回错误信息
	@ApiModelProperty(value = "描述",required = true)
	private String desc;
	//返回的数据对象
	@ApiModelProperty(value = "数据集",required = true)
	private E datas;

	private  boolean success;

	/**
	 * 成功时返回 默认返回0 表示成功 默认信息 success
	 */
	public FacadeResult(E data) {
		this.status = 200;
		this.desc = "";
		this.datas = data;
		this.success = true;
	}
	
	
	public FacadeResult(E data,long results) {
		this.status = 200;
		this.desc = "";
		this.datas = data;
		this.success = true;
	}

	

	/**
	 * 错误时返回 从配置文件中读取 英文信息
	 *
	 */
	public FacadeResult(Integer status) {
		this.status = status;
		this.desc = ReadServiceCodeUtils.getENServiceMessage(status);
		this.datas = null;
		this.success = false;
	}

	/**
	 * 错误时返回 指定信息
	 */
	public FacadeResult(Integer status, String message) {
		this.status = status;
		this.desc = message;
		this.datas = null;
		this.success = false;
	}

	/**
	 * 全量构造
	 * @param message
	 * @param data
	 */
	public FacadeResult(Integer status, String message, E data,boolean success) {
		this.status = status;
		this.desc = message;
		this.datas = data;
		this.success=success;
	}

	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}




	public String getdesc() {
		return desc;
	}

	public void setdesc(String desc) {
		this.desc = desc;
	}

	public E getData() {
		return datas;
	}

	public void setData(E data) {
		this.datas = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
