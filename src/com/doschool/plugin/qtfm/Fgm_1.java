package com.doschool.plugin.qtfm;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.doschool.app.DoschoolApp;
import com.doschool.listener.NetWorkFunction;
import com.umeng.analytics.MobclickAgent;

public class Fgm_1 extends Fragment {
	private static final String PACKAGE_NAME = "com.doschool.plugin.grade";
	private NetWorkFunction n;
	// private Handler handler;
	private Context context;
	// private Resources pluginRes;
	// private LayoutInflater inflater;
	DoschoolApp app;
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onResume();
	}

	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		// handler = h;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return super.onOptionsItemSelected(item);
		case 100:
			Log.i("TOOLINFO","WAWWAABACK!!!");
			WebView wb;
			wb = app.getQTWB();
			if (wb != null) {
				wb.loadUrl("about:blank");
				wb = null;
				app.setQTWB(null);
				getActivity().finish();
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem add = menu.add(0, 100, 0, "stop");
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

	}
	Handler handler;
	WebView wb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		app = (DoschoolApp) getActivity().getApplication();
		wb = app.getQTWB();
		context = getActivity();

		handler = new Handler();
		if (wb == null) {
			wb = new WebView(getActivity().getApplicationContext());
			app.setQTWB(wb);
			wb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			wb.setWebViewClient(new WebViewClient(){

				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
//					super.onReceivedError(view, errorCode, description, failingUrl);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							wb.loadUrl("about:blank");
							if(getActivity()!=null)
							Toast.makeText(getActivity(), "网络错误！请检查网络连接", 1).show();
						}
					});
				}

				public boolean shouldOverrideUrlLoading(WebView view, String url) { 
				       view.loadUrl(url);					
				       return true;
				 }				
			});
//			((ActivityTool) getActivity()).setOnKeyDownLis(new OnKeyDownListener() {
//				public boolean popBack() {
//					if (loadNum == 0)
//						return true;
//					else {
//						wb.goBack();
//						loadNum--;
//						return false;
//					}
//				}
//			});

			wb.getSettings().setJavaScriptEnabled(true);
			wb.loadUrl(PluginListener.postU);
		}
	}
	

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);
        ViewGroup p = (ViewGroup) wb.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 
		
		return wb;

	}

}
