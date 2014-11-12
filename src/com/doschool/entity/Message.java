package com.doschool.entity;

import com.doschool.zother.MJSONObject;

/**
 * 消息动态
 * @author 是我的海
 */
public class Message {
	
	
	public int mid;
	public int type;
	public int state;
	
	public SimplePerson person;
	
	public long time;
	public int mblogId;
	public String newContent;
	public String oldContent;
	public Microblog rootBlog;
	public Microblog newBlog;
	
	public Message(MJSONObject jObj) {
		this.mid=jObj.getInt("msgId",-1);
		this.type = jObj.getInt("msgType",-1);
		this.state = jObj.getInt("msgState",-1);
		this.mblogId = jObj.getInt("blogId",-1);
		this.time = jObj.getLong("time");
		this.person=new SimplePerson(jObj.getMJSONObject("msgPerson"));
		try {
			this.newContent = jObj.getMJSONObject("newContent").getString("string");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.oldContent = jObj.getMJSONObject("oldContent").getString("string");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.rootBlog = new Microblog(jObj.getMJSONObject("rootMblog"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.newBlog = new Microblog(jObj.getMJSONObject("newMblog"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
