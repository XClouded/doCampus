package com.doschool.aa.aa;

import com.doschool.R;
import com.doschool.aa.widget.ActionBarLayout;
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
public abstract class Act_Common_Linear extends Activity{
	
	
	/******** 尺寸控制 ****************************************/
	protected int mScrnWidth = DoschoolApp.widthPixels;
	protected int mScrnHeight = DoschoolApp.heightPixels;
	protected int mDip = DoschoolApp.pxperdp;
	
	/******** 界面组件  ****************************************/
	protected LinearLayout mParent;		//最大父框架
	/******** 数据等  ****************************************/

	protected RelativeLayout mContent;
	protected ActionBarLayout mActionbar;

	
	//初始化数据
	protected abstract void initData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		
		//初始化数据
		initData();	
		
		mParent=new LinearLayout(this);
		setContentView(mParent);
		mParent.setBackgroundResource(R.color.bg_grey);
		mParent.setOrientation(LinearLayout.VERTICAL);
		
		mActionbar=new ActionBarLayout(this);
		mParent.addView(mActionbar,LayoutParams.MATCH_PARENT,ActionBarLayout.TOPBAR_HEIGHT);
		
	}

	public ActionBarLayout getActionBarM()
	{
		return mActionbar;
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
