package com.doschool.entity;

import com.doschool.zother.MJSONObject;


public class Comment{
	
	public int commentId;
	public long time;
	public String content;
	public int rootMblogId;
	public int rootCommentId;
	
	public SimplePerson subPerson;
	public SimplePerson objPerson;

	public Comment(MJSONObject jObj) {
		this.commentId = jObj.getInt("cId",-1);
		this.time = jObj.getLong("time");
		this.content = jObj.getMJSONObject("content").getString("string");
		this.rootMblogId = jObj.getInt("rootMblogId",-1);
		this.rootCommentId = jObj.getInt("rootCommentId",-1);
		this.subPerson=new SimplePerson(jObj.getMJSONObject("subPerson"));
		this.objPerson=new SimplePerson(jObj.getMJSONObject("objPerson"));
		

	}
}