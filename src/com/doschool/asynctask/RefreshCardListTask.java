package com.doschool.asynctask;

import android.os.AsyncTask;
import android.widget.Toast;

import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.adapter.Adp_Standard;
import com.doschool.app.DoschoolApp;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.entity.Person;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;

/**** 更新名片列表的线程**********/
public class RefreshCardListTask extends AsyncTask<Void, Void, Void> {

	private PullToRefreshListView  mRefreshedView;
	private Adp_Standard adapter;
	MJSONObject jResult;
	
	//初始构造函数
	public RefreshCardListTask(PullToRefreshListView  refreshedView,Adp_Standard adapter) {
		this.mRefreshedView = refreshedView;
		this.adapter=adapter;
	}

	//后台从网络拉一列数据，进行添加
	@Override
	protected Void doInBackground(Void... params) {
		jResult = DoUserSever.UserFriendAndCardListGet(DoschoolApp.thisUser.personId,1);
			return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		adapter.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}
	
	//更新界面
	@Override
	protected void onPostExecute(Void result) {
		adapter.notifyDataSetChanged();
		int code = jResult.getInt("code", 9);
		String toast = jResult.getString("data");
		if (code == 0) {
			//联网刷新成功
			MJSONArray mjarr=jResult.getMJSONArray("data");
			mRefreshedView.setLastUpdatedLabel(ConvertMethods.dateLongToDiyStr(System.currentTimeMillis()));
			SpMethods.saveLong(mRefreshedView.getContext(), SpMethods.CARD_LIST_UPDATE_TIME, System.currentTimeMillis());
			SpMethods.saveString(mRefreshedView.getContext(), SpMethods.CARD_LIST_STR, mjarr.toString());
			DoschoolApp.cardsList.clear();
			for (int i = 0; i < mjarr.length(); i++)
					DoschoolApp.cardsList.add(new Person(mjarr.getMJSONObject(i)));
		} else {
			if (toast.length() == 0) {
				if (code == 1)
					toast = "发起人不存在";
				else if (code == 2)
					toast = "错误的类型";
				else if (code == 9)
					toast = "网络服务错误";
			}
			DoMethods.showToast(mRefreshedView.getContext(), toast);
		}

		mRefreshedView.onPullDownRefreshComplete();
		super.onPostExecute(result);
	}
}
