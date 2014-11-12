package com.doschool.aa.widget;

import com.doschool.R;
import com.doschool.app.DoschoolApp;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleHorizontal extends LinearLayout {
	ImageView ivLoading;
	public TextView mtvLoading;
	Animation mAnimation;
	
	int parentWidth =(int) (DoschoolApp.widthPixels);
	
	public CircleHorizontal(Context context,String text) {
		super(context);
		this.setGravity(Gravity.CENTER);
		mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
		mAnimation.setInterpolator(new LinearInterpolator());
		ivLoading=new ImageView(getContext());
		ivLoading.setImageResource(R.drawable.really_rotate);
		ivLoading.startAnimation(mAnimation);
		this.addView(ivLoading,parentWidth/20,parentWidth/20);
		mtvLoading=WidgetFactory.createTextView(getContext(), text, 0, 13);
		mtvLoading.setPadding(DoschoolApp.pxperdp, 0, 0, 0);
		this.addView(mtvLoading);
		
	}
	
	
	public CircleHorizontal(Context context,int color) {
		super(context);
		this.setGravity(Gravity.CENTER);
		mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
		mAnimation.setInterpolator(new LinearInterpolator());
		ivLoading=new ImageView(getContext());
		ivLoading.setImageResource(R.drawable.really_rotate);
		ivLoading.setColorFilter(color);
		ivLoading.startAnimation(mAnimation);
		this.addView(ivLoading,parentWidth/20,parentWidth/20);
		mtvLoading=WidgetFactory.createTextView(getContext(), "", 0, 13);
		mtvLoading.setPadding(DoschoolApp.pxperdp, 0, 0, 0);
		this.addView(mtvLoading);
		
	}
	
	public void setText(String text)
	{
		mtvLoading.setText(text);
	}
	
}
