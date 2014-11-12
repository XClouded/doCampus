package com.doschool.plugin.lib;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.listener.NetWorkFunction;
import com.doschool.methods.SpMethods;
import com.umeng.analytics.MobclickAgent;

public class Fgm_4 extends Fragment {
	List<String> list;
	NetWorkFunction n;
	public void onPause() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+"我的");
		super.onPause();
	}

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME+"我的");
		super.onResume();
	}

	String pw;

	public Fgm_4(NetWorkFunction n2, Handler handler2, String trim) {
		this.n = n2;
		pw = trim;
	}

	LinearLayout ll;
	Context context;
	Handler handler;
	ListView listV;
	LayoutInflater inflater;
	RelativeLayout rl;
	LoadingProgressView proLay;
	int getDatas = 0;
	Object lock;

	public View onCreateView(LayoutInflater ii, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(ii, container, savedInstanceState);
		context = getActivity();
		lock = new Object();
		handler = new Handler();
		proLay = new LoadingProgressView(context, 8);
		rl = new RelativeLayout(context);
		inflater = LayoutInflater.from(context);
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(Color.WHITE);
		View line = new View(context);
		line.setBackgroundDrawable(new ColorDrawable(0xff6699cc));
		listV = new ListView(context);
		Bitmap bm = Bitmap.createBitmap((int)DensityUtil.getWidth(context), 2, Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(bm);
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		c.drawRect(0, 0, DensityUtil.getWidth(context), 2, p);
		p.setColor(0xffbdbdbd);
		c.drawRect((int)DensityUtil.dip2px(context, 12), 0, DensityUtil.getWidth(context)-DensityUtil.dip2px(context, 12), 2, p);
		listV.setDivider(new BitmapDrawable(getResources(), bm));

		final ListAdp addp = new ListAdp();
		listV.setAdapter(addp);
		ll.addView(listV);

//		{
//			String js = getInfo1();
//
//			String js2 = getInfo2();
//
//			try {
//				listData = getMyBook(new JSONObject(js));
//			} catch (JSONException e) {
//				listData = null;
//				e.printStackTrace();
//			}
//
//			try {
//				listData2 = getMyBook(new JSONObject(js2));
//			} catch (JSONException e) {
//				listData2 = null;
//				e.printStackTrace();
//			}
//
//			if (listData != null)
//				handler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						addp.notifyDataSetChanged();
//					}
//				});
//
//		}

//		saveLibPass("");
		rl.addView(ll);
		rl.addView(proLay, RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		proLay.beginMoving();

		new Thread() {
			public void run() {

				String js = n.remoteServer(
				// "http://doschool1.duapp.com/tools/lib.service.php",
						PluginListener.postU, "service=3&jyorgh=" + "1"
								+ "&xh=" + getStuId() + "&pw=" + pw);

				saveInfo1(js);
				try {
					listData = getMyBook(new JSONObject(js));
					l("1的长度"+listData.size());
				} catch (JSONException e) {
					listData = null;
					l("1ERROR");
					e.printStackTrace();
				}
				synchronized (lock) {

					getDatas++;

					if (getDatas == 2) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								proLay.stopMoving();
								ViewGroup vgp = ((ViewGroup) proLay.getParent());
								if (vgp != null)
									vgp.removeView(proLay);
							}
						});
						getDatas = 0;
					}
				}
				if (listData == null) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (getActivity() != null)
								Toast.makeText(getActivity(), "请检查密码是否输入错误!", 1)
										.show();
							saveLibPass("");
							getFragmentManager().popBackStack();

						}
					});
				}
				else 
				{
					handler.post(new Runnable() {

						@Override
						public void run() {
							l("1的 addp.no");

							addp.notifyDataSetChanged();
						}
					});
				}

			}
		}.start();

		new Thread() {
			public void run() {

				String js2 = n.remoteServer(
				// "http://doschool1.duapp.com/tools/lib.service.php",
						PluginListener.postU, "service=3&jyorgh=" + "2"
								+ "&xh=" + getStuId() + "&pw=" + pw);

				saveInfo2(js2);

				try {
					listData2 = getMyBook(new JSONObject(js2));
				} catch (JSONException e) {
					listData2 = null;
					e.printStackTrace();
				}
				
				synchronized (lock) {

					getDatas++;
					if (getDatas == 2) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								proLay.stopMoving();
								ViewGroup vgp = ((ViewGroup) proLay.getParent());
								if (vgp != null)
									vgp.removeView(proLay);
							}
						});
						getDatas = 0;
					}
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						l("2的 addp.no");
						addp.notifyDataSetChanged();
//						if (listData == null) {
//							Toast.makeText(getActivity(), "请检查密码是否输入错误!", 1)
//									.show();
//							saveLibPass("");
//							getFragmentManager().popBackStack();
//						}
					}
				});
			}
		}.start();
		return rl;
	}

	private void saveLibPass(String s) {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("libInfo_pass", s).commit();
	}

	private String getStuId() {
		if (getActivity() == null)
			return "";

		return SpMethods.loadString(context, SpMethods.USER_FUNID);
	}

	private void saveInfo1(String s) {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("libInfo_myB1", s).commit();
	}

	private String getInfo1() {
		if (getActivity() == null)
			return "";
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("libInfo_myB1", "");
	}

	private void saveInfo2(String s) {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("libInfo_myB2", s).commit();
	}

	private String getInfo2() {
		if (getActivity() == null)
			return "";
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("libInfo_myB2", "");
	}

	class ListAdp extends BaseAdapter {

		@Override
		public int getCount() {
			if((listData == null || listData.size() == 0) && (listData2 == null || listData2.size() == 0))
				return 3;
			return ((listData == null || listData.size() == 0) ? 1 : (listData.size() + 1))
					+ ((listData2 == null || listData2.size() == 0) ? 1
							: listData2.size());
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
			l("pos:"+position);
			// 第一部分数据为暂未获取或者失败 显示进度圈
			if (listData == null && position == 0) {
				l("第一部分数据为暂未获取或者失败 显示进度圈");

				LinearLayout ll = new LinearLayout(context);
				ll.setGravity(Gravity.CENTER);
				ProgressBar v1 = new ProgressBar(context);
				TextView t = new TextView(context);
				t.setText("请等待");
				ll.addView(v1);
				ll.addView(t);
				return ll;

			}
			// 第一部分数据获取后发现是空数据
			if (listData != null && listData.size() == 0 && position == 0) {
				l("第一部分数据获取后发现是空数据");
				LinearLayout ll = new LinearLayout(context);
				ll.setGravity(Gravity.CENTER);
				// ProgressBar v1 = new ProgressBar(context);
				TextView t = new TextView(context);
				t.setText("这里空空哒\n╮(╯▽╰)╭");
				t.setGravity(Gravity.CENTER);
				// ll.addView(v1);
				ll.addView(t);
				return ll;

			}

			// 第一部分获取后有数据 那么正常显示
			if ( listData!=null && position < listData.size() ) {
				l("第一部分获取后有数据 那么正常显示");
				View v = inflater.inflate(R.layout.tools_lib_mybook_item, null);
				TextView t1 = (TextView) v
						.findViewById(R.id.tools_lib_mybook_item_text1), t2 = (TextView) v
						.findViewById(R.id.tools_lib_mybook_item_text2);
				t1.setText(listData.get(position).get(0));
				t1.setTextColor(Color.BLUE);
				t2.setText("归还日期：" + listData.get(position).get(4));
				return v;

			}
			// 第一部分数据 正常 那么在数据记录的下一个位置显示借阅记录View 否则在第二个 pos==1 的位置显示
			if ((listData != null && listData.size() != 0 && position == listData
					.size())
					|| ((listData == null || listData.size() == 0) && position == 1)) {
				l("第一部分数据 正常 那么在数据记录的下一个位置显示借阅记录View 否则在第二个 pos==1 的位置显示");
				// ImageView v = new ImageView(ActivityLibMyBook.this);
				LinearLayout v = new LinearLayout(context);
				v.setBackgroundColor(0xfff2f2f2);
				TextView t = new TextView(context);
				t.setText("借阅记录 ↓");
				t.setTextSize(12);
				v.setGravity(Gravity.CENTER);
				v.addView(t);
				v.setBackgroundColor(Color.WHITE);
				((LinearLayout.LayoutParams) t.getLayoutParams()).bottomMargin = (int) DensityUtil
						.dip2px(context, 12);
				((LinearLayout.LayoutParams) t.getLayoutParams()).topMargin = (int) DensityUtil
						.dip2px(context, 12);
				return v;
			}

			// 第一部分数据正常 那么在借阅记录的下一个位置 显示记录View 否则从 pos==2的位置显示
			if ((listData != null && listData.size() != 0 && position > listData
					.size())
					|| ((listData == null || listData.size() == 0) && position > 1)) {
				l("第一部分数据正常 那么在借阅记录的下一个位置 显示记录View 否则从 pos==2的位置显示");
				// 第二部分数据为null 暂未获取到或失败 显示进度圈
				if (listData2 == null) {
					l("第二部分数据为null 暂未获取到或失败 显示进度圈");
					LinearLayout ll = new LinearLayout(context);
					ll.setGravity(Gravity.CENTER);
					ProgressBar v1 = new ProgressBar(context);
					TextView t = new TextView(context);
					t.setText("请等待");
					ll.addView(v1);
					ll.addView(t);
					return ll;
				}
				// 第二部分数据为0 获取成功
				if (listData2.size() == 0) {
					l("第二部分数据为0 获取成功");

					LinearLayout ll = new LinearLayout(context);
					ll.setGravity(Gravity.CENTER);
					// ProgressBar v1 = new ProgressBar(context);
					TextView t = new TextView(context);
					t.setText("这里空空哒\n╮(╯▽╰)╭");
					t.setGravity(Gravity.CENTER);
					// ll.addView(v1);
					ll.addView(t);
					return ll;
				}
				l("第二部分有数据，正常显示");

				// 第二部分有数据，正常显示
				View v = inflater.inflate(R.layout.tools_lib_mybook_item, null);
				TextView t1 = (TextView) v
						.findViewById(R.id.tools_lib_mybook_item_text1), t2 = (TextView) v
						.findViewById(R.id.tools_lib_mybook_item_text2);
				t1.setText(listData2.get(position - ((listData == null || listData.size() == 0) ? 2 : (listData.size() + 1))).get(0));
				t1.setTextColor(0xff6699cc);
				t2.setText("归还日期："
						+ listData2.get(position - ((listData == null || listData.size() == 0) ? 2 : (listData.size() + 1))).get(4));
				return v;

			}
			return null;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
//			if (position == 1)
//				return true;
			return false;
		}

	}

	List<List<String>> listData;
	List<List<String>> listData2;
	
	private void l(String s ){
		Log.i("LIBLIB",s);
	}
	
	public List<List<String>> getMyBook(JSONObject obj) throws JSONException {
		List<List<String>> list = new ArrayList<List<String>>();

		JSONObject o = obj;
		JSONArray ary = o.getJSONArray("info");
		for (int i = 0; i < ary.length(); i++) {
			list.add(new ArrayList<String>());
			JSONObject ob = ary.getJSONObject(i);
			list.get(i).add(ob.getString("book_name"));
			list.get(i).add(ob.getString("address"));
			list.get(i).add(ob.getString("ISBN"));
			list.get(i).add(ob.getString("outtime"));
			list.get(i).add(ob.getString("intime"));
			list.get(i).add(ob.getString("next_times"));
		}
		return list;

	}

}
