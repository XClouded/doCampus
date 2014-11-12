package com.doschool.aa.activity;

import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.adapter.Adp_SearchPerson;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoUserSever;

import android.widget.EditText;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Act_SearchPerson extends Act_CommonOld{
	
	
	/******** 微博列表三金刚  ****************************************/
	private PullToRefreshListView mplvPerson;	
	private Adp_SearchPerson adptPerson;			
	private ArrayList<SimplePerson> personList;		
		
	/******** 数据  ****************************************/
	

	@Override
	public void initData() {
		ACTIONBAR_TITTLE="搜索用户";
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mplvPerson=new PullToRefreshListView(this);
		mplvPerson.setEnabled(false);
		mParentLayout.addView(mplvPerson,mScrnWidth, LayoutParams.MATCH_PARENT);
		personList=new ArrayList<SimplePerson>();
		adptPerson=new Adp_SearchPerson(this, personList);
		mplvPerson.getRefreshableView().setAdapter(adptPerson);
		clickSearch();
	}
	
	
	public void clickSearch()
	{
		final EditText metSearch=WidgetFactory.createEditText(this, "请输入昵称", R.color.black, 0);
		new AlertDialog.Builder(this).setTitle("搜索用户").
		setView(metSearch).setPositiveButton("搜索", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
            	if(metSearch.getText().toString().length()>=1)
            		new SearchTask(metSearch.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            	else
            		clickSearch();
            }
        }).setNegativeButton("取消", null).show();
	}
	
	// 更新微博列表
	private class SearchTask extends AsyncTask<Void, Void, MJSONObject> {
		
		ProgressDialog progressDialog;
		String searchStr;
		public SearchTask(String str)
		{
			this.searchStr=str;
			progressDialog=new ProgressDialog(Act_SearchPerson.this);
			progressDialog.setMessage("不紧不慢地搜索ing");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		
		//后台从网络拉一列数据，进行添加
		@Override
		protected MJSONObject doInBackground(Void... params) {
			return DoUserSever.UserSearch(DoschoolApp.thisUser.personId, searchStr);
		}

		//更新界面
		@Override
		protected void onPostExecute(MJSONObject result) {
			progressDialog.cancel();
			
			
			if(result.getInt("code",9)==0)
			{
				MJSONArray jsonArray=result.getMJSONArray("data");
						
				personList.clear();
				
				if(jsonArray.length()==0)
					DoMethods.showToast(Act_SearchPerson.this, "找不到这样的用户");
				else
					for (int i = 0; i < jsonArray.length(); i++)
						personList.add(new SimplePerson(jsonArray.getMJSONObject(i)));
			}
			else
			{
				DoMethods.showToast(Act_SearchPerson.this, "网络错误=_=");
			}
			adptPerson.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fgm_search, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.search:
			clickSearch();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
