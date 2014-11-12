package com.doschool.plugin.Gongdakcb;

import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.doschool.listener.NetWorkFunction;


public class PluginListener {
	public NetWorkFunction netWorkListener;
	public static int frmId;
	public static String postU,myNAME;
	Resources res;
	Handler handler;
	FragmentActivity act;
	public void setNetWorkFunction(NetWorkFunction n) {
		netWorkListener = n;
	}



	public void setForPlugin(FragmentActivity a,Handler h,Resources r,String post,String name) {
		act = a;
		handler = h;
		res = r;
		postU = post;
		myNAME =name;
	}

	Fragment f1, f2;

	public void INIT() {
		try {
			FragmentManager fm = act.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Log.i("FGMC1",""+fm.getBackStackEntryCount());

			f1 = new Fgm_1(netWorkListener,handler);
			frmId = act.getResources().getIdentifier("frg_tool", "id",
					act.getPackageName());
			ft.add(frmId, f1);
			ft.commit();
			Log.i("FGMC2",""+fm.getBackStackEntryCount());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
