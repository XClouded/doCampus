package com.doschool.entity;

import java.io.Serializable;

import org.json.JSONException;

import com.doschool.zother.MJSONObject;


public class Scrip implements Serializable {
	private static final long serialVersionUID = 5025629686918342351L;
	public int xId;
	public String sendTime;
	public String content;
	public SimplePerson author;
	public SimplePerson toPerson;
	public int status;
	

	 public Scrip(MJSONObject jObj) {
		try {
			this.xId = jObj.getInt("xid");
			this.sendTime = jObj.getString("sendTime");
			this.content= jObj.getString("content");
			this.author=new SimplePerson(jObj.getMJSONObject("author"));
			this.toPerson=new SimplePerson(jObj.getMJSONObject("toPerson"));
			this.toPerson.refreshFlags();
			this.status = jObj.getInt("status");
			if(content==null)
				content="小纸条的内容不见了";
			
			this.status=jObj.getInt("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}
		 
	
}
