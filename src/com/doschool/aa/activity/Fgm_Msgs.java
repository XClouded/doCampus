package com.doschool.aa.activity;

import java.util.ArrayList;
import com.doschool.app.DoschoolApp;
import com.doschool.asynctask.RefreshMsgListTask;
import com.doschool.asynctask.RefreshRequestListTask;
import com.doschool.asynctask.RefreshScipListTask;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONArray;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.aa.aa.Fgm_TabPager;
import com.doschool.aa.adapter.Adp_Messages;
import com.doschool.aa.adapter.Adp_Request;
import com.doschool.aa.adapter.Adp_Scrip;
import com.doschool.aa.widget.TabPagerSpec;
import com.doschool.component.push2refresh.PullToRefreshBase;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.component.push2refresh.PullToRefreshBase.OnRefreshListener;
import com.doschool.entity.FriendRequest;
import com.doschool.entity.Message;
import com.doschool.entity.Scrip;

import android.widget.ListView;

public class Fgm_Msgs extends Fgm_TabPager {

	/******** 常量标识 ****************************************/
	private final static int ID_MENU = 0;

	/******** 核心 ****************************************/
	PullToRefreshListView mplvMessage;
	PullToRefreshListView mplvReuest;
	PullToRefreshListView mplvScrip;
	
	Adp_Messages adptMessage;
	Adp_Request adptRequest;
	Adp_Scrip adptScrip;
	
	ArrayList<FriendRequest> requestDataSet;
	ArrayList<Message> MessageDataSet;
	ArrayList<Scrip> ScripDataSet;
	int showItem;
	
	
	 public void showWhichItem(int n)
	 {
		 showItem=n;
	 }
	
	

	@Override
	public void initData() {
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			((SlidingFragmentActivity)getActivity()).getSlidingMenu().showMenu();
			break;
			}
			
		return super.onOptionsItemSelected(item);
	}
	


		@Override
		public TabPagerSpec[] giveTabPagerSpec() {
			// 新建mplv
			mplvMessage = new PullToRefreshListView(getActivity());
			mplvScrip = new PullToRefreshListView(getActivity());
			mplvReuest = new PullToRefreshListView(getActivity());

			mplvMessage.getRefreshableView().setDivider(null);
			mplvScrip.getRefreshableView().setDivider(null);
			mplvScrip.getRefreshableView().setDivider(null);
			
			// 回忆上一次刷新时间
			long msgLastRefreshTime = SpMethods.loadLong(getActivity(), SpMethods.FRIEND_LIST_UPDATE_TIME, 0);
			mplvMessage.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(msgLastRefreshTime));
			long scripLastRefreshTime = SpMethods.loadLong(getActivity(), SpMethods.SCRIP_TIME_LIST, 0);
			mplvScrip.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(scripLastRefreshTime));
			long reqLastRefreshTime = SpMethods.loadLong(getActivity(), SpMethods.REQ_TIME_LIST, 0);
			mplvReuest.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(reqLastRefreshTime));

			// 设置mplv工作模式
			mplvMessage.setPullLoadEnabled(true);
			mplvMessage.setScrollLoadEnabled(false);
			mplvScrip.setPullLoadEnabled(true);
			mplvScrip.setScrollLoadEnabled(false);
			mplvReuest.setPullLoadEnabled(true);
			mplvReuest.setScrollLoadEnabled(false);

			// 设置mplv的适配器
			requestDataSet = new ArrayList<FriendRequest>();
			ScripDataSet = new ArrayList<Scrip>();
			MessageDataSet = new ArrayList<Message>();

			adptMessage = new Adp_Messages(getActivity(), MessageDataSet);
			adptScrip = new Adp_Scrip(getActivity(), ScripDataSet);
			adptRequest = new Adp_Request(getActivity(), requestDataSet);

			mplvMessage.getRefreshableView().setAdapter(adptMessage);
			mplvScrip.getRefreshableView().setAdapter(adptScrip);
			mplvReuest.getRefreshableView().setAdapter(adptRequest);

			
			//设置mplv滑动监听
			mplvMessage.setOnScrollListener(new PauseOnScrollListener(DoschoolApp.newImageLoader, true, true));
			mplvScrip.setOnScrollListener(new PauseOnScrollListener(DoschoolApp.newImageLoader, true, true));
			mplvReuest.setOnScrollListener(new PauseOnScrollListener(DoschoolApp.newImageLoader, true, true));

			
			
			mplvScrip.setOnRefreshListener(new OnRefreshListener<ListView>() {
	            @Override
	            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	new RefreshScipListTask(mplvScrip, ScripDataSet, adptScrip, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	            @Override
	            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	            	new RefreshScipListTask(mplvScrip, ScripDataSet, adptScrip, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	        });
			mplvReuest.setOnRefreshListener(new OnRefreshListener<ListView>() {
	            @Override
	            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	new RefreshRequestListTask(mplvReuest, requestDataSet, adptRequest, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	            @Override
	            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	new RefreshRequestListTask(mplvReuest, requestDataSet, adptRequest, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	        });
			mplvMessage.setOnRefreshListener(new OnRefreshListener<ListView>() {
	            @Override
	            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	new RefreshMsgListTask(mplvMessage, MessageDataSet, adptMessage, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	            @Override
	            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	            	new RefreshMsgListTask(mplvMessage, MessageDataSet, adptMessage, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	        });
			
			TabPagerSpec[] specList = new TabPagerSpec[3];
			specList[0]=new TabPagerSpec(mplvMessage, "消息");
			specList[1]=new TabPagerSpec(mplvScrip, "小纸条");
			specList[2]=new TabPagerSpec(mplvReuest, "请求");
			return specList;
		}
	
		
	public void doRefreshMessage(final boolean FromNet)
	{
		try {
			MJSONArray jarrayData = new MJSONArray(SpMethods.loadString(getActivity(), SpMethods.MSG_STR_LIST));
			MessageDataSet.clear();
			for (int i = 0; i < jarrayData.length(); i++)
				MessageDataSet.add(new Message(jarrayData.getMJSONObject(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		adptMessage.notifyDataSetChanged();
		if(MessageDataSet.size()==0)
			mplvMessage.doPullRefreshing(true, 0);
	}
	
	public void doRefreshReuest(final boolean FromNet)
	{
//		try {
//			MJSONArray jarrayData = new MJSONArray(DongSpMethods.loadString(getActivity(), DongSpMethods.REQ_STR_LIST));
//			requestDataSet.clear();
//			for (int i = 0; i < jarrayData.length(); i++)
//				requestDataSet.add(new FriendRequest(jarrayData.getMJSONObject(i)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		adptRequest.notifyDataSetChanged();
//		if(requestDataSet.size()==0)
			mplvReuest.doPullRefreshing(true, 0);
	}
	
	public void doRefreshCards(final boolean FromNet)
	{
		try {
			MJSONArray jarrayData = new MJSONArray(SpMethods.loadString(getActivity(), SpMethods.SCRIP_STR_LIST));
			ScripDataSet.clear();
			for (int i = 0; i < jarrayData.length(); i++)
				ScripDataSet.add(new Scrip(jarrayData.getMJSONObject(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		adptScrip.notifyDataSetChanged();
		if(ScripDataSet.size()==0)
			mplvScrip.doPullRefreshing(true, 0);
	}


	@Override
	public void onPageChanged(int newIndex) {

		if (newIndex == 0 && MessageDataSet.size() == 0)
			doRefreshMessage(false);
		else if (newIndex == 1 && ScripDataSet.size() == 0)
			doRefreshCards(false);
		else if (newIndex == 2 && requestDataSet.size() == 0)
			doRefreshReuest(false);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mViewPager.setCurrentItem(0);
		doRefreshMessage(true);
		super.onActivityCreated(savedInstanceState);
	}
	

}
