package com.doschool.entity;

import java.io.Serializable;

import com.doschool.zother.MJSONObject;

public class Xunzhang implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4721763213491225155L;
	
	public String name;
	public String intro;
	public long date;
	public int id;
	public String pic;
	
	public Xunzhang(String pic, String name, String intro, long data, int id) {
		super();
		this.pic = pic;
		this.name = name;
		this.intro = intro;
		this.date = data;
		this.id = id;
	}
	
	public Xunzhang(MJSONObject jObj) {
		super();
		
		this.id = jObj.getInt("id",-1);
		this.name = jObj.getString("name");
		this.intro=jObj.getString("intro");
		this.pic = jObj.getString("pic");
		this.date=jObj.getLong("date");
		
	}
	
	
}