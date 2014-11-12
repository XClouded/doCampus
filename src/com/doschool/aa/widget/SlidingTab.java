package com.doschool.aa.widget;

import com.doschool.R;
import com.doschool.app.DoschoolApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebSettings.TextSize;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class SlidingTab extends RelativeLayout {

	int mDip=DoschoolApp.pxperdp;
	int mScrnWidth=DoschoolApp.widthPixels;
	int tabButtonBackgroundResId=R.drawable.background_tab;
	int moveLineColorResId=R.color.blue;
	
	int indicatorTextSize=16;
	int indicatorTextUnchooseColorResId=R.color.fzd_grey;
	int indicatorTextChoosedColorResId=R.color.dark_blue;
	int bottomDividerColorResId=R.color.fzd_grey;
	
	int tabHeight=mDip*40;
	int moveHeight=mDip*3;
	int bottomDivierHeiht=1;
	
	LinearLayout llTab;
	LinearLayout llContent;
	Moving moving;
	public SlidingTab(Context context) {
		super(context);
		
		llTab=new LinearLayout(getContext());
		llTab.setBackgroundColor(Color.WHITE);
		this.addView(llTab, LayoutParams.MATCH_PARENT, tabHeight);
		
		

		
		
		RelativeLayout.LayoutParams lpDivide=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, bottomDivierHeiht);
		lpDivide.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		ImageView llDivide=new ImageView(getContext());
		llDivide.setBackgroundResource(bottomDividerColorResId);
		this.addView(llDivide, lpDivide);
		
		moving=new Moving(getContext());
		RelativeLayout.LayoutParams lpMove=new RelativeLayout.LayoutParams(mScrnWidth, moveHeight);
		lpMove.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addView(moving, lpMove);
		
		
		
		
	}
	
	public void setViewPager(ViewPager vp, OnPageChangeListener listener)
	{
		viewPager=vp;
		baseListener=listener;
		viewPager.setOnPageChangeListener(extendListener);
		
		count=viewPager.getAdapter().getCount();
		for(int i=0;i<count;i++)
		{
			Button btn=new Button(getContext());
			btn.setTag(i);
			btn.setOnClickListener(onTabButtonClickListener);
			btn.setBackgroundResource(tabButtonBackgroundResId);
			if(i==0)
				btn.setTextColor(indicatorTextChoosedColorResId);
			else
				btn.setTextColor(indicatorTextUnchooseColorResId);
			btn.setText(viewPager.getAdapter().getPageTitle(i));
			btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, indicatorTextSize);
			llTab.addView(btn, mScrnWidth/count, tabHeight);
		}

		moving.invalidate(0,(float) (1.0/count));
	}
	
	
	OnClickListener onTabButtonClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int index=(Integer) v.getTag();
			viewPager.setCurrentItem(index);
			
		}
	};
	
	int count;
	ViewPager viewPager;
	OnPageChangeListener baseListener;
	
	OnPageChangeListener extendListener=new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			baseListener.onPageSelected(arg0);
			for (int i = 0; i < llTab.getChildCount(); i++) {
				Button btn=(Button) llTab.getChildAt(i);
				if(i==arg0)
					btn.setTextColor(getResources().getColor(indicatorTextChoosedColorResId));
				else
					btn.setTextColor(getResources().getColor(indicatorTextUnchooseColorResId));
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			baseListener.onPageScrolled(arg0, arg1, arg2);
			Log.v("arg1="+arg1, "xxxxxxx");
			moving.invalidate((float)((arg0+arg1)/count), (float)((1+arg0+arg1)/count));
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			baseListener.onPageScrollStateChanged(arg0);
			
		}
	};
	
	
	class Moving extends View
	{
		float rightPercent;
		float leftPercent;

		private Paint mPaint;  
		public Moving(Context context) {
			super(context);
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
		}
		
		public void invalidate(float lp,float rp)
		{
			Log.v("lp="+lp, "invalidate");
			Log.v("rp="+rp, "invalidate");
			leftPercent=lp;
			rightPercent=rp;
			this.invalidate();
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			
			mPaint.setColor(getResources().getColor(moveLineColorResId));
	        canvas.drawRect(leftPercent*getWidth(), 0, rightPercent*getWidth(), getHeight(), mPaint);  
	        
			super.onDraw(canvas);
		}

	}
	
}
