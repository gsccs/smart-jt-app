package com.gsccs.smart.utils;

import android.content.Context;
import android.widget.TextView;

import com.gsccs.smart.R;

/**
 * Created by x.d zhang on 16/4/22.
 */
public class ColorUtil {

    private static Context ctx;

    public static void init(Context context){
        ctx = context;
    }

    // 合成新的颜色值
    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
    }
    /**
     * 合成新的颜色值
     * @param fraction 颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue 结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }


    public static void setColor(Context ctx,TextView textView, String status){
        switch (status){
            case "click":
                textView.setTextColor(ctx.getResources().getColor(R.color.click));
                break;
            case "default":
                textView.setTextColor(ctx.getResources().getColor(R.color.text_default));
                break;
        }

    }

}
