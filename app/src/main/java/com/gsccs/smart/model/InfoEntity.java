package com.gsccs.smart.model;

import android.support.annotation.NonNull;

import java.util.List;



public class InfoEntity {

    private Integer id;
    private Integer channel;
    private String content;
    private String userid;
    private String addtimestr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getAddtimestr() {
        return addtimestr;
    }

    public void setAddtimestr(String addtimestr) {
        this.addtimestr = addtimestr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

