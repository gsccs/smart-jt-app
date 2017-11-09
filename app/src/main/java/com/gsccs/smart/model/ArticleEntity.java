package com.gsccs.smart.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 *
 * Created by x.d zhang on 2016/11/12.
 */
public class ArticleEntity implements Parcelable{

    private static final int COUNT = 25;
    private static final int TOTALPAGE = 4;

    /**
     * 是否还有更多
     *
     * @param page
     * @return
     */
    public static boolean hasMore(int page) {
        return page < TOTALPAGE;
    }

    private int id;             //Id
    private String title;       //标题
    private String brief;       //标题
    private String content;     //内容
    private String source;      //来源
    private String author;      //作者
    private String addtimestr;  //发布时间
    private String mainimg;     //图片
    private int type;
    private String url;

    public ArticleEntity() {
    }

    public ArticleEntity(String title) {
        this.title = title;
    }

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
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAddtimestr() {
        return addtimestr;
    }

    public void setAddtimestr(String addtimestr) {
        this.addtimestr = addtimestr;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMainimg() {
        return mainimg;
    }

    public void setMainimg(String mainimg) {
        this.mainimg = mainimg;
    }


    protected ArticleEntity(Parcel in) {
        id = in.readInt();
        title= in.readString();
        content = in.readString();
        source = in.readString();
        author = in.readString();
        addtimestr = in.readString();
        mainimg = in.readString();
        type = in.readInt();
    }

    public static final Creator<ArticleEntity> CREATOR = new Creator<ArticleEntity>() {
        @Override
        public ArticleEntity createFromParcel(Parcel in) {
            return new ArticleEntity(in);
        }

        @Override
        public ArticleEntity[] newArray(int size) {
            return new ArticleEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(source);
        parcel.writeString(author);
        parcel.writeString(addtimestr);
        parcel.writeString(mainimg);
        parcel.writeInt(type);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
