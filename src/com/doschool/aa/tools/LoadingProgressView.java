package com.doschool.aa.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.doschool.plugin.lib.DensityUtil;

public class LoadingProgressView extends RelativeLayout{
	Context context;
	ProgressView pv1,pv2;
	int width;
	int h;
	Handler handler;
	boolean lock;
	public LoadingProgressView(Context context,int h,int c) {
		super(context);
		this.context = context;
		this.h = h;
		handler = new Handler();
		pv1 = new ProgressView(context, h,c);
		pv2 = new ProgressView(context, h,c);
		init();
		
	}
	public LoadingProgressView(Context context,int h) {
		this(context,h,0xff708DAB);
	}
	
	private void init(){
		this.addView(pv1);
		this.addView(pv2);
		width = (int) DensityUtil.getWidth(context);
		((RelativeLayout.LayoutParams)pv1.getLayoutParams()).leftMargin = -width;
		((RelativeLayout.LayoutParams)pv1.getLayoutParams()).width = width;
		((RelativeLayout.LayoutParams)pv1.getLayoutParams()).height = h;
		((RelativeLayout.LayoutParams)pv1.getLayoutParams()).rightMargin = width;

		((RelativeLayout.LayoutParams)pv2.getLayoutParams()).leftMargin = 0;
		((RelativeLayout.LayoutParams)pv2.getLayoutParams()).width = width;
		((RelativeLayout.LayoutParams)pv2.getLayoutParams()).height = h;
		((RelativeLayout.LayoutParams)pv2.getLayoutParams()).rightMargin = 0;

	}
	
	public void beginMoving(){
		lock = true;
		new Thread(){
			public void run(){
				final int step = 80;
				while(lock){
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
//							Log.i("TOOLINFOPRO", "WA");
							RelativeLayout.LayoutParams	rlp = ((RelativeLayout.LayoutParams)pv1.getLayoutParams());
							rlp.leftMargin += width/step;
							rlp.width = width;
							rlp.height = h;
							rlp.rightMargin -= width/step;
							pv1.setLayoutParams(rlp);
							rlp = ((RelativeLayout.LayoutParams)pv2.getLayoutParams());
							rlp.leftMargin += width/step;
							rlp.width = width;
							rlp.height = h;
							rlp.rightMargin -= width/step;
							pv2.setLayoutParams(rlp);
							
							if(((RelativeLayout.LayoutParams)pv1.getLayoutParams()).leftMargin >= 0){
								Log.i("TOOLINFOPRO", "WA222");

								
								
								((RelativeLayout.LayoutParams)pv1.getLayoutParams()).leftMargin = -width;
								((RelativeLayout.LayoutParams)pv1.getLayoutParams()).width = width;
								((RelativeLayout.LayoutParams)pv1.getLayoutParams()).height = h;
								((RelativeLayout.LayoutParams)pv1.getLayoutParams()).rightMargin = width;
								pv1.setLayoutParams(pv1.getLayoutParams());
								((RelativeLayout.LayoutParams)pv2.getLayoutParams()).leftMargin = 0;
								((RelativeLayout.LayoutParams)pv2.getLayoutParams()).width = width;
								((RelativeLayout.LayoutParams)pv2.getLayoutParams()).height = h;
								((RelativeLayout.LayoutParams)pv2.getLayoutParams()).rightMargin = 0;
								pv2.setLayoutParams(pv2.getLayoutParams());
							}
						}
					});
					
				}
				
			}
			
		}.start();
		
	
	}
	
	public void stopMoving(){
		lock = false;
	}
	
}

class ProgressView extends View{
	int height;
	int color = 0xff404d8b;
	Context context;
	Paint p;
	public ProgressView(Context context,int h,int c) {
		super(context);
		this.context = context;
		height = h;
		color = c;
		p = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = canvas.getWidth();
		p.setColor(color);
		for(int i=0;i<4;i++){
			canvas.drawRoundRect(new RectF(i*w/4, 0,i*w/4+ w/4 - DensityUtil.dip2px(context, 4), height), 0, 0,  p);
		}
	}

	
	
}
