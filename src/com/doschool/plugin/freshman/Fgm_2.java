package com.doschool.plugin.freshman;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.doschool.listener.NetWorkFunction;
import com.umeng.analytics.MobclickAgent;

public class Fgm_2 extends Fragment {
	private static final String PACKAGE_NAME = "com.doschool.plugin.grade";
	private NetWorkFunction n;
	private Handler handler;
	private Context context;
//	private Resources pluginRes;
	private LayoutInflater inflater;
	String url;
	String name;
	public Fgm_2(NetWorkFunction netWorkListener,Handler h, String url,String name) {
		n = netWorkListener;
		handler = h;
		this.url = url;
		this.name = name;
		Log.i("URL",url);
	}
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem add = menu.add(0, 100, 0, "刷新");
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return super.onOptionsItemSelected(item);
		case 100:
			if(wv!=null){
				wv.loadUrl("about:blank");
				wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
				wv.loadUrl(url);
			}
			return true;
		}
		return false;
	}



	@Override
	public void onCreate(Bundle b){		
		setHasOptionsMenu(true);
		super.onCreate(b);
	}
	WebView wv;
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);
		
		Log.i("FGMC4",""+getActivity().getSupportFragmentManager().getBackStackEntryCount());
		readyRes();
		wv = new WebView(getActivity());
		wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv.getSettings().setSupportZoom(true);   

        //设置默认缩放方式尺寸是far   

        wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);  

        //设置出现缩放工具   

        wv.getSettings().setBuiltInZoomControls(true);



     // 让网页自适应屏幕宽度

        WebSettings webSettings= wv.getSettings(); // webView: 类WebView的实例

        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);


		wv.loadUrl(url);
		return wv;
	
	}

	private void readyRes() {
		try {
			context = getActivity();
//			.createPackageContext(
//					PACKAGE_NAME,
//					Context.CONTEXT_INCLUDE_CODE
//							| Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		try {
//			pluginRes = getActivity().getPackageManager()
//					.getResourcesForApplication(PACKAGE_NAME);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}

//		handler = PluginListener.handler;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+name);
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+name);
		super.onResume();
	}


	
}
