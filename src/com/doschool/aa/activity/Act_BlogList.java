package com.doschool.aa.activity;

import java.util.ArrayList;
import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.adapter.Adp_Blog;
import com.doschool.aa.widget.TaskLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.asynctask.RefreshBlogListTask;
import com.doschool.component.push2refresh.PullToRefreshBase;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.component.push2refresh.PullToRefreshBase.OnRefreshListener;
import com.doschool.component.updatelater.PostLaterService;
import com.doschool.component.updatelater.TaskManage;
import com.doschool.entity.Microblog;
import com.doschool.entity.SimplePerson;

import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 这个应该没啥可改的了
 * 
 * @author 是我的海
 */
public class Act_BlogList extends Act_CommonOld {

	/******** 界面组件 ****************************************/
	PullToRefreshListView ptrlv;
	ArrayList<Microblog> dataList;
	Adp_Blog adpter;

	/******** 数据其他 ****************************************/
	private SimplePerson personData;

	@Override
	public void initData() {

		personData = (SimplePerson) MySession.getSession().get("person");
		MySession.getSession().remove("person");
		ACTIONBAR_TITTLE = personData.nickName;

		ptrlv = new PullToRefreshListView(this);
		dataList = new ArrayList<Microblog>();
		adpter = new Adp_Blog(this, dataList);
		

		ptrlv.getRefreshableView().setDivider(null);
		// 设置mplv工作模式
		ptrlv.setPullLoadEnabled(false);
		ptrlv.setScrollLoadEnabled(true);

		ptrlv.getRefreshableView().setAdapter(adpter);
		ptrlv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				new RefreshBlogListTask(ptrlv, adpter, dataList, 5,
						personData.personId, false)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new RefreshBlogListTask(ptrlv, adpter, dataList, 5,
						personData.personId, true)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mParentLayout.addView(ptrlv, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		ptrlv.doPullRefreshing(true, 0);
		

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onResume() {
		
		for (int i = 0; i < dataList.size(); i++)
			if (dataList.get(i).blogId == -1) {
				dataList.remove(i);
				Log.v("onResume_" + i, "onResume");

			}
		adpter.notifyDataSetChanged();
		super.onResume();
	}

}
