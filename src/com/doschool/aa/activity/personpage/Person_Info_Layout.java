package com.doschool.aa.activity.personpage;

import com.doschool.R;
import com.doschool.aa.activity.Act_PersonPage;
import com.doschool.aa.widget.MyDialog;
import com.doschool.aa.widget.ProgressImageView;
import com.doschool.aa.widget.ProgressOperateImageView;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Person;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Person_Info_Layout extends RelativeLayout {

	int parentWidth = DoschoolApp.widthPixels;
	int padding = DoschoolApp.pxperdp;
	Person personInfo;
	LinearLayout llHeadNickSign;
	public ImageView circleHead;
	TextView mtvName, mtvSign;
	Act_PersonPage act;
	public ProgressOperateImageView ivBackPerson;

	public Person_Info_Layout(Act_PersonPage iact, Person personinfo) {
		super(iact);
		this.act = iact;
		this.personInfo = personinfo;

		this.setGravity(Gravity.BOTTOM);

		ivBackPerson = new ProgressOperateImageView(getContext(), 200);
		ivBackPerson.mImageView.setScaleType(ScaleType.CENTER_CROP);

		this.addView(ivBackPerson, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		LinearLayout layer = new LinearLayout(iact);
		layer.setBackgroundColor(Color.argb(80, 0, 0, 0));
		this.addView(layer, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		Animation aa = new AlphaAnimation(1.0f, 0f);
		aa.setDuration(6000);
		aa.setRepeatMode(Animation.REVERSE);
		aa.setRepeatCount(Animation.INFINITE);
		layer.startAnimation(aa);

		// 1、信息面板区
		llHeadNickSign = new LinearLayout(iact);
		llHeadNickSign.setOrientation(LinearLayout.HORIZONTAL);
		llHeadNickSign.setBackgroundResource(R.drawable.lianchange);
		llHeadNickSign.setGravity(Gravity.CENTER_VERTICAL);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) (parentWidth * 0.20));
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		Animation aa2 = new AlphaAnimation(1.0f, 0.6f);
		aa2.setDuration(3000);
		aa2.setRepeatMode(Animation.REVERSE);
		aa2.setRepeatCount(Animation.INFINITE);
		llHeadNickSign.startAnimation(aa2);

		this.addView(llHeadNickSign, lp);
		circleHead = new ImageView(getContext());
		circleHead.setPadding(padding, padding, padding, padding);
		llHeadNickSign.addView(circleHead, (int) (parentWidth * 0.20),
				(int) (parentWidth * 0.20));

		LinearLayout llNickSign = new LinearLayout(iact);
		llNickSign.setGravity(Gravity.CENTER_VERTICAL);
		llHeadNickSign.addView(llNickSign, LayoutParams.MATCH_PARENT,
				(int) (parentWidth * 0.20));
		llNickSign.setOrientation(LinearLayout.VERTICAL);

		mtvName = WidgetFactory.createTextView(iact, personinfo.nickName + "  "
				+ personinfo.sex, android.R.color.white, 19);
		mtvName.setMaxEms(12);
		mtvName.setSingleLine();
		mtvName.setPadding(padding * 2, 0, 0, 0);
		llNickSign.addView(mtvName);

		mtvSign = WidgetFactory.createTextView(iact, personinfo.intro,
				R.color.light_light_grey, 14);
				
		if (personinfo.intro == null || personinfo.intro.length() == 0)
			mtvSign.setVisibility(View.GONE);
		else
			mtvSign.setVisibility(View.VISIBLE);
		mtvSign.setPadding(padding * 2, 0, 0, padding * 2);
		llNickSign.addView(mtvSign);

	}

	public void callRefresh(Person pi) {
		this.personInfo = pi;

		DoschoolApp.newImageLoader.displayImage(personInfo.background,
				ivBackPerson.mImageView, DoschoolApp.dioPersonBg,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						ivBackPerson.mCircleProgress.setProgress(0);
						ivBackPerson.mCircleProgress
								.setVisibility(View.VISIBLE);

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						ivBackPerson.mCircleProgress
								.setVisibility(View.INVISIBLE);

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						Log.v("onProgressUpdate" + "current" + current
								+ " total" + total, "onProgressUpdate");
						ivBackPerson.mCircleProgress.setProgress(100 * current
								/ (total + 1));
					}
				});
		DoschoolApp.newImageLoader.displayImage(personInfo.headUrl, circleHead,
				DoschoolApp.dioRound);
		Log.v("name=" + personInfo.nickName, "ooooo");
		if (personInfo.nickName != null && personInfo.nickName.length() > 0)
			mtvName.setText(personInfo.nickName + " " + personInfo.sex);
		mtvSign.setText(personInfo.intro);
		if (personInfo.intro == null || personInfo.intro.length() == 0)
			mtvSign.setVisibility(View.GONE);
		else
			mtvSign.setVisibility(View.VISIBLE);
		llHeadNickSign.setVisibility(View.VISIBLE);

	}

}
