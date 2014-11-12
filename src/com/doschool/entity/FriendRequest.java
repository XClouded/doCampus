package com.doschool.entity;

import java.io.Serializable;

import org.json.JSONException;

import android.util.Log;

import com.doschool.zother.MJSONObject;


public class FriendRequest implements Serializable {
	private static final long serialVersionUID = 5025629686918342351L;
	public int requestId;
	public String reason;
	public int state;
	public long time;
	public SimplePerson launchPerson;
	
	 public FriendRequest(MJSONObject jObj) {
		 
			this.requestId = jObj.getInt("id",-1);
			this.time = jObj.getLong("time");
			this.reason= jObj.getString("reason");
			this.launchPerson=new SimplePerson(jObj.getMJSONObject("launchPerson"));
			if(reason==null)
				reason="这家伙很懒，啥理由都没说";
			this.state=jObj.getInt("state",-1);
		
	}
		 
	
}
