package com.doschool.aa.activity;

import java.io.File;
import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.activity.Oneblog.Oneblog_Cmtbox;
import com.doschool.aa.activity.Oneblog.Oneblog_LaunchCmt;
import com.doschool.aa.activity.personpage.OperateGridview;
import com.doschool.aa.activity.personpage.PersonDataLayout;
import com.doschool.aa.activity.personpage.Person_Info_Layout;
import com.doschool.aa.activity.personpage.XunzhangGridview;
import com.doschool.aa.widget.P2RScrollView;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.component.choosephoto.Act_Crop;
import com.doschool.component.choosephoto.Act_PhotoChoose;
import com.doschool.entity.Microblog;
import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;
import com.doschool.entity.User;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.methods.PathMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.umeng.analytics.MobclickAgent;



import android.R.color;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Act_PersonPage extends Act_CommonOld implements OnClickListener {
	
	
	/******** 常量标识 ****************************************/
	private final static int ID_BACK = 0;
	private final static int ID_CHANGE_BACKGROUND = 3;
	private final static int ID_CHANGE_HEAD = 5;
	private static final int CROP_PICTURE_FOR_BACKGROUND = 4;
	
//	private static final int TAKE_PHOTO = 0;
//	private static final int CHOOSE_PHOTO = 1;
//	
//	
//	private static final int AFTER_TAKEPHOTO = 3;
//	private static final int AFTER_CHOOSEPHOTO = 5;
	

	private static final int CHOOSE_BACKGROUND = 1;
	private static final int CHOOSE_HEAD = 2;
	
	/******** 数据  ****************************************/
	private Person personData;
	
	public P2RScrollView pullToRefreshScrollView;
	public Person_Info_Layout llTopPanel;
	public PersonDataLayout llPersonData;
	public XunzhangGridview gvXunzhang;
	public OperateGridview gvOperate;
	public TextView mtvNoXunZhang;
	public ImageView ivChange;
	
	@Override
	public void initData() {

		ACTIONBAR_TITTLE="";
		SimplePerson simplePerson=(SimplePerson) MySession.getSession().get("person");
		MySession.getSession().remove("person");
		personData=new Person(simplePerson);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		pullToRefreshScrollView=new P2RScrollView(this);
		mParentLayout.addView(pullToRefreshScrollView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		pullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetPersonInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		
		pullToRefreshScrollView.callRefreshListener();
		pullToRefreshScrollView.getRefreshableView().setBackgroundColor(Color.WHITE);
		LinearLayout llContent=new LinearLayout(Act_PersonPage.this);
		llContent.setOrientation(LinearLayout.VERTICAL);
		pullToRefreshScrollView.getRefreshableView().addView(llContent,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		//创建topPanel
		llTopPanel=new Person_Info_Layout(this, personData);
		llContent.addView(llTopPanel, mScrnWidth, (int) (mScrnWidth*0.67));
		llTopPanel.circleHead.setId(ID_CHANGE_HEAD);
		llTopPanel.circleHead.setOnClickListener(this);
		
		
		//创建4大数据块
		llPersonData=new PersonDataLayout(this,personData);
		llPersonData.setPadding(0, mDip, 0, mDip);
		llContent.addView(llPersonData, mScrnWidth, LayoutParams.WRAP_CONTENT);
		
		mtvNoXunZhang=WidgetFactory.createTextView(this, "尚未获得勋章", R.color.light_grey, 18);
		mtvNoXunZhang.setGravity(Gravity.CENTER);
		mtvNoXunZhang.setPadding(0, mDip, 0, mDip);
		llContent.addView(mtvNoXunZhang, mScrnWidth, LayoutParams.WRAP_CONTENT);
		
		//创建勋章
		gvXunzhang=new XunzhangGridview(this);
		gvXunzhang.setEnabled(false);
		llContent.addView(gvXunzhang, mScrnWidth, LayoutParams.WRAP_CONTENT);
		
		gvOperate=new OperateGridview(this);
		llContent.addView(gvOperate, mScrnWidth, mScrnWidth/2);
		
		
		RelativeLayout topBar=new RelativeLayout(this);
		addContentView(topBar, new LayoutParams(LayoutParams.MATCH_PARENT, mDip*48));
		
		ImageView ivBack=new ImageView(this);
		ivBack.setId(ID_BACK);
		ivBack.setOnClickListener(this);
		ivBack.setBackgroundResource(R.drawable.tongyong_bg_btn);
		ivBack.setImageResource(R.drawable.sab_back);
		ivBack.setPadding(mDip*12, mDip*12, mDip*12, mDip*12);
		topBar.addView(ivBack, mDip*48, mDip*48);
		
		ivChange=new ImageView(getApplicationContext());
		ivChange.setId(ID_CHANGE_BACKGROUND);
		ivChange.setOnClickListener(this);
		ivChange.setImageResource(R.drawable.sab_changebg);
		ivChange.setBackgroundResource(R.drawable.tongyong_bg_btn);
		ivChange.setPadding(mDip*12, mDip*12, mDip*12, mDip*12);
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(mDip*48, mDip*48);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		topBar.addView(ivChange, lp);

		llTopPanel.ivBackPerson.setId(ID_CHANGE_BACKGROUND);
		llTopPanel.ivBackPerson.setOnClickListener(this);
		refreshActivityView();
	}


	public void refreshActivityView()
	{
		
		
		llTopPanel.circleHead.setOnClickListener(this);
		
		if(personData.personId==DoschoolApp.thisUser.personId)
			ivChange.setVisibility(View.VISIBLE);
		else
			ivChange.setVisibility(View.INVISIBLE);
		llTopPanel.callRefresh(personData);
		llPersonData.callRefresh(personData);
		
		if(personData.xunList!=null && personData.xunList.size()>0)
		{
			mtvNoXunZhang.setVisibility(View.GONE);
			gvXunzhang.setVisibility(View.VISIBLE);
			gvXunzhang.callRefresh(personData.xunList);
		}else
		{
			mtvNoXunZhang.setVisibility(View.VISIBLE);
			gvXunzhang.setVisibility(View.GONE);
		}
		gvOperate.callRefresh(personData);
		
	}
	
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			
			switch (requestCode) {
			case 103:
				pullToRefreshScrollView.setRefreshing(true);
				break;
				
			case CHOOSE_BACKGROUND:
				ArrayList<String> pathList=data.getBundleExtra("bundle").getStringArrayList("pathList");
				int defination=data.getIntExtra("defination", 0);
				
				Intent it=new Intent(Act_PersonPage.this, Act_Crop.class);
				it.putExtra("fixed", true);
				it.putExtra("defination", defination);
				it.putExtra("ratioX", 3);
				it.putExtra("ratioY", 2);
				it.putExtra("sourcePath", pathList.get(0));
				startActivityForResult(it,CROP_PICTURE_FOR_BACKGROUND);
				
				break;
			case CHOOSE_HEAD:
//				
//				
//				startActivityForResult(new Intent(Act_PersonPage.this, Act_EditInfo.class),ActivityName.CODE_ACT_EDITINFO);
//				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			case CROP_PICTURE_FOR_BACKGROUND:
//				File file=(File) MySession.getSession().get("crop_file");
				String file=data.getStringExtra("crop_file");
				DoschoolApp.newImageLoader.displayImage(file, llTopPanel.ivBackPerson.mImageView, DoschoolApp.dioPersonBg);
				file=Scheme.FILE.crop(file);
				
	            new ChangeBackTask(file).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				break;
			default:
				break;
			}
		}
	}
	
	class ChangeBackTask extends AsyncTask<Void, Void, Integer> {
		
		String backName="";
		ProgressDialog progressDialog;
		
		protected void onPreExecute() {
			//转圈Dialogue
			progressDialog=new ProgressDialog(Act_PersonPage.this);
			progressDialog.setMessage("正在更改你的背景图片");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		
		String file;
		public ChangeBackTask(String file)
		{
			this.file=file;
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			
			int code;
			
			MJSONObject jObj = DoUserSever.UserUploadBackgroundPic(file);
			code=jObj.getInt("code",9);
			if ( code== 0)
				backName = jObj.getString("data");
			else
				return 1;
		
			MJSONObject jObj2 =  DoUserSever.UserUpdateBackgroundPic(DoschoolApp.thisUser.personId, backName);
			code=jObj2.getInt("code",9);
			if (code != 0)
				return 2;
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			progressDialog.cancel();
			if(result!=0){
				DoMethods.showToast(Act_PersonPage.this, "头像上传出错");
				DoschoolApp.newImageLoader.displayImage(DoschoolApp.thisUser.headUrl, llTopPanel.ivBackPerson.mImageView, DoschoolApp.dioPersonBg);
			}
			else
			{
				pullToRefreshScrollView.setRefreshing(true);
			}
		}
	
	}


	

	public class GetPersonInfoTask extends AsyncTask<Void, Void, MJSONObject> {

		@Override
		protected MJSONObject doInBackground(Void... params) {
			return DoUserSever.UserCompleteInfoGet(DoschoolApp.thisUser.personId, personData.personId);
		}
		@Override
		protected void onPostExecute(MJSONObject result) {
			//获取状态值
			//处理各状态
			if (result.getInt("code",9) == 0)		
			{
				personData = new Person(result.getMJSONObject("data"));
				Log.v("personData"+personData.xunList.size(), "fff");
				personData.refreshFlags();
				if(personData.personId==DoschoolApp.thisUser.personId)
				{
					DoschoolApp.thisUser=new User(result.getMJSONObject("data"));
					User.saveUserInfo(getApplicationContext(), result.getMJSONObject("data").toString());
				}
				refreshActivityView();
			}
			pullToRefreshScrollView.onRefreshComplete();
		}
	}
	
	
	
	
	/**** 所有按钮的点击监听器**********/
	@Override
	public void onClick(View v) {
		switch ((Integer)v.getId()) {
		case ID_BACK:
			
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case ID_CHANGE_BACKGROUND:

			MobclickAgent.onEvent(Act_PersonPage.this, "event_personpage_changebg");
			Intent it=new Intent(Act_PersonPage.this, Act_PhotoChoose.class);
			it.putExtra("maxCount", 1);
			it.putExtra("autoFinish", true);
			startActivityForResult(it,CHOOSE_BACKGROUND);
			break;
		case ID_CHANGE_HEAD:
			
			Log.v("dddddd1", "dddddddd");
			if (personData.personId == DoschoolApp.thisUser.personId)
			{
				Log.v("dddddd2", "dddddddd");
				startActivityForResult(new Intent(Act_PersonPage.this, Act_EditInfo.class),ActivityName.CODE_ACT_EDITINFO);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				
//				Intent it2=new Intent(Act_PersonPage.this, Act_PhotoChoose.class);
//				it2.putExtra("maxCount", 1);
//				it2.putExtra("autoFinish", true);
//				startActivityForResult(it2,CHOOSE_HEAD);
			}
			break;
		}
		
	}

	
	//好友申请，删除，等待的弹窗
	public void popFriendRequestDialog() {
		if(personData.isMyFriend==true)
		{	//已经是好友,删除框
			new AlertDialog.Builder(Act_PersonPage.this).
			setTitle("你要把"+personData.nickName+"从好友中删除吗 PS:尚未实现").
	        setNegativeButton("取消", null).
			setPositiveButton("删除", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int i) {
	    			new FriendRemoveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	        }).show();
		}
		else if(personData.doISendFriendRequest==false)
		{	//我没有发送请求，请求框
			final EditText metReason=WidgetFactory.createEditText(Act_PersonPage.this, "填写理由", R.color.black, 0);
			String reason=SpMethods.loadString(getApplicationContext(), SpMethods.MAKE_FRIEND_REASON);
			metReason.setText(reason);
			new AlertDialog.Builder(Act_PersonPage.this).
			setTitle("向"+personData.nickName+"发出好友申请？").
			setView(metReason).
			setNegativeButton("取消", null).
			setPositiveButton("发送申请", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int i) {
	            	SpMethods.saveString(getApplicationContext(), SpMethods.MAKE_FRIEND_REASON, metReason.getText().toString());
	            	new sendFriendRequestTask(metReason.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	        }).show();
		}
	}

		
	public void popSendScripDialog() {

		final EditText metReason=WidgetFactory.createEditText(Act_PersonPage.this, "纸条内容", R.color.black, 0);
		String reason=SpMethods.loadString(getApplicationContext(), SpMethods.SEND_SCRIPT_CONTENT);
		metReason.setText(reason);
		
		new AlertDialog.Builder(Act_PersonPage.this).
		setTitle("小纸条").
		setView(metReason).
		setNegativeButton("取消", null).
		setPositiveButton("扔过去", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
            	if(metReason.getText().toString().length()>0)
            	{
            		SpMethods.saveString(getApplicationContext(), SpMethods.SEND_SCRIPT_CONTENT, metReason.getText().toString());
            		new sendScripTask(metReason.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            	}
            	else
            	{
            		popSendScripDialog();
            	}
            }
        }).show();


	}
	
	
	
	/**** 发送小纸条任务**********/
	class sendScripTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;
		String content;

		public sendScripTask(String rs)
		{
			content=rs;
		}
		
		protected void onPreExecute() {
			// 转圈Dialogue
			
			progressDialog = new ProgressDialog(Act_PersonPage.this);
			progressDialog.setMessage("正在偷偷扔小纸条!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			return DoUserSever.UserScripSend(DoschoolApp.thisUser.personId, personData.personId,content).getInt("code",9);

		}

		@Override
		protected void onPostExecute(Integer state) {
			progressDialog.dismiss();
			// 处理各状态
			if (state ==0) {
				SpMethods.saveString(getApplicationContext(), SpMethods.SEND_SCRIPT_CONTENT, "");
				DoMethods.showToast(Act_PersonPage.this, "发送成功！");
				personData.cardState = state;
				personData.refreshFlags();
				pullToRefreshScrollView. setRefreshing(true);
			} else {
				DoMethods.showToast(Act_PersonPage.this, "网络出错T_T");
				popSendScripDialog();
			}

		}
	}
	
	//名片发送，已发送弹窗
	public void popCardSendDialog() {
			new AlertDialog.Builder(Act_PersonPage.this).
			setTitle("你要向"+personData.nickName+"发送名片吗？").
			setNegativeButton("取消", null).
			setPositiveButton("发送名片", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int i) {
	            	new sendCardTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	            }
	        }).show();
	}
	
	
	
	
	
	
	
	/**** 发送好友请求任务**********/
	class sendFriendRequestTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;
		String reason;

		public sendFriendRequestTask(String rs)
		{
			reason=rs;
		}
		
		protected void onPreExecute() {
			// 转圈Dialogue
			
			progressDialog = new ProgressDialog(Act_PersonPage.this);
			progressDialog.setMessage("正在发出申请!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			return DoUserSever.UserApplySend(DoschoolApp.thisUser.personId, personData.personId,reason).getInt("code",9);

		}

		@Override
		protected void onPostExecute(Integer state) {
			progressDialog.dismiss();
			// 处理各状态
			if (state ==0) {
				DoMethods.showToast(Act_PersonPage.this, "小安已经转达了你的好友申请！");
				personData.friendState = state;
				personData.refreshFlags();
				pullToRefreshScrollView. setRefreshing(true);
			} else {
				DoMethods.showToast(Act_PersonPage.this, "网络出错T_T");
				popFriendRequestDialog();
			}

		}
	}
	

	/**** 发送名片任务**********/
	class sendCardTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;

		protected void onPreExecute(String reason) {
			// 转圈Dialogue
			progressDialog = new ProgressDialog(Act_PersonPage.this);
			progressDialog.setMessage("正在发送名片!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
				return DoUserSever.UserCardSend(DoschoolApp.thisUser.personId, personData.personId).getInt("code",9);
		}

		@Override
		protected void onPostExecute(Integer state) {
			//progressDialog.dismiss();
			// 处理各状态
			if (state ==0) {
				DoMethods.showToast(Act_PersonPage.this, "小安已帮您把名片送过去啦！");
				personData.cardState = state;
				personData.refreshFlags();
				pullToRefreshScrollView. setRefreshing(true);
			} else {
				DoMethods.showToast(Act_PersonPage.this, "网络出错T_T");
				popCardSendDialog();
			}

		}
	}
	
	/**** 发送名片任务**********/
	class FriendRemoveTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;

		protected void onPreExecute(String reason) {
			// 转圈Dialogue
			progressDialog = new ProgressDialog(Act_PersonPage.this);
			progressDialog.setMessage("正在狠心删除好友!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
				return DoUserSever.FriendRemove(DoschoolApp.thisUser.personId, personData.personId).getInt("code",9);
		}

		@Override
		protected void onPostExecute(Integer state) {
			//progressDialog.dismiss();
			// 处理各状态
			if (state ==0) {
				DoMethods.showToast(Act_PersonPage.this, "删除好友成功！");
				personData.friendState = state;
				personData.refreshFlags();
				for(int i=0;i<DoschoolApp.friendList.size();i++)
					if(personData.personId==DoschoolApp.friendList.get(i).personId)
					{
						DoschoolApp.friendList.remove(i);
						break;
					}
				
				pullToRefreshScrollView. setRefreshing(true);
			} else {
				DoMethods.showToast(Act_PersonPage.this, "网络出错T_T");
				popFriendRequestDialog();
			}
		}
	}
	


	
}
