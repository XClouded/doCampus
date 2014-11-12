package com.doschool.component.updatelater;

import java.util.ArrayList;

import com.doschool.app.DoschoolApp;

public class TaskManage {
	
	private static TaskManage mTaskManage;

	public static final int STATE_FREE_AVAILABLE=0;
	public static final int STATE_TASK_START = 1;
	public static final int STATE_UPLOADING=2;
	public static final int STATE_WRONG_WAITING=3;
	public static final int STATE_SUCCESS_WAITING=4;

	public int state=STATE_FREE_AVAILABLE;
	
	
	public Task mTask=null;
	public int currentPicPosition=0;
	public int currentProgress=0;

	public void init()
	{
		state=STATE_FREE_AVAILABLE;
		currentProgress=0;
		currentPicPosition=0;
		mTask= DoschoolApp.mDBHelper.getTask();
	}
	
	public int getStageCount()
	{
		if(mTask!=null)
			return mTask.picIdList.size()+2;
		return 0;
	}
	
	public static TaskManage getInstance()
	{
		if(mTaskManage==null)
			mTaskManage=new TaskManage();
		return mTaskManage;
	}

}
