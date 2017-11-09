package com.gsccs.smart.listener;

/**
 * Created by think on 2016/12/11.
 */

public interface OnUpLoadResultListener {

    /**
     * 上传图片的结果
     *
     * @param imageUrl
     *            上传成功时
     */
    public void upLoadImageResultSuccess(String imageUrl);

    /**
     * 上传图片失败
     *
     * @param msg
     */
    public void upLoadImageResultFailure(String msg);
}
