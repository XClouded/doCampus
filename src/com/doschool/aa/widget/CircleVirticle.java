package com.doschool.aa.widget;

import com.doschool.R;
import com.doschool.app.DoschoolApp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleVirticle extends LinearLayout {
	public ImageView ivLoading;
	public TextView mtvLoading;
	Animation mAnimation;
	
	int parentWidth =(int) (DoschoolApp.widthPixels);
	
	public CircleVirticle(Context context) {
		super(context);
		initUI();
	}
	
	
	public void initUI()
	{
		this.setGravity(Gravity.CENTER);
		this.setOrientation(LinearLayout.VERTICAL);
		mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_anim);
		mAnimation.setInterpolator(new LinearInterpolator());
		ivLoading=new ImageView(getContext());
		ivLoading.setImageResource(R.drawable.really_rotate);
		ivLoading.startAnimation(mAnimation);
		this.addView(ivLoading,parentWidth/20,parentWidth/20);
		mtvLoading=WidgetFactory.createTextView(getContext(), "", 0, 13);
		this.addView(mtvLoading,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	}
	
	public void setText(String text)
	{
		mtvLoading.setText(text);
	}


	public CircleVirticle(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI();
	}
	
}
