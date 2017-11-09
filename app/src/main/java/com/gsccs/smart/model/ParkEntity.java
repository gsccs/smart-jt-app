package com.gsccs.smart.model;

import java.util.Date;

import com.gsccs.smart.network.BaseConst;

/**
 * 停车位对象
 */
public class ParkEntity {

	private Integer id;
	private String name;		//车位名称
	private Integer type;		//车位类型
	private Double lng;
	private Double lat;
	private String address;		//地址
	private String street;			//街道
	private Integer number;		//车位数量
	private String charge;			//收费
	private Integer score;			//评分
	private Integer starnum;		//星级
	private Float dis;
	private Long userid;
	private String remark;
	private String typetitle;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTypetitle() {
		return typetitle;
	}

	public void setTypetitle(String typetitle) {
		this.typetitle = typetitle;
	}

	public Integer getStarnum() {
		return starnum;
	}

	public void setStarnum(Integer starnum) {
		this.starnum = starnum;
	}

	public Float getDis() {
		return dis;
	}

	public void setDis(Float dis) {
		this.dis = dis;
	}
}
