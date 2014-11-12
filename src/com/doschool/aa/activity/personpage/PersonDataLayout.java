package com.doschool.aa.activity.personpage;

import com.doschool.R;
import com.doschool.aa.activity.Act_BlogList;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.entity.Person;
import com.doschool.methods.DoMethods;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PersonDataLayout extends LinearLayout {


	private int parentWidth=(int) (DoschoolApp.widthPixels*1.0);
	private int PaddingWidth=(int) (DoschoolApp.widthPixels*0.01);
	
	RelativeLayout llBlog,llHuoyue,llRenqi,llOther;
	Person personData;
	
	public PersonDataLayout(Context context,Person person) {
		super(context);
		this.personData=person;
		callRefresh(personData);
		
	}

	public void callRefresh(Person person)
	{
		this.personData=person;
		this.removeAllViews();
		for(int i=0;i<4;i++)
		{
			String a;
			if(i==0)
				a="微博数";
			else if(i==1)
				a="活跃值";
			else if(i==2)
				a="人气值";
			else 
				a="贡献度";
			int rid;
			if(i==0)
				rid=R.drawable.circlepop_1;
			else if(i==1)
				rid=R.drawable.circlepop_2;
			else if(i==2)
				rid=R.drawable.circlepop_3;
			else 
				rid=R.drawable.circlepop_4;
			int value;
			if(i==0)
				value=personData.blogCount;
			else if(i==1)
				value=(int) personData.activeValue;
			else if(i==2)
				value=(int) personData.popValue;
			else 
				value=personData.contriValue;
			
			ItemLayout itemLayout=new ItemLayout(getContext(),a,value,rid);
			if(i==0)
				itemLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						MobclickAgent.onEvent(getContext(), "event_personpage_blogcount");
						MySession.getSession().put("person", personData);
						Intent intent = new Intent(getContext(), Act_BlogList.class);
						getContext().startActivity(intent);
						((Activity) getContext()).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						
					}
				});
			else if(i==1)
				itemLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(getContext(), "event_personpage_active");
						
						DoMethods.showToast(getContext(), "多发微博，多和别人互动，多用小安，活跃值就嗖嗖地上去啦");
					}
				});
			else if(i==2)
				itemLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(getContext(), "event_personpage_hot");
						DoMethods.showToast(getContext(), "别人和你的互动越多，人气值越高");
						
					}
				});
			else if(i==3)
				itemLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(getContext(), "event_personpage_contribute");
						DoMethods.showToast(getContext(), "反映了你为小安变得更好，所做的贡献");
						
					}
				});
			this.addView(itemLayout, (int) (parentWidth*0.25), LayoutParams.WRAP_CONTENT);
		}
	}
	
	class ItemLayout extends LinearLayout{

		public ItemLayout(Context context,String text,int num, int rid) {
			super(context);
			this.setOrientation(LinearLayout.VERTICAL);
			this.setGravity(Gravity.CENTER);
			RelativeLayout llCircle=new RelativeLayout(getContext());
			this.addView(llCircle, (int) (parentWidth*0.25), (int) (parentWidth*0.25));
			llCircle.setPadding(PaddingWidth, PaddingWidth, PaddingWidth, PaddingWidth);
				ImageView ivBack=new ImageView(getContext());
				ivBack.setImageResource(rid);
				llCircle.addView(ivBack, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			
				int duration=(int) (20/(Math.sqrt(num+1))*1000);
				if(duration>7000)
					duration=7000;
				else if(duration<200)
					duration=200;
//				Animation aa2=new AlphaAnimation(1.0f, 0.2f);
//				aa2.setDuration(duration);
//				aa2.setRepeatMode(Animation.REVERSE);
//				aa2.setRepeatCount(Animation.INFINITE);
//				ivBack.startAnimation(aa2);
				
				
				Animation bb=new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				bb.setDuration(duration);
				bb.setRepeatMode(Animation.REVERSE);
				bb.setRepeatCount(Animation.INFINITE);
				llCircle.startAnimation(bb);
				
				TextView mtvNum=WidgetFactory.createTextView(getContext(), num+"", android.R.color.white, 36);
				RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
				llCircle.addView(mtvNum,lp);
				
				Typeface face = Typeface.createFromAsset (getContext().getAssets() , "fonts/Future Sallow.ttf" );  
				mtvNum.setTypeface (face); 
				
			TextView mtvText=WidgetFactory.createTextView(getContext(), text, R.color.dark_grey, 14);
			this.addView(mtvText,lp);
		}
		
		
	}
	
}
