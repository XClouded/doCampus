package com.doschool.aa.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.aa.Act_Common_Linear;
import com.doschool.aa.activity.Act_PersonPage.sendCardTask;
import com.doschool.aa.activity.Oneblog.OneBlog_Blog;
import com.doschool.aa.activity.Oneblog.Oneblog_Cmtbox;
import com.doschool.aa.activity.Oneblog.Oneblog_LaunchCmt;
import com.doschool.aa.adapter.Adp_Setting;
import com.doschool.aa.item.Item_SettingListView;
import com.doschool.aa.widget.MyDialog;
import com.doschool.aa.widget.P2RScrollView;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.entity.Microblog;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoBlogSever;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class Act_Setting extends Act_Common_Linear implements OnItemClickListener {

	/******** 界面组件 ****************************************/

	public static final int RESULT_LOGOUT=100;
	
	/******** 数据等 ****************************************/
	/******** 初始化数据 ********/
	@Override
	public void initData() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBarM().setHomeBtnAsBack(this);
		getActionBarM().setTittle("设置");
		
		ListView list = new ListView(this);
		adpter=new Adp_Setting(this, TOOLS_NAME);
		list.setAdapter(adpter);
		list.setOnItemClickListener(this);
		mParent.addView(list, new LinearLayout.LayoutParams(mScrnWidth, LayoutParams.MATCH_PARENT, 2000));
	
		
		
	}

	

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		if(DoschoolApp.isGuest())
		{
			if(arg2==1)
			{
				MyDialog.popURGuest(Act_Setting.this);
				return;
			}
		}
		
		
		switch(arg2)
		{
		case 0:
			
			Item_SettingListView item=(Item_SettingListView)arg1;
			
				if(!item.cbox.isChecked())
				{
					DoMethods.showToast(Act_Setting.this, "已开启无图模式，本地缓存的图片将继续显示");
					SpMethods.saveBoolean(Act_Setting.this, SpMethods.MODE_NOPIC_DOWNLOAD, true);
					DoschoolApp.newImageLoader.denyNetworkDownloads(true);
					item.cbox.setChecked(true);
				}
				else
				{
					DoMethods.showToast(Act_Setting.this, "土豪，已为你关闭无图模式");
					SpMethods.saveBoolean(Act_Setting.this, SpMethods.MODE_NOPIC_DOWNLOAD, false);
					DoschoolApp.newImageLoader.denyNetworkDownloads(false);
					item.cbox.setChecked(false);
				}
				break;
		case 1:
			MobclickAgent.onEvent(Act_Setting.this, "event_setting_changepassword");
			Act_Setting.this.startActivity(new Intent(Act_Setting.this, Act_ChangePassword.class));
			Act_Setting.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		
		case 2:
			MobclickAgent.onEvent(Act_Setting.this, "event_setting_advise");
			FeedbackAgent agent = new FeedbackAgent(Act_Setting.this);
			agent.startFeedbackActivity();
			break;
		case 3:

			MobclickAgent.onEvent(Act_Setting.this, "event_setting_checkupdate");
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int arg0, UpdateResponse arg1) {
					switch (arg0) {
			        case UpdateStatus.Yes: // has update
			            UmengUpdateAgent.showUpdateDialog(Act_Setting.this, arg1);
			            break;
			        case UpdateStatus.No: // has no update
			            try {
			    			DoMethods.showToast(Act_Setting.this, "你已经是最新版本啦");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            break;
			        case UpdateStatus.NoneWifi: // none wifi
						DoMethods.showToast(Act_Setting.this, "没有wifi连接， 只在wifi下更新");
			            break;
			        case UpdateStatus.Timeout: // time out
						DoMethods.showToast(Act_Setting.this, "检测超时T_T");
			            break;
			        }
					
				}
			});
			UmengUpdateAgent.update(Act_Setting.this);
			break;
		case 4:
			MobclickAgent.onEvent(Act_Setting.this, "event_setting_feature");
			Act_Setting.this.startActivity(new Intent(Act_Setting.this, Act_UpdateIntro.class));
			Act_Setting.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case 5:
			MobclickAgent.onEvent(Act_Setting.this, "event_setting_aboutus");
		Act_Setting.this.startActivity(new Intent(Act_Setting.this, Act_About.class));
		Act_Setting.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		break;
		case 6:
			new AlertDialog.Builder(Act_Setting.this).setTitle("注销").setNegativeButton("取消", null)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SpMethods.saveString(Act_Setting.this, "LAST_USER_FUNID","");
					NotificationManager mNotificationManager = (NotificationManager) Act_Setting.this
					.getSystemService(Context.NOTIFICATION_SERVICE);
					mNotificationManager.cancelAll();

					MobclickAgent.onEvent(Act_Setting.this, "event_setting_logout");
					User.clearAutoLoginInfo(Act_Setting.this);
					DoschoolApp.mDBHelper.removeAll();
					
					setResult(RESULT_LOGOUT);
					finish();
					overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				}
			}).create().show();
			
			
		break;
			
		}
		
	}

	
	Adp_Setting adpter;
	String TOOLS_NAME[]={
			"无图模式",
			"修改密码",
			"建议反馈",
			"检查更新",
			"版本特性",
			"关于我们",
			"注销登陆",
	};
}
