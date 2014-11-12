package com.doschool.asynctask;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.widget.Toast;

import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.adapter.Adp_Standard;
import com.doschool.app.DoschoolApp;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.entity.Scrip;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;

/**
 * 所有微博列表的刷新，都可统一于此。
 * 完成度：100%
 * @author 是我的海
 */
public class RefreshScipListTask extends AsyncTask<Void, Void, Integer> {
	
	private PullToRefreshListView listview;
	private ArrayList<Scrip> data;
	private Adp_Standard adapter;
	private boolean isMore;
	

	public RefreshScipListTask(PullToRefreshListView listview, ArrayList<Scrip> data, Adp_Standard adapter, boolean isMore) {
		super();
		this.listview = listview;
		this.data = data;
		this.adapter = adapter;
		this.isMore = isMore;
	}

	//后台从网络拉一列数据，进行添加
	@Override
	protected Integer doInBackground(Void... params) {
		
			int lastBID=0;
			if(isMore!=false && data.size()>0)
				lastBID=data.get(data.size()-1).xId;
			
			MJSONObject jobjReceive = DoUserSever.UserScripListGet(DoschoolApp.thisUser.personId, lastBID, 10);
			
			if(jobjReceive.getInt("code", 9)==0)
			{
				//联网刷新成功
				MJSONArray jarrayData;
				jarrayData = jobjReceive.getMJSONArray("data");
				if(isMore==false)
				{
					SpMethods.saveLong(listview.getContext(), SpMethods.SCRIP_TIME_LIST, System.currentTimeMillis());
					SpMethods.saveString(listview.getContext(), SpMethods.SCRIP_STR_LIST, jarrayData.toString());	
					data.clear();
				}
				for (int i = 0; i < jarrayData.length(); i++)
					data.add(new Scrip(jarrayData.getMJSONObject(i)));
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
		else if(result==0)
			listview.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(System.currentTimeMillis()));
		
		
		if(isMore)
			listview.onPullUpRefreshComplete();
		else
			listview.onPullDownRefreshComplete();
		super.onPostExecute(result);
	}
}

