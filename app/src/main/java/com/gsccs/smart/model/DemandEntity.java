package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gsccs.smart.network.BaseConst;

import java.util.Date;
import java.util.List;


public class DemandEntity implements Parcelable {

	private int id;			// 评论ID
	private int userid;		// 用户ID
	private int type;			// 类型(1:提问,2:回复)
	private String content;		// 内容
	private String imgurls;		// 图片
	private String addtimestr;	// 时间
	private int commentnum = 0;	// 时间

	// 用户信息
	private String userlogo;		// 用户头像
	private String username;		// 用户名

	public DemandEntity(){}

	protected DemandEntity(Parcel in) {
		id = in.readInt();
		type = in.readInt();
		userid = in.readInt();
		content = in.readString();
		imgurls = in.readString();
		addtimestr = in.readString();
		commentnum = in.readInt();
		userlogo = in.readString();
		username = in.readString();
	}

	public static final Creator<DemandEntity> CREATOR = new Creator<DemandEntity>() {
		@Override
		public DemandEntity createFromParcel(Parcel in) {
			return new DemandEntity(in);
		}

		@Override
		public DemandEntity[] newArray(int size) {
			return new DemandEntity[size];
		}
	};

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getUserlogo() {
		return userlogo;
	}

	public void setUserlogo(String userlogo) {
		this.userlogo = userlogo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCommentnum() {
		return commentnum;
	}

	public void setCommentnum(Integer commentnum) {
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
		dest.writeInt(this.type);
		dest.writeString(this.content);
		dest.writeString(this.imgurls);
		dest.writeString(this.addtimestr);
		dest.writeInt(this.commentnum);
		dest.writeString(this.userlogo);
		dest.writeString(this.username);
	}
}
