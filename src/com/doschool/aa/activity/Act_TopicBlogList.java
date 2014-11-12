package com.doschool.aa.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.adapter.Adp_Blog;
import com.doschool.aa.widget.MyDialog;
import com.doschool.aa.widget.TaskLayout;
import com.doschool.methods.ConvertMethods;
import com.doschool.zother.MJSONObject;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.asynctask.RefreshBlogListTask;
import com.doschool.asynctask.RefreshFriendListTask;
import com.doschool.component.choosephoto.Act_PhotoChoose;
import com.doschool.component.push2refresh.PullToRefreshBase;
import com.doschool.component.push2refresh.PullToRefreshListView;
import com.doschool.component.push2refresh.PullToRefreshBase.OnRefreshListener;
import com.doschool.component.updatelater.PostLaterService;
import com.doschool.component.updatelater.TaskManage;
import com.doschool.entity.Microblog;
import com.doschool.entity.Topic;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 这个应该没啥可改的了
 * 
 * @author 是我的海
 */
public class Act_TopicBlogList extends Act_CommonOld implements OnClickListener   {

	
	private final static int AFTER_CAMERA = 2;
	private String lastPhotoName;
	private ArrayList<String> mSelectList=new ArrayList<String>();
	
	
	/******** 界面组件 ****************************************/
	PullToRefreshListView ptrlv;
	ArrayList<Microblog> dataList;
	protected TaskLayout taskLayout;
	Adp_Blog adpter;

	/******** 数据其他 ****************************************/
	private Topic topicData;

	private PopupWindow postBlogPopWindow;
	@Override
	public void initData() {

		registerBoradcastReceiver();
		topicData = (Topic) MySession.getSession().get("topic");
		MySession.getSession().remove("topic");
		ACTIONBAR_TITTLE = "# " + topicData.topic;

		ptrlv = new PullToRefreshListView(this);
		dataList = new ArrayList<Microblog>();
		adpter = new Adp_Blog(this, dataList);

		ptrlv.getRefreshableView().setDivider(null);
		// 设置mplv工作模式
		ptrlv.setPullLoadEnabled(false);
		ptrlv.setScrollLoadEnabled(true);
		ptrlv.getRefreshableView().setAdapter(adpter);
		// 设置mplv滑动监听
		ptrlv.setOnScrollListener(new PauseOnScrollListener(DoschoolApp.newImageLoader, true, true));

		ptrlv.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            	new RefreshBlogListTask(ptrlv, adpter, dataList, 6, 0, topicData.topic, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            	new RefreshBlogListTask(ptrlv, adpter, dataList, 6, 0, topicData.topic, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mParentLayout.addView(ptrlv, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ptrlv.doPullRefreshing(true, 0);
		

		taskLayout = new TaskLayout(this);
		mTrueParentLayout.addView(taskLayout, mScrnWidth, mDip*46);
		

		View postBlogPopView = LayoutInflater.from(this).inflate(R.layout.add_popwindow, null);
		postBlogPopView.setOnClickListener(this);
		postBlogPopView.findViewById(R.id.btSendText).setOnClickListener(this);
		postBlogPopView.findViewById(R.id.btSendGallery).setOnClickListener(this);
		postBlogPopView.findViewById(R.id.btSendCamera).setOnClickListener(this);

		postBlogPopWindow = new PopupWindow(postBlogPopView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		postBlogPopWindow.setBackgroundDrawable(new BitmapDrawable());

	}
	

	/**
	 * 定义稍后上传广播的接收器
	 */
	/**
	 * 定义稍后上传广播的接收器
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {

			final TaskManage taskManage = TaskManage.getInstance();
			int currentProgress = taskManage.currentProgress;
			String sign=intent.getStringExtra("sign");
			
			if(sign.equals("send_show"))
			{
				taskLayout.sendShow(taskManage.mTask.mblogContent);
			}
			
			
			else if(sign.equals("update_progress"))
			{
				taskLayout.updateProgress(currentProgress);
			}
			
			
			else if(sign.equals("success_show"))
			{
				taskLayout.successShow();
				taskManage.state=TaskManage.STATE_FREE_AVAILABLE;
				onBlogPostSucceed();
				startSevice();
			}
			
			
			else if(sign.equals("wrong_show"))
			{
				OnClickListener onCancel = new OnClickListener() {
					@Override
					public void onClick(View v) {
						taskLayout.FadeAll();
						DoschoolApp.mDBHelper.removeTask(taskManage.mTask._id);
						taskManage.state = taskManage.STATE_FREE_AVAILABLE;
						startSevice();
					}
				};

				OnClickListener onRetry = new OnClickListener() {
					@Override
					public void onClick(View v) {
						taskLayout.FadeAll();
						taskManage.state = taskManage.STATE_FREE_AVAILABLE;
						startSevice();
					}
				};
				taskLayout.wrongShow(onCancel, onRetry);
			}
		}
	};

	
	public  void onBlogPostSucceed()
	{
		if(dataList.size()<=DoschoolApp.BLOG_LIST_LOAD_COUNT)
			ptrlv.doPullRefreshing(true, 500);
		
	}
	
	public void startSevice()
	{
		Intent startServiceIntent = new Intent(this, PostLaterService.class);
		Bundle bundle = new Bundle();
		bundle.putString("operate", "start");
		startServiceIntent.putExtras(bundle);
		startService(startServiceIntent);
	}
	
	
	// 注册广播
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(DoschoolApp.UPLOAD_LATER_ACTION_NAME);
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}


	@Override
	public void onPause() {
		unregisterReceiver(mBroadcastReceiver);
		super.onPause();
	}

	
	
	public void onResume() {
		registerBoradcastReceiver();
		
		startSevice();
		
		for (int i = 0; i < dataList.size(); i++)
			if (dataList.get(i).blogId == -1) {
				dataList.remove(i);
			}
		adpter.notifyDataSetChanged();
		super.onResume();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.act_topic_bloglist, menu);
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
		case R.id.add:
			
			if (DoschoolApp.isGuest()) {
				MyDialog.popURGuest(this);
			} else {
				postBlogPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
			}
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==AFTER_CAMERA)
		{
			if(resultCode==RESULT_OK)
			{
				Uri source = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), lastPhotoName));
		        mSelectList.add(source.getPath());
		        
				Intent it = new Intent(this,Act_Write.class);
				Bundle b = new Bundle();
				b.putStringArrayList("pathList", mSelectList);
				it.putExtra("bundle", b);
				startActivityForResult(it, ActivityName.CODE_ACT_WRITEBLOG);
			}
		}
		else
			postBlogPopWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, Act_Write.class);
		intent.putExtra("topic", topicData.topic);
		switch (v.getId()) {
		case R.id.postBlogPopView:
			postBlogPopWindow.dismiss();
			break;
		case R.id.btSendText:
			intent.putExtra("startType", Act_Write.START_TYPE_TEXT);
			startActivityForResult(intent, ActivityName.CODE_ACT_WRITEBLOG);
			this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case R.id.btSendGallery:
			intent.putExtra("startType", Act_Write.START_TYPE_GALLERY);
			startActivityForResult(intent, ActivityName.CODE_ACT_WRITEBLOG);
			this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case R.id.btSendCamera:
			intent.putExtra("startType", Act_Write.START_TYPE_CAMERA);
			startActivityForResult(intent, ActivityName.CODE_ACT_WRITEBLOG);
			this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		}
		
	}
}
