package com.gsccs.smart.model;

public class VersionInfo {

	private Integer code;	// 系统版本号
	private String name;	// 内部版本号
	private String url;	// 下载版本的地址
	private String remark;	// 版本描述

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
