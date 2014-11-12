package com.doschool.plugin.grade;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.listener.NetWorkFunction;
import com.doschool.methods.SpMethods;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("DrawAllocation")
public class Fgm_1 extends Fragment {

	int time = 0;

	class CircleView extends View {
		Paint p = new Paint();
		int centerLine = 270;

		public CircleView(Context context) {
			super(context);
		}

		public void changeLine(int l) {
			centerLine = l;
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (pjjd.equals("-11"))
				return;

			p.setAntiAlias(true);
			p.setColor(0xffc6d6dc);
			p.setStrokeWidth(DensityUtil.dip2px(context, 4));
			p.setStyle(Style.STROKE);
			canvas.drawCircle(
					DensityUtil.dip2px(context, 36 + 50),
					DensityUtil.dip2px(context, 70 + 50)
							- DensityUtil.getActBarHeight(context),
					DensityUtil.dip2px(context, 50), p);
			int i = (int) ((4 - Double.valueOf(pjjd)) / 4 * 180) - 30;
			if (i < 0)
				i = 0;
			i *= 1.5;
			p.setColor(0xff416371);
			p.setStyle(Style.FILL);

			// 莫改莫改。。。
			RectF rect = new RectF(DensityUtil.dip2px(context, 38),

			DensityUtil.dip2px(context, 72)
					- DensityUtil.getActBarHeight(context),

			DensityUtil.dip2px(context, 38 + 96),

			DensityUtil.dip2px(context, 72 + 96)
					- DensityUtil.getActBarHeight(context));

			canvas.drawArc(rect, // 弧线所使用的矩形区域大小
					// 270度是y正轴线
					centerLine + i, // 开始角度
					360 - i - i, // 扫过的角度
					false, // 是否使用中心
					p);

			double x1 = 1 - Math.sin((centerLine + i - 270) * Math.PI / 180);
			double y1 = 1 - Math.cos((centerLine + i - 270) * Math.PI / 180);

			// Log.i("DDDG",""+centerLine+" "+i+" "+x1+" "+y1);

			double x2 = 1 - Math.sin((centerLine - i - 270) * Math.PI / 180);
			double y2 = 1 - Math.cos((centerLine - i - 270) * Math.PI / 180);

			x1 *= 48;
			x2 *= 48;
			y1 *= 48;
			y2 *= 48;

			p.setColor(Color.WHITE);
			// canvas.drawCircle((int)(DensityUtil.dip2px(context, 38 + 96) -
			// x1), (int)(DensityUtil.dip2px(context, 72)
			// - DensityUtil.getActBarHeight(context)+y1), 3, p);
			float xx1 = (DensityUtil.dip2px(context, 38 + 96 - (int) x1));

			float yy1 = (DensityUtil.dip2px(context, 72 + (int) y1) - DensityUtil
					.getActBarHeight(context));
			float xx2 = (DensityUtil.dip2px(context, 38 + 96 - (int) x2));

			float yy2 = (DensityUtil.dip2px(context, 72 + (int) y2) - DensityUtil
					.getActBarHeight(context));

			float k;
			k = (yy1 - yy2) / (xx1 - xx2);
			float b = (yy1) - k * (xx1);
			{
				float xxx = (xx2 - xx1) * time / 200.0f + xx1;
				float yyy = xxx * k + b;

				float k2 = -(1.0f / k);
				float b2 = (yy1 + yyy) / 2 - k2 * (xx1 + xxx) / 2;
				float y_ = (yy1 + yyy) / 2;
				float x_ = (xx1 + xxx) / 2;
				double on_x1 = ((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2) + Math
						.sqrt((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								* (2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								- 4
								* (1 + k2 * k2)
								* (x_ * x_ + b2 * b2 + y_ * y_ - 2 * y_ * b2 - 48 * 48)))
						/ (2 * (1 + k2 * k2));

				double on_x2 = ((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2) - Math
						.sqrt((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								* (2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								- 4
								* (1 + k2 * k2)
								* (x_ * x_ + b2 * b2 + y_ * y_ - 2 * y_ * b2 - 48 * 48)))
						/ (2 * (1 + k2 * k2));

				double on_y1 = k2 * on_x1 + b2;
				double on_y2 = k2 * on_x2 + b2;
				Log.i("DDDG",""+k);
				if (k > 0) {
					on_x1 = on_x2;
					on_y1 = on_y2;
				}

				Path path = new Path();
				path.moveTo(xx1, yy1);
				path.quadTo(
				// (3*xx1+xx2)/4,
				// (3*yy1+yy2)/4,
						(float) on_x1, (float) on_y1, (xxx), (yyy));
				path.close();
				LinearGradient lg = new LinearGradient(canvas.getWidth() / 2,
						0, canvas.getWidth() / 2, DensityUtil.dip2px(context,
								256) - DensityUtil.getActBarHeight(context),
						0xff173946, 0xff335f70, Shader.TileMode.CLAMP);
				Paint p = new Paint();
				p.setShader(lg);
				// p.setColor(Color.TRANSPARENT);
				// p.setColor(Color.BLACK);
				canvas.drawPath(path, p);

			}
			
			{
				float xxx = (xx2 - xx1) * time / 200.0f + xx1;
				float yyy = xxx * k + b;

				float k2 = -(1.0f / k);
				float b2 = (yy2 + yyy) / 2 - k2 * (xx2 + xxx) / 2;
				float y_ = (yy2 + yyy) / 2;
				float x_ = (xx2 + xxx) / 2;
				double on_x1 = ((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2) + Math
						.sqrt((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								* (2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								- 4
								* (1 + k2 * k2)
								* (x_ * x_ + b2 * b2 + y_ * y_ - 2 * y_ * b2 - 48 * 48)))
						/ (2 * (1 + k2 * k2));

				double on_x2 = ((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2) - Math
						.sqrt((2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								* (2 * x_ - 2 * k2 * b2 + 2 * y_ * k2)
								- 4
								* (1 + k2 * k2)
								* (x_ * x_ + b2 * b2 + y_ * y_ - 2 * y_ * b2 - 48 * 48)))
						/ (2 * (1 + k2 * k2));

				double on_y1 = k2 * on_x1 + b2;
				double on_y2 = k2 * on_x2 + b2;

				if (k > 0) {
					on_x2 = on_x1;
					on_y2 = on_y1;
				}

				Path path = new Path();
				path.moveTo(xxx, yyy);
				path.quadTo(
				// (3*xx1+xx2)/4,
				// (3*yy1+yy2)/4,
						(float) on_x2, (float) on_y2, (xx2), (yy2));
				path.close();
				p.setColor(0xff416371);
				canvas.drawPath(path, p);

			}
			// path.quadTo(
			// // (xx1+3*xx2) /4 +r.nextInt((int)xx1/2),
			// // (yy1+3*yy2)/4 +r.nextInt((int)xx1/2) ,
			//
			// (xx2),
			// (yy2));

			// canvas.drawPoint((int)(DensityUtil.dip2px(context, 38 +
			// 96-(int)x2)), (int)(DensityUtil.dip2px(context, 72+(int)y2)
			// - DensityUtil.getActBarHeight(context)), p);

			// canvas.drawCircle((int)(DensityUtil.dip2px(context, 38 + 96) -
			// x2), (int)(DensityUtil.dip2px(context, 72)
			// - DensityUtil.getActBarHeight(context)+y2), 3, p);

			p.setColor(Color.WHITE);
			p.setTextSize(DensityUtil.sp2px(context, 32));

			canvas.drawText(
					"" + pjjd,
					DensityUtil.dip2px(context, 36 + 18),
					DensityUtil.dip2px(context, 70 + 40 + 10)
							- DensityUtil.getActBarHeight(context), p);
			p.setTextSize(DensityUtil.sp2px(context, 12));
			canvas.drawText(
					"平均绩点",
					DensityUtil.dip2px(context, 36 + 25),
					DensityUtil.dip2px(context, 70 + 40)
							- DensityUtil.getActBarHeight(context)
							+ DensityUtil.sp2px(context, 32), p);

		}

	}

	class GradeListAdap extends BaseAdapter {

		List<List<String>> data;

		GradeListAdap(List<List<String>> d) {
			data = d;
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
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
		public View getView(int arg0, View v, ViewGroup arg2) {
			v = new ListItem(context, data.get(arg0));
			// 2 5

			// ((ViewGroup.LayoutParams)v.getLayoutParams()).height =
			// (int)DensityUtil.dip2px(context, 36);
			// ((ViewGroup.LayoutParams)v.getLayoutParams()).width =
			// ViewGroup.LayoutParams.MATCH_PARENT;

			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					(int) DensityUtil.dip2px(context, 36));
			v.setLayoutParams(lp);

			return v;
		}

	}

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
			return super.onOptionsItemSelected(item);
		case 100:
			new UpDataFromNet().start();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class LineView extends View {

		public LineView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			Log.i("ONDRAW", "LINE");
			// Paint p = new Paint();

			// canvas.drawRect(0, 0, canvas.getWidth(),
			// DensityUtil.dip2px(context, 256), p);

			// 256-208
			// 左边到36dp，右边到324dp为限
			Path path = new Path();
			path.moveTo(0, 0);

			path.lineTo(
					0,
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context));
			for (int i = 0; i < jdHs.length; i++) {
				path.lineTo(jdWs[i], jdHs[i]);
			}
			path.lineTo(
					DensityUtil.getWidth(context),
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context));
			path.lineTo(DensityUtil.getWidth(context), 0);
			path.lineTo(0, 0);

			path.close();
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.tool_grade_back);
			float width = bm.getWidth();
			float height = bm.getHeight();

			Matrix mm = new Matrix();
			mm.postScale((DensityUtil.getWidth(context) / width),
					(DensityUtil.dip2px(context, 48) / height));

			Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, (int) width - 1,
					(int) height - 1, mm, true);
			// Log.i("AAAAAAA","222TL:"+bitmap.getWidth()+"H"+bitmap.getHeight());
			//
			// BitmapShader s = new BitmapShader(bitmap, TileMode.REPEAT,
			// TileMode.REPEAT);

			// int c[] =
			// {0xfff2f2f2,0xfff2f2f2,0xffffffff,0xfff2f2f2,0xfff2f2f2};
			// RadialGradient radialGradient=new
			// RadialGradient(DensityUtil.getWidth(context)/2,
			// DensityUtil.dip2px(context,
			// 256)-DensityUtil.getActBarHeight(context)
			// , 500, c, null, TileMode.REPEAT);

			LinearGradient lg = new LinearGradient(canvas.getWidth() / 2, 0,
					canvas.getWidth() / 2, DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context), 0xff173946,
					0xff335f70, Shader.TileMode.CLAMP);
			Paint p = new Paint();
			p.setShader(lg);

			// p.setShader(radialGradient);
			int t = 100000;
			for (int i = 0; i < jdHs.length; i++) {
				if (jdHs[i] < t)
					t = jdHs[i];
			}
			// 选最高的点 从这个点的高度贴图

			canvas.drawBitmap(bitmap, 0, t, p);
			canvas.drawPath(path, p);
			p.setShader(null);
			p.setColor(Color.WHITE);
			canvas.drawRect(
					0,
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context) + 1,
					DensityUtil.getWidth(context),
					DensityUtil.dip2px(context, 276)
							- DensityUtil.getActBarHeight(context) + 1, p);

			p.setColor(0xff335f70);
			// 学期：字体大小12sp 行距16sp 上边距：90dp 右边距：324dp 时间轴：粗细1px 小竖线 1px 高：6dp
			p.setTextSize(DensityUtil.sp2px(context, 12));
			Rect r = new Rect();
			p.getTextBounds("学期", 0, 2, r);
			p.setAntiAlias(true);
			int textH = r.height();
			canvas.drawText("学期", 0, DensityUtil.dip2px(context, 256)
					- DensityUtil.getActBarHeight(context) + textH, p);
			p.setTextAlign(Align.CENTER);

			for (int i = 0; i < jdWs.length; i++) {
				canvas.drawText(
						(i + 1) + "",
						jdWs[i],
						DensityUtil.dip2px(context, 256)
								- DensityUtil.getActBarHeight(context) + textH,
						p);
			}

			// p.setColor(0xff335f70);
			p.setColor(0xff959595);
			p.setStrokeWidth(1);
			canvas.drawLine(
					0,
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context) + 1,
					DensityUtil.getWidth(context),
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context) + 1, p);
			canvas.drawLine(
					1,
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context) + 1,
					1,
					DensityUtil.dip2px(context, 256)
							- DensityUtil.getActBarHeight(context) - 10, p);

			for (int i = 0; i < jdWs.length; i++) {
				canvas.drawLine(
						jdWs[i],
						DensityUtil.dip2px(context, 256)
								- DensityUtil.getActBarHeight(context) + 1,
						jdWs[i],
						DensityUtil.dip2px(context, 256)
								- DensityUtil.getActBarHeight(context) - 10, p);
			}

			//
			//
			// canvas.drawLine(0, DensityUtil.dip2px(context, 256)-
			// DensityUtil.getActBarHeight(context)+1,
			// DensityUtil.getWidth(context), DensityUtil.dip2px(context, 256)-
			// DensityUtil.getActBarHeight(context)+1, p);
		}

	}

	class ListItem extends View {
		List<String> list;

		public ListItem(Context context, List<String> s) {
			super(context);
			list = s;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			Paint p = new Paint();
			p.setColor(0xff346172);
			canvas.drawRect(DensityUtil.dip2px(context, 22), 0,
					canvas.getWidth() - DensityUtil.dip2px(context, 22),
					DensityUtil.dip2px(context, 36), p);
			// 4 3.4 2.7 1.5 0
			double rate = 0;

			try {
				rate = Double.valueOf(list.get(5)) / 100;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				rate = Double.valueOf(list.get(4)) / 4;
			}

			if (rate == 0)
				rate += 0.01;

			int gradeW = (int) ((DensityUtil.getWidth(context) - DensityUtil
					.dip2px(context, 22) * 2) * (rate));

			canvas.drawRect(DensityUtil.dip2px(context, 22), 0, gradeW,
					DensityUtil.dip2px(context, 36), p);
			p.setColor(0xffc6d6dc);
			canvas.drawRect(DensityUtil.dip2px(context, 22) + gradeW, 0,
					(int) ((DensityUtil.getWidth(context) - DensityUtil.dip2px(
							context, 22))), DensityUtil.dip2px(context, 36), p);

			// TODO 可优化
			p.setColor(0xff273238);
			p.setTextSize(DensityUtil.sp2px(context, 12));

			Rect rect = new Rect();
			// 返回包围整个字符串的最小的一个Rect区域
			p.getTextBounds(list.get(5), 0, 1, rect);
			int h = rect.height();

			p.setColor(Color.WHITE);
			canvas.drawText(list.get(2), DensityUtil.dip2px(context, 36),
					(DensityUtil.dip2px(context, 36) + h) / 2, p);
			p.setColor(Color.BLACK);

			canvas.drawText(
					list.get(5),
					DensityUtil.getWidth(context)
							- DensityUtil.dip2px(context, 36)
							- p.measureText(list.get(5)),
					(DensityUtil.dip2px(context, 36) + h) / 2, p);

		}

	}

	class PageChangeLis implements OnPageChangeListener {
		int lastP = 0;

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int w) {
			pointNotNowPage(pv[lastP], lastP);
			pointToNowPage(pv[w], w);
			lastP = w;
			nowPage = w;
			upNowP();
		}

	}

	class PointTouchListener implements OnTouchListener {
		int lastP = 0;

		private void dealSta() {
			for (int i = 0; i < pSta.length; i++) {
				// if(pSta[i] == 3){
				pointNotNowPage(pv[i], i);
				// pSta[i] = 2;
				// }
			}
		}

		@Override
		public boolean onTouch(View arg0, MotionEvent e) {
			if (e.getX() < DensityUtil.dip2px(context, 32)
					|| e.getX() > DensityUtil.getWidth(context)
							- DensityUtil.dip2px(context, 40)
					|| e.getY() > DensityUtil.dip2px(context, 256 + 5)
							- DensityUtil.getActBarHeight(context)
					|| e.getY() < DensityUtil.dip2px(context, 208 - 5)
							- DensityUtil.getActBarHeight(context))
				return false;
			// View v = null;
			// v.dispatchTouchEvent(e);
			int begin = (int) DensityUtil.dip2px(context, 36);
			int eachW = (int) ((DensityUtil.getWidth(context) - 2 * begin) / xqjdList
					.size());
			int w = (int) (e.getX() - begin);
			w = w / eachW;
			// dealSta();
			nowPage = w;
			if (lastP != w) {
				vp.setCurrentItem(w);
				pointNotNowPage(pv[lastP], lastP);
				pointToNowPage(pv[w], w);
				lastP = w;
				Log.i("WAHAHH", "" + w);
			}
			// if(e.getAction()==MotionEvent.ACTION_MOVE){
			// int marginLeft = (int) e.getX();
			// LinearLayout rootLay = new LinearLayout(context);
			// rootLay.setBackgroundColor(Color.WHITE);
			//
			// RelativeLayout mainLayout = new RelativeLayout(context);
			//
			//
			// rootLay.addView(mainLayout);
			// LinearLayout.LayoutParams llp =
			// (android.widget.LinearLayout.LayoutParams)
			// mainLayout.getLayoutParams();
			// llp.leftMargin = marginLeft;
			// }
			return true;
		}

	}

	class PointView extends View {
		int x = 0, y = 0;
		static final int cl1 = 0xffebeff2;
		static final int cl2 = 0xffff6600;
		int myColor = 0xffebeff2;

		public PointView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Log.i("ONDRAW", "POINT");

			Paint p = new Paint();
			p.setAntiAlias(true);
			p.setColor(myColor);
			canvas.drawCircle(DensityUtil.dip2px(context, 5),
					DensityUtil.dip2px(context, 5),
					DensityUtil.dip2px(context, 5), p);
		}

		public void setCurrentPage() {
			myColor = cl2;
			this.invalidate();
		}

		public void setNotCurrentPage() {
			myColor = cl1;
			this.invalidate();
		}

		public void setXY(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	class UpDataFromNet extends Thread {

		public void run() {
			JSONObject s = null;
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
				s = getNetData();
			} catch (Exception e) {
				s = null;
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
					upData(ss);
					upUI();
				}
			});
		}

	}

	class ViewPagerAdapter extends PagerAdapter {
		private List<ListView> mListViews;

		public ViewPagerAdapter(List<ListView> lists) {
			this.mListViews = lists;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			// container.setBackgroundColor(Color.WHITE);
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

	Context context;
	LayoutInflater inflater;
	// Resources pluginRes;
	final String PACKAGE_NAME = "com.doschool";
	RelativeLayout rl;
	View circV, lineV;

	LinearLayout llTexts;

	TextView txq, tpm, twish;

	PointView pv[];
	NetWorkFunction n;

	Handler handler;

	ViewGroup vg;

	List<ListView> lists;

	ViewPager vp;

	List<List<List<String>>> data = null;

	private List<Double> xqjdList = null;
	private int nowPage = 0;

	int pSta[];// 1 放大动画in 2 缩回动画已开始 3放大状态

	Animation p2Ls[];

	Animation p2Ms[];

	private String pjjd = "-11";

	int jdHs[];

	int jdWs[];

	Bitmap bm;

	public Fgm_1(NetWorkFunction netWorkListener, Handler h) {
		n = netWorkListener;
		handler = h;
	}

	private void error(String s) {
		if (getActivity() == null)
			return;
		if (s.equals("Epass")) {
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
							Toast.makeText(getActivity(), "密码不允许为空", 1).show();
							errorOnEpassClick();
							return;
						} else {
							savePassWord(edit.getText().toString().trim());
							new UpDataFromNet().start();
						}
					}

				}).create().show();
	}

	List<String> pmList;

	private List<List<List<String>>> getGrade(JSONObject jsonObject) {
		// TODO
		List<List<List<String>>> dataList = new ArrayList<List<List<String>>>();

		int xqNum = 0;

		try {
			JSONObject jso = jsonObject;
			JSONArray ary = jso.getJSONArray("cj_info");

			ArrayList<List<String>> tmp = null;

			// 反向加入，保证最新的课程在前
			for (int i = 0; i < ary.length(); i++) {
				ArrayList<String> list = new ArrayList<String>();
				// xq不一样则分开为另一个List
				if (i == 0
						|| !ary.getJSONObject(i)
								.getString("xq")
								.equals(ary.getJSONObject(i - 1)
										.getString("xq"))) {

					tmp = new ArrayList<List<String>>();
					dataList.add(tmp);
				}
				list.add(ary.getJSONObject(i).getString("xn"));
				list.add(ary.getJSONObject(i).getString("xq"));
				list.add(ary.getJSONObject(i).getString("subname"));
				list.add(ary.getJSONObject(i).getString("xf"));
				list.add(ary.getJSONObject(i).getString("jd"));

				list.add(ary.getJSONObject(i).getString("point"));

				tmp.add(list);
			}

			pmList = new ArrayList<String>();
			JSONArray pmar = jso.getJSONArray("pm_info");
			try {
				for (int i = 0; i < pmar.length(); i++) {
					pmList.add(pmar.getJSONObject(i).getString("pm"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				pmList = null;
			}

			// "pm_info": [
			// {
			// "xn": "2012-2013",
			// "xq": "1",
			// "xf": "暂无数据",
			// "jd": "暂无数据",
			// "pm": "暂无数据"
			// },
			// {
			// "xn": "2012-2013",
			// "xq": "2",
			// "xf": "18.50",
			// "jd": "2.14",
			// "pm": "224"
			// },
			// {
			// "xn": "2013-2014",
			// "xq": "1",
			// "xf": "17.00",
			// "jd": "1.33",
			// "pm": "274"
			// },
			// {
			// "xn": "2013-2014",
			// "xq": "2",
			// "xf": "24.00",
			// "jd": "2.3",
			// "pm": "77"
			// }
			// ],
			// zxf += Double.valueOf(ary.getJSONObject(i).getString("xf"));
			// zjd += Double.valueOf(ary.getJSONObject(i).getString("jd"))
			// * Double.valueOf(ary.getJSONObject(i).getString("xf"));

			// DecimalFormat df = new DecimalFormat(".##");
			//
			// String st = df.format((zjd / zxf));
			// dataList.get(position).add("" + (st));
			pjjd = jso.getString("pjjd");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return dataList;
	}

	private String getInfo() {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("gradeInfo", "");
	}

	private JSONObject getNetData() throws Exception {

		if (getActivity() == null)
			return null;
		final String s = n.remoteServer(
		// "http://doschool1.duapp.com/tools/getGrade.php",
				PluginListener.postU, "xh=" + getStuId() + "&pw="
						+ getPassWord() + "&xn=0&xq=0");

		// 工大的服务
		// final String s = n.remoteServer(
		// "http://doschool1.duapp.com/hfutools/jwNOAES.service.php",
		// "service=1&xh="
		// + getStuId() + "&pw=" + getPassWord() + "&xn=0&xq=0");

		Log.i("POST", "xh=" + getStuId() + "&pw=" + getPassWord()
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

	private String getPassWord() {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		return sp.getString("STUPASS", "");
	}

	private void getPointPos() {
		// 256-208
		// 左边到36dp，右边到324dp为限
		jdHs = new int[xqjdList.size()];
		jdWs = new int[xqjdList.size()];
		int begin = (int) DensityUtil.dip2px(context, 36);
		int eachW = (int) ((DensityUtil.getWidth(context) - 2 * begin) / xqjdList
				.size());
		for (int i = 0; i < xqjdList.size(); i++) {
			int t = (int) (xqjdList.get(i) / 0.5);
			jdHs[i] = 256 - t * 6;
			jdHs[i] = (int) (DensityUtil.dip2px(context, jdHs[i]) - DensityUtil
					.getActBarHeight(context));
		}

		for (int i = 0; i < jdHs.length; i++) {
			Log.i("JDS", "" + jdHs[i]);
			jdWs[i] = (int) (begin + eachW * (i + 0.5));
		}

	}

	private String getStuId() {
		if (getActivity() == null)
			return "";
		return SpMethods.loadString(context, SpMethods.USER_FUNID);
		// SharedPreferences sp = getActivity().getSharedPreferences("MySP",
		// Context.MODE_PRIVATE);
		// return sp.getString("funId", "").toUpperCase();
	}

	private void initListView(List<List<List<String>>> data) {
		lists = new ArrayList<ListView>();
		for (int i = 0; i < data.size(); i++) {
			ListView l = new ListView(context);
			l.setAdapter(new GradeListAdap(data.get(i)));
			l.setDivider(new ColorDrawable(Color.WHITE));
			l.setDividerHeight((int) DensityUtil.dip2px(context, 12));
			lists.add(l);
		}
	}

	private void initText() {
		llTexts = new LinearLayout(context);
		llTexts.setOrientation(LinearLayout.VERTICAL);
		tpm = new TextView(context);
		txq = new TextView(context);
		twish = new TextView(context);
		txq.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		tpm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		twish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

		// txq.setText("2013-2014学年 第2学期");
		// tpm.setText("班级排名：3");
		// twish.setText("学霸!请收膝盖!");

		txq.setTextColor(Color.WHITE);
		tpm.setTextColor(Color.WHITE);
		twish.setTextColor(Color.WHITE);

		llTexts.addView(txq);
		llTexts.addView(tpm);
		llTexts.addView(twish);
		((LinearLayout.LayoutParams) twish.getLayoutParams()).topMargin = 0;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	ProgressBar proBar;
	LoadingProgressView proLay;
	SensorManager sm;
	boolean waterRefresh = false;
	SensorEventListener sel;
	float sensor_X;

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(inflater, container, savedInstanceState);
		readyRes();
		initText();
		getActivity().getActionBar().setTitle("成绩查询");
		proLay = new LoadingProgressView(context, 8);
		sm = (SensorManager) getActivity().getSystemService(
				getActivity().SENSOR_SERVICE);
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// 添加重力感应侦听，并实现其方法，
		sel = new SensorEventListener() {
			public void onSensorChanged(SensorEvent se) {
				sensor_X = se.values[SensorManager.DATA_X];
			}

			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}
		};
		// 注册Listener，SENSOR_DELAY_GAME为检测的精确度，
		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);
//		new Thread() {
//			int d = 0; // 0 is inc , 1 is de
//			public void run() {
//				waterRefresh = true;
//				while (waterRefresh) {
//					Log.i("SenSor", "" + sensor_X);
//					handler.post(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							((CircleView) circV)
//									.changeLine((int) (sensor_X * 180 / 10 + 270));
//						}
//					});
//
//					try {
//						Thread.sleep(50);
//						if(d == 0)time++;
//						else if(d == 1)time --;
//						if(time > 180)d=1;
//						else if(time < 20)d=0;
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}.start();

		vg = new RelativeLayout(context);
		vp = new ViewPager(context);
		rl = new RelativeLayout(context);
		rl.setBackgroundColor(Color.WHITE);
		rl.addView(vg);
		circV = new CircleView(context);

		rl.addView(circV);
		rl.addView(llTexts);
		rl.addView(vp);
		rl.setOnTouchListener(new PointTouchListener());

		// 显示参数，必须在addView之后调用
		// ------------------------------------
		((RelativeLayout.LayoutParams) llTexts.getLayoutParams()).topMargin = (int) (DensityUtil
				.dip2px(context, 90) - DensityUtil.getActBarHeight(context));
		Paint paint = new Paint();
		paint.setTextSize(DensityUtil.sp2px(context, 12));
		int width = (int) paint.measureText("2013-2014学年 第二学期");

		((RelativeLayout.LayoutParams) llTexts.getLayoutParams()).leftMargin = (int) (DensityUtil
				.getWidth(context) - DensityUtil.dip2px(context, 32) - width);

		((RelativeLayout.LayoutParams) vp.getLayoutParams()).width = RelativeLayout.LayoutParams.MATCH_PARENT;
		((RelativeLayout.LayoutParams) vp.getLayoutParams()).topMargin = (int) (DensityUtil
				.dip2px(context, 276) - DensityUtil.getActBarHeight(context));
		// ------------------------------------

		// 本地数据
		String info = getInfo();
		Log.i("INFO", info);
		if (getPassWord() == null || getPassWord().equals("")) {
			errorOnEpassClick();
		}
		else if (info != null && !info.equals(""))
			try {
				upData(new JSONObject(getInfo()));
				upUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		// 网络获取数据
		else
			new UpDataFromNet().start();
		vp.setBackgroundColor(Color.WHITE);
		return rl;
	}

	private void pointNotNowPage(PointView v, int p) {
		v.setNotCurrentPage();

		// //初始化
		// Animation scaleAnimation = new ScaleAnimation(1.6f,
		// 1.0f,1.6f,1.0f,Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// if(p2Ls[p]!=null)
		// p2Ls[p].cancel();
		// p2Ls[p] = scaleAnimation;
		// scaleAnimation.setFillAfter(true);
		//
		// //设置动画时间
		// scaleAnimation.setDuration(500);
		// v.startAnimation(scaleAnimation);

	}

	private void pointToNowPage(final PointView v, final int p) {
		v.setCurrentPage();
		// //初始化
		// Animation anim = new ScaleAnimation(1.0f,
		// 1.6f,1.0f,1.6f,Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// pSta[p] = 1;
		// if(p2Ls[p]!=null)
		// p2Ls[p].cancel();
		// p2Ls[p] = anim;
		// //设置动画时间
		// anim.setDuration(500);
		// anim.setFillAfter(true);
		//
		// v.startAnimation(anim);
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

	private void saveInfo(String s) {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("gradeInfo", s).commit();
	}

	private void savePassWord(String ps) {
		SharedPreferences sp = context.getSharedPreferences("AnSP",
				Context.MODE_PRIVATE);
		sp.edit().putString("STUPASS", ps).commit();
	}

	private void upData(JSONObject jsonObject) {
		xqjdList = new ArrayList<Double>();
		data = getGrade(jsonObject);
		for (int i = 0; i < data.size(); i++) {
			List<List<String>> a = data.get(i);
			double xqxf = 0, xqjd = 0;
			for (List<String> s : a) {
				xqxf += Double.valueOf(s.get(3));
				xqjd += Double.valueOf(s.get(4)) * Double.valueOf(s.get(3));
			}
			xqjdList.add(xqjd / xqxf);
		}
		pSta = new int[xqjdList.size()];
		p2Ls = new Animation[xqjdList.size()];
		p2Ms = new Animation[xqjdList.size()];
		getPointPos();
		pv = new PointView[xqjdList.size()];
		for (int i = 0; i < xqjdList.size(); i++) {
			pv[i] = new PointView(context);
			pv[i].setXY(jdWs[i], jdHs[i]);
		}
		initListView(data);
		vp.setAdapter(new ViewPagerAdapter(lists));
		vp.setBackgroundColor(Color.WHITE);
		vp.setOnPageChangeListener(new PageChangeLis());
	}

	private void upNowP() {
		txq.setText(data.get(nowPage).get(0).get(0) + "学年 第"
				+ data.get(nowPage).get(0).get(1) + "学期");
		String pm = "暂无数据";
		if (pmList != null)
			pm = pmList.get(nowPage);
		tpm.setText("班级排名：" + pm + "\n" + "该学期绩点："
				+ new DecimalFormat(".##").format(xqjdList.get(nowPage)));
	}

	private void upUI() {
		// TODO Auto-generated method stub
		if (pv.length != 0)
			pointToNowPage(pv[0], 0);
		vg.removeView(lineV);
		// 这是为了保证在线程运行时用户推出了 不出现bug
		if (context == null)
			return;
		lineV = new LineView(context);
		vg.addView(lineV);
		circV.invalidate();
		for (int i = 0; i < jdWs.length; i++) {
			rl.addView(pv[i]);
			RelativeLayout.LayoutParams lp = (LayoutParams) pv[i]
					.getLayoutParams();
			lp.width = (int) DensityUtil.dip2px(context, 10);
			lp.height = (int) DensityUtil.dip2px(context, 10);
			lp.leftMargin = jdWs[i] - (int) DensityUtil.dip2px(context, 5);
			lp.topMargin = jdHs[i] - (int) DensityUtil.dip2px(context, 5);
		}
		txq.setText("x");
		// tpm.setText("班级排名：" + "x");
		tpm.setText("");

		double jd = Double.valueOf(pjjd);
		// 3.50-4.00 学霸！请收膝盖！
		// 3.00-3.49 你是最棒哒！
		// 2.50-2.99 相信自己一定行！
		// 2.00-2.49 超越自己！加油！
		// 1.00-1.99 同学！要加油咯！
		String wish = "同学！要加油咯！";
		if (jd > 2)
			wish = "超越自己！加油！";
		if (jd > 2.5)
			wish = "相信自己一定行！";
		if (jd > 3)
			wish = "你是最棒哒！";
		if (jd > 3.5)
			wish = "学霸！请收膝盖！";
		twish.setText(wish);
		upNowP();
	}

	@Override
	public void onPause() {
		MobclickAgent.onPageStart(PluginListener.myNAME);
		waterRefresh = false;
		sm.unregisterListener(sel);
		super.onPause();
	}

	@Override
	public void onResume() {
		MobclickAgent.onPageStart(PluginListener.myNAME);
		super.onResume();
	}

}
