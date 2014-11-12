package com.doschool.methods;

import java.util.ArrayList;

import com.doschool.app.DoschoolApp;
import com.doschool.component.updatelater.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "missan14.db";
	static final String TBL_TASK = "task";
	static final String TBL_PIC = "pic";

	public static final String TBL_TASK_FILD_TID = "tid";
	public static final String TBL_TASK_FILD_USRID = "usrId";
	public static final String TBL_TASK_FILD_TRANID = "tranId";
	public static final String TBL_TASK_FILD_ROOTID = "rootId";
	public static final String TBL_TASK_FILD_CONTENT = "Content";
	public static final String TBL_TASK_FILD_TOPIC = "topic";
	public static final String TBL_TASK_FILD_DEFINATION = "defination";
	public static final String TBL_TASK_FILD_DATE = "date";

	public static final String TBL_PIC_FILD_PID = "pid";
	public static final String TBL_PIC_FILD_TID = "tid";
	public static final String TBL_PIC_FILD_PATH = "path";
	
	
	private static final String CREATE_TBL_TASK = "create table " + TBL_TASK
			+ "(tid integer primary key autoincrement,usrId text, tranId text,rootId text,Content text,topic text,defination text,date text)";
	
	private static final String CREATE_TBL_PIC = "create table " + TBL_PIC
			+ "(pid integer primary key autoincrement,tid text, path text)";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TBL_TASK);
		db.execSQL(CREATE_TBL_PIC);
	}

	
	public void insertTask(Task task) {

		ContentValues cv = new ContentValues();
		cv.put(TBL_TASK_FILD_USRID, task.usrId);
		cv.put(TBL_TASK_FILD_TRANID, task.tranMblogId);
		cv.put(TBL_TASK_FILD_ROOTID, task.rootMblogId);
		cv.put(TBL_TASK_FILD_CONTENT, task.mblogContent);
		cv.put(TBL_TASK_FILD_DEFINATION, task.defination);
		cv.put(TBL_TASK_FILD_TOPIC, task.topic);
		cv.put(TBL_TASK_FILD_DATE, System.currentTimeMillis());
		long rowId=DoschoolApp.db.insert(TBL_TASK, null, cv);
		
		
		
		for (String path : task.picPathList) {
			ContentValues cv2 = new ContentValues();
			cv2.put(TBL_PIC_FILD_TID, rowId);
			cv2.put(TBL_PIC_FILD_PATH, path);
			DoschoolApp.db.insert(TBL_PIC, null, cv2);
		}
	}

	
	
	
	
	public Task getTask() {

		Cursor cursor_task = null;
		Cursor cursor_pic = null;
		Task task = null;
		try {
			
			cursor_task = DoschoolApp.db.query(TBL_TASK, new String[] { 
					TBL_TASK_FILD_TID, 
					TBL_TASK_FILD_USRID, 
					TBL_TASK_FILD_TRANID, 
					TBL_TASK_FILD_ROOTID, 
					TBL_TASK_FILD_CONTENT,
					TBL_TASK_FILD_TOPIC, 
					TBL_TASK_FILD_DEFINATION, 
					TBL_TASK_FILD_DATE}, null, null,
					null, null, "date ASC");
				while (cursor_task.moveToNext()) {
					task = new Task();
					task._id = cursor_task.getInt(0);
					task.usrId = cursor_task.getInt(1);
					task.tranMblogId = cursor_task.getInt(2);
					task.rootMblogId = cursor_task.getInt(3);
					task.mblogContent = cursor_task.getString(4);
					task.topic = cursor_task.getString(5);
					task.defination = cursor_task.getInt(6);
					task.date = cursor_task.getLong(7);
					cursor_pic = DoschoolApp.db.rawQuery("SELECT pid,path,tid From pic WHERE tid = ?", new String[]{task._id+""});
					task.picIdList=new ArrayList<Integer>();
					task.picPathList=new ArrayList<String>();
					while (cursor_pic.moveToNext()) {
						task.picIdList.add(cursor_pic.getInt(0));
						task.picPathList.add(cursor_pic.getString(1));
					}
					
					break;
					
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor_task != null) {
				cursor_task.close();
			}
			if (cursor_pic != null) {
				cursor_pic.close();
			}
		}
		return task;
	}

	public void updatePic(String value, int pid) {
		ContentValues values = new ContentValues();
		values.put(TBL_PIC_FILD_PATH, value);// key为字段名，value为值
		DoschoolApp.db.update(TBL_PIC, values, "pid=?", new String[] { pid + "" });
	}

	public void removeTask(int tid) {

		try {
			DoschoolApp.db.delete(TBL_TASK, "tid=" + tid, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void removeAll()
	{
		DoschoolApp.db.execSQL("delete from '"+TBL_TASK+"'");
		DoschoolApp.db.execSQL("delete from '"+TBL_PIC+"'");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}