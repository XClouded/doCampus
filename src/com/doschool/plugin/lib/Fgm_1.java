package com.doschool.plugin.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.listener.NetWorkFunction;
import com.umeng.analytics.MobclickAgent;

public class Fgm_1 extends Fragment {

	@Override
	public void onResume() {
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onResume();
	}

	class LibListAdp extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listdata == null ? 1 : listdata.size();
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
			if (listdata == null) {
				LinearLayout ll = new LinearLayout(context);
				ll.setGravity(Gravity.CENTER_HORIZONTAL);
				ProgressBar pb = new ProgressBar(context);
				pb.setIndeterminate(true);
				ll.addView(pb);
				return ll;
			}

			// if(convertView != null)return convertView;
			Log.i("TOOLINFOLIB", "refreshView!" + position);
			RelativeLayout ll = new RelativeLayout(context);
			// ll.setGravity(Gravity.CENTER_VERTICAL);
			final int inNum = Integer.parseInt(listdata.get(position).get(2));
			final int emNum = Integer.parseInt(listdata.get(position).get(3));
			Log.i("TIAOSHIBUG","eN:"+emNum+"inNum"+inNum);
			float rate = (float) (((emNum+1.0) / (inNum + emNum + 1.0)));// 这是一个比例
			View ba = new View(context);
			Paint p = new Paint();
			p.setTextSize(DensityUtil.sp2px(context, 12));
			int widthT = (int) p.measureText("000/000");
			int width = (int) (DensityUtil.getWidth(context)
					- DensityUtil.dip2px(context, 22 + 22 + 12) - widthT);
			ba.setBackgroundDrawable(new BitmapDrawable(getResources(),
					getBackBitmap(colors[position % colors.length], rate)));
			Log.i("TIAOSHIBUG","eN:"+emNum+"inNum"+inNum+"rate"+rate);

			Rect rect = new Rect();
			// 返回包围整个字符串的最小的一个Rect区域
			p.getTextBounds("总数", 0, 1, rect);
			int h = rect.height();

			ll.addView(ba,(int)(DensityUtil.getWidth(context)),RelativeLayout.LayoutParams.WRAP_CONTENT);
			TextView t1, t2;
			t1 = new TextView(context);
			t2 = new TextView(context);
			t1.setText(listdata.get(position).get(0));
			t2.setText("" + emNum + "/" + (inNum + emNum));
			t1.setTextSize(12);
			t1.setTextColor(Color.WHITE);

			t2.setTextSize(12);
			t2.setTextColor(Color.BLACK);

			ll.addView(t1);
			ll.addView(t2);

			((RelativeLayout.LayoutParams) ba.getLayoutParams()).width = width;
			((RelativeLayout.LayoutParams) ba.getLayoutParams()).height = (int) DensityUtil
					.dip2px(context, 24);

			((RelativeLayout.LayoutParams) t1.getLayoutParams()).leftMargin = (int) DensityUtil
					.dip2px(context, 22 - 8);
			((RelativeLayout.LayoutParams) t1.getLayoutParams()).height = RelativeLayout.LayoutParams.MATCH_PARENT;
			((RelativeLayout.LayoutParams) t1.getLayoutParams()).topMargin = (int) (DensityUtil
					.dip2px(context, 24 - 6) - h) / 2;

			t1.setGravity(Gravity.CENTER_VERTICAL);

			((RelativeLayout.LayoutParams) t2.getLayoutParams())
					.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// = (int)
																// DensityUtil.getWidth(context)
																// -(int)
																// DensityUtil.dip2px(context,
																// 22) - widthT;
			((RelativeLayout.LayoutParams) t2.getLayoutParams()).height = RelativeLayout.LayoutParams.MATCH_PARENT;
			((RelativeLayout.LayoutParams) t2.getLayoutParams()).topMargin = (int) (DensityUtil
					.dip2px(context, 24 - 6) - h) / 2;

			t2.setGravity(Gravity.CENTER);
			// convertView = ll;
			return ll;

		}

	}

	Button btInSearch;
	int colors[] = { 0xffaa89bd, 0xff7b9daa, 0xff84ccc9, 0xff89c997,
			0xffeace6e, 0xfff6b37f, 0xffe65d5d, };
	Context context;

	Handler handler;

	// private Resources pluginRes;
	LayoutInflater inf;

	private LibListAdp listAdp;

	private ArrayList<List<String>> listdata;

	ListView listInRect;
	LinearLayout ll;
	Map<String, List<String[]>> map;

	Button myBook;

	NetWorkFunction n;

	int nowView = 0;

	final String PACKAGE_NAME = "com.doschool.plugin.lib";

	RelativeLayout rectLay;
	RelativeLayout searchLay;
	TextView t1, t2;
	EditText textInSearch;
	RelativeLayout textLayInRect;
	boolean threadRun = false;

	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		handler = h;
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

	private Bitmap getBackBitmap(int color, float rate) {
		// rate = (float) 0.5;
		Paint p = new Paint();
		p.setTextSize(DensityUtil.sp2px(context, 12));
		int width = (int) p.measureText("000/000");
		width = (int) (DensityUtil.getWidth(context)
				- DensityUtil.dip2px(context, 22 + 22 + 12) - width);
		Bitmap bm = Bitmap.createBitmap(width,
				(int) DensityUtil.dip2px(context, 24), Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(bm);
		p.setColor(0xfff2f2f2);
		c.drawRect(0, 0, width, (int) DensityUtil.dip2px(context, 24), p);
		p.setColor(color);
		c.drawRect(0, 0, width * rate, (int) DensityUtil.dip2px(context, 24), p);
		Log.i("TIAOSHIBUG",rate+"");
		return bm;
	}

	private String getInfo() {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		return sp.getString("libInfo_mainP", "");
	}

	public List<List<String>> getLibEmList(JSONObject o) {
		List<List<String>> list = new ArrayList<List<String>>();

		try {
			JSONArray ary;

			ary = o.getJSONObject("info").getJSONArray("list");

			for (int i = 0; i < ary.length(); i++) {
				JSONObject jso0 = ary.getJSONObject(i);
				List<String> listt = new ArrayList<String>();
				listt.add(jso0.getString("name"));
				listt.add(jso0.getString("entered"));
				listt.add(jso0.getString("now"));
				listt.add(jso0.getString("empty"));
				list.add(listt);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	// final int inNum = Integer.parseInt(listdata.get(position).get(2));
	// final int emNum = Integer.parseInt(listdata.get(position).get(3));

	private String getLibPass() {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("libInfo_pass", "");
	}

	@Override
	public void onPause() {
		super.onPause();
	    MobclickAgent.onPageStart(PluginListener.myNAME);
		threadRun = false;
	}

	private JSONObject getNetData() {

		final String s = n.remoteServer(
		// "http://doschool1.duapp.com/tools/lib.service.php",
				PluginListener.postU, "service=4");

		if (s.charAt(0) != 'E')
			try {
				saveInfo(s);
				return new JSONObject(s);
			} catch (JSONException e) {
				// 不会出现此异�?
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

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);
		readyRes();
		inf = LayoutInflater.from(context);

		ll = new LinearLayout(context);
		ImageView backG = new ImageView(context);
		backG.setImageResource(R.drawable.tool_lib_image);
		ll.addView(backG, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		((LinearLayout.LayoutParams) backG.getLayoutParams()).topMargin = 0;
		((LinearLayout.LayoutParams) backG.getLayoutParams()).width = (int) DensityUtil
				.getWidth(context);
		((LinearLayout.LayoutParams) backG.getLayoutParams()).height = (int) (DensityUtil
				.getWidth(context) * 221 / 720);
		searchLay = new RelativeLayout(context);
		rectLay = new RelativeLayout(context);
		myBook = new Button(context);
		myBook.setBackgroundColor(0xff607d8b);
		myBook.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundColor(Color.YELLOW);
					return true;
				} else if (e.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundColor(0xff607d8b);
					if (e.getX() >= 0 && e.getX() <= v.getWidth()
							&& e.getY() >= 0 && e.getY() <= v.getHeight()) {
						if(DoschoolApp.isGuest()){
							AlertDialog alertDialog = new AlertDialog.Builder(
									getActivity())
									.setMessage("抱歉，游客不能进入此模块").setPositiveButton("确定", null).create();
							alertDialog.show();
							return true;
						}
						if (getLibPass() == null || getLibPass().equals("")) {
							//
							Toast.makeText(getActivity(), "密码默认是学号首字母小写哦", 0)
									.show();
							//
							// if (alertDialog != null)
							// alertDialog.show();
							// else
							{
								final EditText pw = new EditText(context);
								pw.setInputType(InputType.TYPE_CLASS_TEXT
										| InputType.TYPE_TEXT_VARIATION_PASSWORD);

								AlertDialog alertDialog = new AlertDialog.Builder(
										getActivity())
										.setTitle("请输入你的图书馆登陆密码。")
										.setView(pw)
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {

														Activity act = getActivity();
														int frmId = act
																.getResources()
																.getIdentifier(
																		"frg_tool",
																		"id",
																		act.getPackageName());
														FragmentTransaction ft = getActivity()
																.getSupportFragmentManager()
																.beginTransaction();
														ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
														saveLibPass(pw
																.getText()
																.toString());
														ft.replace(
																frmId,
																new Fgm_4(
																		n,
																		handler,
																		pw.getText()
																				.toString()));
														ft.addToBackStack("Fgm1");
														ft.commit();
														// InputMethodManager
														// imm =
														// (InputMethodManager)
														// getActivity()
														// .getSystemService(Context.INPUT_METHOD_SERVICE);
														// imm.hideSoftInputFromWindow(arg0.getWindowToken(),
														// 0); // 强制隐藏键盘

														// SpMethods.saveLibPass(getApplicationContext(),
														// pw.getText().toString().trim());
														// Log.i("Whie",
														// pw.getText().toString().trim()
														// + "aaaa2");
														// Intent it = new
														// Intent(ActivityLib.this,
														// ActivityLibMyBook.class);
														// it.putExtra("password",
														// pw.getText().toString().trim());
														// ActivityLib.this.startActivity(it);
													}
												})
										.setNegativeButton("取消", null).create();
								alertDialog.show();
							}
						} else {
							Activity act = getActivity();
							int frmId = act.getResources().getIdentifier(
									"frg_tool", "id", act.getPackageName());
							FragmentTransaction ft = getActivity()
									.getSupportFragmentManager()
									.beginTransaction();
							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
							ft.replace(frmId, new Fgm_4(n, handler,
									getLibPass()));
							ft.addToBackStack("Fgm1");
							ft.commit();
						}
					}
				} else if (e.getAction() == MotionEvent.ACTION_CANCEL) {
					v.setBackgroundColor(0xff607d8b);
				}
				// v.setBackgroundColor(0xff607d8b);
				return true;
			}
		});

		ll.setBackgroundColor(0xfff2f2f2);
		ll.setOrientation(LinearLayout.VERTICAL);

		ll.addView(searchLay);
		ll.addView(rectLay);
		searchLay.setBackgroundColor(Color.WHITE);
		searchLay.setGravity(Gravity.CENTER_VERTICAL);
		// 显示参数，必须在addView之后调用
		// ------------------------------------
		((LinearLayout.LayoutParams) searchLay.getLayoutParams()).topMargin = (int) DensityUtil
				.dip2px(context, 12);
		((LinearLayout.LayoutParams) searchLay.getLayoutParams()).leftMargin = (int) DensityUtil
				.dip2px(context, 8);
		((LinearLayout.LayoutParams) searchLay.getLayoutParams()).rightMargin = (int) DensityUtil
				.dip2px(context, 8);
		((LinearLayout.LayoutParams) searchLay.getLayoutParams()).width = LinearLayout.LayoutParams.MATCH_PARENT;

		((LinearLayout.LayoutParams) searchLay.getLayoutParams()).height = (int) DensityUtil
				.dip2px(context, 36);

		((LinearLayout.LayoutParams) rectLay.getLayoutParams()).topMargin = (int) DensityUtil
				.dip2px(context, 12);
		((LinearLayout.LayoutParams) rectLay.getLayoutParams()).leftMargin = (int) DensityUtil
				.dip2px(context, 8);
		((LinearLayout.LayoutParams) rectLay.getLayoutParams()).rightMargin = (int) DensityUtil
				.dip2px(context, 8);
		((LinearLayout.LayoutParams) rectLay.getLayoutParams()).width = LinearLayout.LayoutParams.MATCH_PARENT;
		// ------------------------------------
		readyUI();
		try {
			// 本地数据
			String st = getInfo();
			if (st != null && !st.equals("")) {
				try {
					upData(new JSONObject(getInfo()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				upUI();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 更新完数据后会同时请更新界面
		threadRun = true;
		Log.i("TOOLINFOLIB", "THREADRUN");
		runThread();
		// new UpDataFromNet().start();
		return ll;
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
		// inf = LayoutInflater.from(context);
	}

	private void readyUI() {

		// int edId = pluginRes.getIdentifier("search_edit", "layout",
		// PACKAGE_NAME);

		textInSearch = (EditText) inf.inflate(R.layout.tools_lib_search_edit,
				null);
		textInSearch.setSingleLine();
		// new EditText(context);
		btInSearch = new Button(context);
		listInRect = new ListView(context);
		listAdp = new LibListAdp();

		listInRect.setAdapter(listAdp);

		listInRect.setDivider(new ColorDrawable(Color.WHITE));
		listInRect.setDividerHeight((int) DensityUtil.dip2px(context, 12));
		textLayInRect = new RelativeLayout(context);

		t1 = new TextView(context);
		t2 = new TextView(context);
		rectLay.addView(textLayInRect);

		rectLay.addView(listInRect);
		rectLay.setBackgroundColor(Color.WHITE);

		// int id = pluginRes.getIdentifier("search_icon", "drawable",
		// PACKAGE_NAME);
		// Log.i("IDDDDD", "" + id);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.tools_lib_search_icon);
		BitmapDrawable bmd = new BitmapDrawable(bm);
		btInSearch.setBackgroundDrawable(bmd);
		textInSearch.setCursorVisible(true);
		textInSearch.setTextSize(12);
		textInSearch.setHint("馆藏搜索");
		textInSearch.setTextColor(Color.BLACK);
		textInSearch.setPadding(0, 0, 0, 0);
		textInSearch.setBackgroundDrawable(null);
		// textInSearch.setBackgroundColor(0xff000000);

		// textInSearch.set
		// TODO searchText.setHintTextColor(color);
		searchLay.addView(textInSearch);
		searchLay.addView(btInSearch);
		((RelativeLayout.LayoutParams) textInSearch.getLayoutParams()).width = RelativeLayout.LayoutParams.MATCH_PARENT;
		((RelativeLayout.LayoutParams) textInSearch.getLayoutParams()).leftMargin = (int) DensityUtil
				.dip2px(context, 22 - 8);
		((RelativeLayout.LayoutParams) textInSearch.getLayoutParams()).height = (int) DensityUtil
				.dip2px(context, 24);
		((RelativeLayout.LayoutParams) textInSearch.getLayoutParams()).topMargin = (int) DensityUtil
				.dip2px(context, (36 - 24) / 2 + 1);

		((RelativeLayout.LayoutParams) btInSearch.getLayoutParams()).width = (int) DensityUtil
				.dip2px(context, 36);
		((RelativeLayout.LayoutParams) btInSearch.getLayoutParams()).height = (int) DensityUtil
				.dip2px(context, 36);
		((RelativeLayout.LayoutParams) btInSearch.getLayoutParams())
				.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// (int) DensityUtil
		// .dip2px(context, 36);

		btInSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if( textInSearch
						.getEditableText().toString().trim().equals(""))
				{
//					Toast.makeText(getActivity(), "请", duration)
					return;
				}
				Activity act = getActivity();
				int frmId = act.getResources().getIdentifier("frg_tool", "id",
						act.getPackageName());
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.replace(frmId, new Fgm_2(n, handler, textInSearch
						.getEditableText().toString()));
				ft.addToBackStack("Fgm1");
				ft.commit();
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0); // 强制隐藏键盘

			}
		});
		((RelativeLayout.LayoutParams) textLayInRect.getLayoutParams()).width = RelativeLayout.LayoutParams.MATCH_PARENT;
		((RelativeLayout.LayoutParams) textLayInRect.getLayoutParams()).topMargin = (int) DensityUtil
				.dip2px(context, 12);

		((RelativeLayout.LayoutParams) textLayInRect.getLayoutParams()).leftMargin = (int) DensityUtil
				.dip2px(context, 22 - 8);
		((RelativeLayout.LayoutParams) textLayInRect.getLayoutParams()).rightMargin = (int) DensityUtil
				.dip2px(context, 22 - 8);
		((RelativeLayout.LayoutParams) textLayInRect.getLayoutParams()).bottomMargin = (int) DensityUtil
				.dip2px(context, 24);

		textLayInRect.setGravity(Gravity.CENTER_VERTICAL);
		textLayInRect.addView(t1);
		textLayInRect.addView(t2);
		Paint p = new Paint();
		Rect rect = new Rect();
		// 返回包围整个字符串的最小的一个Rect区域
		p.setTextSize(DensityUtil.sp2px(context, 12));
		p.getTextBounds("空余/总数", 0, "空余/总数".length(), rect);
		int h = rect.height();

		t1.setText("实时剩余座位情况");
		t1.setTextSize(12);
		((RelativeLayout.LayoutParams) t1.getLayoutParams()).leftMargin = 0;
		((RelativeLayout.LayoutParams) t1.getLayoutParams()).height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		t1.setGravity(Gravity.CENTER_VERTICAL);

		t1.setTextColor(Color.BLACK);
		t2.setText("空余/总数");
		t2.setTextSize(12);
		((RelativeLayout.LayoutParams) t2.getLayoutParams())
				.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// = (int) (
															// DensityUtil.getWidth(context)
															// -
															// DensityUtil.dip2px(context,
															// 22+22) - w - 10)
															// ;
		((RelativeLayout.LayoutParams) t2.getLayoutParams()).height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		t2.setGravity(Gravity.CENTER_VERTICAL);
		t2.setTextColor(Color.BLACK);
		listInRect.setVerticalScrollBarEnabled(false);

		((RelativeLayout.LayoutParams) listInRect.getLayoutParams()).width = RelativeLayout.LayoutParams.MATCH_PARENT;
		((RelativeLayout.LayoutParams) listInRect.getLayoutParams()).height = (int) (DensityUtil
				.getHeight(context)
				- (DensityUtil.dip2px(context, 12 + 12 + 12) + h) - (DensityUtil
					.dip2px(context, 12 + 36 + 12 + 24 + 48 + 24 + 100)))
				- (int) (DensityUtil.getWidth(context) * 221 / 720);
		((RelativeLayout.LayoutParams) listInRect.getLayoutParams()).topMargin = (int) DensityUtil
				.dip2px(context, 12 + 12 + 12) + h;
		((RelativeLayout.LayoutParams) listInRect.getLayoutParams()).leftMargin = (int) DensityUtil
				.dip2px(context, 22 - 8);
		((RelativeLayout.LayoutParams) listInRect.getLayoutParams()).rightMargin = (int) DensityUtil
				.dip2px(context, 22 - 8);

		Log.i("WHAIHI",
				""
						+ (DensityUtil.getHeight(context) + "LLLL" + ((RelativeLayout.LayoutParams) listInRect
								.getLayoutParams()).height));
		// ((RelativeLayout.LayoutParams)
		// listInRect.getLayoutParams()).bottomMargin = (int) DensityUtil
		// .dip2px(context, 24);

		RelativeLayout trl = new RelativeLayout(context);
		ll.addView(trl);
		trl.addView(myBook);
		myBook.setText("我的图书");
		myBook.setTextColor(Color.WHITE);
		myBook.setTextSize(24);
		((RelativeLayout.LayoutParams) myBook.getLayoutParams()).topMargin = (int) (DensityUtil
				.dip2px(context, 24));

		((RelativeLayout.LayoutParams) myBook.getLayoutParams()).width = (int) (DensityUtil
				.dip2px(context, 184));
		((RelativeLayout.LayoutParams) myBook.getLayoutParams()).leftMargin = (int) (DensityUtil
				.getWidth(context) - DensityUtil.dip2px(context, 184)) / 2;

	}

	private void runThread() {
		new Thread() {
			public void run() {
				while (threadRun) {
					final JSONObject s = getNetData();
					if (s == null)
						return;
					handler.post(new Runnable() {

						@Override
						public void run() {
							upData(s);
							upUI();
						}
					});

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						threadRun = false;
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void saveInfo(String s) {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		sp.edit().putString("libInfo_mainP", s).commit();
	}

	private void saveLibPass(String s) {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		sp.edit().putString("libInfo_pass", s).commit();
	}

	private void upData(JSONObject o) {
		listdata = new ArrayList<List<String>>();
		JSONArray ary;
		try {
			ary = o.getJSONObject("info").getJSONArray("list");
			for (int i = 0; i < ary.length(); i++) {
				JSONObject jso0 = ary.getJSONObject(i);
				List<String> listt = new ArrayList<String>();
				listt.add(jso0.getString("name"));
				listt.add(jso0.getString("entered"));
				listt.add(jso0.getString("now"));
				listt.add(jso0.getString("empty"));
				listdata.add(listt);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void upUI() {
		listAdp.notifyDataSetChanged();
	}

	// class UpDataFromNet extends Thread {
	//
	// public void run() {
	// final JSONObject s = getNetData();
	// if (s == null)
	// return;
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// upData(s);
	// upUI();
	// }
	// });
	// }
	//
	// }

	// private

}
