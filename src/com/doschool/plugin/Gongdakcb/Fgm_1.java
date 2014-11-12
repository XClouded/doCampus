package com.doschool.plugin.Gongdakcb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.listener.NetWorkFunction;

public class Fgm_1 extends Fragment {
	Context context;
	// LayoutInflater inflater;
	// Resources pluginRes;
	final String PACKAGE_NAME = "com.doschool.plugin.Gongdakcb";
	RelativeLayout rl;

	NetWorkFunction n;
	Handler handler;

	int proViewW = 0, proViewH = 0;
	int leftNumW = 0, topWeekH = 0;

	void readyValues() {
		leftNumW = (int) DensityUtil.dip2px(context, 24);
		topWeekH = (int) DensityUtil.dip2px(context, 24);

		proViewW = (int) ((DensityUtil.getWidth(context) - leftNumW) / 7);
		proViewH = (int) ((DensityUtil.getHeight(context) - topWeekH
				- DensityUtil.getActBarHeight(context) - DensityUtil
				.getStatusBarHeight(context)) / 11);
	}

	int colors[] = { 0xffe75d5d, 0xffde6bae, 0xff78909c, 0xff89c997,
			0xffdfbd42, 0xfff29c9f, 0xff84ccc9, 0xff8c98cc, 0xfff6b37f };

	String wHanzi = "一二三四五六日X";

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
			if (getActivity() == null)
				return;
			Project v = projList.get(i);
			rlp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlp.leftMargin = ((v.weekday - 1) * proViewW);
			rlp.topMargin = ((v.begin - 1) * (proViewH));
			rlp.width = proViewW - 1;
			rlp.height = proViewH * v.last - 1;
			FrameLayout vv = getProjectView(v, colors[i % colors.length],
					v.last);
			pjrl.addView(vv, rlp);
		}
	}

	FrameLayout getProjectView(final Project v, int color, int i) {
		if (getActivity() == null)
			return null;
		TextView t = new TextView(context);
		t.setText(v.subName + "&" + v.address);
		t.setGravity(Gravity.CENTER);
		t.setTextSize(12);
		t.setTextColor(0xffffffff);
		FrameLayout fl = new FrameLayout(context);

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

				ft.replace(frmId, new Fgm_2(v));
				ft.addToBackStack("Fgm1");
				ft.commit();

			}
		});

		return fl;
	}

	Bitmap getBgBitmap(int color, int t) {
		Bitmap bm = Bitmap.createBitmap(proViewW, proViewH * t,
				Bitmap.Config.ARGB_4444);
		Canvas c = new Canvas(bm);
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);// 充满
		p.setColor(color);
		p.setAntiAlias(true);// 设置画笔的锯齿效�?
		RectF oval3 = new RectF(0, 0, proViewW - 1, proViewH * t - 1);// 设置个新的长方形
		c.drawRoundRect(oval3, DensityUtil.dip2px(context, 5),
				DensityUtil.dip2px(context, 5), p);
		return bm;
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

	void setBack() {
		pjrl.addView(new BackGView(context));
	}

	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		handler = h;
	}

	RelativeLayout pjrl;

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(i, container, savedInstanceState);

		readyRes();
		readyValues();
		getActivity().getActionBar().setTitle("课程表");

		rl = new RelativeLayout(context);
		pjrl = new RelativeLayout(context);

		rl.setBackgroundColor(Color.WHITE);
		rl.addView(pjrl);
		// 显示参数，必须在addView之后调用
		// ------------------------------------

		((RelativeLayout.LayoutParams) pjrl.getLayoutParams()).leftMargin = leftNumW;
		((RelativeLayout.LayoutParams) pjrl.getLayoutParams()).topMargin = topWeekH;
		// ------------------------------------

		try {
			// 本地数据
			String info = getInfo();
			if (info != null && !info.equals(""))
				try {
					upData(new JSONObject(getInfo()));
					upUI();
				} catch (JSONException e) {
					e.printStackTrace();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 网络获取数据
		new UpDataFromNet().start();

		return rl;
	}

	List<Project> projList;

	private void upData(JSONObject o) {
		projList = new ArrayList<Project>();
		JSONArray ary;
		try {
			ary = o.getJSONArray("kb");
			for (int i = 0; i < ary.length(); i++) {
				JSONObject obj = ary.getJSONObject(i);
				int ws[] = new int[obj.getJSONArray("week").length()];
				for (int k = 0; k < ws.length; k++)
					ws[k] = obj.getJSONArray("week").getInt(k);
				Project p = new Project(obj.getString("subname"), ws,
						obj.getString("area"), obj.getInt("weekday"),
						obj.getInt("begin"), obj.getInt("last"));
				projList.add(p);
			}
		} catch (JSONException e) {
			projList = null;
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
		//
		// try {
		// pluginRes = getActivity().getPackageManager()
		// .getResourcesForApplication(PACKAGE_NAME);
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// }

		// inflater = LayoutInflater.from(context);
	}

	private JSONObject getNetData() throws Exception {
		if (getActivity() == null)
			return null;
		final String s = n.remoteServer(
				"http://doschool1.duapp.com/hfutools/jwNOAES.service.php",
				"service=0&xh=" + getStuId() + "&pw=" + getPassWord()
						+ "&xn=0&xq=0");
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

	private void error(String s) {
		if (getActivity() == null)
			return;
		if (s.equals("Epass")) {
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

	private String getStuId() {
		if (getActivity() == null)
			return "";
		SharedPreferences sp = getActivity().getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		return sp.getString("funId", "").toUpperCase();
	}

	private String getPassWord() {
		SharedPreferences sp = getActivity().getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		return sp.getString("STUPASS", "");
	}

	private void savePassWord(String ps) {
		SharedPreferences sp = getActivity().getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		sp.edit().putString("STUPASS", ps).commit();
	}

	private void saveInfo(String s) {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		sp.edit().putString("kcbInfo", s).commit();
	}

	private String getInfo() {
		SharedPreferences sp = context.getSharedPreferences("MySP",
				Context.MODE_PRIVATE);
		return sp.getString("kcbInfo", "");
	}

	int week = -1;

	class UpDataFromNet extends Thread {

		public void run() {
			JSONObject s = null;
			try {
				s = getNetData();
				try {
					JSONObject weekJson = new JSONObject(n.remoteServer(
							"http://doschool1.duapp.com/tools/getWeek.php", ""));
					week = weekJson.getInt("week");
				} catch (JSONException e) {
					week = -1;
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (s == null)
				return;
			final JSONObject ss = s;
			handler.post(new Runnable() {

				@Override
				public void run() {
					upData(ss);
					upUI();
				}
			});
		}

	}

}
