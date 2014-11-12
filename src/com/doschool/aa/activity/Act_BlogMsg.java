package com.doschool.aa.activity;

import java.util.ArrayList;
import com.doschool.aa.aa.Act_Common_Linear;
import com.doschool.aa.adapter.Adp_Messages;
import com.doschool.app.DoschoolApp;
import com.doschool.asynctask.RefreshBlogListTask;
import com.doschool.asynctask.RefreshMsgListTask;
import com.doschool.component.push2refresh.PullToRefreshBase;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.component.push2refresh.PullToRefreshBase.OnRefreshListener;
import com.doschool.entity.Message;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONArray;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

public class Act_BlogMsg extends Act_Common_Linear {

	/******** 界面组件 ****************************************/

	public static final int RESULT_LOGOUT = 100;
	PullToRefreshListView mplvMessage;
	Adp_Messages adptMessage;
	ArrayList<Message> MessageDataSet;

	/******** 数据等 ****************************************/
	/******** 初始化数据 ********/
	@Override
	public void initData() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBarM().setHomeBtnAsBack(this);
		getActionBarM().setTittle("消息盒子");

		// 新建mplv
		mplvMessage = new PullToRefreshListView(this);

		mplvMessage.getRefreshableView().setDivider(null);

		// 回忆上一次刷新时间
		long msgLastRefreshTime = SpMethods.loadLong(this, SpMethods.FRIEND_LIST_UPDATE_TIME, 0);
		mplvMessage.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(msgLastRefreshTime));

		// 设置mplv工作模式
		mplvMessage.setPullLoadEnabled(true);
		mplvMessage.setScrollLoadEnabled(false);

		// 设置mplv的适配器
		MessageDataSet = new ArrayList<Message>();

		adptMessage = new Adp_Messages(this, MessageDataSet);

		mplvMessage.getRefreshableView().setAdapter(adptMessage);

		mplvMessage.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new RefreshMsgListTask(mplvMessage, MessageDataSet, adptMessage, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				new RefreshMsgListTask(mplvMessage, MessageDataSet, adptMessage, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				//
			}
		});
		mParent.addView(mplvMessage, mScrnWidth, LayoutParams.MATCH_PARENT);

		doRefreshMessage(true);
	}

	public void doRefreshMessage(final boolean FromNet) {
		try {
			MJSONArray jarrayData = new MJSONArray(SpMethods.loadString(this, SpMethods.MSG_STR_LIST));
			MessageDataSet.clear();
			for (int i = 0; i < jarrayData.length(); i++)
				MessageDataSet.add(new Message(jarrayData.getMJSONObject(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		adptMessage.notifyDataSetChanged();
		if (MessageDataSet.size() == 0)
			mplvMessage.doPullRefreshing(true, 0);
	}

}
