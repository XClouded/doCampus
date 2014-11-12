package com.doschool.aa.activity;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.R;
import com.doschool.aa.widget.CircleVirticle;
import com.doschool.aa.widget.FixedSpeedScroller;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.User;
import com.doschool.message.MessageReceiver;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

/**
 * 欢迎页 完成度：99%
 * 
 * @author 是我的海
 * 
 */
public class Act_Wele extends Activity implements OnClickListener {

	int leftMidrighr = 0;

	int currentPage;
	boolean isPageChangedByTouch = false;

	/******** 尺寸控制 ****************************************/
	protected int parentHeight;
	protected int parentWidth;
	protected int mDip;

	/******** 常量标识 ****************************************/
	private static final int ID_LOGIN = 0;
	private static final int ID_REGISTER = 1;
	private static final int ID_GUEST = 2;
	/******** 控件 ****************************************/
	RelativeLayout llRegLog;
	CircleVirticle loadingCircle;
	ViewPager vpGallery;
	Button btLogin, btReg, btGuest;

	/******** 其他 ****************************************/
	FixedSpeedScroller scroller;

	ArrayList<String> strList = new ArrayList<String>();
	ArrayList<Integer> ridList = new ArrayList<Integer>();
	ArrayList<ImageView> ivBackList = new ArrayList<ImageView>();
	ArrayList<ImageView> ivPicList = new ArrayList<ImageView>();
	ImageView ivBackCurrent;
	ImageView ivPicCurrent;

	protected void onCreate(Bundle savedInstanceState) {
		
		Log.v("Dong", this.getClass().getSimpleName()+"_"+"onCreate");
		
		long start = System.currentTimeMillis();

		// 安卓主题的初始化
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);

		// 友盟的初始化
		MobclickAgent.openActivityDurationTrack(false);
		new FeedbackAgent(getApplicationContext()).sync();

		super.onCreate(savedInstanceState);

		

		// 登陆还是直接进入，这是一个问题
		try {
			DoschoolApp.thisUser = new User(new MJSONObject(User.loadUserInfo(getApplicationContext())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (System.currentTimeMillis() - User.loadLastLoginTime(getApplicationContext()) < 1000 * 60 * 60 * 24 * 7 && DoschoolApp.thisUser.personId > 0) {

			new RefreshCardListTask().execute();
			
		} else {
			InitUI();

			String funId=User.loadFunId(getApplicationContext());
			String password=User.loadPassword(getApplicationContext());
			if (funId.length() > 0) // 自动登录
				new LoginTask(funId, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			else {
				// 2-2手工登录
				showLoading(false, "");
			}
			new NextBackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		
		
	}


    public class RefreshCardListTask extends AsyncTask<Void, Void, Void> {
    	private PopupWindow postBlogPopWindow;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            publishProgress(null);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
		protected void onProgressUpdate(Void... values) {
        	View postBlogPopView = LayoutInflater.from(Act_Wele.this).inflate(R.layout.main_oncreate_popwindow, null);
    		postBlogPopWindow = new PopupWindow(postBlogPopView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
    		try {
				postBlogPopWindow.showAtLocation(Act_Wele.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    		super.onProgressUpdate(values);
		}

		@Override
        protected void onPostExecute(Void result) {
			startActivity(new Intent(Act_Wele.this, zAct_Main.class));
			finish();
            super.onPostExecute(result);
        }
    }

	/**
	 * 
	 */
	public void InitUI() {
		// 父框架的设置
		parentWidth = DoschoolApp.widthPixels;
		parentHeight = DoschoolApp.heightPixels;
		mDip = DoschoolApp.pxperdp;
		RelativeLayout llParent = new RelativeLayout(this);
		llParent.setBackgroundResource(R.color.light_blue_jia);
		addContentView(llParent, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// 幻灯的设置
		strList.add("属于你我的,\n安大人自己的软件。");
		strList.add("奇葩的你，\n我们做朋友吧。");
		strList.add("学渣VS学霸，\n认真的你萌萌哒！");
		strList.add("拥抱小安,\n享受极致生活。");
		ridList.add(R.drawable.wel1);
		ridList.add(R.drawable.wel2);
		ridList.add(R.drawable.wel3);
		ridList.add(R.drawable.wel4);

		for (int i = 0; i < strList.size(); i++) {
			RelativeLayout ll = new RelativeLayout(this);
			llParent.addView(ll, LinearLayout.LayoutParams.MATCH_PARENT, parentHeight - mDip * 152);

			ImageView ivBack = new ImageView(this);
			ivBack.setVisibility(View.GONE);
			ivBack.setScaleType(ScaleType.FIT_XY);
			ivBackList.add(ivBack);
			ivBack.setImageResource(R.drawable.black_square);
			ivBack.setColorFilter(Color.argb(100, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
			// ll.addView(ivBack, LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.MATCH_PARENT);

			ImageView iv = new ImageView(this);
			iv.setVisibility(View.GONE);
			ivPicList.add(iv);
			InputStream is = getResources().openRawResource(ridList.get(i));
			Bitmap bm = BitmapFactory.decodeStream(is);
			iv.setImageBitmap(bm);
			iv.setScaleType(ScaleType.CENTER_INSIDE);
			iv.setPadding((int) (parentWidth * 0.1), 0, (int) (parentWidth * 0.1), 0);
			ll.addView(iv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

		}

		ivPicCurrent = ivPicList.get(0);
		ivBackCurrent = ivBackList.get(0);
		ivPicCurrent.setVisibility(View.VISIBLE);
		ivBackCurrent.setVisibility(View.VISIBLE);

		vpGallery = new ViewPager(this);
		vpGallery.setOffscreenPageLimit(3);
		llParent.addView(vpGallery, parentWidth, LayoutParams.MATCH_PARENT);
		vpGallery.setId("vp".hashCode());
		vpGallery.setOnPageChangeListener(new PageChangeListener());
		vpGallery.setAdapter(new ListViewPagerAdapter());
		vpGallery.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isPageChangedByTouch = true;
				scroller.setmDuration(200);
				return false;
			}
		});

		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			scroller = new FixedSpeedScroller(vpGallery.getContext(), new AccelerateInterpolator());
			field.set(vpGallery, scroller);
			scroller.setmDuration(800);
		} catch (Exception e) {
		}

		// Logo
		ImageView mtvMissAn = new ImageView(this);
		mtvMissAn.setImageResource(R.drawable.img_missan);
		mtvMissAn.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mDip * 272, mDip * 100);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.setMargins(0, mDip * 22, 0, 0);
		llParent.addView(mtvMissAn, lp);

		// 注册登陆
		llRegLog = new RelativeLayout(this);
		llRegLog.setBackgroundColor(Color.WHITE);
		llRegLog.setPadding((int) (parentWidth*0.0611), (int) (parentWidth*0.0611), (int) (parentWidth*0.0611), (int) (parentWidth*0.0611));
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llParent.addView(llRegLog, lp2);

		
		btLogin = WidgetFactory.createButton(this, "", R.drawable.btn_style_wel_logreg, android.R.color.white, 0);
		btLogin.setBackgroundResource(R.drawable.btn_style_login);
		btLogin.setId(ID_LOGIN);
		RelativeLayout.LayoutParams lpLogin = new RelativeLayout.LayoutParams((int) (parentWidth*0.4777), (int) (parentWidth*0.2222));
		lpLogin.addRule(RelativeLayout.CENTER_VERTICAL);
		llRegLog.addView(btLogin, lpLogin);

		btReg = WidgetFactory.createButton(this, "", R.drawable.btn_style_wel_logreg, android.R.color.white, 0);
		btReg.setId(ID_REGISTER);
		btReg.setBackgroundResource(R.drawable.btn_style_reg);
		RelativeLayout.LayoutParams lpReg = new RelativeLayout.LayoutParams((int) (parentWidth*0.3388), (int) (parentWidth*0.1055));
		lpReg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		llRegLog.addView(btReg, lpReg);

		btGuest = WidgetFactory.createButton(this, "", R.drawable.btn_style_wel_logreg, android.R.color.white, 0);

		btGuest.setBackgroundResource(R.drawable.btn_style_guest);
		btGuest.setId(ID_GUEST);
		RelativeLayout.LayoutParams lpGuest = new RelativeLayout.LayoutParams((int) (parentWidth*0.3388), (int) (parentWidth*0.1055));
		lpGuest.setMargins(0, (int) (parentWidth*0.0111), 0, 0);
		lpGuest.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpGuest.addRule(RelativeLayout.BELOW, ID_REGISTER);

		llRegLog.addView(btGuest, lpGuest);

		loadingCircle = new CircleVirticle(this);
		loadingCircle.mtvLoading.setTextColor(getResources().getColor(R.color.dark_blue));
		loadingCircle.ivLoading.setColorFilter(getResources().getColor(R.color.dark_blue));
		loadingCircle.setPadding(0, (int) (parentWidth * 0.1), 0, (int) (parentWidth * 0.2));
		loadingCircle.setOrientation(LinearLayout.HORIZONTAL);
		loadingCircle.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		 llParent.addView(loadingCircle, LayoutParams.MATCH_PARENT,
		 LayoutParams.MATCH_PARENT);

		// 设置监听器
		btLogin.setOnClickListener(this);
		btReg.setOnClickListener(this);
		btGuest.setOnClickListener(this);
	}

	/**** Viewpager适配器 **********/
	class ListViewPagerAdapter extends PagerAdapter {

		// 这个方法决定了ViewPager每个页面显示的东西
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			LinearLayout llPage = new LinearLayout(Act_Wele.this);
			llPage.setGravity(Gravity.BOTTOM);

		TextView mtvWords = WidgetFactory.createTextView(Act_Wele.this, strList.get(position), R.color.dark_blue, 20);

			mtvWords.setPadding(mDip * 22, 0, 0, mDip * (124 + 11));
			llPage.addView(mtvWords, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			container.addView(llPage, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return llPage;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getCount() {
			return strList.size();
		}
	}

	public void showLoading(boolean showLoading, String text) {
		if (showLoading) {
			loadingCircle.setText(text);
			loadingCircle.setVisibility(View.VISIBLE);
			llRegLog.setVisibility(View.GONE);
		} else {
			loadingCircle.setVisibility(View.GONE);

			llRegLog.setVisibility(View.VISIBLE);
		}
	}

	// -2-登陆
	public class LoginTask extends AsyncTask<Void, Void, Void> {
		String acctount, password;
		MJSONObject jResult;

		public LoginTask(String account, String password) {
			this.acctount = account;
			this.password = password;
		}

		protected void onPreExecute() {
			// 转圈加载
			showLoading(true, "正在登录");
		}

		@Override
		protected Void doInBackground(Void... params) {
			jResult = DoUserSever.UserLogin(acctount, password);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			

			MobclickAgent.onEvent(Act_Wele.this, "event_wel_logintask_start");
			int code = jResult.getInt("code", 9);
			String toast = jResult.getString("data");
			if (code == 0) {
				
				MobclickAgent.onEvent(Act_Wele.this, "event_wel_logintask_succ");
				User.saveLoginInfo(getApplicationContext(), acctount, password, System.currentTimeMillis());
				new getInfoTask(jResult.getInt("data", 0)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {

				MobclickAgent.onEvent(Act_Wele.this, "event_wel_logintask_fail");
				if (toast.length() == 0) {
					if (code == 1)
						toast = "用户不存在";
					else if (code == 2)
						toast = "用户名或密码不正确";
					else if (code == 9)
						toast = "网络服务错误";
				}
				DoMethods.showToast(Act_Wele.this, toast);
				if(acctount.equals("CU002"))
				{
					showLoading(false, "");
				}
				else
				{

					isPageChangedByTouch = true;
					startActivityForResult(new Intent(Act_Wele.this, Act_Login.class), 1);
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				}
				
			}

		}
	}

	// -3-拉取用户信息和好友列表
	public class getInfoTask extends AsyncTask<Void, Void, Boolean> {
		int userId;

		public getInfoTask(int userId) {
			this.userId = userId;
		}

		protected void onPreExecute() {
			showLoading(true, "正在获取用户信息");

		}

		@Override
		protected Boolean doInBackground(Void... params) {


			// 拉取用户信息
			MJSONObject myInfoJson = DoUserSever.UserCompleteInfoGet(userId, userId);
			int code = myInfoJson.getInt("code", 9);
			if (code == 0) {
				DoschoolApp.thisUser = new User(myInfoJson.getMJSONObject("data"));
				User.saveUserInfo(getApplicationContext(), myInfoJson.getMJSONObject("data").toString());

				return true;
			} else
				return false;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			MobclickAgent.onEvent(Act_Wele.this, "event_wel_getuserinfo_start");
			if (result) {
				MobclickAgent.onEvent(Act_Wele.this, "event_wel_getuserinfo_succ");
				// 万事俱备，进入主界面
				Intent intent = null;
				intent = new Intent(Act_Wele.this, zAct_Main.class);
				intent.putExtra("type", getIntent().getIntExtra("type", -1));
				startActivity(intent);
				overridePendingTransition(R.anim.biggerin, android.R.anim.fade_out);
				finish();
			} else {
				MobclickAgent.onEvent(Act_Wele.this, "event_wel_getuserinfo_fail");
				// 失败了
				User.clearAutoLoginInfo(getApplicationContext());
				DoMethods.showToast(Act_Wele.this, "加载用户信息失败");
				startActivityForResult(new Intent(Act_Wele.this, Act_Login.class), 1);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		}
	}

	public class NextBackgroundTask extends AsyncTask<Void, Integer, Void> {

		int postion;
		boolean right = true;

		@Override
		protected Void doInBackground(Void... params) {
			while (!isPageChangedByTouch) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (right) {
					postion++;
					if (postion == strList.size() - 1) {
						right = false;
					}
				} else {
					postion--;
					if (postion == 0) {
						right = true;
					}
				}

				postion = postion % strList.size();
				publishProgress(1);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			vpGallery.setCurrentItem(postion, true);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {

		}
	}

	public void onClick(View v) {
		switch ((Integer) v.getId()) {
		case ID_LOGIN:
			// 注册按钮
			MobclickAgent.onEvent(this, "event_wel_login");
			isPageChangedByTouch = true;
			startActivityForResult(new Intent(Act_Wele.this, Act_Login.class), 1);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case ID_REGISTER:
			// 注册按钮
			MobclickAgent.onEvent(this, "event_wel_reg");
			isPageChangedByTouch = true;
			startActivityForResult(new Intent(Act_Wele.this, Act_UserCheck.class), 0);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case ID_GUEST:
			MobclickAgent.onEvent(this, "event_wel_guest");
			User.saveLoginInfo(getApplicationContext(), "CU002", "123456", System.currentTimeMillis());
			String funId=User.loadFunId(getApplicationContext());
			String password=User.loadPassword(getApplicationContext());
			if (funId.length() > 0) // 自动登录
				new LoginTask(funId, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			switch (resultCode) {
			case RESULT_CANCELED:
				// 没有注册
				showLoading(false, "");
				break;
			case RESULT_OK:
				// 注册成功
				String funId=User.loadFunId(getApplicationContext());
				String password=User.loadPassword(getApplicationContext());
				if (funId.length() > 0) // 自动登录
					new LoginTask(funId, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				break;
			}
		} else if (requestCode == 1) {
			switch (resultCode) {
			case RESULT_CANCELED:
				// 没有登录
				showLoading(false, "");
				break;
			case RESULT_OK:
				// 进行登陆
				String funId=User.loadFunId(getApplicationContext());
				String password=User.loadPassword(getApplicationContext());
				if (funId.length() > 0) // 自动登录
					new LoginTask(funId, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				break;
			}
		}
	}

	// 背景Viewpager换页监听器
	class PageChangeListener implements OnPageChangeListener {

		float last = -1;
		int lastPage = 0;

		@Override
		public void onPageSelected(int arg0) {
			isPageChangedByTouch = true;
			currentPage = arg0;
			
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (leftMidrighr != currentPage)
				leftMidrighr = currentPage;
			if(arg0==0)
			{

				ivBackCurrent.setAlpha((int) (255.0f));
				for(int i=0;i<ivPicList.size();i++)
				{
					if(i!=currentPage)
					{
						ivPicList.get(i).setAlpha((int) (0.0f));

					}
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (last == -1)
				last = arg1;
			if (arg1 == 0 || arg1 == 1)
				last = -1;

			if (arg1 > 0.5f && last < 0.5f && last > 0) // 向右滑动
			{
				last = -1;
				leftMidrighr++;
				ivBackCurrent.setVisibility(View.GONE);
				ivPicCurrent.setVisibility(View.GONE);
				try {
					ivBackCurrent = ivBackList.get(leftMidrighr);
					ivPicCurrent = ivPicList.get(leftMidrighr);
					ivBackCurrent.setVisibility(View.VISIBLE);
					ivPicCurrent.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (arg1 < 0.5f && last > 0.5f && last > 0) {
				last = -1;
				leftMidrighr--;
				ivBackCurrent.setVisibility(View.GONE);
				try {
					ivBackCurrent = ivBackList.get(leftMidrighr);
					ivPicCurrent = ivPicList.get(leftMidrighr);
					ivBackCurrent.setVisibility(View.VISIBLE);
					ivPicCurrent.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			float alpha = Math.abs(1 - arg1 * 2.0f);
			ivBackCurrent.setAlpha((int) (alpha * 255.0f));
			ivPicCurrent.setAlpha((int) (alpha * 255.0f));
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName()); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		for (int i = 0; i < ivBackList.size(); i++) {
			ImageView iv = (ImageView) ivBackList.get(i);
			BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
			if (bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			iv = (ImageView) ivPicList.get(i);
			bitmapDrawable = (BitmapDrawable) iv.getDrawable();
			if (bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
		}
		System.gc();

	}

}
