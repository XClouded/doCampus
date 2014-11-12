package com.doschool.plugin.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.listener.NetWorkFunction;
import com.umeng.analytics.MobclickAgent;

public class Fgm_3 extends Fragment {
	List<String> list;
	NetWorkFunction n;
	public Fgm_3(NetWorkFunction n, Handler handler, List<String> list) {
		this.n = n;
		this.list = list;
	}	@Override
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+"具体");
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+"具体");
		super.onResume();
	}


	LinearLayout ll;
	Context context;
	Handler handler;
	ListView listV;
	List<String[]> adds;
	LoadingProgressView proLay;
	RelativeLayout rl;
	public View onCreateView(LayoutInflater ii, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(ii, container, savedInstanceState);
		context = getActivity();
		handler = new Handler();
		proLay = new LoadingProgressView(context, 8);
		rl = new RelativeLayout(context);
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		View line = new View(context);
		line.setBackgroundDrawable(new ColorDrawable(0xff6699cc));
		listV = new ListView(context);

		TextView t[] = new TextView[4];
		for(int i=0;i<4;i++){
			t[i]= new TextView(context);
			t[i].setText(list.get(i));
			t[i].setTextColor(Color.BLACK);
			t[i].setTextSize(16);
			ll.addView(t[i]);
			if(i==0){
				ll.addView(line);
				((LinearLayout.LayoutParams) line.getLayoutParams()).leftMargin =  (int) (DensityUtil
						.dip2px(context,22 )); 
				((LinearLayout.LayoutParams) line.getLayoutParams()).height =  (int) (DensityUtil
						.dip2px(context,1 )); 
				((LinearLayout.LayoutParams) line.getLayoutParams()).width =  (int) (DensityUtil.getWidth(context) - DensityUtil
						.dip2px(context,22+22 )); 
			
			}
			((LinearLayout.LayoutParams) t[i].getLayoutParams()).topMargin =  (int) (DensityUtil
					.sp2px(context,3 )); 
			((LinearLayout.LayoutParams) t[i].getLayoutParams()).leftMargin =  (int) (DensityUtil
					.dip2px(context,22 )); 

		}				
		((LinearLayout.LayoutParams) t[0].getLayoutParams()).topMargin =  (int) (DensityUtil
				.dip2px(context,22 )); 
		((LinearLayout.LayoutParams) t[0].getLayoutParams()).bottomMargin =  (int) (DensityUtil
				.dip2px(context,22 )); 
		((LinearLayout.LayoutParams) t[1].getLayoutParams()).topMargin =  (int) (DensityUtil
				.dip2px(context,22 )); 

		((LinearLayout.LayoutParams) t[3].getLayoutParams()).bottomMargin =  (int) (DensityUtil
				.dip2px(context,22 )); 

		t[0].setTextColor(0xff6699cc);
		t[0].setTextSize(24);
		((LinearLayout.LayoutParams) t[3].getLayoutParams()).bottomMargin =  (int) (DensityUtil
				.dip2px(context,22 )); 

		
		
		
		final ListAdp addp = new ListAdp();
		listV.setAdapter(addp);
		listV.setDivider(new ColorDrawable(Color.WHITE));
		rl.addView(ll);
		final String link = list.get(5);
		ll.addView(listV);

		new Thread(){
			public void run(){
				String js = null;
				handler.post(new Runnable() {
					@Override
					public void run() {
						rl.addView(proLay,
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT);
						proLay.beginMoving();
					}
				});


				try {
					
					js = n.remoteServer(
							PluginListener.postU,
//						"http://doschool1.duapp.com/tools/lib.service.php", 
							"service=2&link="+URLEncoder.encode(link, "utf-8"));
				} catch (UnsupportedEncodingException e1) {
					js = "";
					e1.printStackTrace();
				}
				
				handler.post(new Runnable() {

					@Override
					public void run() {
						proLay.stopMoving();
						ViewGroup vgp = ((ViewGroup) proLay.getParent());
						if (vgp != null)
							vgp.removeView(proLay);
					}
				});

				try {
					adds = getAddressAndStatus(new JSONObject(js));
				} catch (JSONException e) {
					adds = null;
					e.printStackTrace();
				}
				if(adds==null)return;
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						addp.notifyDataSetChanged();
					}
				});
			}
		}.start();
		
		rl.setBackgroundColor(Color.WHITE);
		rl.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		return rl;
	}
	
	class ListAdp extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return adds==null?0:adds.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RelativeLayout rl = new RelativeLayout(context);
			LinearLayout ll = new LinearLayout(context);
			rl.addView(ll);
			TextView t1 = new TextView(context);
			String[] ss = adds.get(position);
			
			t1.setTextSize(16);
			t1.setTextColor(Color.WHITE);
			ll.addView(t1,(int)DensityUtil.dip2px(context, 60),(int)DensityUtil.dip2px(context, 30));
			((LinearLayout.LayoutParams) t1.getLayoutParams()).leftMargin =  (int) (DensityUtil
					.dip2px(context, 22)); 
			ll.setGravity(Gravity.CENTER_VERTICAL);
			TextView t2 = new TextView(context);
			t2.setTextSize(12);
			t2.setTextColor(Color.BLACK);
			ll.addView(t2);
			
			((LinearLayout.LayoutParams) t2.getLayoutParams()).leftMargin =  (int) (DensityUtil
					.dip2px(context,30)); 
			t2.setText(ss[0]);
			t2.setGravity(Gravity.CENTER_VERTICAL);

//			((LinearLayout.LayoutParams) t1.getLayoutParams()).height =  (int) (DensityUtil
//					.dip2px(context, 40)); 
			if (ss[1].equals("\u5728\u9986"))
			{	
				t1.setText("在馆");
				t1.setBackgroundResource(com.doschool.R.drawable.tool_lib_s1);
				t1.setGravity(Gravity.CENTER_VERTICAL);
				View bv = new View(context);
				bv.setBackgroundColor(Color.GREEN);
				rl.addView(bv,RelativeLayout.LayoutParams.MATCH_PARENT,2);
				((RelativeLayout.LayoutParams) bv.getLayoutParams()).leftMargin =  (int) (DensityUtil
						.dip2px(context, 22)); 
			}
			else{
				
				t1.setBackgroundResource(com.doschool.R.drawable.tool_lib_s2);
				t1.setText("借出");
				t1.setGravity(Gravity.CENTER_VERTICAL);
				View bv = new View(context);
				bv.setBackgroundColor(Color.RED-0x99000000);
				rl.addView(bv,RelativeLayout.LayoutParams.MATCH_PARENT,2);
				((RelativeLayout.LayoutParams) bv.getLayoutParams()).leftMargin =  (int) (DensityUtil
						.dip2px(context, 22)); 
			}

			
			
			return rl;
		}
		
	}

	public List<String[]> getAddressAndStatus(JSONObject obj) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			JSONObject oo = obj;
			JSONArray jary = oo.getJSONArray("info");
			for (int i = 0; i < jary.length(); i++) {
				JSONObject o = jary.getJSONObject(i);
				String[] ss = new String[2];
				ss[0] = (o.getString("address"));
				ss[1] = (o.getString("statue"));
				list.add(ss);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
