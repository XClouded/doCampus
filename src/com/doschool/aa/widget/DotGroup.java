package com.doschool.aa.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.doschool.R;
import com.doschool.app.DoschoolApp;

public class DotGroup extends LinearLayout
{

	private int mDip = DoschoolApp.pxperdp;
	public int total;
	public int current;
	private ViewPager mViewPager;

	public DotGroup(Context context,ViewPager vp) {
		super(context);
		this.total=vp.getAdapter().getCount();
		this.current=0;
		this.mViewPager=vp;
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER);
		for(int i=0;i<total;i++)
		{
			ImageView ivDot=new ImageView(getContext());
			ivDot.setPadding(mDip, mDip, mDip, mDip);
			this.addView(ivDot,mDip*6,mDip*6);
		}
		setCurrentItem(0);
	}
	
	public void setCurrentItem(int cur)
	{
		this.current=cur;
		for(int i=0;i<total;i++)
		{
			ImageView iv=(ImageView)this.getChildAt(i);
			if(i==cur)
				iv.setImageResource(R.drawable.circle_dot_dark);
			else
				iv.setImageResource(R.drawable.circle_dot);
				
		}
	}
}