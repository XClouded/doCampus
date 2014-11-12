package com.doschool.entity;

import java.io.Serializable;

import android.util.Log;

import com.doschool.zother.MJSONObject;

public class Topic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6839363970377416755L;

	
	public Topic(MJSONObject jobj)
	{
		this.tid=jobj.getInt("tid",-1);
		this.pid=jobj.getInt("createUsrId",-1);
		this.hot=jobj.getInt("hot",0);
		this.status=jobj.getInt("status",-1);
		this.topic=jobj.getString("topic");
		
		Log.v("topic=="+this.topic, "topic");
	}
	
	public int tid;
	public int pid;
	public int hot;
	public int status;
	public String topic;
}
