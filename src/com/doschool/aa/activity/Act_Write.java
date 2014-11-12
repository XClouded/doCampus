package com.doschool.aa.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;

import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.component.atemotion.BlogContentTextWatcher;
import com.doschool.component.atemotion.BlogEditText;
import com.doschool.component.choosephoto.Act_PicPreview;
import com.doschool.component.choosephoto.Act_PhotoChoose;
import com.doschool.component.choosephoto.PicBoxAdpter;
import com.doschool.component.updatelater.Task;
import com.doschool.entity.Topic;
import com.doschool.entity.User;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.SpMethods;
import com.doschool.methods.FileMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.PathMethods;
import com.doschool.network.DoUserSever;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这边不想改
 * 心太累
 * 
 * @author 是我的海
 */
public class Act_Write extends Act_CommonOld implements OnClickListener {

	/******** 常量标识 ****************************************/
	private static final int CHOOSE_PHOTO = 1;

	private static final int GOTO_TOPIC = 6;

	private static final int ID_CHOOSE_PICTURE = 13;
	private static final int ID_EMOTION = 14;
	private static final int ID_AT = 16;
	private static final int ID_TOPIC = 17;
	private static final int ID_PARENT = 18;
	

	public static final int START_TYPE_TEXT = 1;
	public static final int START_TYPE_GALLERY = 2;
	public static final int START_TYPE_CAMERA = 3;
	public int startType = 3;
	public int defination = 0;

	/******** 界面组件 ****************************************/
	private BlogEditText blogEditText;
	private TextView mtvTopic;
	private Button btAt,btEmotion,btTopic;
	private LinearLayout llImage;
	private GridView gvPicBox;

	/******** 其他成员 ****************************************/

	private PicBoxAdpter picboxAdpter;
	private int transBlogId = 0;
	private int rootMblogId = 0;
	private String topicData;

	private ArrayList<String> pathList;

	

	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "发表微博";
		topicData=getIntent().getStringExtra("topic");
		startType=getIntent().getIntExtra("startType",START_TYPE_TEXT);
		transBlogId = getIntent().getIntExtra("transBlogId", 0);
		rootMblogId = getIntent().getIntExtra("rootMblogId", 0);
		if (rootMblogId == 0)	rootMblogId = transBlogId;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout mTrueParentLayout=new RelativeLayout(getApplicationContext());
		mParentLayout.addView(mTrueParentLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mTrueParentLayout.setBackgroundResource(R.color.bg_grey);
		
		
		
		LinearLayout upLayout = new LinearLayout(this);
		upLayout.setId("upLayout".hashCode());
		upLayout.setBackgroundResource(R.color.white);
		upLayout.setOrientation(LinearLayout.VERTICAL);
		mTrueParentLayout.addView(upLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		btAt = WidgetFactory.createPicButton(this, R.drawable.img_at, this);
		btEmotion = WidgetFactory.createPicButton(this, R.drawable.img_emotion, this);
		btTopic = WidgetFactory.createPicButton(this, R.drawable.icon_addtopic, this);
		
		
		// 内容文本框
		blogEditText = new BlogEditText(getApplicationContext(), btEmotion, btAt, mParentLayout);
		blogEditText.setBackgroundResource(R.color.white);
		blogEditText.setMaxLines(5);
		blogEditText.setTextColor(Color.BLACK);
		blogEditText.setHint(DoMethods.getRandomHintForWrite(getApplicationContext()));
		LayoutParams lpContent=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpContent.setMargins(mDip*16, mDip*24, mDip*16, 0);
		upLayout.addView(blogEditText, lpContent);
		
		// 表情-艾特-话题
		RelativeLayout llOperate = new RelativeLayout(this);
		llOperate.setPadding(mDip*16, mDip*6, mDip*16, mDip*6);
		upLayout.addView(llOperate, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
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

		// 话题按钮
		btTopic.setId(ID_TOPIC);
		btTopic.setOnClickListener(this);
		RelativeLayout.LayoutParams lpBtTopic=new RelativeLayout.LayoutParams(mDip * 24, mDip * 24);
		lpBtTopic.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		llOperate.addView(btTopic, lpBtTopic);
		
		// 话题文本框
		mtvTopic=WidgetFactory.createTextView(this, "", getResources().getColor(android.R.color.white), 16);
		mtvTopic.setBackgroundResource(R.color.light_greyblue2);
		mtvTopic.setGravity(Gravity.CENTER);
		mtvTopic.setPadding(mDip*4, 0, mDip*4, 0);
		if(topicData!=null &&topicData.length()>1)
		{
			mtvTopic.setText(topicData);
			mtvTopic.setVisibility(View.VISIBLE);
			btTopic.setBackgroundResource(R.drawable.icon_canceltopic);
		}
		else
		{
			mtvTopic.setVisibility(View.INVISIBLE);
		}
		
		RelativeLayout.LayoutParams lpTvTopic=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,mDip * 24);
		lpTvTopic.addRule(RelativeLayout.LEFT_OF,ID_TOPIC);
		llOperate.addView(mtvTopic, lpTvTopic);
		
		// 图片组件
		llImage = new LinearLayout(this);
		llImage.setBackgroundResource(R.color.bg_grey);
		llImage.setOrientation(LinearLayout.HORIZONTAL);
		llImage.setPadding(mDip*16, mDip*16, mDip*16, mDip*16);
		RelativeLayout.LayoutParams lpImage=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpImage.addRule(RelativeLayout.BELOW, "upLayout".hashCode());
		mTrueParentLayout.addView(llImage, lpImage);
		
		gvPicBox=new GridView(Act_Write.this);
		gvPicBox.setColumnWidth((mScrnWidth-32*mDip-4*mDip*5)/5);
		gvPicBox.setHorizontalSpacing(mDip*5);
		gvPicBox.setVerticalSpacing(mDip*5);
		gvPicBox.setNumColumns(5);
		gvPicBox.setStretchMode(GridView.NO_STRETCH);
		llImage.addView(gvPicBox, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		gvPicBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(arg2==pathList.size())
				{
					Intent it=new Intent(Act_Write.this, Act_PhotoChoose.class);
					it.putExtra("maxCount", DoschoolApp.BLOG_PIC_MAX_COUNT);
					it.putExtra("autoFinish", false);
					Bundle b=new Bundle();
					b.putStringArrayList("pathList", pathList);
					it.putExtra("bundle", b);
					startActivityForResult(it,CHOOSE_PHOTO);
				}
				else
				{
					Intent it=new Intent(Act_Write.this, Act_PicPreview.class);
					Bundle b=new Bundle();
					b.putStringArrayList("pathList", pathList);
					it.putExtra("bundle", b);
					it.putExtra("order", arg2);
					startActivityForResult(it,CHOOSE_PHOTO);
				}
			}
		});

		RelativeLayout.LayoutParams lpDown=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lpDown.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mTrueParentLayout.addView(blogEditText.emotionLayout,lpDown);
		mTrueParentLayout.addView(blogEditText.atListLayout,lpDown);
		blogEditText.addTextChangedListener(new BlogContentTextWatcher(null, null, blogEditText, DoschoolApp.BLOG_CONTENT_CHARACTER_NUM));
	
		
		try {
			pathList=getIntent().getBundleExtra("bundle").getStringArrayList("pathList");
		} catch (Exception e) {
			pathList=new ArrayList<String>();
		}

		picboxAdpter=new PicBoxAdpter(Act_Write.this, pathList);
		gvPicBox.setAdapter(picboxAdpter);
		
		if(startType==START_TYPE_TEXT){
			
		}
		else
		{
			Intent it=new Intent(Act_Write.this, Act_PhotoChoose.class);
			it.putExtra("maxCount", DoschoolApp.BLOG_PIC_MAX_COUNT);
			it.putExtra("autoFinish", false);
			Bundle b=new Bundle();
			b.putStringArrayList("pathList", pathList);
			it.putExtra("bundle", b);
			if(startType==START_TYPE_GALLERY)
				it.putExtra("startType", Act_PhotoChoose.START_TYPE_GALLERY);
			else
				it.putExtra("startType", Act_PhotoChoose.START_TYPE_CAMERA);
			startActivityForResult(it,CHOOSE_PHOTO);
		}
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
			Act_Write.this.finish();
			Act_Write.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.confirm:
			
			
			if(pathList.size()>0)
			{
				if(startType==START_TYPE_TEXT)
					MobclickAgent.onEvent(this, "event_picblog_bytext");
				else if(startType==START_TYPE_GALLERY)
					MobclickAgent.onEvent(this, "event_picblog_bygallery");
				else if(startType==START_TYPE_CAMERA)
					MobclickAgent.onEvent(this, "event_picblog_bycamera");
			}
			
			
			
			long timeCha = System.currentTimeMillis() - SpMethods.loadLong(getApplicationContext(), SpMethods.LAST_SENDBLOG_TIME, 0);
			String textContent=blogEditText.getText().toString();
			String jsonContent2=blogEditText.toJMjsonObject().toString();
			
			final String jsonContent=DoMethods.deleteEmptyLine(jsonContent2);
			
			
			boolean isEmpty=true;
			for(int i=0;i<textContent.length();i++)
			{
				if(textContent.charAt(i)!='\n' && textContent.charAt(i)!=' ')
				{
					isEmpty=false;
					break;
				}
			}
			
			if (timeCha < 10000)
				DoMethods.showToast(Act_Write.this, "10秒钟内不能连续发表或转发微博(⊙▽⊙)");
			else if(textContent.length()>DoschoolApp.BLOG_CONTENT_CHARACTER_NUM)
			{
				DoMethods.showToast(Act_Write.this, "微博字数不能超过1000字哦");
			}
			else if(isEmpty)
			{
				if(pathList.size()==0)
				{
					DoMethods.showToast(Act_Write.this, "你还啥都没说呢");
				}
				else
				{
					new AlertDialog.Builder(this).setTitle("只发图，不说点什么吗？")
					.setNegativeButton("补充点文字", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							blogEditText.requestFocus();
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							// 得到InputMethodManager的实例
							if (imm.isActive()) {
								// 如果开启
								imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
								// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
							}
							
						}
					})
					.setPositiveButton("直接发！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							blogEditText.setText(DoMethods.getRandomBlogContentWhenNoPic(Act_Write.this));
							String jsonContent2=blogEditText.toJMjsonObject().toString();
							
							ArrayList<String> imgList=new ArrayList<String>();
							for (int i = 0; i < pathList.size(); i++) {
								imgList.add("path="+pathList.get(i));
							}
							Task task=new Task(DoschoolApp.thisUser.personId, 0, 0, jsonContent2,mtvTopic.getText().toString(), imgList,defination);
							DoschoolApp.mDBHelper.insertTask(task);
							setResult(RESULT_OK);
							Act_Write.this.finish();
							Act_Write.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
							
						}
					})
					.create().show();
				}
			}
			else
			{
				ArrayList<String> imgList=new ArrayList<String>();
				for (int i = 0; i < pathList.size(); i++) {
					imgList.add("path="+pathList.get(i));
				}
				Task task=new Task(DoschoolApp.thisUser.personId, 0, 0, jsonContent,mtvTopic.getText().toString(), imgList,defination);
				DoschoolApp.mDBHelper.insertTask(task);
				setResult(RESULT_OK);
				Act_Write.this.finish();
				Act_Write.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}
				
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GOTO_TOPIC:
				Animation animation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0f);
				mtvTopic.startAnimation(animation);
				mtvTopic.setVisibility(View.VISIBLE);
				mtvTopic.setText(data.getStringExtra("topic"));
				btTopic.setBackgroundResource(R.drawable.icon_canceltopic);
				break;
			case CHOOSE_PHOTO:
				pathList=data.getBundleExtra("bundle").getStringArrayList("pathList");
				defination=data.getIntExtra("defination", 0);
				Log.v("pathList.size="+pathList.size(), "YO");
				picboxAdpter=new PicBoxAdpter(Act_Write.this, pathList);
				gvPicBox.setAdapter(picboxAdpter);
				
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case ID_TOPIC:
			
			if(mtvTopic.getVisibility()==View.VISIBLE)
			{
				btTopic.setBackgroundResource(R.drawable.icon_addtopic);
				mtvTopic.setVisibility(View.INVISIBLE);
				mtvTopic.setText("");
			}
			else
				startActivityForResult(new Intent(Act_Write.this, Act_Topic.class), GOTO_TOPIC);
			DoMethods.addAnimation(v);
			break;
		}
	}
	
	
}
