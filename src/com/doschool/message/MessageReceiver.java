package com.doschool.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.doschool.R;
import com.doschool.aa.activity.Act_Wele;
import com.doschool.aa.tools.ActivityTool;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.User;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;

public class MessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = "BDMESSAGERECEIVER";
	public static Thread run;
	DoschoolApp app;
	static int no=1100; 
	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d(TAG, ">>> Receive intent: \r\n" + intent);
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			no++;
			// 获取消息内容
			String message = intent.getExtras().getString(
					PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			// 消息的用户自定义内容读取方式
			Log.i(TAG, "onMessage: " + message);
			// {"type":1,"notify":"xxx赞了你","title":"xxx赞了你","msg":"xxx在小安上赞了你"}
			try {
				JSONObject o = new JSONObject(message);
				Intent it = new Intent();
				it.setAction("com.doscholl.onClickNotifycation");
				it.putExtra("type", o.getInt("type"));
				if(o.getInt("type") == 0){
					//其实应该是JSONObject
					
					it.putExtra("info", o.getString("info").toString());
				}
				
//				SharedPreferences sp0 = context.getSharedPreferences("MySP",Context.MODE_PRIVATE);
//				int st = sp0.getInt("YDHDY", -1);
//				if(o.getInt("type") == 6 && st !=1){
//					return;
//				}
				
				
				Notification notification = new Notification();
				notification.icon = R.drawable.ic_launcher;
				notification.when = System.currentTimeMillis();
				notification.tickerText = o.getString("tip");
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_LIGHTS;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.defaults |= Notification.DEFAULT_LIGHTS;
				PendingIntent contentIntent = PendingIntent.getBroadcast(
						context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
				notification.setLatestEventInfo(context, o.getString("topic"),
						o.getString("content"), contentIntent);
				NotificationManager mNotificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.cancel(no);
				mNotificationManager.notify(no, notification);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// 处理绑定等方法的返回数据
			// PushManager.startWork()的返回值通过 PushConstants.METHOD_BIND 得到
			// 获取方法
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			// 方法返回错误码。若绑定返回错误(非 0),则应用将不能正常接收消息。
			// 绑定失败的原因有多种,如网络原因,或 access token 过期。
			// 请不要在出错时进行简单的 startWork 调用,这有可能导致死循环。
			// 可以通过限制重试次数,或者在其他时机重新调用来解决。
			int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
					PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				// 返回内容
				content = new String(
						intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}
			// 用户在此自定义处理消息,以下代码为 demo 界面展示用
			Log.d(TAG, "onMessage: method : " + method);
			Log.d(TAG, "onMessage: result : " + errorCode);
			Log.d(TAG, "onMessage: content : " + content);
			//{"response_params":{"appid":"2306306","channel_id":"3712319622952527617","user_id":"1104215921561698587"},"request_id":1245338079}
			String userId = null;
			try {
				JSONObject o = new JSONObject(content);
				userId = o.getJSONObject("response_params").getString("user_id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
						Log.i(TAG,""+userId);
			final String fid = userId;
			
			//开线程提交deid而不是用那个类。。 
			new Thread(){
				public void run(){
					Log.i("LOGINING","Begin:::dID:"+fid+SpMethods.loadString(context, SpMethods.USER_FUNID));

					DoUserSever.UserLoginPush(DoschoolApp.thisUser.personId, fid);
					Log.i("LOGINING","dID:"+fid);
				}
			}.start();
			
		} 
		else 
			//点击了通知之后的回调处理
			if (intent.getAction()
				.equals("com.doscholl.onClickNotifycation")) {
			app = ((DoschoolApp)context.getApplicationContext());
			int pageType = intent.getIntExtra("type", -1);
			if(DoschoolApp.widthPixels==0 || User.loadUserInfo(context)==null){
				Intent it = new Intent(context, Act_Wele.class);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.putExtra("type", intent.getIntExtra("type", 0));
				context.startActivity(it);
			}
			else if(pageType == 0){
				String info = intent.getStringExtra("info");
				try {
					JSONObject obj = new JSONObject(info);
					int toolid = obj.getInt("toolid");
					String name = obj.getString("cnname");
					int type = obj.getInt("type");
					String serURL = obj.getString("locationsite");
					int isPublic = obj.getInt("public");
					int status = obj.getInt("status");
					
					int needShare = obj.getInt("needshare");
					
					String jsName = "",shareURL = "";
					if(needShare == 1){
					   jsName = obj.getString("jsname");
					   shareURL = obj.getString("sharesite");
					}
					
//					this.needShare = needShare;
//					this.shareURL = shareURL;
//					this.jsName = jsName;

					Intent it = new Intent(context,ActivityTool.class);
					it.putExtra("toolid", toolid);
					it.putExtra("name", name);
					it.putExtra("type", type);
					it.putExtra("serURL", serURL);
					it.putExtra("isPublic", isPublic);
					it.putExtra("status", status);
					it.putExtra("needShare", needShare);
					it.putExtra("jsName", jsName);
					it.putExtra("shareURL", shareURL);
					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(it);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				app.finishToMe(intent.getIntExtra("type", 0));
				Log.i(TAG,""+"wawa");
			}

		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			Log.d(TAG, "intent=" + intent.toUri(0));
			// 自定义内容的 json 串
			Log.d(TAG,
					"EXTRA_EXTRA = "
							+ intent.getStringExtra(PushConstants.EXTRA_EXTRA));
		}
	}

}
// //废弃
// class MessageReceiverTTT extends FrontiaPushMessageReceiver {
// /** TAG to Log */
// public static final String TAG = "BDMESSAGERECEIVER";
// public static Thread run;
//
//
//
// /**
// * 调用 PushManager.startWork 后，sdk 将对 push server 发起绑定请求，这个过程是异步 的。绑定请求的结果通过
// * onBind 返回。
// */
// @Override
// public void onBind(Context context, int errorCode, String appid,
// String userId, String channelId, String requestId) {
// String responseString = "onBind errorCode=" + errorCode + " appid="
// + appid + " userId=" + userId + " channelId=" + channelId
// + " requestId=" + requestId;
// Log.d(TAG, responseString);
// new LoginPushTask(userId).execute();
//
// }
//
// // -2-登陆
// public class LoginPushTask extends AsyncTask<Void, Void, Void> {
//
// String devId;
//
// public LoginPushTask(String userId)
// {
// devId=userId;
// }
//
// protected void onPreExecute() {
// }
//
// @Override
// protected Void doInBackground(Void... params) {
// UserServer.UserLoginPush(DoschoolApp.thisUser.personId, devId);
// return null;
// }
//
// @Override
// protected void onPostExecute(Void result) {
//
// }
// }
//
//
// /**
// * 接收透传消息的函数。
// */
// @Override
// public void onMessage(Context context, String message,
// String customContentString) {
// String messageString = "透传消息  message=" + message
// + " customContentString=" + customContentString;
// Log.d(TAG, messageString);
//
// int s = 1;
// String pkg = "";
// String cls = message;
// try {
// // o = new JSONObject(customContentString);
// // s = o.getInt("type");
// // pkg = o.getString("pkg");
// // cls = o.getString("cls");
// // s=2;
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// // DoschoolApp.app.finishToMe(s, cls);
//
//
//
// }
//
// /**
// * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内 容。
// */
// @Override
// public void onNotificationClicked(Context context, String title,
// String description, String customContentString) {
// String notifyString = "通知点击  title=" + title + " description="
// + description + " customContent=" + customContentString;
// Log.d(TAG, notifyString);
// Log.i("WHAT!","dou="+DoschoolApp.dou);
// // 1、动态 2、好友请求 3、好友 4、名片
// // JSONObject o;
// // int s = 0;
// // String pkg = "";
// // String cls = "";
// // try {
// // o = new JSONObject(customContentString);
// // s = o.getInt("type");
// //// pkg = o.getString("pkg");
// // cls = o.getString("cls");
// //// s=2;
// // } catch (JSONException e) {
// // e.printStackTrace();
// // }
//
//
// // DoschoolApp.app.finishToMe(s, cls);
// // Intent it = new Intent("com.doschool.pushReceiverToMain");
// // it.putExtra("type", s);
// // Log.i(TAG,""+s+""+it.toURI());
// // context.getApplicationContext();
//
// }
//
// /**
// * setTags() 的回调函数。
// */
// @Override
// public void onSetTags(Context context, int errorCode,
// List<String> sucessTags, List<String> failTags, String requestId) {
// String responseString = "onSetTags errorCode=" + errorCode
// + " sucessTags=" + sucessTags + " failTags=" + failTags
// + " requestId=" + requestId;
// }
//
// /**
// * delT ags() 的回调函数。
// */
// @Override
// public void onDelTags(Context context, int errorCode,
// List<String> sucessTags, List<String> failTags, String requestId) {
// String responseString = "onDelTags errorCode=" + errorCode
// + " sucessTags=" + sucessTags + " failTags=" + failTags
// + " requestId=" + requestId;
// }
//
// /**
// * listTags() 的回调函数。
// */
// @Override
// public void onListTags(Context context, int errorCode, List<String> tags,
// String requestId) {
// String responseString = "onListTags errorCode=" + errorCode + " tags="
// + tags;
// }
//
// /**
// * PushManager.stopWork() 的回调函数。
// */
// @Override
// public void onUnbind(Context context, int errorCode, String requestId) {
// String responseString = "onUnbind errorCode=" + errorCode
// + " requestId = " + requestId;
// Log.i(TAG,responseString);
// }
// }

// package us.dobell.doschool.message;
//
// import org.json.JSONException;
// import org.json.JSONObject;
//
// import us.dobell.doschool.base.Values;
// import us.dobell.xtools.XService;
// import android.content.BroadcastReceiver;
// import android.content.Context;
// import android.content.Intent;
// import android.os.AsyncTask;
// import android.util.Log;
//
// import com.baidu.android.pushservice.PushConstants;
//
// public class MessageReceiver extends BroadcastReceiver {
// static final String TAG = "MessageReceiver";
//
// @Override
// public void onReceive(Context context, Intent intent) {
// Log.d(TAG, "获取到新的推送消息");
// Log.d(TAG, "消息:" + intent);
// if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
// try {
// JSONObject jObj = new JSONObject(intent.getExtras().getString(
// PushConstants.EXTRA_PUSH_MESSAGE_STRING));
// Log.d(TAG, "消息为" + jObj.toString());
// String messageId = jObj.getString("_mid");
// int userId = Integer.valueOf(jObj.getString("_uid"));
// // TODO 这里还需要根据message的type来做，根据type，目前有三个
// // 但是我觉得后续还可能更多，所以应该加入switch来做选择
// if (userId == Values.User.me.id) {
// new PushSuccessTask().execute(messageId);
// Intent mIntent = new Intent(context, XService.class);
// mIntent.putExtra("message", jObj.getJSONObject("_content")
// .toString());
// context.startService(mIntent);
// // XNotification.addNotification(context, mIntent);
// Log.v(TAG, "已经添加");
// } else {
// // TODO 这个我不好测试，但是根据Receiver的规则，好像
// // 数据操作有时间限制，所以这个地方可能需要将消息发送给某个后台，以此来返回数据
// // 另，在sdk中，我有看到向推送服务器发送消息，所以，看这个地方是否可以直接用这个方法
// // 目测用这个方法，cly需要将接口位置调整到推送服务器下，而不是在bae下
// Log.d("test", "消息不匹配，请求网络操作，告知服务器");
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
// // 处理 bind、setTags 等方法口的返回数据
// final String method = intent
// .getStringExtra(PushConstants.EXTRA_METHOD);
// final int errorCode = intent
// .getIntExtra(PushConstants.EXTRA_ERROR_CODE,
// PushConstants.ERROR_SUCCESS);
// final String content = new String(
// intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
// Log.i(TAG, "onMessage: method: " + method);
// Log.i(TAG, "onMessage: result : " + errorCode);
// Log.i(TAG, "onMessage: content : " + content);
// try {
// Values.Message.devId = new JSONObject(content).getJSONObject(
// "response_params").getString("user_id");
// new MessageLoginTask().execute();
// } catch (JSONException e) {
// e.printStackTrace();
// }
// } else if (intent.getAction().equals(
// PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
// }
// Log.d(TAG, "onReceive-->End");
// }
//
// class MessageLoginTask extends AsyncTask<Void, Void, Void> {
//
// @Override
// protected Void doInBackground(Void... params) {
// Log.d(TAG, "更新设备码" + Values.Message.devId);
// MessageServer.messageLogin(Values.User.me.id, Values.Message.devId);
// Log.d(TAG, "更新设备码成功");
// return null;
// }
// }
//
// class PushSuccessTask extends AsyncTask<String, Void, Void> {
//
// @Override
// protected Void doInBackground(String... params) {
// MessageServer.messageSuccess(params[0]);
// return null;
// }
//
// }
// }
