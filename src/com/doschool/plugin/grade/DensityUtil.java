package com.doschool.plugin.grade;

import android.content.Context;
import android.content.res.TypedArray;

public class DensityUtil{

	
    public static float getActBarHeight(Context context) {
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] {
                android.R.attr.actionBarSize
        });

        float h = actionbarSizeTypedArray.getDimension(0, 0);
        return h;
	}
    
	public static float getWidth(Context context) {
		final float w = context.getResources().getDisplayMetrics().widthPixels;
		return w ;
	}
	
	public static float getHeight(Context context) {
		final float w = context.getResources().getDisplayMetrics().heightPixels;
		return w ;
	}


	
	/**
	 * 根据手机的分辨率�?dp 的单�?转成�?px(像素)
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�?px(像素) 的单�?转成�?dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}

