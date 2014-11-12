package com.doschool.aa.activity;

import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.WidgetFactory;
import com.umeng.analytics.MobclickAgent;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.component.atemotion.BlogContentTextWatcher;
import com.doschool.component.atemotion.BlogEditText;
import com.doschool.component.updatelater.Task;
import com.doschool.entity.Microblog;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;


/**
 * 这个应该没啥可改的了
 * @author 是我的海
 */
public class Act_Trans extends Act_CommonOld  {

	/******** 常量标识 ****************************************/
	private static final int ID_EMOTION = 3;
	private static final int ID_AT = 4;
	private static final int ID_PARENT=5;

	/******** 界面组件 ****************************************/
	private BlogEditText xetContent;
	Button btAt,btEmotion;
	
	/******** 数据其他 ****************************************/
	private Microblog blogData;
	

	@Override
	public void initData() {
		blogData=(Microblog) MySession.getSession().get("microblog");
		MySession.getSession().remove("microblog");
		ACTIONBAR_TITTLE="转发微博";
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mParentLayout.setId(ID_PARENT);
		mParentLayout.setBackgroundResource(R.color.bg_grey);
		
		LinearLayout upLayout = new LinearLayout(this);
		upLayout.setBackgroundResource(R.color.white);
		upLayout.setOrientation(LinearLayout.VERTICAL);
		mParentLayout.addView(upLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		

		btAt = WidgetFactory.createPicButton(this, R.drawable.img_at, null);
		btEmotion = WidgetFactory.createPicButton(this, R.drawable.img_emotion, null);
		
		// 内容文本框
		xetContent = new BlogEditText(this, btEmotion, btAt, mParentLayout);
		xetContent.setTextColor(Color.BLACK);
		xetContent.setMaxLines(5);
		LayoutParams lpContent=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpContent.setMargins(mDip*16, mDip*24, mDip*16, 0);
		upLayout.addView(xetContent, lpContent);
		
		// 表情-艾特-话题
		RelativeLayout llOperate = new RelativeLayout(this);
		llOperate.setPadding(mDip*16, mDip*6, mDip*16, mDip*6);
		LayoutParams lpOPRET=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		upLayout.addView(llOperate, lpOPRET);
		
		// at按钮
		btAt.setId(ID_AT);
		LayoutParams lpAt=new LayoutParams(mDip * 24, mDip * 24);
		lpAt.setMargins(0, 0, mDip*6, 0);
		llOperate.addView(btAt, lpAt);

		// 表情按钮
		btEmotion.setId(ID_EMOTION);
		RelativeLayout.LayoutParams lpEmotion=new RelativeLayout.LayoutParams(mDip * 24, mDip * 24);
		lpEmotion.addRule(RelativeLayout.RIGHT_OF,ID_AT);
		lpEmotion.setMargins(mDip*6, 0, mDip*6, 0);
		llOperate.addView(btEmotion, lpEmotion);

		LinearLayout llImage = new LinearLayout(this);
		mParentLayout.addView(llImage, LayoutParams.MATCH_PARENT, mDip*32);
		
		
		LinearLayout llDown=new LinearLayout(this);
		llDown.setGravity(Gravity.BOTTOM);
		mParentLayout.addView(llDown, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		
		// 表情输入器
		llDown.addView(xetContent.emotionLayout,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		llDown.addView(xetContent.atListLayout,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		xetContent.addTextChangedListener(new BlogContentTextWatcher(null, null, xetContent, DoschoolApp.BLOG_CONTENT_CHARACTER_NUM));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();  
	    inflater.inflate(R.menu.common_confirm, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.confirm:
			long lastTimeSendBlog=SpMethods.loadLong(this, SpMethods.LASTTIME_SEND_BLOG, 0);
			long timeCha=System.currentTimeMillis()- lastTimeSendBlog;
			String content=xetContent.getText().toString();
			boolean isEmpty=true;
			for(int i=0;i<content.length();i++)
			{
				if(content.charAt(i)!='\n' && content.charAt(i)!=' ')
				{
					isEmpty=false;
					break;
				}
			}
			
			
			if (timeCha < 10000)
				DoMethods.showToast(Act_Trans.this, "10秒钟内不能连续发表或转发微博(⊙▽⊙)");
			else if(content.length()>DoschoolApp.BLOG_CONTENT_CHARACTER_NUM)
			{
				DoMethods.showToast(Act_Trans.this, "微博字数不能超过1000字哦");
			}
			else if(isEmpty)
			{
				
				new AlertDialog.Builder(this).setTitle("转发的时候，不说点什么吗？")
				.setNegativeButton("补充点文字", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						xetContent.requestFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						// 得到InputMethodManager的实例
						if (imm.isActive()) {
							// 如果开启
							imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
							// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
						}
						
					}
				})
				.setPositiveButton("直接转发！", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int root=blogData.root;
						if(root==0)
							root=blogData.blogId;
						xetContent.setText(DoMethods.getRandomTransContentWhenNoWords(Act_Trans.this));
						String jsonContent2=xetContent.toJMjsonObject().toString();
						
						Task task=new Task(DoschoolApp.thisUser.personId, blogData.blogId, root, jsonContent2,"",  new ArrayList<String>(),0);
						DoschoolApp.mDBHelper.insertTask(task);
						setResult(RESULT_OK);
						Act_Trans.this.finish();
						Act_Trans.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
						
					}
				})
				.create().show();
				
			}
			else
			{
				int root=blogData.root;
				if(root==0)
					root=blogData.blogId;
				Task task=new Task(DoschoolApp.thisUser.personId, blogData.blogId, root, xetContent.toJMjsonObject().toString(),"",  new ArrayList<String>(),0);
				DoschoolApp.mDBHelper.insertTask(task);
				setResult(RESULT_OK);
				Act_Trans.this.finish();
				Act_Trans.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}

			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		//形成文本框里面的转发后缀，如: ||是我的海:不错  ||大葱: 哈哈。
		if(blogData.root!=0)
		{
			xetContent.shouldWordk=false;
			SpannableString sps=xetContent.strtosp(" ||"+blogData.author.nickName+":"+blogData.blogContent);
			xetContent.setText(sps);
			xetContent.shouldWordk=true;
		}
		super.onStart();
	}

}
