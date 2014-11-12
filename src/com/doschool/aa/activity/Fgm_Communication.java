package com.doschool.aa.activity;

import com.doschool.R;
import com.doschool.aa.aa.Fgm_TabPager;
import com.doschool.aa.adapter.Adp_Card;
import com.doschool.aa.adapter.Adp_Friend;
import com.doschool.aa.widget.TabPagerSpec;
import com.doschool.aa.widget.TaskLayout;
import com.doschool.app.DoschoolApp;
import com.doschool.asynctask.RefreshCardListTask;
import com.doschool.asynctask.RefreshFriendListTask;
import com.doschool.component.push2refresh.PullToRefreshBase;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.component.push2refresh.PullToRefreshBase.OnRefreshListener;
import com.doschool.component.updatelater.PostLaterService;
import com.doschool.component.updatelater.TaskManage;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONArray;
import com.umeng.analytics.MobclickAgent;


import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

public class Fgm_Communication extends Fgm_TabPager {

	/******** 核心****************************************/
	PullToRefreshListView mplvFriends;
	PullToRefreshListView mplvCards;
	Adp_Friend adptFriends;
	Adp_Card adptCards;
	

	@Override
	public void initData() {
		indicateList = new String[] { "会话", "好友" };
		isHideWhenBrowse = false;
	}
	
	@Override
	public void addViewToFgm() {
		super.addViewToFgm();
		getActionBar().setTittle("口袋小安");

	}
	
	@Override
	public TabPagerSpec[] giveTabPagerSpec() {
		//新建mplv
		mplvFriends= new PullToRefreshListView(getActivity());
		mplvCards= new PullToRefreshListView(getActivity());

		mplvFriends.setDividerDrawable(getResources().getDrawable(R.color.bg_grey));
		mplvCards.setDividerDrawable(getResources().getDrawable(R.color.bg_grey));
		
		//回忆上一次刷新时间
		long friendLastRefreshTime = SpMethods
				.loadLong(getActivity(), SpMethods.FRIEND_LIST_UPDATE_TIME, 0);
		mplvFriends.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(friendLastRefreshTime));
		long cardLastRefreshTime = SpMethods
				.loadLong(getActivity(), SpMethods.CARD_LIST_UPDATE_TIME, 0);
		mplvCards.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(cardLastRefreshTime));
		
		//设置mplv工作模式
		mplvFriends.setPullLoadEnabled(false);
		mplvFriends.setScrollLoadEnabled(false);
		mplvCards.setPullLoadEnabled(false);
		mplvCards.setScrollLoadEnabled(false);
		
		//设置mplv的适配器
		adptFriends =new Adp_Friend(getActivity(), DoschoolApp.friendList);
		adptCards =new Adp_Card(getActivity(), DoschoolApp.cardsList);
		mplvFriends.getRefreshableView().setAdapter(adptFriends);
		mplvCards.getRefreshableView().setAdapter(adptCards);
		
		//设置mplv滑动监听
		mplvCards.setOnScrollListener(new onListViewScrollListener(DoschoolApp.newImageLoader, true, true));
		mplvFriends.setOnScrollListener(new onListViewScrollListener(DoschoolApp.newImageLoader, true, true));
		
		mplvFriends.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            	new RefreshFriendListTask(mplvFriends, adptFriends).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
		
		mplvCards.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            	new RefreshCardListTask(mplvCards, adptCards).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

		TabPagerSpec[] specList = new TabPagerSpec[2];
		specList[0]=new TabPagerSpec(mplvFriends, "会话");
		specList[1]=new TabPagerSpec(mplvCards, "好友");
		return specList;
	}

	
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mViewPager.setCurrentItem(0);
		doRefreshFriends(true);
		super.onActivityCreated(savedInstanceState);
	}
	
	
	public void doRefreshFriends(final boolean FromNet)
	{
		try {
			MJSONArray mjarr = new MJSONArray(SpMethods.loadString(getActivity(), SpMethods.FRIEND_LIST_STR));
			DoschoolApp.friendList.clear();
			for (int i = 0; i < mjarr.length(); i++)
				DoschoolApp.friendList.add(new SimplePerson(mjarr.getMJSONObject(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		adptFriends.notifyDataSetChanged();
		if(DoschoolApp.friendList.size()==0)
			mplvFriends.doPullRefreshing(true, 0);
	}
	
	public void doRefreshCards(final boolean FromNet)
	{
		//先读本地
		try {
			MJSONArray mjarr = new MJSONArray(SpMethods.loadString(getActivity(), SpMethods.CARD_LIST_STR));
			DoschoolApp.cardsList.clear();
			for (int i = 0; i < mjarr.length(); i++)
				DoschoolApp.cardsList.add(new Person(mjarr.getMJSONObject(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		adptCards.notifyDataSetChanged();
		if(DoschoolApp.cardsList.size()==0)
			mplvCards.doPullRefreshing(true, 0);
	}


	@Override
	public void onPageChanged(int newIndex) {

		if(newIndex==0 && DoschoolApp.friendList.size()==0)
			doRefreshFriends(false);
		else if(newIndex==1 && DoschoolApp.cardsList.size()==0)
			doRefreshCards(false);

	}

	
	public void onResume() {
	    super.onResume();
	    if(currentPageIndex==0)
		    adptFriends.notifyDataSetChanged();
	}
	public void onPause() {
		 if(currentPageIndex==0)
			    adptCards.notifyDataSetChanged();
	    super.onPause();
	}



}
