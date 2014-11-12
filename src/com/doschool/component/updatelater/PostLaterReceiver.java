package com.doschool.component.updatelater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.doschool.aa.widget.TaskLayout;
import com.doschool.app.DoschoolApp;

public class PostLaterReceiver extends BroadcastReceiver
{

	private TaskLayout taskLayout;
	private IPostLater iUpdateLater;
	
	public PostLaterReceiver(TaskLayout taskLayout, IPostLater iUpdateLater) {
		super();
		this.taskLayout = taskLayout;
		this.iUpdateLater = iUpdateLater;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		final TaskManage taskManage = TaskManage.getInstance();
		int currentProgress = taskManage.currentProgress;
		String sign=intent.getStringExtra("sign");
		if(sign.equals("send_show")){
			taskLayout.sendShow(taskManage.mTask.mblogContent);
		}
		
		else if(sign.equals("update_progress")){
			taskLayout.updateProgress(currentProgress);
		}
		
		else if(sign.equals("success_show"))
		{
			taskLayout.successShow();
			taskManage.state=TaskManage.STATE_FREE_AVAILABLE;
			iUpdateLater.onPostSucceed();
			
			iUpdateLater.startUploadSevice();
		}
		
		else if(sign.equals("wrong_show"))
		{
			OnClickListener onCancel = new OnClickListener() {
				@Override
				public void onClick(View v) {
					taskLayout.FadeAll();
					DoschoolApp.mDBHelper.removeTask(taskManage.mTask._id);
					taskManage.state = taskManage.STATE_FREE_AVAILABLE;
					iUpdateLater.startUploadSevice();
				}
			};

			OnClickListener onRetry = new OnClickListener() {
				@Override
				public void onClick(View v) {
					taskLayout.FadeAll();
					taskManage.state = taskManage.STATE_FREE_AVAILABLE;
					iUpdateLater.startUploadSevice();
				}
			};
			taskLayout.wrongShow(onCancel, onRetry);
		}
	}
	
}