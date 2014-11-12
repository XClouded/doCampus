package com.doschool.aa.aa;

import com.doschool.R;
import com.doschool.aa.widget.ActionBarLayout;
import com.doschool.app.DoschoolApp;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public abstract class Fgm_Standard_Linear extends Fragment {

	/******** 尺寸控制 ****************************************/
	protected int mScrnWidth = DoschoolApp.widthPixels;
	protected int mScrnHeight = DoschoolApp.heightPixels;
	protected int mDip = DoschoolApp.pxperdp;

	/******** 界面组件 ****************************************/
	protected LinearLayout mParent;
	protected ActionBarLayout mActionbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 初始化数据
		initData();
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mParent = new LinearLayout(getActivity());
		mParent.setOrientation(LinearLayout.VERTICAL);
		
		mActionbar=new ActionBarLayout(getActivity());
		mParent.addView(mActionbar,mScrnWidth,ActionBarLayout.TOPBAR_HEIGHT);
		
		addViewToFgm();
		return mParent;
	}

	public abstract void initData();
	public abstract void addViewToFgm();

	public ActionBarLayout getActionBar()
	{
		return mActionbar;
	}
}
