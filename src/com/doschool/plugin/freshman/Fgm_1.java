package com.doschool.plugin.freshman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.listener.NetWorkFunction;
import com.doschool.network.DoServer;
import com.umeng.analytics.MobclickAgent;

public class Fgm_1 extends Fragment {
	Context context;
	final String PACKAGE_NAME = "com.doschool";

	RelativeLayout rootRl;
	RelativeLayout rl;

	ListView leftL, rightL;

	NetWorkFunction n;
	Handler handler;
	
	LoadingProgressView lp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setHasOptionsMenu(true);
		getActivity().getActionBar().setTitle("新生指南");
		super.onCreate(savedInstanceState);
	}

	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		handler = h;
	}

	Bitmap bmNotC, bmOnC;

	private void initBitmap() {

		bmNotC = Bitmap.createBitmap((int) DensityUtil.dip2px(context, 89),
				(int) DensityUtil.dip2px(context, 48) + 2,
				Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(bmNotC);
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		c.drawRect(0, 0, DensityUtil.dip2px(context, 89),
				DensityUtil.dip2px(context, 48), p);
		p.setStrokeWidth(2);
		p.setColor(0xffbdbdbd);
		c.drawLine(0, DensityUtil.dip2px(context, 48) + 1,
				DensityUtil.getWidth(context),
				DensityUtil.dip2px(context, 48) + 1, p);

		bmOnC = Bitmap.createBitmap((int) DensityUtil.dip2px(context, 89),
				(int) DensityUtil.dip2px(context, 48) + 2,
				Bitmap.Config.ARGB_4444);
		c = new Canvas(bmOnC);
		p.setColor(0xfff6b37f);
		c.drawRect(0, 0, DensityUtil.dip2px(context, 10),
				DensityUtil.dip2px(context, 48), p);
		p.setColor(0xffefefef);
		c.drawRect(DensityUtil.dip2px(context, 10), 0,
				DensityUtil.dip2px(context, 89),
				DensityUtil.dip2px(context, 48), p);
		p.setColor(0xffbdbdbd);
		c.drawLine(0, DensityUtil.dip2px(context, 48) + 1,
				DensityUtil.getWidth(context),
				DensityUtil.dip2px(context, 48) + 1, p);

	}

	RightAdp rAdp;
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		getActivity().getActionBar().setTitle("新生指南");
//		super.onCreateOptionsMenu(menu, inflater);
//	}

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);
		readyRes();
		initBitmap();
		lp = new LoadingProgressView(context, 8);
//		getActivity().getActionBar().show();
		rootRl = new RelativeLayout(context);
		rl = new RelativeLayout(context);
		leftL = new ListView(context);
		rightL = new ListView(context);
		rootRl.addView(rl);
		rl.addView(leftL);
		rl.addView(rightL);
		rl.setBackgroundColor(0xffefefef);
		rootRl.setBackgroundColor(0xffefefef);
		// 显示参数，必须在addView之后调用
		// ------------------------------------
		((RelativeLayout.LayoutParams) leftL.getLayoutParams()).width = (int) DensityUtil
				.dip2px(context, 89);
		((RelativeLayout.LayoutParams) rightL.getLayoutParams()).topMargin = (int) (DensityUtil
				.dip2px(context, 36 / 2));
		((RelativeLayout.LayoutParams) rightL.getLayoutParams()).leftMargin = (int) DensityUtil
				.dip2px(context, 89 + 22);
		((RelativeLayout.LayoutParams) rightL.getLayoutParams()).width = (int) (DensityUtil
				.getWidth(context) - DensityUtil.dip2px(context, 89 + 22 + 22));
		// ------------------------------------

		try {
			// 本地数据
			String st = getInfo();
			if (st != null && !st.equals("")) {
				try {
					upData(new JSONObject(getInfo()));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				upUI();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 更新完数据后会同时请更新界面
		new UpDataFromNet().start();

		return rootRl;
	}

	int nowView = 0;

	private void upUI() {
		rAdp = new RightAdp();
		rightL.setAdapter(rAdp);
		rightL.setDivider(new ColorDrawable(0xffefefef));
		if(context == null)return;
		rightL.setDividerHeight((int) DensityUtil.dip2px(context, 16));
		leftL.setAdapter(new LeftAdp());

		leftL.setOnItemClickListener(new OnItemClickListener() {
			int lastV = 0;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				leftL.getChildAt(lastV)
						.setBackgroundDrawable(
								new BitmapDrawable(
										getActivity().getResources(), bmNotC));
				leftL.getChildAt(arg2)
						.setBackgroundDrawable(
								new BitmapDrawable(
										getActivity().getResources(), bmOnC));

				// arg1.setBackground(new BitmapDrawable(pluginRes, bmOnC));
				lastV = arg2;
				rAdp.setNowView(arg2);
				nowView = arg2;
				rAdp.notifyDataSetChanged();

			}
		});

		rightL.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Activity act = getActivity();
				int frmId = act.getResources().getIdentifier("frg_tool", "id",
						act.getPackageName());
				Log.i("FGMC3", ""
						+ getActivity().getSupportFragmentManager()
								.getBackStackEntryCount());
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
		        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 

				Object[] ens;
				ens = (map.entrySet().toArray());

				ft.add(frmId,
						new Fgm_2(n, handler,
								((List<String[]>) ((Entry) ens[nowView])
										.getValue()).get(arg2)[1],
										((List<String[]>) ((Entry) ens[nowView])
												.getValue()).get(arg2)[0]));
				ft.addToBackStack("Fgm1");
				ft.commit();
			}
		});
	}

	Map<String, List<String[]>> map;

	private void upData(JSONObject json) {
		JSONArray ary;
		try {
			ary = json.getJSONArray("info");

			map = new HashMap<String, List<String[]>>();
			for (int i = 0; i < ary.length(); i++) {
				JSONObject jo = ary.getJSONObject(i);

				if (map.get(jo.getString("typeName")) == null) {
					map.put(jo.getString("typeName"), new ArrayList<String[]>());
				}
				String[] strs = new String[2];
				strs[0] = jo.getString("title");
				strs[1] = jo.getString("site");
				map.get(jo.getString("typeName")).add(strs);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readyRes() {
		try {
			context = getActivity();
			// .createPackageContext(
			// PACKAGE_NAME,
			// Context.CONTEXT_INCLUDE_CODE
			// | Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// pluginRes = getActivity().getPackageManager()
		// .getResourcesForApplication(PACKAGE_NAME);
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// }

		// handler = PluginListener.handler;
		// inflater = LayoutInflater.from(context);
	}

	private JSONObject getNetData() {

		final String s = n.remoteServer(
//				"http://doschool1.duapp.com/tools/fresher/fresher.service.php",
				PluginListener.postU,				
				"schoolid="+DoServer.SCHOOLID);

		if (s.charAt(0) != 'E')
			try {
				saveInfo(s);
				return new JSONObject(s);
			} catch (JSONException e) {
				// 不会出现此异常
				return null;
			}
		else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					error(s);
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
			Toast.makeText(getActivity(), "网络错误！请检查网络连接", 1).show();
		}
	}

	private void saveInfo(String s) {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		sp.edit().putString("freshInfo", s).commit();
	}

	private String getInfo() {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		return sp.getString("freshInfo", "");
	}

	class LeftAdp extends BaseAdapter {

		private Object[] ens;

		LeftAdp() {
			ens = (map.entrySet().toArray());
		}

		@Override
		public int getCount() {
			return ens.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int p, View t, ViewGroup arg2) {
			t = new TextView(context);
			((TextView) t).setGravity(Gravity.CENTER);
			((TextView) t).setTextColor(Color.BLACK);
			((TextView) t).setTextSize(16);
			if (p == 0)
				t.setBackgroundDrawable(new BitmapDrawable(getActivity()
						.getResources(), bmOnC));
			else
				t.setBackgroundDrawable(new BitmapDrawable(getActivity()
						.getResources(), bmNotC));
			((TextView) t).setText((String) ((Entry) ens[p]).getKey());
			return t;
		}

	}

	class RightAdp extends BaseAdapter {

		private Object[] ens;
		int p = 0;

		RightAdp() {
			ens = (map.entrySet().toArray());
		}

		public void setNowView(int i) {
			p = i;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ((List) ((Entry) ens[p]).getValue()).size();
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
		public View getView(int arg0, View l, ViewGroup arg2) {

			TextView t = new TextView(context);

			((TextView) t).setGravity(Gravity.CENTER);
			((TextView) t).setBackgroundColor(Color.WHITE);
			((TextView) t).setTextSize(16);

			((TextView) t).setText(((List<String[]>) ((Entry) ens[p])
					.getValue()).get(arg0)[0]);
			((TextView) t).setTextColor(Color.BLACK);
			LinearLayout ll = new LinearLayout(context);
			ll.setGravity(Gravity.CENTER);
			ll.addView(t);
			ll.setBackgroundColor(Color.WHITE);
			((LinearLayout.LayoutParams) t.getLayoutParams()).bottomMargin = (int) DensityUtil
					.sp2px(context, 16);
			((LinearLayout.LayoutParams) t.getLayoutParams()).topMargin = (int) DensityUtil
					.sp2px(context, 16);

			return ll;
		}

	}

	class UpDataFromNet extends Thread {

		public void run() {
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					rootRl.addView(lp,
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
					lp.beginMoving();
				}
			});

			
			final JSONObject s = getNetData();
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					lp.stopMoving();
					ViewGroup vgp = ((ViewGroup) lp.getParent());
					if (vgp != null)
						vgp.removeView(lp);
				}
			});

			if (s == null)
				return;
			handler.post(new Runnable() {

				@Override
				public void run() {
					upData(s);
					upUI();
				}
			});
		}

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

}
