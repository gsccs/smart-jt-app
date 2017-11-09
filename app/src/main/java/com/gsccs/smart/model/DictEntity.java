package com.gsccs.smart.model;

import java.io.Serializable;

/**
 * 数据字典
 * Created by x.d zhang on 2016/12/20.
 */
public class DictEntity implements Serializable {

    private int id;
    private String code;
    private String title;

    public DictEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
