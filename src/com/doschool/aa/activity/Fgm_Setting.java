//package com.doschool.aa.activity;
//
//import android.app.AlertDialog;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//
//import com.doschool.R;
//import com.doschool.aa.aa.Fgm_Standard;
//import com.doschool.aa.listadapt.Adp_Setting;
//import com.doschool.aa.listitem.Item_SettingListView;
//import com.doschool.aa.widget.MyDialog;
//import com.doschool.app.DoschoolApp;
//import com.doschool.entity.User;
//import com.doschool.methods.DoMethods;
//import com.doschool.methods.DongSpMethods;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.fb.FeedbackAgent;
//import com.umeng.update.UmengUpdateAgent;
//import com.umeng.update.UmengUpdateListener;
//import com.umeng.update.UpdateResponse;
//import com.umeng.update.UpdateStatus;
//
///**
// * 注册界面
// * 完成度99%
// * @author 是我的海
// *
// */
//public class Fgm_Setting extends Fgm_StandardNew implements OnItemClickListener {
//	
//	String TOOLS_NAME[]={
//			"无图模式",
//			"修改密码",
//			"建议反馈",
//			"检查更新",
//			"版本特性",
//			"关于我们",
//			"注销登陆",
//	};
//	
//	Adp_Setting adpter;
//	Context ctx;
//	
//	public Fgm_Setting()
//	{
//		ctx=getActivity();
//	}
//
//	@Override
//	public void initData() {
//	}
//
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//	}
//
//
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			((SlidingFragmentActivity)getActivity()).getSlidingMenu().showMenu();
//			break;
//
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
//	@Override
//	public void addContentToFgm() {
//		ListView list = new ListView(getActivity());
//		adpter=new Adp_Setting(getActivity(), TOOLS_NAME);
//		list.setAdapter(adpter);
//		list.setOnItemClickListener(this);
//		mParent.addView(list, new LinearLayout.LayoutParams(mScrnWidth, LayoutParams.MATCH_PARENT, 2000));
//	}
//	
//	
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		
//		if(DoschoolApp.isGuest())
//		{
//			if(arg2==1)
//			{
//				MyDialog.popURGuest(getActivity());
//				return;
//			}
//		}
//		
//		
//		switch(arg2)
//		{
//		case 0:
//			
//			Item_SettingListView item=(Item_SettingListView)arg1;
//			
//				if(!item.cbox.isChecked())
//				{
//					DoMethods.showToast(getActivity(), "已开启无图模式，本地缓存的图片将继续显示");
//					DongSpMethods.saveBoolean(getActivity(), DongSpMethods.MODE_NOPIC_DOWNLOAD, true);
//					DoschoolApp.newImageLoader.denyNetworkDownloads(true);
//					item.cbox.setChecked(true);
//				}
//				else
//				{
//					DoMethods.showToast(getActivity(), "土豪，已为你关闭无图模式");
//					DongSpMethods.saveBoolean(getActivity(), DongSpMethods.MODE_NOPIC_DOWNLOAD, false);
//					DoschoolApp.newImageLoader.denyNetworkDownloads(false);
//					item.cbox.setChecked(false);
//				}
//				break;
//		case 1:
//			MobclickAgent.onEvent(getActivity(), "event_setting_changepassword");
//			getActivity().startActivity(new Intent(getActivity(), Act_ChangePassword.class));
//			getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//			break;
//		
//		case 2:
//			MobclickAgent.onEvent(getActivity(), "event_setting_advise");
//			FeedbackAgent agent = new FeedbackAgent(getActivity());
//			agent.startFeedbackActivity();
//			break;
//		case 3:
//
//			MobclickAgent.onEvent(getActivity(), "event_setting_checkupdate");
//			UmengUpdateAgent.setUpdateAutoPopup(false);
//			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//				@Override
//				public void onUpdateReturned(int arg0, UpdateResponse arg1) {
//					switch (arg0) {
//			        case UpdateStatus.Yes: // has update
//			            UmengUpdateAgent.showUpdateDialog(getActivity(), arg1);
//			            break;
//			        case UpdateStatus.No: // has no update
//			            try {
//			    			DoMethods.showToast(getActivity(), "你已经是最新版本啦");
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//			            break;
//			        case UpdateStatus.NoneWifi: // none wifi
//						DoMethods.showToast(getActivity(), "没有wifi连接， 只在wifi下更新");
//			            break;
//			        case UpdateStatus.Timeout: // time out
//						DoMethods.showToast(getActivity(), "检测超时T_T");
//			            break;
//			        }
//					
//				}
//			});
//			UmengUpdateAgent.update(getActivity());
//			break;
//		case 4:
//			MobclickAgent.onEvent(getActivity(), "event_setting_feature");
//			getActivity().startActivity(new Intent(getActivity(), Act_UpdateIntro.class));
//			getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//			break;
//		case 5:
//			MobclickAgent.onEvent(getActivity(), "event_setting_aboutus");
//		getActivity().startActivity(new Intent(getActivity(), Act_About.class));
//		getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//		break;
//		case 6:
//			
//			new AlertDialog.Builder(getActivity()).setTitle("注销").setNegativeButton("取消", null)
//			.setPositiveButton("确定", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//
//					DongSpMethods.saveString(getActivity(), "LAST_USER_FUNID","");
//					NotificationManager mNotificationManager = (NotificationManager) getActivity()
//					.getSystemService(Context.NOTIFICATION_SERVICE);
//					mNotificationManager.cancelAll();
//
//					MobclickAgent.onEvent(getActivity(), "event_setting_logout");
//					User.clearAutoLoginInfo(getActivity());
//					DoschoolApp.mDBHelper.removeAll();
//					startActivity(new Intent(getActivity(), Act_Wele.class));
//					getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
//					getActivity().finish();
//					
//				}
//			}).show();
//			
//			
//		break;
//			
//		}
//		
//	}
//
//}
