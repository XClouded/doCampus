//package com.doschool.entity;
//
//import android.util.Log;
//
//import com.doschool.methods.ConvertMethods;
//import com.doschool.zz.MJSONObject;
//
//public class Notice {
//	public int nid;
//	public int type;
//	public String content;
//	public long time;
//	
//	public Notice(MJSONObject jObj) {
//		this.nid= jObj.getInt("nid",-1);
//		this.type = jObj.getInt("type",-1);
//		this.content = jObj.getString("content");
//		Log.v(content, "vvvvv");
//		this.time = ConvertMethods.dateStrToLong(jObj.getString("time"));
//	}
//	
//}
