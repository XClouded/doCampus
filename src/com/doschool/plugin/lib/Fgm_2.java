package com.doschool.plugin.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.listener.NetWorkFunction;
import com.umeng.analytics.MobclickAgent;

public class Fgm_2 extends Fragment {
	private static final String PACKAGE_NAME = "com.doschool.plugin.lib";
	private NetWorkFunction n;
	private Handler handler;
	private Context context;
//	private Resources pluginRes;
	private LayoutInflater inflater;
	String word;

	@Override
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+"搜索");
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+"搜索");
		super.onResume();
	}

	
	public Fgm_2(NetWorkFunction netWorkListener, Handler h, String word) {
		n = netWorkListener;
		handler = h;
		this.word = word;
	}

	ListView listSearch;
	ListAdp adp; 
	Bitmap bm;
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);

		Log.i("FGMC4", ""
				+ getActivity().getSupportFragmentManager()
						.getBackStackEntryCount());
		readyRes();
		adp = new ListAdp();
		
		bm = Bitmap.createBitmap((int)DensityUtil.getWidth(context), 2, Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(bm);
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		c.drawRect(0, 0, DensityUtil.getWidth(context), 2, p);
		p.setColor(0xffbdbdbd);
		c.drawRect((int)DensityUtil.dip2px(context, 22), 0, DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 22), 2, p);
		listSearch = new ListView(context);
		listSearch.setBackgroundColor(Color.WHITE);
		listSearch.setDivider(new BitmapDrawable(getResources(), bm));
		listSearch.setAdapter(adp);
		listSearch.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
	                int totalItemCount) {
				Log.i("LISTV",""+firstVisibleItem+" "+visibleItemCount+" "+totalItemCount);
				if(firstVisibleItem + visibleItemCount >= totalItemCount){
					if(runT.isRunning() || hasNext == 0)
						return;
					runT = new UpDataFromNet();
					runT.start();
				}
			}
		});		
		listSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if( list==null || position==list.size()){
					return;
				}
				Activity act = getActivity();
				int frmId = act.getResources().getIdentifier("frg_tool", "id",
						act.getPackageName());
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.add(frmId, new Fgm_3(n, handler, list.get(position)
						));
				ft.addToBackStack("Fgm1");
				ft.commit();

			}
		});
		
		runT = new UpDataFromNet();
		runT.start();
		return listSearch;

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

		// handler = PluginListener.handler;
		inflater = LayoutInflater.from(context);
	}

	
	private void upUI(){
		adp.notifyDataSetChanged();
	}
	
	int nowPage = 1;
	List<List<String>> list;
	int hasNext = 1;
	private void upData(JSONObject ob) {
		if(nowPage==1)
			list = new ArrayList<List<String>>();
		int count;
		JSONObject jsob;

		try {
			jsob = ob.getJSONObject("info");
			count = jsob.getInt("count");
			JSONArray jsary = jsob.getJSONArray("info");

			for (int i = 0; i < count; i++) {
				ArrayList<String> t = new ArrayList<String>();
				JSONObject o = jsary.getJSONObject(i);
				t.add(o.getString("name"));
				t.add(o.getString("author"));
				t.add(o.getString("req_number"));
				t.add(o.getString("publisher"));
				t.add(o.getString("pub_date"));
				t.add(o.getString("link"));
				list.add(t);
			}
			hasNext = jsob.getInt("next_page");
			if(hasNext==0)nowPage = -1;
			else nowPage++;
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	private JSONObject getNetData() {

		String s = "";
		try {
			s = n.remoteServer(
//					"http://doschool1.duapp.com/tools/lib.service.php",
					PluginListener.postU,
					"service=1&word=" + URLEncoder.encode(word,"GBK")
							+ "&type="+ "title" + "&page=" +  (nowPage));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (s.charAt(0) != 'E')
			try {
				return new JSONObject(s);
			} catch (JSONException e) {
				// 不会出现此异�?
				return null;
			}
		else {
			final String ss = s;
			handler.post(new Runnable() {

				@Override
				public void run() {
					error(ss);
				}
			});
			return null;
		}
	}
	
	private void error(String s) {
		if (getActivity() == null)
			return;
		if (s.equals("Epass")) {
			
		} 
		if (s.equals("EnetWork")) {

		} 
		if (s.equals("Ejson")) {

		} 
		if (s.equals("Emsg")) {

		}  
		
		{
			if (getActivity() == null)
				return;
			
			Toast.makeText(getActivity(), "没有获取到数据哟~", 1).show();
			getActivity().finish();
		}
	}

	class ListAdp extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(nowPage != -1)
				return list==null?1:list.size()+1;
			else
				return list==null?0:list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if( list==null || arg0==list.size()){
				LinearLayout ll = new LinearLayout(context);
				ll.setGravity(Gravity.CENTER);
				ProgressBar v1 = new ProgressBar(context);
				TextView t = new TextView(context);
				t.setText("请等待");
				ll.addView(v1);
				ll.addView(t);
				return ll;
			}
			
			LinearLayout ll = new LinearLayout(context);
			ll.setBackgroundColor(Color.WHITE);
			ll.setOrientation(LinearLayout.VERTICAL);
			TextView t[] = new TextView[4];
			for(int i=0;i<4;i++){
				t[i]= new TextView(context);
				t[i].setText(list.get(arg0).get(i));
				t[i].setTextColor(Color.BLACK);
				t[i].setTextSize(16);
				ll.addView(t[i]);
				((LinearLayout.LayoutParams) t[i].getLayoutParams()).topMargin =  (int) (DensityUtil
						.sp2px(context,3 )); 
				((LinearLayout.LayoutParams) t[i].getLayoutParams()).leftMargin =  (int) (DensityUtil
						.dip2px(context,22 )); 

			}				
			((LinearLayout.LayoutParams) t[0].getLayoutParams()).topMargin =  (int) (DensityUtil
					.dip2px(context,12 )); 
			t[0].setTextColor(0xff6699cc);
			t[0].setTextSize(20);
			((LinearLayout.LayoutParams) t[3].getLayoutParams()).bottomMargin =  (int) (DensityUtil
					.dip2px(context,12 )); 

			
			return ll;
		}
		
	}

	
	UpDataFromNet runT;
	class UpDataFromNet extends Thread {
		boolean flag = false;
		public boolean isRunning(){
			return flag;
		}
		
		public void run() {
			flag = true;
			final JSONObject s = getNetData();
			flag = false;
			if (s == null)
				return;
			handler.post(new Runnable() {

				@Override
				public void run() {
					upData(s);
					upUI();
				}
			}
			
			);
		}

	}

}
