package com.doschool.aa.widget;

import java.util.ArrayList;
import com.doschool.R;
import com.doschool.app.DoschoolApp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Topbar标题栏 只要给定参数，就能生出一个多功能的Topbar.
 * 
 * @author 是我的海
 */
public class ActionBarLayout extends LinearLayout {

	private int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;
	private int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

	private int mDip = DoschoolApp.pxperdp;
	private int padding = 8 * mDip;
	public static int TOPBAR_HEIGHT = 48 * DoschoolApp.pxperdp;

	public ImageButton getBtnHome() {
		return btnHome;
	}


	public void setBtnHome(ImageButton btnHome) {
		this.btnHome = btnHome;
	}


	public TextView getTvTittle() {
		return tvTittle;
	}


	public void setTvTittle(TextView tvTittle) {
		this.tvTittle = tvTittle;
	}


	public ArrayList<ImageButton> getOperateBtnList() {
		return operateBtnList;
	}


	public void setOperateBtnList(ArrayList<ImageButton> operateBtnList) {
		this.operateBtnList = operateBtnList;
	}

	private ImageButton btnHome;
	private TextView tvTittle;
	private ArrayList<ImageButton> operateBtnList;



	public ActionBarLayout(Context context) {
		super(context);
		this.setBackgroundResource(R.color.dark_blue);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER);
		
		btnHome = new ImageButton(getContext());
		btnHome.setBackgroundResource(R.drawable.tongyong_bg_btn);
		btnHome.setPadding(padding, padding, padding, padding);
		btnHome.setScaleType(ScaleType.FIT_XY);
		this.addView(btnHome, TOPBAR_HEIGHT, TOPBAR_HEIGHT);
		setHomeBtnVisibility(View.GONE);
		
		tvTittle = WidgetFactory.createTextView(getContext(), "", 0, 20);
		tvTittle.setPadding(padding, 0, padding, 0);
		LinearLayout.LayoutParams lpTittle = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1);
		this.addView(tvTittle, lpTittle);
		
		operateBtnList=new ArrayList<ImageButton>();
	}


	public void addOperateButton(int drawableResId, OnClickListener listener) {
		ImageButton ibtn = new ImageButton(getContext());
		operateBtnList.add(ibtn);
		ibtn.setBackgroundResource(R.drawable.tongyong_bg_btn);
		ibtn.setPadding(padding, padding, padding, padding);
		ibtn.setScaleType(ScaleType.FIT_XY);
		ibtn.setImageResource(drawableResId);
		ibtn.setOnClickListener(listener);
		this.addView(ibtn, TOPBAR_HEIGHT, TOPBAR_HEIGHT);

	}
	
	public void setHomeBtnAsBack(final Activity act)
	{
		setHomeBtnVisibility(View.VISIBLE);
		setHomeBtnDrawable(R.drawable.topbar_btn_back);
		setHomeBtnClickLisener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				act.setResult(Activity.RESULT_CANCELED);
				act.finish();
				act.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
		});

	}
	
	public void setTittle(String tittle) {
		tvTittle.setText(tittle);
	}

	public void setHomeBtnDrawable(int resId) {
		btnHome.setImageResource(resId);
	}

	public void setHomeBtnClickLisener(OnClickListener listener) {
		btnHome.setOnClickListener(listener);
	}

	public void setHomeBtnVisibility(int visible) {
		btnHome.setVisibility(visible);
	}

}
