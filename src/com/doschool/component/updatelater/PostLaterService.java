package com.doschool.component.updatelater;

import java.io.File;
import java.util.ArrayList;

import com.doschool.app.DoschoolApp;
import com.doschool.methods.BitmapIOMethod;
import com.doschool.methods.DoMethods;
import com.doschool.methods.PathMethods;
import com.doschool.network.DoBlogSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

public class PostLaterService extends IntentService {

	public PostLaterService() {
		super("uploadLaterService");
	}

	public PostLaterService(String name) {
		super(name);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	Intent mIntent = new Intent(DoschoolApp.UPLOAD_LATER_ACTION_NAME);

	@Override
	protected void onHandleIntent(Intent intent) {

		DoMethods.sleep(500);
		Log.v("BroadcastReceiver", "onHandleIntent");
		
		TaskManage taskManage = TaskManage.getInstance();

		if(taskManage.state==taskManage.STATE_FREE_AVAILABLE)
		{
			
			Log.v("BroadcastReceiver", "STATE_FREE_AVAILABLE");
			taskManage.init();
			if (taskManage.mTask != null) {

				taskManage.state=taskManage.STATE_TASK_START;
				mIntent.putExtra("sign", "send_show");
				sendBroadcast(mIntent);
				
				
				DoMethods.sleep(1000);
				for (int i = 1; i <= 20; i++) {
					DoMethods.sleep(50);
					taskManage.currentProgress =(int) ((1000/taskManage.getStageCount())*(i*0.05));
					mIntent.putExtra("sign", "update_progress");
					sendBroadcast(mIntent);
				}

				taskManage.state=taskManage.STATE_UPLOADING;
				for (int p = 0; p < taskManage.mTask.picPathList.size(); p++) {
					
					
					Log.v("BroadcastReceiver", "UPLOAD_A_PIC");
					
					String imagePath = taskManage.mTask.picPathList.get(p);
					if (imagePath.startsWith("name=")) {
					} else {
						imagePath = imagePath.substring(5);
						Log.v("upload_imagePath"+imagePath, "vvvvb");
						Bitmap bmp=BitmapIOMethod.compressImageFromFile(imagePath,taskManage.mTask.defination);
						String file = BitmapIOMethod.compressBmpToFile(bmp,taskManage.mTask.defination);
						file=Scheme.FILE.crop(file);
						
						MJSONObject jObj = DoBlogSever.MicroblogImageUpload(file);
						int code = jObj.getInt("code", 9);
						if (code == 0) {
							taskManage.mTask.picPathList.set(p, "name=" + jObj.getString("data"));
							DoschoolApp.mDBHelper.updatePic("name=" + jObj.getString("data"), taskManage.mTask.picIdList.get(p));
						} else {
							// 通知：task已经上传失败
							taskManage.state=taskManage.STATE_WRONG_WAITING;
							mIntent.putExtra("sign", "wrong_show");
							sendBroadcast(mIntent);
							return;
						}
					}

					for (int i = 1; i <= 20; i++) {
						Log.v("BroadcastReceiver", "UPLOAD_A_PIC"+i);
						DoMethods.sleep(50);
						taskManage.currentProgress =(int) ((1000/taskManage.getStageCount())*(1+p+i*0.05));
						mIntent.putExtra("sign", "update_progress");
						sendBroadcast(mIntent);
					}
				}
				
				
				MJSONArray jArr = new MJSONArray();
				for (int jjj = 0; jjj < taskManage.mTask.picPathList.size(); jjj++) {
					if (taskManage.mTask.picPathList.get(jjj).startsWith("name="))
						jArr.put(taskManage.mTask.picPathList.get(jjj).substring(5));
				}

				MJSONObject mj = DoBlogSever.MicroblogSend(taskManage.mTask.usrId, taskManage.mTask.tranMblogId, taskManage.mTask.rootMblogId, taskManage.mTask.mblogContent, taskManage.mTask.topic, jArr);

				if (mj.getInt("code", 9) == 0) {
					DoschoolApp.mDBHelper.removeTask(taskManage.mTask._id);
				} else {
					taskManage.state=taskManage.STATE_WRONG_WAITING;
					mIntent.putExtra("sign", "wrong_show");
					sendBroadcast(mIntent);
					return;
				}

				
				for (int i = 1; i <= 20; i++) {
					
					taskManage.currentProgress =(int) ((1000/taskManage.getStageCount())*(taskManage.getStageCount()-1+i*0.05));
					mIntent.putExtra("sign", "update_progress");
					sendBroadcast(mIntent);
					DoMethods.sleep(50);
				}

				//长满了
				
				//显示成功
				taskManage.state = taskManage.STATE_SUCCESS_WAITING;
				mIntent.putExtra("sign", "success_show");
				sendBroadcast(mIntent);
			}
		}
		else if(taskManage.state==taskManage.STATE_SUCCESS_WAITING)
		{
			Log.v("BroadcastReceiver", "STATE_SUCCESS_WAITING");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mIntent.putExtra("sign", "success_hide");
			sendBroadcast(mIntent);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		else if(taskManage.state==taskManage.STATE_WRONG)
//		{
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			sendBroadcast(mIntent);
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}

	}

}
