package com.doschool.aa.activity.Oneblog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.doschool.R;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.adapter.Adp_Blog;
import com.doschool.aa.item.Blog_Item_Bottom;
import com.doschool.aa.item.Blog_Item_Content;
import com.doschool.aa.item.Blog_Item_Header;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Microblog;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoBlogSever;
import com.doschool.zother.MJSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 微博广场ListView的Item布局
 * 
 * @author 是我的海
 */

public class OneBlog_Blog extends LinearLayout {

	/******** 常量标识 ****************************************/
	private final static int TAG_BLOG = 3;

	/******** 界面组件 ****************************************/
	private Blog_Item_Header item_Header; // 头像-昵称-时间-活动
	private Blog_Item_Content item_Content; // 微博图片
	private Blog_Item_Content item_Trans; // 根微博
	private Blog_Item_Bottom item_Bottom; // 底数

	/******** 数据等 ****************************************/
	private Microblog blogData;
	ArrayList<Microblog> sayDataSet;
	Adp_Blog adpt;
	int position;

	// 初始化界面
	public OneBlog_Blog(Context context) {
		super(context);
		init();
	}

	public OneBlog_Blog(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_blog, this);
		item_Header = (Blog_Item_Header) findViewById(R.id.blog_item_header);
		item_Content = (Blog_Item_Content) findViewById(R.id.blog_item_content);
		item_Content.isOneBlog=true;
		item_Trans = (Blog_Item_Content) findViewById(R.id.blog_item_trans);
		item_Trans.isOneBlog=true;
		item_Bottom = (Blog_Item_Bottom) findViewById(R.id.blog_item_footer);
	}

	// 注入数据
	public void setNoData(Microblog data, ArrayList<Microblog> sayDataSet, int position, Adp_Blog adpt, boolean isLoadOnlyFromCache) {
		this.sayDataSet = sayDataSet;
		this.position = position;
		this.adpt = adpt;
		blogData = data;
		
		item_Header.updateUI(blogData);
		item_Content.updateUI(blogData);
		item_Trans.updateUI(blogData.rootBlog);
		item_Bottom.updateUI(blogData,new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
							InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							// 得到InputMethodManager的实例
							if (imm.isActive()) {
								// 如果开启
								imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
								// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
							}
						
					}
				}, 300);
				
			}
		});

	}

	public void callRefresh()
	{
		new RefreshBolg().execute();
	}
	
	
	public class RefreshBolg extends AsyncTask<Void, Void, Void> {
		MJSONObject jResult;
		@Override
		protected Void doInBackground(Void... params) {

			jResult = DoBlogSever.MicroblogDetailGet(DoschoolApp.thisUser.personId, blogData.blogId);
			blogData = new Microblog(jResult.getMJSONObject("data"));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			int code = jResult.getInt("code", 9);
			String toast = jResult.getString("data");
			if (code == 0) {
				setNoData(blogData, null, 0, null, false);
			} else {
				if (toast.length() == 0) {
					if (code == 1)
						toast = "无此微博";
					else if (code == 9)
						toast = "网络服务错误";
				}
				DoMethods.showToast(getContext(), toast);
			}
			
			
		}
	}

	public void addCommentCount() {
		blogData.commentCount++;
		item_Bottom.getCommentCountTextView().setText(blogData.commentCount + "");
	}

	public void addTransCount() {
		blogData.transCount++;
		item_Bottom.getTransCountTextView().setText(blogData.transCount + "");
	}

	public void setCommentCount(int count) {
		blogData.commentCount = count;
		item_Bottom.getCommentCountTextView().setText(blogData.commentCount + "");
	}

	public void setTransCount(int count) {
		blogData.transCount = count;
		item_Bottom.getTransCountTextView().setText(blogData.transCount + "");
	}

}
