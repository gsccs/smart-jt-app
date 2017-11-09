package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by x.d zhang on 2016/12/4.
 */
public class LostFoundEntity implements Parcelable {

        private int id;
        private int userid;
        private int commentnum;
        private int upvote_num;
        private int upvote_status;
        private String content;
        private String imgurls;
        private String addtimestr;
        private String username;
        private String userlogo;

        public void setUpvote_num(int upvote_num) {
            this.upvote_num = upvote_num;
        }

        public void setUpvote_status(int upvote_status) {
            this.upvote_status = upvote_status;
        }

        public int getUpvote_num() {
            return upvote_num;
        }

        public int getUpvote_status() {
            return upvote_status;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgurls() {
            return imgurls;
        }

        public void setImgurls(String imgurls) {
            this.imgurls = imgurls;
        }

        public String getAddtimestr() {
            return addtimestr;
        }

        public void setAddtimestr(String addtimestr) {
            this.addtimestr = addtimestr;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserlogo() {
            return userlogo;
        }

        public void setUserlogo(String userlogo) {
            this.userlogo = userlogo;
        }

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }

    @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.userid);
            dest.writeInt(this.commentnum);
            dest.writeInt(this.upvote_num);
            dest.writeInt(this.upvote_status);
            dest.writeString(this.content);
            dest.writeString(this.imgurls);
            dest.writeString(this.addtimestr);
            dest.writeString(this.username);
            dest.writeString(this.userlogo);
        }

        public LostFoundEntity() {
        }

        protected LostFoundEntity(Parcel in) {
            this.id = in.readInt();
            this.userid = in.readInt();
            this.commentnum = in.readInt();
            this.upvote_num = in.readInt();
            this.upvote_status = in.readInt();
            this.content = in.readString();
            this.imgurls = in.readString();
            this.addtimestr = in.readString();
            this.username = in.readString();
            this.userlogo = in.readString();
        }

        public static final Creator<LostFoundEntity> CREATOR = new Creator<LostFoundEntity>() {
            public LostFoundEntity createFromParcel(Parcel source) {
                return new LostFoundEntity(source);
            }

            public LostFoundEntity[] newArray(int size) {
                return new LostFoundEntity[size];
            }
        };
    }
