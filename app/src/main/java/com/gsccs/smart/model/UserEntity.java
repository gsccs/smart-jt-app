package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by x.d zhang on 2016/11/12.
 */
public class UserEntity implements Parcelable {

    private int id;
    private String account;
    private String title;
    private String email;
    private String result;
    private String work;
    private String sex;
    private String birth;
    private String location;
    private String logo;
    private String usersign;
    private int follow_num;
    private int focus_num;


    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getResult() {
        return result;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setUsersign(String usersign) {
        this.usersign = usersign;
    }

    public String getUsersign() {
        return usersign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.account);
        dest.writeString(this.email);
        dest.writeString(this.result);
        dest.writeString(this.work);
        dest.writeString(this.sex);
        dest.writeString(this.birth);
        dest.writeString(this.location);
        dest.writeString(this.logo);
        dest.writeString(this.title);
        dest.writeString(this.usersign);

    }

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        this.id = in.readInt();
        this.account = in.readString();
        this.email = in.readString();
        this.result = in.readString();
        this.work = in.readString();
        this.sex = in.readString();
        this.birth = in.readString();
        this.location = in.readString();
        this.logo = in.readString();
        this.usersign = in.readString();
        this.title = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public void setFocus_num(int focus_num) {
        this.focus_num = focus_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public int getFocus_num() {
        return focus_num;
    }
}
