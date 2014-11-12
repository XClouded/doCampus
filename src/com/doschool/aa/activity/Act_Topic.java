package com.doschool.aa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.adapter.Adp_Blog;
import com.doschool.aa.adapter.Adp_Topic;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.asynctask.RefreshBlogListTask;
import com.doschool.asynctask.RefreshTopicListTask;
import com.doschool.component.push2refresh.PullToRefreshBase;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.component.push2refresh.PullToRefreshBase.OnRefreshListener;
import com.doschool.entity.Microblog;
import com.doschool.entity.Topic;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONArray;

public class Act_Topic extends Act_CommonOld implements OnClickListener{

	
	PullToRefreshListView ptrlv;
	ArrayList<Topic> dataList;
	Adp_Topic adpter;
	
	EditText etTopic;
	Button btConfirm;
	@Override
	public void initData() {
		ACTIONBAR_TITTLE="添加话题";
		ptrlv= new PullToRefreshListView(this);
		dataList= new ArrayList<Topic>();
		adpter = new Adp_Topic(this, dataList);
		
		ptrlv.getRefreshableView().setDivider(null);
		//设置mplv工作模式
		ptrlv.setPullLoadEnabled(false);
		ptrlv.setScrollLoadEnabled(true);
		ptrlv.getRefreshableView().setAdapter(adpter);
		
		ptrlv.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            	
            	new RefreshTopicListTask(ptrlv, adpter, dataList, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
           }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            	new RefreshTopicListTask(ptrlv, adpter, dataList, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		RelativeLayout EditLayout=new RelativeLayout(this);
		EditLayout.setPadding(mDip*16, 0, mDip*16, 0);
		mParentLayout.addView(EditLayout,LayoutParams.MATCH_PARENT,mDip*52);
		mParentLayout.setBackgroundColor(Color.WHITE);
		
		
		etTopic=new EditText(this);
		etTopic.setSingleLine();
		etTopic.setMaxEms(18);
		RelativeLayout.LayoutParams lpetTopic=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, mDip*36);
		lpetTopic.addRule(RelativeLayout.CENTER_VERTICAL);
		lpetTopic.setMargins(0, 0, mDip*68, 0);
		EditLayout.addView(etTopic, lpetTopic);
		etTopic.setTextColor(getResources().getColor(R.color.fzd_black));
		etTopic.setHint("写下一个话题");
		
		btConfirm=WidgetFactory.createButtonWithMainStyle(this, "确定");
		btConfirm.setOnClickListener(this);
		RelativeLayout.LayoutParams lpbtConfirm=new RelativeLayout.LayoutParams( mDip*60, mDip*36);
		lpbtConfirm.addRule(RelativeLayout.CENTER_VERTICAL);
		lpbtConfirm.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		EditLayout.addView(btConfirm, lpbtConfirm);
		ImageView ivShadow=new ImageView(getApplicationContext());
		ivShadow.setImageResource(R.drawable.shadow_fromtop);

		mParentLayout.addView(ivShadow,LayoutParams.MATCH_PARENT,mDip*2);
		
		mParentLayout.addView(ptrlv,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		ptrlv.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it=new Intent();
				it.putExtra("topic", dataList.get(arg2).topic);
				setResult(RESULT_OK, it);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				
			}
		});
		doRefresh(false);
	}

	
	public void doRefresh(final boolean ForceFromNet) {
		// 如果翻到的这页，数据集大小为0
		if (dataList.size() == 0) {
			// 那么想都不要想，先去本地读缓存数据
			try {
				MJSONArray jarrayData = new MJSONArray(SpMethods.loadString(getApplicationContext(), SpMethods.TOPIC_STR_LIST_));
				for (int i = 0; i < jarrayData.length(); i++)
					dataList.add(new Topic(jarrayData.getMJSONObject(i)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			adpter.notifyDataSetChanged();
		}
		// 如果强制联网读，或者读完本地数据还是0，那么必然要联网了
		if (ForceFromNet || dataList.size() == 0)
			ptrlv.doPullRefreshing(true, 0);
	}
	
	
	@Override
	public void onClick(View v) {
		String topic=etTopic.getText().toString();
		topic=topic.replace(" ", "");
		if(topic.length()<=0 )
		{
			DoMethods.showToast(Act_Topic.this, "话题不可为空哦");
		}else if(topic.length()>18)
		{

			DoMethods.showToast(Act_Topic.this, "话题不能多于18个字哦");
		}
		else
		{
			Intent it=new Intent();
			it.putExtra("topic", topic);
			setResult(RESULT_OK, it);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		}
	}
	
	
	

}
