//package com.doschool.aa.tools;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.doschool.app.DoschoolApp;
//import com.doschool.methods.SpMethods;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Environment;
//import android.os.Handler;
//import android.util.Log;
//
///**
// * 对于所有的JSON数据获取分为三步<br>
// * 1.获取json字符串 2.格式化为json对象 3.字段解析 分别对应三种类型的错误，置于NetWorkUtils类处理，该类定义了三个接口，
// * 在实际调用服务的activity代码中实现接口完成三个步骤，并且实现三个对应的错误处理方法。
// * 
// * @author Ma Ganxuan
// * 
// */
//
///*
// * {"msg":"SUCCESS","info"
// * 
// * :[{"id":"23","userid":"0","title":"1","t
// * ext":"123123412312","lftype":"1","usertype":"2","status":"1","time":"2
// * 014-03-17 12:05:45","contact":"123","image":["http:\/\/bcs.duapp.com\/doschool-data
// * base\/%2Flostandfound%2Fc0329f2f174a01260e050389c9ff595e.jpg?sign=MBO:E041ffd82e2e6068b99429f8e
// * ebde832:sS6MILoPx33OmzoEDQb%2BF3V6KYs%3D"]},{"id":"22","userid":"0","title":"1234213","text":"asd
// * asd","lftype":"1","usertype":"2","status":"1","time":"2014-03-17 11:07:24","contact":"asdasdsad11213","image":["http:\/\/bcs.duapp.com\/doschool-database\/%2Flostandfound%2F6fe13dea4853105b2b81a4b78574cbc9.jpg?sign=MBO:E041ffd82e2e6068b99429f8eebde832:ELEBVj%2Bw7lKxlWbWCrXcknHS7zc%3D"]}]}
//
// */
//
//public class DisposeDataUtils {
//	
//	public static List<List<String>> getLibEmList(JSONObject o) throws JSONException {
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = o.getJSONObject("info").getJSONArray("list");
//		
//		for(int i=0;i<ary.length();i++){
//			JSONObject jso0 = ary.getJSONObject(i);
//			List<String> listt = new ArrayList<String>();
//			listt.add(jso0.getString("name"));
//			listt.add(jso0.getString("entered"));
//			listt.add(jso0.getString("now"));
//			listt.add(jso0.getString("empty"));
//			list.add(listt);
//		}
//		return list;
//	}
//	
//	
//	public static List<Project> getKCBList(JSONObject o) throws JSONException {
//		List<Project> list = new ArrayList<Project>();
//		JSONArray ary = o.getJSONArray("kb");
//		
//		for(int i=0;i<ary.length();i++){
//			JSONObject jso0 = ary.getJSONObject(i);
//			JSONObject info = jso0.getJSONObject("info");
//			List<Project> listt = new ArrayList<Project>();
//			for(int j=0;j<info.getInt("times");j++){
//				
//				
//				Project p = new Project(0, 
//						info.getJSONArray("week").getJSONArray(j).getInt(0), 
//						info.getJSONArray("week").getJSONArray(j).getInt(1), 
//						info.getJSONArray("jc").getJSONArray(j).getInt(0), 
//						1+info.getJSONArray("jc").getJSONArray(j).getInt(info.getJSONArray("jc").getJSONArray(j).length()-1) - info.getJSONArray("jc").getJSONArray(j).getInt(0), 
//						info.getJSONArray("weekday").getInt(j), 
//						jso0.getString("subname"), 
//						info.getJSONArray("area").getString(j), 
//						jso0.getString("teacher"), 
//						info.getJSONArray("ds_week").getInt(j)
//						);
//				listt.add(p);
//			}
//			combine(listt);
//			list.addAll(listt);
//		}
//		
//		
//		return list;
//	}
//	
//	private static void combine(List<Project> list){
//		for(int i=0;i<list.size();i++){
//			for(int j=i+1;j<list.size();j++){
//				if(eqProject(list.get(i),list.get(j))){
//					list.get(i).times+=list.get(j).times;
//					list.get(i).startTime = list.get(i).startTime<list.get(j).startTime?list.get(i).startTime:list.get(j).startTime;
//					list.set(j, null);
//				}
//			}
//		}
//		int k=list.size();
//		for(int i=0;i<k;i++){
//			if(list.get(i)==null){
//				list.remove(i);k--;i--;
//			}
//		}
//	}
//	
//	private static boolean eqProject(Project a,Project b){
//		if(a==null || b==null)return false;
//		if(a.address.equals(b.address) && a.ds==b.ds && a.name.equals(b.name) && a.startWeek == b.startWeek 
//				&& a.teacherName.equals(b.teacherName) && a.toWeek==b.toWeek
//				)
//			return true;
//		return false;
//	}
////    "rank": 1,
////    "rowname": 2,
////    "department": "商学院",
////    "personname": "荣玉虎",
////    "point": 0,
////    "notice": "无"
//
//	//GetRaceDataDetail
//	public static List<List<String>> getSSSBDetail(JSONObject obj) throws JSONException{ 
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("rank"));
//			list.get(i).add(o.getString("rowname"));
//			list.get(i).add(o.getString("department"));
//			list.get(i).add(o.getString("personname"));
//			list.get(i).add(o.getString("point"));
//			list.get(i).add(o.getString("notice"));
//		}
//		return list;
//	}
//	
//	
//	//GetRaceDataList
//	public static List<List<String>> getYDHSSSBList(JSONObject obj) throws JSONException{ 
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("racename"));
//			list.get(i).add(o.getString("time"));
//		}
//		return list;
//	}
//	
//	//GetDepartmentList
//	public static List<List<String>> getYDHYXPMList(JSONObject obj) throws JSONException{
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("rank"));
//			list.get(i).add(o.getString("point"));
//			list.get(i).add(o.getString("department"));
//			list.get(i).add(o.getString("time"));
//		}
//		return list;
//	}
//
//	
//	public static List<List<String>> getLAFList(JSONObject obj) throws JSONException{
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("id"));
//			list.get(i).add(o.getString("userid"));
//			list.get(i).add(o.getString("title"));
//			list.get(i).add(o.getString("text"));
//			list.get(i).add(o.getString("lftype"));//1失物招领 2寻物启事
//			list.get(i).add(o.getString("usertype"));//?
//			list.get(i).add(o.getString("status"));
//			list.get(i).add(o.getString("time"));
//			list.get(i).add(o.getString("contact"));//联系方式
//			JSONArray ims = o.getJSONArray("image");
//			for(int k=0;k<ims.length();k++){
//				list.get(i).add(ims.getString(k));
//			}
//		}
//		return list;
//
//	}
//	
//	public static List<List<String>> getWaiMaiListFromDB() {
//		SQLiteDatabase db;
//		Cursor cursor;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//			cursor = null;
//		} catch (Exception e) {
//			return null;
//		}
//		try {
//			cursor = db.rawQuery("select * from " + "WaiMai", null);
//		} catch (Exception e) {
//			db.close();
//			return null;
//		}
//		List<List<String>> list = new ArrayList<List<String>>();
//		while (cursor.moveToNext()) {
//			List<String> newList = new ArrayList<String>();
//			newList.add(cursor.getString(0));
//			newList.add(cursor.getString(1));
//			newList.add(cursor.getString(2));
//			newList.add(cursor.getString(3));
//			list.add(newList);
//		}
//		cursor.close();
//		db.close();
//
//		return list;
//	}
//
//	
//	
//	private static void deleteWaiMaiListFromDB() {
//		SQLiteDatabase db;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//		} catch (Exception e) {
//			return;
//		}
//		try {
//			db.execSQL("DROP TABLE " + "WaiMai");
//		} catch (Exception e) {
//			db.close();
//			e.printStackTrace();
//		}
//		db.close();
//	}
//
//	
//	public static void saveWaiMaiListFromDB(List<List<String>> list) {
//		deleteWaiMaiListFromDB();
//		SQLiteDatabase db;
//		Cursor cursor;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//									+ "/doBell/data.db", null);
//			cursor = db
//					.rawQuery(
//							"select name from sqlite_master where type='table' order by name",
//							null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//		boolean isExist = false;
//		while (cursor.moveToNext()) {
//			if ("WaiMai".equals(cursor.getString(0)))
//				isExist = true;
//		}
//		if (!isExist) {
//			db.execSQL("create table " + "WaiMai" + "(restaurant varchar ,"
//					+ "phonenumber varchar ," + "notice varchar,"
//					+ "dish  varchar)");
//		}		
//		for (int i = 0; i < list.size(); i++) {
//			list.get(i).size();
//			
//			
//			db.execSQL("insert into " + "WaiMai" + " values ('"
//					+ 
//					list.get(i).get(0) + "','" + // 0
//					list.get(i).get(1) + "','" + // 1
//					list.get(i).get(2) + "','" + // 2
//					list.get(i).get(3) + "')"); // 3
//		}
//		cursor.close();
//		db.close();
//	}
//
//	
//	
//	public static List<List<String>> getFood(JSONObject obj)
//			throws JSONException {
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("restaurant"));
//			list.get(i).add(o.getString("phonenumber"));
//			Log.i("qqqqqq",o.getString("notice"));
//			if(o.getString("notice").equals("无"))
//				list.get(i).add("");
//			else
//				list.get(i).add(o.getString("notice"));
//			JSONArray dishAry = o.getJSONArray("dish");
//			StringBuffer sb = new StringBuffer();
//			for(int k=0;k<dishAry.length();k++){
//				sb.append(dishAry.get(k)+"#");
//			}
//			list.get(i).add(sb.toString());
//		}
//		saveWaiMaiListFromDB(list);
//		return list;
//	}
//	
//	public static List<List<String>> getGradeListFromDB() {
//		List<List<String>> list = null;
//		try{
//		SQLiteDatabase db;
//		Cursor cursor;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//			cursor = null;
//		} catch (Exception e) {
//			return null;
//		}
//		try {
//			cursor = db.rawQuery("select * from " + "Grade", null);
//		} catch (Exception e) {
//			db.close();
//			return null;
//		}
//		list = new ArrayList<List<String>>();
//		while (cursor.moveToNext()) {
//			List<String> newList = new ArrayList<String>();
//			newList.add(cursor.getString(0));
//			newList.add(cursor.getString(1));
//			newList.add(cursor.getString(2));
//			newList.add(cursor.getString(3));
//			newList.add(cursor.getString(4));
//			newList.add(cursor.getString(5));
//			newList.add(cursor.getString(6));
//			list.add(newList);
//		}
//		cursor.close();
//		db.close();
//		}catch(Exception e)
//		{
//			deleteGradeListFromDB();
//		}
//		return list;
//	}
//
//	
//	
//	public static void deleteGradeListFromDB() {
//		SQLiteDatabase db;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//		} catch (Exception e) {
//			return;
//		}
//		try {
//			db.execSQL("DROP TABLE " + "Grade");
//		} catch (Exception e) {
//			db.close();
//			e.printStackTrace();
//		}
//		db.close();
//	}
//
//	
//	public static void saveGradeListFromDB(List<List<String>> list) {
//		deleteGradeListFromDB();
//		SQLiteDatabase db;
//		Cursor cursor;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//									+ "/doBell/data.db", null);
//			cursor = db
//					.rawQuery(
//							"select name from sqlite_master where type='table' order by name",
//							null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//		boolean isExist = false;
//		while (cursor.moveToNext()) {
//			if ("Grade".equals(cursor.getString(0)))
//				isExist = true;
//		}
//		if (!isExist) {
//			db.execSQL("create table " + "Grade" + "(xn varchar ,"
//					+ "xq varchar ," + "name varchar,"+ "xq11 varchar ," + "name11 varchar,"+ "xq12 varchar ,"
//					+ "score  varchar)");
//		}		
//		for (int i = 0; i < list.size()-1; i++) {
//			list.get(i).size();
//			if(!list.get(i).get(0).equals("seg")){
//				db.execSQL("insert into " + "Grade" + " values ('"
//					+ 
//					list.get(i).get(0) + "','" + // 0
//					list.get(i).get(1) + "','" + // 1
//					list.get(i).get(2) + "','" + // 2
//					list.get(i).get(3) + "','" + // 2
//					list.get(i).get(4) + "','" + // 2
//					list.get(i).get(5) + "','" + // 2
//					list.get(i).get(6) + "')"); // 3
//				}
//			else{
//				db.execSQL("insert into " + "Grade" + " values ('"
//						+ 
//						list.get(i).get(0) + "','" + // 0
//						"" + "','" + // 1
//						"" + "','" + // 2
//						"" + "','" + // 2
//						"" + "','" + // 2
//						"" + "','" + // 2
//						"" + "')"); // 3
//
//			}
//
//			}				
//			Log.i("what"," a a a :"+list.get(list.size()-1).get(0));
//			db.execSQL("insert into " + "Grade" + " values ('"
//				+ 
//				list.get(list.size()-1).get(0) + "','" + // 0
//				"" + "','" + // 1
//				"" + "','" + // 2
//				"" + "','" + // 2
//				"" + "','" + // 2
//				"" + "','" + // 2
//				"" + "')"); // 3
//
//		cursor.close();
//		db.close();
//	}
//
//
//	public static List<List<String>> getGrade(JSONObject jsonObject) {
//		List<List<String>> dataList = new ArrayList<List<String>>();
//		double zxf = 0;
//		double zjd = 0;
//		try {
//			JSONObject jso = jsonObject;
//			JSONArray ary = jso.getJSONArray("cj_info");
//			boolean flag = true;
//			int position;
//			// 反向加入，保证最新的课程在前
//			for (int i = ary.length() - 1; i >= 0; i--) {
//				dataList.add(new ArrayList<String>());
//				position = dataList.size() - 1;
//				// xq不一样则添加一个标志
//				if (i == ary.length() - 1
//						|| !ary.getJSONObject(i)
//								.getString("xq")
//								.equals(ary.getJSONObject(i + 1)
//										.getString("xq"))) {
//					if (position != 0)
//						flag = false;
//					dataList.get(position).add("seg");
//					dataList.add(new ArrayList<String>());
//					position = dataList.size() - 1;
//				}
//				dataList.get(position)
//						.add(ary.getJSONObject(i).getString("xn"));
//				dataList.get(position)
//						.add(ary.getJSONObject(i).getString("xq"));
//				dataList.get(position).add(
//						ary.getJSONObject(i).getString("subname"));
//				dataList.get(position)
//						.add(ary.getJSONObject(i).getString("xf"));
//				dataList.get(position)
//						.add(ary.getJSONObject(i).getString("jd"));
//				zxf += Double.valueOf(ary.getJSONObject(i).getString("xf"));
//				zjd += Double.valueOf(ary.getJSONObject(i).getString("jd"))
//						* Double.valueOf(ary.getJSONObject(i).getString("xf"));
//
//				dataList.get(position).add(
//						ary.getJSONObject(i).getString("point"));
//
//				if (flag)
//					dataList.get(position).add("notgetted");
//				else
//					dataList.get(position).add("getted");
//
//			}
//
//			dataList.add(new ArrayList<String>());
//			position = dataList.size() - 1;
//
//			DecimalFormat df = new DecimalFormat(".##");
//
//			String st = df.format((zjd / zxf));
//			dataList.get(position).add("" + (st));
//			// pjjd = jso.getString("pjjd");
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return dataList;
//	}
//
//	public static List<List<String>> getKscx(JSONObject obj)
//			throws JSONException {
//		List<List<String>> list = new ArrayList<List<String>>();
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("subname"));
//			list.get(i).add(o.getString("time"));
//			list.get(i).add(o.getString("area"));
//			list.get(i).add(o.getString("seat"));
//			list.get(i).add(o.getString("campus"));
//
//		}
//
//		return list;
//	}
//
//	public static List<List<String>> getGongao(JSONObject obj)
//			throws JSONException {
//		List<List<String>> list = new ArrayList<List<String>>();
//
//		JSONArray ary = obj.getJSONArray("info");
//		for (int i = 0; i < ary.length(); i++) {
//			list.add(new ArrayList<String>());
//			JSONObject o = ary.getJSONObject(i);
//			list.get(i).add(o.getString("title"));
//			list.get(i).add(o.getString("text"));
//			list.get(i).add(o.getString("add_date"));
//			list.get(i).add(o.getString("position"));
//			try {
//				JSONArray imageAry = o.getJSONArray("image");
//				for (int i2 = 0; i2 < imageAry.length(); i2++)
//					list.get(i).add(imageAry.getString(i2));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	public static List<List<String>> getXSJZ(JSONObject obj) {
//		List<List<String>> list = new ArrayList<List<String>>();
//
//		try {
//			JSONArray ary = obj.getJSONArray("info");
//			for (int i = 0; i < ary.length(); i++) {
//
//				list.add(new ArrayList<String>());
//				JSONObject o = ary.getJSONObject(i);
//				list.get(i).add(o.getString("title"));
//				list.get(i).add(o.getString("time"));
//				list.get(i).add(o.getString("link"));
////				list.get(i).add(o.getString("topic"));
//
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		return list;
//	}
//
//	private static void deleteKCBListFromDB(String id) {
//		SQLiteDatabase db;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//		} catch (Exception e) {
//			return;
//		}
//		try {
//			db.execSQL("DROP TABLE " + id);
//		} catch (Exception e) {
//			db.close();
//			e.printStackTrace();
//		}
//		db.close();
//	}
//
//	public static void saveKCBListFromDB(String id, List<Project> list) {
//		deleteKCBListFromDB(id);
//		SQLiteDatabase db;
//		Cursor cursor;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//			cursor = db
//					.rawQuery(
//							"select name from sqlite_master where type='table' order by name",
//							null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//		boolean isExist = false;
//		while (cursor.moveToNext()) {
//			if (id.equals(cursor.getString(0)))
//				isExist = true;
//		}
//		if (!isExist) {
//			db.execSQL("create table " + id + "(name varchar ,"
//					+ "teacherName varchar ," 
//					+ "addname varchar,"
//					+ "fromWeek  tinyint," 
//					+ "toWeek tinyint,"
//					+ "startWeek tinyint," 
//					+ "startTime tinyint,"
//					+ "times tinyint," 
//					+ "endTime tinyint," 
//					+ "color int,"
//					+ "ds tinyint )");
//		}
//		for (int i = 0; i < list.size(); i++) {
//			db.execSQL("insert into " + id + " values ('"
//					+ list.get(i).name + "','" + // 0
//					list.get(i).teacherName + "','" + // 1
//					list.get(i).address + "','" + // 2
//					list.get(i).fromWeek + "','" + // 3
//					list.get(i).toWeek + "','" + // 4
//					list.get(i).startWeek + "','" + // 5
//					list.get(i).startTime + "','" + // 6
//					list.get(i).times + "','" + // 7
//					list.get(i).endTime + "','" + // 8
//					list.get(i).color + "','" + // 9
//					list.get(i).ds + "')"); // 10
//		}
//		cursor.close();
//		db.close();
//	}
//
//	public static List<Project> getKCBListFromDB(String id) {
//		SQLiteDatabase db;
//		Cursor cursor;
//		try {
//			db = SQLiteDatabase
//					.openOrCreateDatabase(
//							Environment.getExternalStorageDirectory() 
//							+ "/doBell/data.db", null);
//			cursor = null;
//		} catch (Exception e) {
//			return null;
//		}
//		try {
//			cursor = db.rawQuery("select * from " + id, null);
//		} catch (Exception e) {
//			db.close();
//			return null;
//		}
//		List<Project> list = new ArrayList<Project>();
//		while (cursor.moveToNext()) {
//			list.add(new Project(cursor.getInt(9), cursor.getInt(3), cursor
//					.getInt(4), cursor.getInt(6), cursor.getInt(7), cursor
//					.getInt(5), cursor.getString(0), cursor.getString(2),
//					cursor.getString(1), cursor.getInt(10)));
//		}
//		cursor.close();
//		db.close();
//
//		return list;
//	}
//
////	public static List<Project> getKCBList(JSONObject jsO) {
////
////		List<Project> list = new ArrayList<Project>();
////		String subname = "", teacher = "";
////		int t;
////
////		int clo[] = { 0xEEA9B8,0xEE9572,0xEE799F,0xEE9A00,0xEE9A49,0xEEC591,
////				0xE9967A ,0xDB7093,0xD8BFD8	};
////
////		try {
////			JSONObject fo = jsO;
////			// 抛出异常
////			if (!fo.getString("msg").equals("SUCCESS")) {
////				return null;
////			}
////			JSONArray ary = fo.getJSONArray("kb");
////			for (int i = 0; i < ary.length(); i++) {
////				JSONObject o = ary.getJSONObject(i);
////				subname = (String) o.get("subname");
////				teacher = (String) o.get("teacher");
////				int dsWeek;
////				JSONObject o2 = o.getJSONObject("info");
////				t = o2.getInt("times");
////				for (int j = 0; j < t; j++) {
////					JSONArray aryWD = o2.getJSONArray("weekday");
////					JSONArray aryJC = o2.getJSONArray("jc");
////					JSONArray arywk = o2.getJSONArray("week");
////					JSONArray aryds = o2.getJSONArray("ds_week");
////					dsWeek = aryds.getInt(j);
////					JSONArray aryar = o2.getJSONArray("area");
////					if (j > 0
////							&& list.get(list.size() - 1).getStartWeek() == aryWD
////									.getInt(j)
////							&& list.get(list.size() - 1).getAddress()
////									.equals(aryar.getString(j))) {
////						int l = aryJC.getJSONArray(j).length();
////						list.get(list.size() - 1).setEndTime(
////								aryJC.getJSONArray(j).getInt(l - 1));
////						list.get(list.size() - 1).setTimes(
////								list.get(list.size() - 1).getEndTime()
////										- list.get(list.size() - 1)
////												.getStartTime() + 1);
////					} else {
////						int l = aryJC.getJSONArray(j).length();
////						list.add(new Project(clo[i % clo.length], arywk.getJSONArray(j)
////								.getInt(0), arywk.getJSONArray(j).getInt(1),
////								aryJC.getJSONArray(j).getInt(0), aryJC
////										.getJSONArray(j).getInt(l - 1)
////										- aryJC.getJSONArray(j).getInt(0) + 1,
////								aryWD.getInt(j), subname, aryar.getString(j),
////								teacher, dsWeek));
////					}
////
////				}
////			}
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			return null;
////		}
////
////		return list;
////	}
//
//	public static int NEXT_PAGE = 1;
//
//	public static List<List<String>> getLibSearchList(JSONObject obj)
//			throws JSONException {
//		List<List<String>> list = new ArrayList<List<String>>();
//		int count;
//
//		JSONObject jsob;
//		
//		jsob = obj.getJSONObject("info");
//		count = jsob.getInt("count");
//		JSONArray jsary = jsob.getJSONArray("info");
//
//		for (int i = 0; i < count; i++) {
//			ArrayList<String> t = new ArrayList<String>();
//			// list.add(new ArrayList<String>());
//			JSONObject o = jsary.getJSONObject(i);
//			t.add(o.getString("name"));
//			t.add(o.getString("author"));
//			t.add(o.getString("req_number"));
//			t.add(o.getString("publisher"));
//			t.add(o.getString("pub_date"));
//			t.add(o.getString("link"));
//			list.add(t);
//		}
//		NEXT_PAGE = jsob.getInt("next_page");
//		Log.i("tttt", "" + NEXT_PAGE);
//		return list;
//	}
//
//	public static List<String> getAddressAndStatus(JSONObject obj)
//			throws JSONException {
//		List<String> list = new ArrayList<String>();
//		try {
//			JSONObject oo =obj;
//			JSONArray jary = oo.getJSONArray("info");
//			for (int i = 0; i < jary.length(); i++) {
//				JSONObject o = jary.getJSONObject(i);
//				list.add(o.getString("address"));
//				list.add(o.getString("statue"));
//			}
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//		return list;
//	}
//
//	/**
//	 * 
//	 * @param type
//	 *            值为1则返回借阅的没有还，为2则是归还过的
//	 * @param pw
//	 * @return
//	 * @throws JSONException 
//	 */
//	public static List<List<String>> getMyBook(JSONObject obj) throws JSONException {
//		List<List<String>> list = new ArrayList<List<String>>();
//		
//			JSONObject o = obj;
//			JSONArray ary = o.getJSONArray("info");
//			for (int i = 0; i < ary.length(); i++) {
//				list.add(new ArrayList<String>());
//				JSONObject ob = ary.getJSONObject(i);
//				list.get(i).add(ob.getString("book_name"));
//				list.get(i).add(ob.getString("address"));
//				list.get(i).add(ob.getString("ISBN"));
//				list.get(i).add(ob.getString("outtime"));
//				list.get(i).add(ob.getString("intime"));
//				list.get(i).add(ob.getString("next_times"));
//			}
//		return list;
//
//	}
//
//	
//	
//}
