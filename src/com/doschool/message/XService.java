package com.doschool.message;

import org.json.JSONObject;

import com.doschool.R;
import com.doschool.aa.activity.Act_Wele;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class XService extends Service {
	public static final String TAG = "XService";
	public static final String DOSCHOOL = "doSchool";
	public static final int BASE_ID = 56654;
	private static Notification notification;
	private NotificationManager notificationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Log.d(TAG, "启动通知服务");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "接受到消息");
		try {
			JSONObject jObj = new JSONObject(intent.getStringExtra("message"));
			jObj.put("type", Integer.decode(jObj.getString("type")));
			PushMessage pushMessage = new PushMessage(jObj);
			Intent mIntent = new Intent(this, Act_Wele.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			String message = pushMessage.getMessage();
			// int[] position = pushMessage.getPosition();
			// if (position != null) {
			// Log.d(TAG, "0=" + position[0] + "1=" + position[1]);
			// ActivityMain.addNews(position[0], position[1]);
			// }
			notification = new Notification(R.drawable.ic_launcher, "",
					System.currentTimeMillis());
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			notification.defaults = Notification.DEFAULT_ALL;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(this, "doSchool", message,
					pendingIntent);
			notificationManager.notify(DOSCHOOL, BASE_ID, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
}
