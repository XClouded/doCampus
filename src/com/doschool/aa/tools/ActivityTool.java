package com.doschool.aa.tools;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.listener.NetWorkFunction;
import com.umeng.analytics.MobclickAgent;

import dalvik.system.DexClassLoader;

@SuppressLint("NewApi")
public class ActivityTool extends FragmentActivity {
	Class<?> clz = null;
	Resources pluginRes;
	Handler handler;
	Object obj;

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.fgm_tool_kcb, menu);
	// return super.onCreateOptionsMenu(menu);
	// }
	
	
	

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	


	OnKeyDownListener l;
	public void setOnKeyDownLis(OnKeyDownListener l){
		Log.i("KEYTOOLINFO",""+		this.l +"  "+ l);
		this.l = l;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("KEYTOOLINFO","wawa");
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Log.i("KEYTOOLINFO","wawa1");

			if(l.popBack()==true){
				Log.i("KEYTOOLINFO","wawa2");
				setOnKeyDownLis(new OnKeyDownListener());
				return super.onKeyDown(keyCode, event);
			}
			else{				
				Log.i("KEYTOOLINFO","waw3");
				return true;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
	String localPacName[] = { "com.doschool.plugin.freshman",
			"com.doschool.plugin.grade", "com.doschool.plugin.kcb",
			"com.doschool.plugin.waimai", "com.doschool.plugin.lib",
			"com.doschool.plugin.qtfm", "com.doschool.plugin.web", };

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		l = new OnKeyDownListener();
		setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);

		// 设置ActionBar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.topbar_bg));
		getActionBar().setTitle("校园神器");
		getActionBar().setIcon(
				getResources().getDrawable(R.drawable.ic_launcher));

		setContentView(R.layout.tools_activity);
		handler = new Handler();

		Intent it = getIntent();
		String packageName = it.getStringExtra("packageName");

		// if (!packageName.equals("local")) {
		// String dexPath = it.getStringExtra("dexPath");
		// String dexOutputDir = it.getStringExtra("dexOutputDir");
		// String libPath = it.getStringExtra("libPath");
		// PackageManager pm = getPackageManager();
		//
		// try {
		// pluginRes = pm.getResourcesForApplication(packageName);
		// } catch (NameNotFoundException e1) {
		// e1.printStackTrace();
		// }
		//
		// Log.i("WHATTT", dexPath + "  " + dexOutputDir + "   " + libPath);
		// DexClassLoader cl = new DexClassLoader(dexPath, dexOutputDir,
		// libPath, getClass().getClassLoader());
		// try {
		// clz = cl.loadClass(packageName + ".PluginListener");
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		// //本地应用
		// else{
//		if (toolsInfo.get(p).toolid <= localPacName.length)
//			it.putExtra("localp", localPacName[toolsInfo.get(p).toolid - 1]);

		String name = it.getStringExtra("name");
		getActionBar().setTitle(name);
		String serURL = it.getStringExtra("serURL");
		int toolid = it.getIntExtra("toolid", 1);
		int status = it.getIntExtra("status", -1);
		int isPublic = it.getIntExtra("isPublic", -1);
		int type = it.getIntExtra("type", -1);
		String className = "" ;
		Log.i("TOOLINFO","name"+name+"  serU:"+serURL+"  type:"+type);
		if(status != 1){
			//TODO 
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setMessage("抱歉，本应用暂时无法使用").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityTool.this.finish();
				}
			}).create().show();
			return ;
		}
		
		else if(isPublic != 1 && DoschoolApp.isGuest()){
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setMessage("抱歉，游客暂时无法使用本工具").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityTool.this.finish();
				}
			}).create().show();
			return ;
		}
		
		if(type == 1){
			className = localPacName[toolid-1];

		}
		else{
			className = "com.doschool.plugin.web";
		}
		
		
		try {
			clz = Class.forName(className + ".PluginListener");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// }
		
		try {
			obj = clz.newInstance();// PluginListener类的实例
			Method action = obj.getClass().getDeclaredMethod(
					"setNetWorkFunction", NetWorkFunction.class);
			action.setAccessible(true);
			action.invoke(obj, new NetWorkFunction() {
				@Override
				public String remoteServer(String arg0, String arg1) {
					return NetWorkUtils.remoteServer(arg0, arg1);
				}
			});

			action = obj.getClass().getDeclaredMethod("setForPlugin",
					FragmentActivity.class, Handler.class, Resources.class,String.class,String.class);
			action.invoke(obj, this, handler, pluginRes,serURL,name);

			action = obj.getClass().getDeclaredMethod("INIT");
			action.invoke(obj);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	protected void onPause() {
	    MobclickAgent.onPageEnd(getClass().getName());
	    MobclickAgent.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
	    MobclickAgent.onPageStart(getClass().getName());
	    MobclickAgent.onResume(this);
		super.onResume();
	}
	
	

}
