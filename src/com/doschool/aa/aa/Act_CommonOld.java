package com.doschool.aa.aa;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.umeng.analytics.MobclickAgent;


import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;


/**
 * 99%
 * @author 是我的海
 */
public abstract class Act_CommonOld extends Activity{
	
	
	/******** 尺寸控制 ****************************************/
	protected int mScrnWidth = DoschoolApp.widthPixels;
	protected int mScrnHeight = DoschoolApp.heightPixels;
	protected int mDip = DoschoolApp.pxperdp;
	
	/******** 界面组件  ****************************************/
	protected LinearLayout mParentLayout;		//最大父框架
	/******** 数据等  ****************************************/
	protected static String ACTIONBAR_TITTLE="";		//ActionBar标题
	
	protected RelativeLayout mTrueParentLayout;
	
	//初始化数据
	protected abstract void initData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		
		//初始化数据
		initData();	
		
		//设置ActionBar
		getActionBar().setDisplayHomeAsUpEnabled(true);  
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.topbar_bg));
		getActionBar().setTitle(ACTIONBAR_TITTLE);
		getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		
		mTrueParentLayout=new RelativeLayout(this);
		setContentView(mTrueParentLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		//最大的父框架，是一个线性布局
		mParentLayout=new LinearLayout(this);
		mParentLayout.setOrientation(LinearLayout.VERTICAL);
		mParentLayout.setBackgroundResource(R.color.bg_grey);
		
		mTrueParentLayout.addView(mParentLayout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);


		
	}

	public void onResume() {
		super.onResume(); 
	    MobclickAgent.onPageStart(getClass().getName());
	    MobclickAgent.onResume(this);
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(getClass().getName());
	    MobclickAgent.onPause(this);
	}
	
	public void onDestroy(){
		super.onDestroy();

	}
}
