package com.doschool.entity;

import java.io.Serializable;

import com.doschool.zother.MJSONObject;

public class BlogMedal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6357276203107110580L;
	public int mid;
	public int status;
	public String theme;
	public String intro;
	public int type;
	public String picUrl;
	public long rewardTime;
	public String rewardXuhao;
	
	public BlogMedal(MJSONObject jObj) {
		this.mid=jObj.getInt("mid",-1);
		this.status = jObj.getInt("status",-1);
		this.type = jObj.getInt("type",-1);
		this.theme = jObj.getString("theme");
		this.intro = jObj.getString("intro");
		this.picUrl = jObj.getString("picUrl");
		this.rewardTime = jObj.getLong("rewardTime");
		this.rewardXuhao = jObj.getString("rewardXuhao");
		
	}
	
}
