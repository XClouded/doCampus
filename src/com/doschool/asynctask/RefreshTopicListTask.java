package com.doschool.asynctask;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.widget.Toast;

import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.adapter.Adp_Standard;
import com.doschool.app.DoschoolApp;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.entity.Microblog;
import com.doschool.entity.Topic;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoBlogSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;

/**
 * 所有微博列表的刷新，都可统一于此。
 * 完成度：100%
 * @author 是我的海
 */
public class RefreshTopicListTask extends AsyncTask<Void, Void, Integer> {
	
	private PullToRefreshListView listview;
	private ArrayList<Topic> data;
	private Adp_Standard adapter;
	
	private boolean isMore;
	
	//初始构造函数
	public RefreshTopicListTask(PullToRefreshListView refreshedView,Adp_Standard adapter, ArrayList<Topic> data,boolean isMore) {
		this.listview = refreshedView;
		this.data=data;
		this.adapter=adapter;
		this.isMore=isMore;
	}

	
	//后台从网络拉一列数据，进行添加
	@Override
	protected Integer doInBackground(Void... params) {
		
			int lastBID=0;
			if(isMore!=false && data.size()>0)
				lastBID=data.get(data.size()-1).tid;
			
			MJSONObject jobjReceive = DoBlogSever.MicroblogTopicListGet(lastBID, 16);
			if(jobjReceive.getInt("code", 9)==0)
			{
				//联网刷新成功
				MJSONArray jarrayData;
				jarrayData = jobjReceive.getMJSONArray("data");
				if(isMore==false)
				{
					SpMethods.saveLong(listview.getContext(), SpMethods.TOPIC_TIME_LIST_, System.currentTimeMillis());
					SpMethods.saveString(listview.getContext(), SpMethods.TOPIC_STR_LIST_, jarrayData.toString());
					data.clear();
				}
				
				
				for (int i = 0; i < jarrayData.length(); i++)
					data.add(new Topic(jarrayData.getMJSONObject(i)));
				return 0;
			}
			else 
				//联网刷新失败
				return jobjReceive.getInt("code", 9);
		
			
	}

	//更新界面
	@Override
	protected void onPostExecute(Integer result) {
		adapter.notifyDataSetChanged();
		if(result!=0)
			DoMethods.showToast(listview.getContext(), "网络错误");
		if(isMore)
			listview.onPullUpRefreshComplete();
		else
			listview.onPullDownRefreshComplete();
		super.onPostExecute(result);
	}
}

