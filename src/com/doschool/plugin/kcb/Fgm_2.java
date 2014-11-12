package com.doschool.plugin.kcb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.listener.NetWorkFunction;

public class Fgm_2 extends Fragment {
	private NetWorkFunction n;
	private Context context;
	
	RelativeLayout rl;
//	LayoutInflater inflater;
//	Resources pluginRes;

	public Fgm_2(Project v) {
		this.v = v;
	}
	Project v;
	
	String wHanzi = "一二三四五六日X";
	@Override
	public View onCreateView(LayoutInflater ii, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(ii, container, savedInstanceState);
		readyRes();
		rl = new RelativeLayout(context);
		
		View topR = new TopRView(context);
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundColor(Color.WHITE);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView t[] = new TextView[4];
		 t[0] = new TextView(context);
		t[0].setText(v.name);
		 t[1] = new TextView(context);
		t[1].setText(v.teacherName);
		 t[2] = new TextView(context);
		t[2].setText(v.fromWeek + "-" + v.toWeek + "周 "+ "周"+wHanzi.charAt(v.startWeek)+" "+v.startTime+"-"+(v.times+v.startTime)+"节");
		 t[3] = new TextView(context);
		t[3].setText(v.address);
		
		for(int i=0;i<4;i++){
			ll.addView(t[i]);
			((LinearLayout.LayoutParams) t[i].getLayoutParams()).leftMargin = (int) DensityUtil.dip2px(context, 24-8);
			((LinearLayout.LayoutParams) t[i].getLayoutParams()).topMargin = (int) DensityUtil.sp2px(context, 18);
			t[i].setTextColor(0xff7a7a7a);
			t[i].setTextSize(18);
			t[i].setBackgroundColor(Color.WHITE);
			if(i==1){
				View lv = new LineView(context);
				lv.setBackgroundColor(Color.WHITE);
				ll.addView(lv);
				((LinearLayout.LayoutParams) lv.getLayoutParams()).topMargin = (int) DensityUtil.sp2px(context, 18);
				((LinearLayout.LayoutParams) lv.getLayoutParams()).height = (int) DensityUtil.dip2px(context, 1);
//				((LinearLayout.LayoutParams) lv.getLayoutParams()).bottomMargin = (int) DensityUtil.sp2px(context, 36-18);
				((LinearLayout.LayoutParams) lv.getLayoutParams()).leftMargin = (int) DensityUtil.dip2px(context, 2);
				((LinearLayout.LayoutParams) lv.getLayoutParams()).rightMargin = (int) DensityUtil.dip2px(context, 2+8);

			}
		}
		((LinearLayout.LayoutParams) t[0].getLayoutParams()).topMargin =  (int) DensityUtil.sp2px(context, 18);
		t[0].setTextColor(0xff607d8b);
		t[0].setTextSize(36);
		((LinearLayout.LayoutParams)t[3].getLayoutParams()).bottomMargin = (int) DensityUtil.sp2px(context, 18+8);

		View bv = new BottomRView(context);
		ll.addView(bv);
		((LinearLayout.LayoutParams)bv.getLayoutParams()).topMargin = -(int) DensityUtil.dip2px(context, 18-5);
		((LinearLayout.LayoutParams)bv.getLayoutParams()).height = (int) DensityUtil.dip2px(context, 18+4);

		rl.addView(topR);
		rl.addView(ll);
		rl.setBackgroundColor(0xfff2f2f2);
		((RelativeLayout.LayoutParams) topR.getLayoutParams()).height = (int) DensityUtil.dip2px(context, 8+5);
		((RelativeLayout.LayoutParams) ll.getLayoutParams()).topMargin = (int) DensityUtil.dip2px(context, 8+5);
		((RelativeLayout.LayoutParams) ll.getLayoutParams()).leftMargin = (int) DensityUtil.dip2px(context, 8);
		((RelativeLayout.LayoutParams) ll.getLayoutParams()).rightMargin = (int) DensityUtil.dip2px(context, 8);
		((RelativeLayout.LayoutParams) ll.getLayoutParams()).height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		
		rl.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		return rl;
	
	}
	
	class LineView extends View{

		public LineView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			Canvas c = canvas;
			Paint p = new Paint();
	        p.setStyle(Paint.Style.FILL);//����  
	        p.setColor(Color.BLACK);  
	        p.setAntiAlias(true);// ���û��ʵľ��Ч��  
	        c.drawLine(DensityUtil.dip2px(context, 2+8), 0, DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 2+8), 0, p);
			
		}
		
	}

	class TopRView extends View{

		public TopRView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			Canvas c = canvas;
			Paint p = new Paint();
	        p.setStyle(Paint.Style.FILL);//����  
	        p.setColor(Color.WHITE);  
	        p.setAntiAlias(true);// ���û��ʵľ��Ч��  
	        RectF oval3 = new RectF(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8),
	        		DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 16));// ���ø��µĳ�����  
	        c.drawRoundRect(oval3, DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), p);

			
		}
		
	}
	class BottomRView extends View{

		public BottomRView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			Canvas c = canvas;
			Paint p = new Paint();
	        p.setStyle(Paint.Style.FILL);//����  
	        p.setColor(Color.WHITE);  
	        p.setAntiAlias(true);// ���û��ʵľ��Ч��  
	        
	        p.setColor(0xfff2f2f2);
	        c.drawRect(0, 0,
	        		DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 8*2), DensityUtil.dip2px(context, 18+4),p);
	        
	        
	        p.setColor(0xffdadada);  
	        RectF oval3 = new RectF(0, DensityUtil.dip2px(context, 4),
	        		DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 8*2), DensityUtil.dip2px(context, 18+4));// ���ø��µĳ�����  
	        c.drawRoundRect(oval3, DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), p);

	        
	        p.setColor(0xffc6c6c6);  
	        oval3 = new RectF(0, DensityUtil.dip2px(context, 2),
	        		DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 8*2), DensityUtil.dip2px(context, 18+2));// ���ø��µĳ�����  
	        c.drawRoundRect(oval3, DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), p);
	        
	        p.setColor(Color.WHITE);  
	         oval3 = new RectF(0, 0,
	        		DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 8*2), DensityUtil.dip2px(context, 18));// ���ø��µĳ�����  
	        c.drawRoundRect(oval3, DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5), p);

			
		}
		
	}

	
	private void readyRes() {
		try {
			context = getActivity();
//					.createPackageContext(
//					PACKAGE_NAME,
//					Context.CONTEXT_INCLUDE_CODE
//							| Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			pluginRes = getActivity().getPackageManager().getResourcesForApplication(PACKAGE_NAME);
//		} catch (NameNotFoundException e) {
//			Log.i("EEEEE","EE");
//			e.printStackTrace();
//		}
		
	}


	
}
