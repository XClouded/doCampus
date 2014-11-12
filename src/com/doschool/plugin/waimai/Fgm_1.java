package com.doschool.plugin.waimai;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.listener.NetWorkFunction;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

public class Fgm_1 extends Fragment {
	Context context;
	LayoutInflater inflater;
	Handler handler;
	MyListAdapter myListAdapter;
	PullToRefreshListView list;
	protected List<List<String>> data;
	final String PACKAGE_NAME = "com.doschool.plugin.waimai";
	NetWorkFunction netWorkListener;
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onResume();
	}

	public Fgm_1(NetWorkFunction netWorkListener, Handler handler2) {
		this.netWorkListener = netWorkListener;
		handler = handler2;
	}

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null)
			return super.onCreateView(inflater, container, savedInstanceState);
		Log.i("adaa", "aaaa");

		handler = new Handler();
		try {
			context = getActivity();// .createPackageContext(PACKAGE_NAME,
			// Context.CONTEXT_INCLUDE_CODE
			// | Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("EEEEE", "E2E");

			e.printStackTrace();
		}
		Log.i("adaa", "aaaa2");

		// try {
		// pluginRes =
		// getActivity().getPackageManager().getResourcesForApplication(PACKAGE_NAME);
		// } catch (NameNotFoundException e) {
		// Log.i("EEEEE","EE");
		// e.printStackTrace();
		// }
		//
		try {
			Log.i("adaa", "aaaa3");

			inflater = i;// LayoutInflater.from(context);
			myListAdapter = new MyListAdapter();
			list = new PullToRefreshListView(getActivity());
			// list.setMode(Mode.PULL_DOWN_TO_REFRESH);
			list.setMode(Mode.BOTH);
			list.getLoadingLayoutProxy(false, true).setPullLabel("上拉更多");
			list.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
			list.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
			list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
			list.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
			list.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");

			list.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(
						final PullToRefreshBase<ListView> refreshView) {
					getNetData(refreshView);

				}
			});
			Log.i("adaa", "aaaa4");
			myListAdapter = new MyListAdapter();
			list.setAdapter(myListAdapter);
			Log.i("adaa", "aaaa5");

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Activity act = getActivity();
					int frmId = act.getResources().getIdentifier("frg_tool",
							"id", act.getPackageName());
					FragmentTransaction ft = getActivity()
							.getSupportFragmentManager().beginTransaction();
			        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 

					ft.replace(frmId, new Fgm_2(data.get(arg2 - 1)));
					ft.addToBackStack("Fgm1");
					ft.commit();

				}
			});
			String info = getInfo();
			try {
				if (info != null && !info.equals("")) {
					data = getFood(new JSONObject(info));
					handler.post(new UpDataUI(null));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			getNetData(null);
			Log.i("adaa", "aaaa6");

		} catch (Exception e) {
			Log.i("pluginT", "EE222");
			e.printStackTrace();
		}

		return list;
	}

	private void saveInfo(String s) {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		sp.edit().putString("foodInfo", s).commit();
	}

	private String getInfo() {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		return sp.getString("foodInfo", "");
	}

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			try {
				Log.i("adaa", "aaaa8");
				final int p = position;
				// int id = pluginRes.getIdentifier("tools_waimai_item",
				// "layout", PACKAGE_NAME);
				// Log.i("adaa","a00"+id);

				View v = inflater.inflate(R.layout.tools_waimai_item, null);
				TextView t;
				ImageView im;
				t = (TextView) v.findViewById(R.id.tools_waimai_item_text);
				im = (ImageView) v.findViewById(R.id.tools_waimai_item_image);
				t.setText(data.get(position).get(0));
				im.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri telUri = Uri.parse("tel:" + data.get(p).get(1));
						Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
						startActivity(intent);
					}
				});

				return v;
			} catch (Exception e) {
				Log.i("EEE", "EE22332");

				e.printStackTrace();
			}
			return null;
		}

	}

	private void error(String s) {
		if (getActivity() == null)
			return;
		if (s.equals("Epass")) {
			
		} else if (s.equals("EnetWork")) {

		} else if (s.equals("Ejson")) {

		} else if (s.equals("Emsg")) {

		} else {
			if (getActivity() == null)
				return;
			Toast.makeText(getActivity(), "网络错误！请检查网络连接", 1).show();
		}
	}

	private void getNetData(final PullToRefreshBase<ListView> refreshView) {
		new Thread() {
			public void run() {
				String json = netWorkListener.remoteServer(
//						"http://doschool1.duapp.com/tools/Food/getFood.php",
						PluginListener.postU,
						"schoolid=1");
				if (json.charAt(0) == 'E') {
					error(json);
				} else {
					try {
						saveInfo(json);
						data = getFood(new JSONObject(json));
					} catch (JSONException e) {
						data = null;
						e.printStackTrace();
					}
				}

				if (data == null) {
					return;
				}

				handler.post(new UpDataUI(refreshView));
			}
		}.start();

	}

	private List<List<String>> getFood(JSONObject obj) throws JSONException {
		List<List<String>> list = new ArrayList<List<String>>();
		JSONArray ary = obj.getJSONArray("info");
		for (int i = 0; i < ary.length(); i++) {

			list.add(new ArrayList<String>());
			JSONObject o = ary.getJSONObject(i);
			list.get(i).add(o.getString("restaurant"));
			list.get(i).add(o.getString("phonenumber"));
			Log.i("qqqqqq", o.getString("notice"));
			if (o.getString("notice").equals(""))
				list.get(i).add("");
			else
				list.get(i).add(o.getString("notice"));
			JSONArray dishAry = o.getJSONArray("dish");
			StringBuffer sb = new StringBuffer();
			for (int k = 0; k < dishAry.length(); k++) {
				sb.append(dishAry.get(k) + "#");
			}
			list.get(i).add(sb.toString());
		}
		return list;
	}

	class UpDataUI implements Runnable {
		PullToRefreshBase<ListView> refreshView;

		public UpDataUI(PullToRefreshBase<ListView> refreshView) {
			this.refreshView = refreshView;
		}

		public void run() {
			myListAdapter.notifyDataSetChanged();
			if (refreshView != null)
				refreshView.onRefreshComplete();
		}
	}

}
