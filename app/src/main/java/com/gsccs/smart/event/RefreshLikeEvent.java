package com.gsccs.smart.event;

/**
 * Created by x.d zhang on 2016/11/17.
 */
public class RefreshLikeEvent {
    public static final int TYPE_UPVOTE = 0;
    public static final int TYPE_UNUPVOTE = 1;
    public static final int TYPE_INIT = 2;

    public int type = 2;

    public RefreshLikeEvent(int type){
        this.type = type;
    }
}
