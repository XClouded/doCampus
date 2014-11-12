package com.doschool.aa.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.aa.Act_Common_Linear;
import com.doschool.aa.activity.Act_PersonPage.sendCardTask;
import com.doschool.aa.activity.Oneblog.OneBlog_Blog;
import com.doschool.aa.activity.Oneblog.Oneblog_Cmtbox;
import com.doschool.aa.activity.Oneblog.Oneblog_LaunchCmt;
import com.doschool.aa.widget.MyDialog;
import com.doschool.aa.widget.P2RScrollView;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.entity.Microblog;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoBlogSever;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class Act_OneBlog extends Act_Common_Linear  {

	/******** 界面组件 ****************************************/
	public P2RScrollView mScrollView;
	public OneBlog_Blog blogInfoLayout; // 微博面板
	public Oneblog_Cmtbox cmtListLayout; // 评论面板
	public Oneblog_LaunchCmt makeCmtBox; // 评论框

	/******** 数据等 ****************************************/
	public Microblog thisBlogdata;
	public int cmtObjId;
	public String cmtObjNick;
	public boolean isOpenKeyboard;

	/******** 初始化数据 ********/
	@Override
	public void initData() {
		thisBlogdata = (Microblog) MySession.getSession().get("microblog");
		MySession.getSession().remove("microblog");
		cmtObjId = thisBlogdata.author.personId;
		cmtObjNick = thisBlogdata.author.nickName;
		isOpenKeyboard = getIntent().getBooleanExtra("isOpenKeyboard", false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBarM().setHomeBtnAsBack(this);

		getActionBarM().addOperateButton(R.drawable.sab_transend, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(DoschoolApp.isGuest())
				{
					MyDialog.popURGuest(Act_OneBlog.this);
				}
				else
				{
				MySession.getSession().put("microblog", thisBlogdata);
				Intent intent = new Intent(Act_OneBlog.this, Act_Trans.class);
				startActivityForResult(intent, ActivityName.CODE_ACT_ONEBLOG);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				}
				
			}
		});
		if(thisBlogdata.author.personId==DoschoolApp.thisUser.personId)
		{
		getActionBarM().addOperateButton(R.drawable.sab_delete, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popDeleteDialog();
			}
		});
		}
		
		mScrollView = new P2RScrollView(this);
		mParent.addView(mScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 100));

		LinearLayout llContent = new LinearLayout(this);
		llContent.setOrientation(LinearLayout.VERTICAL);
		mScrollView.addView(llContent, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		mScrollView.setMode(Mode.BOTH);
		mScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				
				if (refreshView.isHeaderShown()) {
					refreshView.onRefreshComplete();
					blogInfoLayout.callRefresh();
					cmtListLayout.callRefresh(thisBlogdata, false);
				} else {
					refreshView.onRefreshComplete();
					cmtListLayout.callRefresh(thisBlogdata, true);
				}
			}
		});

		blogInfoLayout = new OneBlog_Blog(this);
		llContent.addView(blogInfoLayout);

		cmtListLayout = new Oneblog_Cmtbox(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(mDip * 8, mDip * 8, mDip * 8, mDip * 8);
		llContent.addView(cmtListLayout, lp);

		blogInfoLayout.setNoData(thisBlogdata, null, 0, null, false);
		cmtListLayout.callRefresh(thisBlogdata, false);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (isOpenKeyboard) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// 得到InputMethodManager的实例
					if (imm.isActive()) {
						// 如果开启
						imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
						// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
					}
				}
			}
		}, 300);

		makeCmtBox = new Oneblog_LaunchCmt(this, thisBlogdata);
		makeCmtBox.resetCmtBoxOnCmtItemClicked();
		mParent.addView(makeCmtBox, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.act_oneblog, menu);
		if (thisBlogdata.author.personId != DoschoolApp.thisUser.personId)
			menu.getItem(0).setVisible(false);
		menu.getItem(2).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ActivityName.CODE_ACT_ONEBLOG && resultCode == RESULT_OK)
			blogInfoLayout.addTransCount();
	}

	// 名片发送，已发送弹窗
	public void popDeleteDialog() {
		new AlertDialog.Builder(this).setTitle("你要删除这条微博吗？").setNegativeButton("取消", null).setPositiveButton("删除", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				new DeleteBlogTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}).show();
	}

	/**** 删除微博任务 **********/
	class DeleteBlogTask extends AsyncTask<Void, Void, Integer> {
		ProgressDialog progressDialog;

		protected void onPreExecute() {
			// 转圈Dialogue
			progressDialog = new ProgressDialog(Act_OneBlog.this);
			progressDialog.setMessage("正在删除微博");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			return DoBlogSever.MicroblogDelete(DoschoolApp.thisUser.personId, thisBlogdata.blogId).getInt("code", 9);

		}

		@Override
		protected void onPostExecute(Integer state) {
			progressDialog.dismiss();
			// 处理各状态
			if (state == 0) {
				thisBlogdata.blogId = -1;
				DoMethods.showToast(Act_OneBlog.this, "微博已经被删除！");
				Act_OneBlog.this.finish();
			} else {
				DoMethods.showToast(Act_OneBlog.this, "童鞋，你账号或密码还空着哟T");
			}

		}
	}

	/**
	 * 评论成功之后，增加评论数，刷新评论列表
	 */
	public void callRefreshAfterCommentSucceed() {
		blogInfoLayout.addCommentCount();
		cmtListLayout.callRefresh(thisBlogdata, true);
	}

	/**
	 * 当点击评论列表中的item之后，更改评论对象
	 */
	public void onCmtItemClick(int objId, String objNick) {
		this.cmtObjId = objId;
		this.cmtObjNick = objNick;
		makeCmtBox.resetCmtBoxOnCmtItemClicked();

	}


}
