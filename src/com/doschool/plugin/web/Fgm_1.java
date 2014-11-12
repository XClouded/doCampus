package com.doschool.plugin.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.doschool.aa.tools.ActivityTool;
import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.aa.tools.NetWorkUtils;
import com.doschool.aa.tools.OnKeyDownListener;
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
	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		// handler = h;
	}	
	
	@Override
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onResume();
	}

	
	
	int needShare = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		needShare = getActivity().getIntent().getIntExtra("needShare",0);
		if(needShare == 1){
		jsName = getActivity().getIntent().getStringExtra("jsName");
		shareURL = getActivity().getIntent().getStringExtra("shareURL");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if(needShare ==1){
			MenuItem i = menu.add(0,100,0,"分享");
			i.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	String jsName = "";
	String shareURL = "";
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return super.onOptionsItemSelected(item);
		case 100:
			Log.i("jsName","1111:"+jsName);
//			wv.loadUrl("javascript:shareAlert()");

			wv.loadUrl("javascript:"+jsName+"()");//+jsName+"()");
			return true;
		}
		return false;
	}


	AlertDialog al;
	WebView wv;
	int loadNum = 0;
	Handler handler;
	LoadingProgressView proLay;
	RelativeLayout rl ;
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);
		Log.i("TOOLINFO", "url" + PluginListener.postU);
		handler = new Handler();
		wv = new WebView(getActivity());
		context = getActivity();
		rl = new RelativeLayout(context);
		proLay = new LoadingProgressView(context, 8);
		proLay.beginMoving();
		
		wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageFinished(WebView view, String url) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						proLay.stopMoving();
						ViewGroup vgp = ((ViewGroup) proLay.getParent());
						if (vgp != null)
							vgp.removeView(proLay);
					}
				});
				super.onPageFinished(view, url);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				loadNum++;
				return true;
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
//				super.onReceivedError(view, errorCode, description, failingUrl);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						proLay.stopMoving();
						ViewGroup vgp = ((ViewGroup) proLay.getParent());
						if (vgp != null)
							vgp.removeView(proLay);

						wv.loadUrl("about:blank");
						if(getActivity()!=null)
						Toast.makeText(getActivity(), "网络情况不太好啊，暂时无法连接哦~", 1).show();
					}
				});
			}


		});
        wv.setWebChromeClient(new WebChromeClient() {  
            /** 
             * 处理JavaScript Alert事件 
             */  
            @Override  
            public boolean onJsAlert(WebView view, String url,  
                   final String message, final JsResult result) {  
            	Log.i("jsName","aaaaa");
                //用Android组件替换  
            	if(getActivity()!=null && message.startsWith("doBell:"))
            	{
                	final Context c = getActivity().getApplicationContext();
                new AlertDialog.Builder(getActivity())  
                    .setTitle(getActivity().getIntent().getStringExtra("name"))
                    .setMessage(message.subSequence(7, message.length()))  
                    .setPositiveButton("一键分享", new AlertDialog.OnClickListener() {  
                        public void onClick(DialogInterface dialog, int which) {  
                        	new Thread(){
                        		public void run(){
                        			handler.post(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											ProgressBar pro = new ProgressBar(getActivity());
											pro.setIndeterminate(true);
											al = new AlertDialog.Builder(getActivity())
											.setView(pro)
											.setCancelable(false)
											.create();//.show();
											al.show();
										}
									});
                        			String s = null;
									try {
										s = NetWorkUtils.post("http://commsev4all.duapp.com/beta1.0/android/microblog/MicroblogShareAPI.php", 
												"usrId="+DoschoolApp.thisUser.personId+
												"&topic="+
												URLEncoder.encode(getActivity().getIntent().getStringExtra("name"),"UTF-8")+ 
												"&shareStr="+
												URLEncoder.encode(message.subSequence(7, message.length()).toString(),"UTF-8"));
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                        			if(s==null || s.equals("") || s.startsWith("E")){
                        				handler.post(new Runnable(){
                        					public void run(){
                        						Toast.makeText(c, "网络错误",1).show();
                        					}
                        				});
                        			}
                        			
                    				handler.post(new Runnable(){
                    					public void run(){
                    						if(al!=null)
                    						al.dismiss();
                    					}
                    				});

                        		}
                        	}.start();
                        }
                    })
                    .setCancelable(true)  
                    .create().show();  
            	}
                result.confirm();  
                return true;  
            }  
        });  

		
		((ActivityTool) getActivity()).setOnKeyDownLis(new OnKeyDownListener() {
			public boolean popBack() {
				if (loadNum == 0)
					return true;
				else {
					wv.goBack();
					loadNum--;
					return false;
				}
			}
		});
		wv.loadUrl(PluginListener.postU);
//		wv.loadUrl("http://dotools.sinaapp.com/2048-1/");

		rl.addView(wv,RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
		rl.addView(proLay);
//		rl.addView(child)
		return rl;

	}

}
