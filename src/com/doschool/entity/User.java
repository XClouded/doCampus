package com.doschool.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONObject;

public class User extends Person {

	public User(MJSONObject jObj){
		super(jObj);
		// TODO Auto-generated constructor stub
	}
	
	//登陆账号、密码、时间
	public static void saveLoginInfo(Context ctx, String funId,String usrPasswd,long LastLoginTime){
		
		SpMethods.saveString(ctx, SpMethods.USER_FUNID, funId.toUpperCase());
		SpMethods.saveString(ctx, SpMethods.USER_PASSWORD, usrPasswd);
		SpMethods.saveLong(ctx, SpMethods.USER_LAST_LOGINTIME, LastLoginTime);
	}
	
	public static String loadFunId(Context ctx) {
		return SpMethods.loadString(ctx, SpMethods.USER_FUNID);
	}
	
	public static String loadPassword(Context ctx) {
		return SpMethods.loadString(ctx, SpMethods.USER_PASSWORD);
	}
	
	public static long loadLastLoginTime(Context ctx) {
		return SpMethods.loadLong(ctx, SpMethods.USER_LAST_LOGINTIME,0);
	}
	
	//使用者信息
	public static void saveUserInfo(Context ctx, String UserInfo) {
		SpMethods.saveString(ctx, SpMethods.USER_INFO, UserInfo);
	}
	public static String loadUserInfo(Context ctx) {
		return SpMethods.loadString(ctx, SpMethods.USER_INFO);
	}
	
	
	public static void clearAutoLoginInfo(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("MySP",Context.MODE_PRIVATE);
		sp.edit().clear().commit();
		sp = ctx.getSharedPreferences(SpMethods.SPSET_NAME,Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
}


