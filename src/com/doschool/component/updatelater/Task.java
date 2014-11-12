package com.doschool.component.updatelater;

import java.util.ArrayList;

public class Task
{
	public int _id;
	public int usrId;
	public int tranMblogId;
	public int rootMblogId;
	public String mblogContent;
	public String topic;
	public ArrayList<String> picPathList;
	public ArrayList<Integer> picIdList;
	public long date;
	public int defination;
	
	public Task()
	{}
	
	public Task(int _id, int usrId, int tranMblogId, int rootMblogId, String mblogContent,String topic, ArrayList<String> picPath, long date,int defination) {
		super();
		this._id = _id;
		this.usrId = usrId;
		this.tranMblogId = tranMblogId;
		this.rootMblogId = rootMblogId;
		this.mblogContent = mblogContent;
		this.topic=topic;
		this.picPathList = picPath;
		this.date = date;
		this.defination=defination;
	}
	
	public Task(int usrId, int tranMblogId, int rootMblogId, String mblogContent,String topic, ArrayList<String> picPath,int defination) {
		super();
		this.usrId = usrId;
		this.tranMblogId = tranMblogId;
		this.rootMblogId = rootMblogId;
		this.mblogContent = mblogContent;
		this.topic=topic;
		this.picPathList = picPath;
		this.defination=defination;
	}
	
}

