package com.doschool.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;

import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;


public class Microblog implements Serializable{
	private static final long serialVersionUID = -2581201520205744030L;
	
	//---- 本条微博的数据结构  ----

	public SimplePerson author;
	public BlogMedal medal;
	public Topic topic;
	
	public int blogId;
	public int status;
	public int isZaned;
	public boolean isZaning=false;
	
	public long launchTime;
	public String lauchPlace;
	
	public String blogContent;
	public ArrayList<String> imageUrlList;
	public ArrayList<String> imageUrlListHD;
	

	public int root;
	public Microblog rootBlog;
	
	public int browseCount;
	public int commentCount;
	public int transCount;
	public int zanCount;
	
	public Microblog(MJSONObject jObj){
		
		imageUrlList=new ArrayList<String>();
		imageUrlListHD=new ArrayList<String>();
		

		this.blogId=jObj.getInt("blogId",-1);
		this.status=jObj.getInt("status",-1);
		this.isZaned=jObj.getInt("isZaned",0);
		this.launchTime = jObj.getLong("launchTime");
		this.lauchPlace = jObj.getString("lauchPlace");
		this.blogContent = jObj.getMJSONObject("blogContent").getString("string");

		this.imageUrlList = new ArrayList<String>();
		MJSONArray jArr = jObj.getMJSONArray("imageUrlList");
		if (jArr != null) 
			for (int i = 0; i < jArr.length(); i++) 
				imageUrlList.add(jArr.getString(i));
		MJSONArray jArr2 = jObj.getMJSONArray("imageUrlListHD");
		if (jArr2 != null) 
			for (int i = 0; i < jArr2.length(); i++) 
				imageUrlListHD.add(jArr2.getString(i));
		
		this.root = jObj.getInt("root",-1);
		if(root!=0 && root!=-1)
			this.rootBlog=new Microblog(jObj.getMJSONObject("rootMblog"));
		
		this.browseCount=jObj.getInt("browseCount",0);
		this.zanCount=jObj.getInt("zanCount",0);
		this.commentCount = jObj.getInt("commentCount",0);
		this.transCount = jObj.getInt("transCount",0);
		
		
		
		this.author=new SimplePerson(jObj.getMJSONObject("author"));
		this.medal=new BlogMedal(jObj.getMJSONObject("medal"));
		this.topic=new Topic(jObj.getMJSONObject("topic"));
		
	}
}
