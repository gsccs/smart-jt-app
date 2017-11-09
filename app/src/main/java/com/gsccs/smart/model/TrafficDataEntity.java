package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * 交通指数
 * Created by x.d zhang on 2016/12/20.
 */
public class TrafficDataEntity implements Parcelable{

    private Integer id;		//ID
    private String name;		//区域名称
    private Float value;		//指数
    private Float speed;		//车速
    private Integer type;	    //区域类型
    private Date addtime;		//发布时间
    private String valuestr;
    private String remark;		//备注信息


    public TrafficDataEntity() {
    }

    protected TrafficDataEntity(Parcel in) {
        name = in.readString();
        remark = in.readString();
    }

    public static final Creator<TrafficDataEntity> CREATOR = new Creator<TrafficDataEntity>() {
        @Override
        public TrafficDataEntity createFromParcel(Parcel in) {
            return new TrafficDataEntity(in);
        }

        @Override
        public TrafficDataEntity[] newArray(int size) {
            return new TrafficDataEntity[size];
        }
    };

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

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValuestr() {
        return valuestr;
    }

    public void setValuestr(String valuestr) {
        this.valuestr = valuestr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeFloat(this.value);
        dest.writeFloat(this.speed);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.valuestr);
    }
}
