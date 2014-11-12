package com.doschool.plugin.kcb;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.listener.NetWorkFunction;
import com.doschool.methods.SpMethods;

@SuppressLint("NewApi")
public class Fgm_1 extends Fragment {
	
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
			getActivity().setResult(0);
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);

			return super.onOptionsItemSelected(item);
		case 100:
			spy = 30;
			spx = (int) DensityUtil.getWidth(context) - proViewW * 2 - 30;
			spn.setSelection(0);
			new UpDataFromNet().start();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class BackGView extends View {

		public BackGView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Paint p = new Paint();
			p.setColor(0xfff2f2f2);
			p.setStrokeWidth(5);
			for (int i = 0; i <= 7; i++)
				for (int j = 0; j <= 11; j++) {
					canvas.drawLine(
							proViewW * i - DensityUtil.dip2px(context, 5),
							proViewH * j,
							proViewW * i + DensityUtil.dip2px(context, 5),
							proViewH * j, p);
					canvas.drawLine(proViewW * i,
							proViewH * j - DensityUtil.dip2px(context, 5),
							proViewW * i,
							proViewH * j + DensityUtil.dip2px(context, 5), p);

				}
		}

	}
	class SPAdp extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 20 + 1;
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
			TextView t = new TextView(context);
			t.setSingleLine();
			if (position == 0)
				t.setText("本周@第" + (bkweek) + "周");
			else
				t.setText("   第" + (position) + "周       ");
			t.setTextSize(18);
			t.setBackgroundColor(0xaff266cc);
			t.setTextColor(Color.WHITE);
			if (position == week)
				t.setTextColor(Color.RED);

			return t;
		}

	}
	class UpDataFromNet extends Thread {

		public void run() {
			JSONObject s = null;
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					rootRl.addView(proLay,
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
					proLay.beginMoving();
				}
			});

			try {
				s = getNetData();

			} catch (Exception e) {
				e.printStackTrace();
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

			if (s == null)
				return;
			final JSONObject ss = s;
			handler.post(new Runnable() {

				@Override
				public void run() {
					upDate(ss);
					upUI();
				}
			});
		}

	}

	int beginx, beginy;

	private int bkweek;

	int colors[] = { 0xffe75d5d, 0xffde6bae, 0xff78909c, 0xff89c997,
			0xffdfbd42, 0xfff29c9f, 0xff84ccc9, 0xff8c98cc, 0xfff6b37f };
	Context context;
	Handler handler;
	int leftNumW = 0, topWeekH = 0;
	NetWorkFunction n;

	// LayoutInflater inflater;
	// Resources pluginRes;
	final String PACKAGE_NAME = "com.doschool.plugin.kcb";
	RelativeLayout pjrl;
	List<Project> projList;
	LoadingProgressView proLay;
	int proViewW = 0, proViewH = 0;

	RelativeLayout rl;
	RelativeLayout rootRl;

	BaseAdapter spadp;

	Spinner spn;

	// class DropDownListenser implements OnNavigationListener
	// {
	//
	// /* 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment */
	// public boolean onNavigationItemSelected(int itemPosition, long itemId)
	// {
	// week = itemPosition + 1;
	// upUI();
	// return true;
	// }
	// }
	int spy, spx;

	int week = -1;

	String wHanzi = "一二三四五六日";

	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		handler = h;
	}

	private void combine(List<Project> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (eqProject(list.get(i), list.get(j))) {
					list.get(i).times += list.get(j).times;
					list.get(i).startTime = list.get(i).startTime < list.get(j).startTime ? list
							.get(i).startTime : list.get(j).startTime;
					list.set(j, null);
				}
			}
		}
		int k = list.size();
		for (int i = 0; i < k; i++) {
			if (list.get(i) == null) {
				list.remove(i);
				k--;
				i--;
			}
		}
	}

	private boolean eqProject(Project a, Project b) {
		if (a == null || b == null)
			return false;
		if (a.address.equals(b.address) && a.ds == b.ds
				&& a.name.equals(b.name) && a.startWeek == b.startWeek
				&& a.teacherName.equals(b.teacherName) && a.toWeek == b.toWeek)
			return true;
		return false;
	}

	private void error(String s) {
		if (getActivity() == null)
			return;
		if( s.equals(
		 "Epass")){
			savePassWord("");
			new AlertDialog.Builder(getActivity())
					.setMessage("密码错误！请再次输入密码")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									errorOnEpassClick();
								}
							}).create().show();
			return;
		}
			 if(s.equals("EnetWork")){
				
			}
			 if(s.equals("Ejson")){
				
			}
			 if(s.equals("Emsg")){
				
			}
			{
				if (getActivity() == null)
					return;
				Toast.makeText(getActivity(), "网络错误！请检查网络连接", 1).show();
			}
	}

	private void errorOnEpassClick() {
		final EditText edit = new EditText(getActivity());
		edit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// edit.setBackgroundResource(R.drawable.edit_search_bg);
		new AlertDialog.Builder(getActivity()).setTitle("请输入你的教务处密码")
				.setView(edit)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (edit.getText().toString().trim().equals("")) {
							if (getActivity() == null)
								return;
							Toast.makeText(getActivity(), "密码不允许为空！", 1);
							errorOnEpassClick();
							return;
						} else {
							savePassWord(edit.getText().toString().trim());
							new UpDataFromNet().start();
						}
					}

				}).create().show();
	}

	Bitmap getBgBitmap(int color, int t) {
		Bitmap bm = Bitmap.createBitmap(proViewW, proViewH * t,
				Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(bm);
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);// 充满
		p.setColor(color);
		p.setAntiAlias(true);// 设置画笔的锯齿效果
		RectF oval3 = new RectF(0, 0, proViewW - 1, proViewH * t - 1);// 设置个新的长方形
		c.drawRoundRect(oval3, DensityUtil.dip2px(context, 5),
				DensityUtil.dip2px(context, 5), p);
		return bm;
	}

	private String getInfo() {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("kcbInfo", "");
	}

	private JSONObject getNetData() throws Exception {
		if (getActivity() == null)
			return null;
		final String s = n.remoteServer(
		// "http://doschool1.duapp.com/tools/getClass.php",
				PluginListener.postU, "xh=" + getStuId() + "&pw="
						+ getPassWord() + "&xn=0&xq=0");
		if (s.charAt(0) != 'E')
			try {
				saveInfo(s);
				return new JSONObject(s);
			} catch (JSONException e) {
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

	private String getPassWord() {
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("STUPASS", "");
	}
	
	private int getSPX() {
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getInt("SPX", -1);
	}
	
	private int getSPY() {
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getInt("SPY", -1);
	}
	
	private void setSPX(int x) {
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putInt("SPX", x).commit();
	}
	
	private void setSPY(int y) {
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putInt("SPY", y).commit();
	}



	FrameLayout getProjectView(final Project v, int color, int i) {
		if (getActivity() == null)
			return null;
		TextView t = new TextView(context);
		t.setText(v.name + "&" + v.address);
		t.setGravity(Gravity.CENTER);
		t.setTextSize(12);
		t.setTextColor(0xffffffff);
		FrameLayout fl = new FrameLayout(context);
		if (getActivity() == null)
			return null;

		BitmapDrawable bmd = new BitmapDrawable(getResources(), getBgBitmap(
				color, i));
		fl.setBackgroundDrawable(bmd);
		fl.addView(t);
		FrameLayout.LayoutParams flp = (LayoutParams) t.getLayoutParams();
		flp.topMargin = (int) DensityUtil.dip2px(context, 5);
		flp.bottomMargin = (int) DensityUtil.dip2px(context, 5);
		flp.gravity = Gravity.CENTER;

		fl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Activity act = getActivity();
				int frmId = act.getResources().getIdentifier("frg_tool", "id",
						act.getPackageName());
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

				ft.add(frmId, new Fgm_2(v));
				ft.addToBackStack("Fgm1");
				ft.commit();

			}
		});

		return fl;
	}

	private String getStuId() {
		if (getActivity() == null)
			return "";
		return SpMethods.loadString(context, SpMethods.USER_FUNID);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// inflater.inflate(R.menu.common_confirm, menu);
//		super.onCreateOptionsMenu(menu, inflater);
//	}

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);

		readyRes();
		readyValues();
		rootRl = new RelativeLayout(context);
		rootRl.setBackgroundColor(Color.WHITE);
		proLay = new LoadingProgressView(context, 8);
		handler = new Handler();
		
		if(getSPX()==-1){
			Log.i("SPSPSP",""+spx);
			spy = 30;
			spx = (int) DensityUtil.getWidth(context) - proViewW * 2 - 30;
		}
		else{
			Log.i("SPSPSP2",""+spx);

			spx = getSPX();
			spy = getSPY();
		}

		rl = new RelativeLayout(context);
		pjrl = new RelativeLayout(context);

		spadp = new SPAdp();
		spn = new Spinner(context);
		spn.setAdapter(spadp);
		spn.setBackgroundColor(Color.TRANSPARENT);
		spn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0)
					week = bkweek;
				else
					week = position;
				upUI();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spn.setOnTouchListener(new OnTouchListener() {
			float ex, ey;

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// Log.i("HIHI","x:"+spx+"  y:"+spy);
				int w = spn.getWidth();
				int h = spn.getHeight();
				// 不可能达到-3000 除非是自己设置的
				if (e.getX() == -3000)
					return false;

				if (e.getAction() == e.ACTION_DOWN) {
					ex = e.getX();
					ey = e.getY();
					Log.i("HIHI", "DOWN!!" + ex + " " + ey + " " + e.getX()
							+ " " + e.getY());
					return true;
				} else if (e.getAction() == e.ACTION_MOVE) {
					RelativeLayout.LayoutParams rll = (RelativeLayout.LayoutParams) spn
							.getLayoutParams();
					Log.i("HIHI","L:"+spx+" R:"+spy);
					if(spy + e.getY() - ey <0 || spx +e.getX() - ex < 0 || spx +e.getX() - ex + w >DensityUtil.getWidth(context))return true;
//					if(rll.leftMargin + e.getX() - ex <20 || rll.rightMargin + e.getX() - ex <20 
//							|| rll.topMargin + e.getY() - ey < 20
//							|| rll.bottomMargin + e.getY() - ey < 20)return true;
					
					rll.leftMargin += e.getX() - ex;
					rll.topMargin += e.getY() - ey;
					spx += e.getX() - ex;
					spy += e.getY() - ey;
					Log.i("HIHI", "MOVE!!" + ex + " " + ey + " " + e.getX()
							+ " " + e.getY());

					spn.setLayoutParams(rll);
//					spn.invalidate()
					return true;

				} else if (e.getAction() == e.ACTION_UP) {
					// 当滑动范围并不大时我认为他想点击
					Log.i("HIHI", "UP2!!" + spx + " " + spy + " " + beginx
							+ " " + beginy);

					if (spx <= beginx + 20 && spx >= beginx - 20
							&& spy <= beginy + 20 && spy >= beginy - 20) {
						e.setLocation(-3000, -3000);
						e.setAction(e.ACTION_DOWN);
						spn.dispatchTouchEvent(e);
						e.setAction(e.ACTION_UP);
						spn.dispatchTouchEvent(e);
					}
					beginx = spx;
					beginy = spy;

					return true;
				}
				return false;

			}
		});
		getActivity().getActionBar().setTitle("课程表");
		// getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// getActivity().getActionBar().setListNavigationCallbacks(spadp, new
		// DropDownListenser());

		// getActivity().getActionBar().hide();

		rl.setBackgroundColor(Color.WHITE);
		rl.addView(pjrl);
		// 显示参数，必须在addView之后调用
		// ------------------------------------

		((RelativeLayout.LayoutParams) pjrl.getLayoutParams()).leftMargin = leftNumW;
		((RelativeLayout.LayoutParams) pjrl.getLayoutParams()).topMargin = topWeekH;
		// ------------------------------------
		rootRl.addView(rl);
		if(getPassWord()==null || getPassWord().equals("")){
			errorOnEpassClick();
		}
		else{
			try {
				// 本地数据
				String info = getInfo();
				if (info != null && !info.equals(""))
					try {
						upDate(new JSONObject(getInfo()));
						upUI();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				else{
					new UpDataFromNet().start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// 网络获取数据
//		else new UpDataFromNet().start();

		return rootRl;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
	}
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("SPSPSP","PPPPP"+spx+" "+spy);
		setSPX(spx);
		setSPY(spy);

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

		// inflater = LayoutInflater.from(context);
	}

	void readyValues() {
		leftNumW = (int) DensityUtil.dip2px(context, 24);
		topWeekH = (int) DensityUtil.dip2px(context, 24);

		proViewW = (int) ((DensityUtil.getWidth(context) - leftNumW) / 7);
		proViewH = (int) ((DensityUtil.getHeight(context) - topWeekH
				- DensityUtil.getActBarHeight(context) - DensityUtil
				.getStatusBarHeight(context)) / 11);
	}

	private void saveInfo(String s) {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("kcbInfo", s).commit();
	}

	private void savePassWord(String ps) {
		SharedPreferences sp = getActivity().getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("STUPASS", ps).commit();
	}

	void setBack() {
		pjrl.addView(new BackGView(context));
	}

	private void upDate(JSONObject o) {
		projList = new ArrayList<Project>();
		JSONArray ary;
		try {
			week = o.getInt("week");
			bkweek = week;
			ary = o.getJSONArray("kb");
			for (int i = 0; i < ary.length(); i++) {
				JSONObject jso0 = ary.getJSONObject(i);
				JSONObject info = jso0.getJSONObject("info");
//				info = null;
				List<Project> listt = new ArrayList<Project>();
				for (int j = 0; j < info.getInt("times"); j++) {

					Project p = new Project(
							0,
							info.getJSONArray("week").getJSONArray(j).getInt(0),
							info.getJSONArray("week").getJSONArray(j).getInt(1),
							info.getJSONArray("jc").getJSONArray(j).getInt(0),
							1
									+ info.getJSONArray("jc")
											.getJSONArray(j)
											.getInt(info.getJSONArray("jc")
													.getJSONArray(j).length() - 1)
									- info.getJSONArray("jc").getJSONArray(j)
											.getInt(0), info.getJSONArray(
									"weekday").getInt(j), jso0
									.getString("subname"), info.getJSONArray(
									"area").getString(j), jso0
									.getString("teacher"), info.getJSONArray(
									"ds_week").getInt(j));
					listt.add(p);
				}
				combine(listt);
				projList.addAll(listt);
			}
		} catch (JSONException e) {
			projList = null;
			e.printStackTrace();
		}

	}

	void upUI() {
		pjrl.removeAllViews();
		setBack();
		RelativeLayout.LayoutParams rlp;
		rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		View leftTopV = new View(context);
		leftTopV.setBackgroundColor(0xffe2e8ea);
		rlp.width = leftNumW;
		rlp.height = topWeekH;
		rl.addView(leftTopV, rlp);

		for (int i = 0; i < 7; i++) {
			TextView weekT = new TextView(context);
			weekT.setText("星期" + wHanzi.charAt(i));
			weekT.setTextSize(12);
			weekT.setTextColor(0xff9e9e9e);
			weekT.setGravity(Gravity.CENTER);
			if (i % 2 == 0) {
				weekT.setBackgroundColor(0xffffffff);
			} else {
				weekT.setBackgroundColor(0xffe2e8ea);
			}

			rl.addView(weekT);
			rlp = (RelativeLayout.LayoutParams) weekT.getLayoutParams();
			rlp.height = topWeekH;
			rlp.width = proViewW;
			rlp.leftMargin = leftNumW + proViewW * i;
		}

		for (int i = 0; i < 11; i++) {
			TextView numT = new TextView(context);
			numT.setText("" + (i + 1));
			numT.setTextSize(12);
			numT.setTextColor(0xff9e9e9e);
			numT.setGravity(Gravity.CENTER);
			if (i % 2 == 0) {
				numT.setBackgroundColor(0xffffffff);
			} else {
				numT.setBackgroundColor(0xffe2e8ea);
			}

			rl.addView(numT);
			rlp = (RelativeLayout.LayoutParams) numT.getLayoutParams();
			rlp.height = proViewH;
			rlp.width = leftNumW;
			rlp.topMargin = topWeekH + proViewH * i;

		}

		for (int i = 0; i < projList.size(); i++) {
			Project v = projList.get(i);
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			Log.i("KCBA", "\nweek " + week + "\ngetName " + v.name + "\ngetDs "
					+ v.ds + "\ngetStartTime " + v.startTime + "\ngetEndTime "
					+ v.endTime + "\ngetFromWeek " + v.fromWeek
					+ "\ngetToWeek " + v.toWeek + "\ngetStartWeek "
					+ v.startWeek + "\ngetTimes " + v.times);

			if ((week < v.fromWeek || week > v.toWeek) && week != -1) {
				continue;
			}

			int ds = v.ds;
			if (ds == 1 && week % 2 == 0 && week != -1) {
				continue;
			}
			if (ds == 2 && week % 2 == 1 && week != -1) {
				continue;
			}
			rlp.leftMargin = ((v.startWeek - 1) * proViewW);
			rlp.topMargin = ((v.startTime - 1) * (proViewH));
			rlp.width = proViewW - 1;
			rlp.height = proViewH * v.times - 1;
			if (getActivity() == null)
				return;
			FrameLayout vv = getProjectView(v, colors[i % colors.length],
					v.times);
			pjrl.addView(vv, rlp);
		}
		ViewGroup vgp = ((ViewGroup) spn.getParent());
		if (vgp != null)
			vgp.removeView(spn);
		rl.addView(spn);
		spadp.notifyDataSetChanged();
		rlp = (android.widget.RelativeLayout.LayoutParams) spn
				.getLayoutParams();
		rlp.leftMargin = spx;
		rlp.topMargin = spy;
		beginx = spx;
		beginy = spy;
		// TODO
	}

}
