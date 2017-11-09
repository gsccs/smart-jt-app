package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by x.d zhang on 2016/11/12.
 */
public class CameraEntity implements Parcelable{

    private int id;         //Id
    private String name;   //标题
    private Double lat;     //经度
    private Double lng;     //维度
    private String author;  //作者
    private String addtime; //发布时间


    public CameraEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    protected CameraEntity(Parcel in) {
        id = in.readInt();
        name= in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        author = in.readString();
        addtime = in.readString();
    }

    public static final Creator<CameraEntity> CREATOR = new Creator<CameraEntity>() {
        @Override
        public CameraEntity createFromParcel(Parcel in) {
            return new CameraEntity(in);
        }

        @Override
        public CameraEntity[] newArray(int size) {
            return new CameraEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(author);
        parcel.writeString(addtime);
    }

}
