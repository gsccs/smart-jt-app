package com.gsccs.smart.model;

public class ResultBean {

	private int code;
	private Object data;
	private String message;

	public ResultBean(int code, Object data, String message) {
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public ResultBean(int code) {
		this.code = code;
	}

	public ResultBean() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
