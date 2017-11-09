package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by x.d zhang on 2016/12/4.
 */
public class CorpEntity implements Parcelable {

        private int id;
        private String title;
        private String logo;
        private String address;     // 地址
        private String linker; 	  // 联系人
        private String linktel;     // 联系电话
        private String phone; 	      // 移动电话
        private String content;     // 企业简介
    private String imgurls;
        private int commentnum;
        private String addtimestr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgurls() {
        return imgurls;
    }

    public void setImgurls(String imgurls) {
        this.imgurls = imgurls;
    }

    public String getLinker() {
        return linker;
    }

    public void setLinker(String linker) {
        this.linker = linker;
    }

    public String getLinktel() {
        return linktel;
    }

    public void setLinktel(String linktel) {
        this.linktel = linktel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public String getAddtimestr() {
        return addtimestr;
    }

    public void setAddtimestr(String addtimestr) {
        this.addtimestr = addtimestr;
    }

    @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.commentnum);
            dest.writeString(this.addtimestr);
            dest.writeString(this.content);
            dest.writeString(this.logo);
            dest.writeString(this.title);
            dest.writeString(this.linker);
            dest.writeString(this.linktel);
            dest.writeString(this.address);
        }

        public CorpEntity() {

        }

        protected CorpEntity(Parcel in) {
            this.id = in.readInt();
            this.logo = in.readString();
            this.title = in.readString();
            this.content = in.readString();
            this.linker = in.readString();
            this.linktel = in.readString();
            this.address = in.readString();
            this.commentnum = in.readInt();
            this.addtimestr = in.readString();
        }

        public static final Creator<CorpEntity> CREATOR = new Creator<CorpEntity>() {
            public CorpEntity createFromParcel(Parcel source) {
                return new CorpEntity(source);
            }

            public CorpEntity[] newArray(int size) {
                return new CorpEntity[size];
            }
        };
    }
