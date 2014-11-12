package com.doschool.aa.widget;

import java.util.zip.Inflater;

import org.json.JSONException;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.zother.MJSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskLayout extends RelativeLayout {

	protected int mScrnWidth = DoschoolApp.widthPixels;
	protected int mDip = DoschoolApp.pxperdp;
	LinearLayout llSending;
	LinearLayout llWrong;
//	LinearLayout llSuccess;
	RelativeLayout llProgress;
	View[] viewList;
	
	ProgressBar progressBar;
	TextView tvSending;
	Button btCancel;
	Button btRetry;
	ImageView ivMoving;
	
	public int PROGRESS_HEIGHT=4*mDip;
	public int TOTAL_HEIGHT=40*mDip;
	

	public int STATE_SEND_SHOWING=2;
//	
	public int viewState=1;
//	public int STATE_NOT_SHOW=1;
//	public int STATE_SHOW_UPLOADING=3;
//	public int STATE_SHOW_WRONG=4;
//	public int STATE_SHOW_SUCESS=5;
	
	
	Animation anim_show_and_hide;
	Animation anim_show;
	Animation anim_hide;
	
	
	public TaskLayout(Context context) {
		super(context);

		
		LayoutInflater inflater=LayoutInflater.from(getContext());
		inflater.inflate(R.layout.task_layout, this, true);
		
		llSending=(LinearLayout) findViewById(R.id.llSending);
		llWrong=(LinearLayout) findViewById(R.id.llWrongL);
		llProgress=(RelativeLayout) findViewById(R.id.llProgress);
		
		
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		tvSending=(TextView) findViewById(R.id.tvSending);
		btCancel=(Button) findViewById(R.id.btCancel);
		btRetry=(Button) findViewById(R.id.btRetry);
		ivMoving=(ImageView) findViewById(R.id.ivMoving);
		
		viewList=new View[3];
		viewList[0]=llSending;
		viewList[1]=llWrong;
		viewList[2]=llProgress;
		
		anim_show_and_hide = new TranslateAnimation(0, 0, -TOTAL_HEIGHT-PROGRESS_HEIGHT, 0);
		anim_show_and_hide.setDuration(600);
		anim_show_and_hide.setFillAfter(true);
		anim_show_and_hide.setRepeatCount(1);
		anim_show_and_hide.setRepeatMode(Animation.REVERSE);
		anim_show_and_hide.setInterpolator(new DecelerateInterpolator(2.0f));
		
//		anim_show_and_hide.setInterpolator(new AccelerateInterpolator(4.0f));
		anim_show = AnimationUtils.loadAnimation(getContext(), R.anim.upin);
		
		anim_hide = AnimationUtils.loadAnimation(getContext(), R.anim.upout);
		
		init();
		
		Animation translateAnim = new TranslateAnimation(mScrnWidth ,0 , 0, 0);
		translateAnim.setInterpolator(new AccelerateInterpolator());
		translateAnim.setDuration(2000);
		translateAnim.setRepeatCount(Animation.INFINITE);
		ivMoving.startAnimation(translateAnim);
		
	}
	
	public void init()
	{
		llSending.setVisibility(View.INVISIBLE);
		llWrong.setVisibility(View.INVISIBLE);
		llProgress.setVisibility(View.INVISIBLE);
	}
	


	
	
	
	public void sendShow(String text)
	{

		try {
			text = new MJSONObject(text).getString("string");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		llSending.setBackgroundColor(Color.argb(0xee, 0xcc, 0xcc, 0xff));
		tvSending.setText("正在发送: "+ConvertMethods.removeTextTag(text));
		ShowThenHideView(llSending);
	}

	

	public void wrongShow(OnClickListener onCancel,OnClickListener onRetry)
	{
		btCancel.setOnClickListener(onCancel);
		btRetry.setOnClickListener(onRetry);
		
		ShowView(llWrong);
	}

	public void successShow()
	{

		llSending.setBackgroundColor(Color.argb(0xee, 0xcc, 0xff, 0xcc));
		tvSending.setText("发送成功 ^_^");
		ShowThenHideView(llSending);
	}
	
	
	public void updateProgress(int p)
	{
		if(llProgress.getVisibility()==View.INVISIBLE)
		{
			ShowView(llProgress);
		}
		progressBar.setProgress(p);
	}

	
	
	public void FadeAll()
	{
		for (View view : viewList) {
			if (view.getVisibility() == View.VISIBLE) {
				

				Log.v("sign="+"aaaaaaaaa", "sign");
				view.startAnimation(anim_hide);
				view.setVisibility(View.INVISIBLE);
			}
			
		}
	}

	public void ShowView(View view)
	{
		FadeAll();
		view.startAnimation(anim_show);
		view.setVisibility(View.VISIBLE);
	}
	
	public void ShowThenHideView(View view)
	{
		FadeAll();
		view.setVisibility(View.VISIBLE);
		view.startAnimation(anim_show_and_hide);
		view.setVisibility(View.INVISIBLE);
	}
	
	
}
