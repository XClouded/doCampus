package com.doschool.methods;

import android.content.Context;
import android.content.SharedPreferences;

public class SpMethods {
	public static final String SPSET_NAME="AnSP";
	public static final String LASTTIME_SEND_BLOG = "LASTTIME_SEND_BLOG";
	
	
	public static final String FRIEND_LIST_STR="FRIEND_LIST_STR";
	public static final String FRIEND_LIST_UPDATE_TIME="FRIEND_LIST_UPDATE_TIME";
	
	public static final String CARD_LIST_STR="CARD_LIST_STR";
	public static final String CARD_LIST_UPDATE_TIME="CARD_LIST_UPDATE_TIME";
	
	public static final String BLOG_STR_LIST="BLOG_STR_LIST_";
	public static final String BLOG_TIME_LIST="BLOG_TIME_LIST_";
	

	public static final String TOPIC_STR_LIST_="TOPIC_STR_LIST_";
	public static final String TOPIC_TIME_LIST_="TOPIC_TIME_LIST_";
	
	public static final String MSG_TIME_LIST="MSG_TIME_LIST";
	public static final String MSG_STR_LIST="MSG_STR_LIST";
	
	public static final String SCRIP_TIME_LIST="SLIP_TIME_LIST";
	public static final String SCRIP_STR_LIST="SLIP_STR_LIST";
	
	public static final String REQ_TIME_LIST="REQ_TIME_LIST";
	public static final String REQ_STR_LIST="REQ_STR_LIST";
	

	public static final String ABOUT_STR_LIST="ABOUT_STR_LIST";
	public static final String FEATURE_STR_LIST="FEATURE_STR_LIST";
	
	public static final String MODE_NOPIC_DOWNLOAD="MODE_NOPIC_DOWNLOAD";
	

	public static final String MAKE_FRIEND_REASON="MAKE_FRIEND_REASON";
	public static final String SEND_SCRIPT_CONTENT="SEND_SCRIPT_CONTENT";
	

	public static final String LAST_SENDBLOG_TIME="LAST_SENDBLOG_TIME";

	public static final String LAST_COMMENT_TIME="LAST_COMMENT_TIME";
	public static final String LAST_RENAME_TIME="LAST_RENAME_TIME";
	

	public static final String USER_FUNID="USER_FUNID";
	public static final String USER_PASSWORD="USER_PASSWORD";
	public static final String USER_INFO="USER_INFO";
	public static final String USER_LAST_LOGINTIME="USER_LAST_LOGINTIME";
	

	public static final String LASTEST_VERSION_CODE_USED="LASTEST_VERSION_USED";
	
	
	
	//------------------------以下 马干宣的
	public static final String TOOL_INFOS = "TOOL_INFOS";
		
	/**
	 * 下面是一组操作工具
	 */
	
	public static void saveString(Context ctx,String key,String value)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	public static String loadString(Context ctx,String key)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
	
	public static void saveInt(Context ctx,String key,int value)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	public static int loadInt(Context ctx,String key,int defValue)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	
	public static void saveFloat(Context ctx,String key,float value)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		sp.edit().putFloat(key, value).commit();
	}
	public static float loadFloat(Context ctx,String key,float defValue)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		return sp.getFloat(key, defValue);
	}
	
	public static void saveBoolean(Context ctx,String key,boolean value)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	public static boolean loadBoolean(Context ctx,String key,boolean defValue)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	
	public static void saveLong(Context ctx,String key,long value)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}
	public static long loadLong(Context ctx,String key,long defValue)
	{
		SharedPreferences sp = ctx.getSharedPreferences(SPSET_NAME,Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
