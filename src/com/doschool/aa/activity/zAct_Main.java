package com.doschool.aa.activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.doschool.R;
import com.doschool.aa.widget.MyDialog;
import com.doschool.aa.widget.TaskLayout;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.component.updatelater.IPostLater;
import com.doschool.component.updatelater.PostLaterReceiver;
import com.doschool.component.updatelater.PostLaterService;
import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 主页框架 完成度:99%
 * 
 * @author 是我的海
 * 
 */
public class zAct_Main extends FragmentActivity implements IPostLater {

	private final static String TAG="MAIN";
    private final static boolean DEBUG = true;
    private final int mScrnHeight=DoschoolApp.heightPixels;
    private final int mScrnWidth=DoschoolApp.widthPixels;
    private final int mDip=DoschoolApp.pxperdp;
    private Button[] mTabs;
    private Button btPost;
    private RelativeLayout llTask;
    private TaskLayout taskLayout;
    private boolean isShowUploadLater=true;
    private PostLaterReceiver postLaterReceiver;
    
    protected Fgm_BlogSquare fgmSquare;
    protected Fgm_Communication fgmCommuication;
    protected Fgm_Discover fgmDiscover;
    protected Fgm_Mine fgmMine;
    protected Fragment[] fgmList;
    
    protected int currentIndex = 0;
    Button btSendText,btSendGallery,btSendCamera;
	public PopupWindow postBlogPopWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        //软件升级
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(getApplicationContext());

        //创建界面
        createUI();
        
    }


    private void createUI() { 

        // 3.创建所有Fragment
		setContentView(R.layout.act_main);
        fgmSquare = new Fgm_BlogSquare();
        fgmCommuication = new Fgm_Communication();
        fgmDiscover = new Fgm_Discover();
        fgmMine = new Fgm_Mine();
        fgmList = new Fragment[] { fgmSquare, fgmCommuication, fgmDiscover, fgmMine };
        
        
        mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.tabSquare);
		mTabs[1] = (Button) findViewById(R.id.tabFriends);
		mTabs[2] = (Button) findViewById(R.id.tabMsg);
		mTabs[3] = (Button) findViewById(R.id.tabTool);
		for(int i=0;i<mTabs.length;i++){
			mTabs[i].setTag(i);
		}
		mTabs[0].setSelected(true);
		
		// 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fgmSquare)
        .add(R.id.fragment_container, fgmCommuication)
        .add(R.id.fragment_container, fgmDiscover)
        .add(R.id.fragment_container, fgmMine)
        .hide(fgmCommuication)
        .hide(fgmDiscover)
        .hide(fgmMine)
        .show(fgmSquare)
		.commit();
        
        llTask=(RelativeLayout) findViewById(R.id.llTask);
		if (isShowUploadLater) {
			taskLayout = new TaskLayout(this);
			llTask.addView(taskLayout, mScrnWidth, mDip * 46);
    	}
        
		View postBlogPopView = LayoutInflater.from(this).inflate(R.layout.add_popwindow, null);
		btSendText=(Button) postBlogPopView.findViewById(R.id.btSendText);
		btSendGallery=(Button) postBlogPopView.findViewById(R.id.btSendGallery);
		btSendCamera=(Button) postBlogPopView.findViewById(R.id.btSendCamera);
		
		postBlogPopWindow = new PopupWindow(postBlogPopView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		postBlogPopWindow.setBackgroundDrawable(new BitmapDrawable());

		btPost = (Button) findViewById(R.id.btPost);
    }

    
	public void onPlusClick(View v) {
		Intent intent = new Intent(this, Act_Write.class);
		switch (v.getId()) {
		case R.id.btPost:
			MobclickAgent.onEvent(this, "event_actionbar_writeblog");
			if (DoschoolApp.isGuest()) {
				MyDialog.popURGuest(this);
			} else {
				postBlogPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

				TranslateAnimation upin = new TranslateAnimation(0, 0, -mScrnHeight, 0);
				upin.setDuration(500);
				upin.setFillAfter(true);
				upin.setInterpolator(new OvershootInterpolator(1.1f));
				btSendText.startAnimation(upin);
				
				TranslateAnimation upin2 = new TranslateAnimation(0, 0, -mScrnHeight, 0);
				upin2.setDuration(700);
				upin2.setFillAfter(true);
				upin2.setInterpolator(new OvershootInterpolator(1.1f));
				btSendGallery.startAnimation(upin2);
				
				TranslateAnimation upin3 = new TranslateAnimation(0, 0, -mScrnHeight, 0);
				upin3.setDuration(900);
				upin3.setFillAfter(true);
				upin3.setInterpolator(new OvershootInterpolator(1.1f));
				btSendCamera.startAnimation(upin3);
				
			}
			break;
		case R.id.postBlogPopView:
			MobclickAgent.onEvent(this, "event_actionbar_writeblog");
			postBlogPopWindow.dismiss();
			break;
		case R.id.btSendText:
			MobclickAgent.onEvent(this, "event_click_post_text");
			intent.putExtra("startType", Act_Write.START_TYPE_TEXT);
			startActivityForResult(intent, ActivityName.CODE_ACT_WRITEBLOG);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case R.id.btSendGallery:
			MobclickAgent.onEvent(this, "event_click_post_gallery");
			intent.putExtra("startType", Act_Write.START_TYPE_GALLERY);
			startActivityForResult(intent, ActivityName.CODE_ACT_WRITEBLOG);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case R.id.btSendCamera:
			MobclickAgent.onEvent(this, "event_click_post_cameta");
			intent.putExtra("startType", Act_Write.START_TYPE_CAMERA);
			startActivityForResult(intent, ActivityName.CODE_ACT_WRITEBLOG);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		}
	}
    
    
    
	public void onTabClicked(View view) {
		
		int clickIndex=(Integer) view.getTag();
		if (currentIndex != clickIndex) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fgmList[currentIndex]);
			if (!fgmList[clickIndex].isAdded()) {
				trx.add(R.id.fragment_container, fgmList[clickIndex]);
			}
			trx.show(fgmList[clickIndex]).commit();
		}
		mTabs[currentIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[clickIndex].setSelected(true);
		currentIndex = clickIndex;
	}
    
    
    
    
    /**
     * 预先装载好友列表的信息
     */
    public void initFriends() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先读本地
                try {
                    DoschoolApp.friendList.clear();
                    MJSONArray mjarr = new MJSONArray(SpMethods.loadString(getApplicationContext(), SpMethods.FRIEND_LIST_STR));
                    for (int i = 0; i < mjarr.length(); i++)
                        DoschoolApp.friendList.add(new SimplePerson(mjarr.getMJSONObject(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 如果强制联网刷新 或者 数据集为空
                if (DoschoolApp.friendList.size() == 0) {
                	
                	DoMethods.sleep(3000);
                    MJSONObject result = DoUserSever.UserFriendAndCardListGet(DoschoolApp.thisUser.personId, 0);
                    if (result != null && result.getInt("code", 9) == 0) {
                        // 联网刷新成功
                        MJSONArray mjarr = result.getMJSONArray("data");
                        SpMethods.saveLong(getApplicationContext(), SpMethods.FRIEND_LIST_UPDATE_TIME, System.currentTimeMillis());
                        SpMethods.saveString(getApplicationContext(), SpMethods.FRIEND_LIST_STR, mjarr.toString());
                        DoschoolApp.friendList.clear();
                        for (int i = 0; i < mjarr.length(); i++)
                            DoschoolApp.friendList.add(new SimplePerson(mjarr.getMJSONObject(i)));
                    }
                }
                
                
                
                try {
                    DoschoolApp.cardsList.clear();
                    MJSONArray mjarr = new MJSONArray(SpMethods.loadString(getApplicationContext(), SpMethods.CARD_LIST_STR));
                    for (int i = 0; i < mjarr.length(); i++)
                        DoschoolApp.cardsList.add(new Person(mjarr.getMJSONObject(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 如果强制联网刷新 或者 数据集为空
                if (DoschoolApp.cardsList.size() == 0) {
                	DoMethods.sleep(4000);
                    MJSONObject result = DoUserSever.UserFriendAndCardListGet(DoschoolApp.thisUser.personId, 1);
                    if (result != null && result.getInt("code", 9) == 0) {
                        // 联网刷新成功
                        MJSONArray mjarr = result.getMJSONArray("data");
                        SpMethods.saveLong(getApplicationContext(), SpMethods.CARD_LIST_UPDATE_TIME, System.currentTimeMillis());
                        SpMethods.saveString(getApplicationContext(), SpMethods.CARD_LIST_STR, mjarr.toString());
                        DoschoolApp.cardsList.clear();
                        for (int i = 0; i < mjarr.length(); i++)
                            DoschoolApp.cardsList.add(new Person(mjarr.getMJSONObject(i)));
                    }
                }
                
            }
        }).start();
    }

    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}


	/**
     * 预先装载名片列表的信息
     */
    public void initCards() {
        new Thread(new Runnable() {
            @Override
            public void run() {
            	// 先读本地
                try {
                    DoschoolApp.cardsList.clear();
                    MJSONArray mjarr = new MJSONArray(SpMethods.loadString(getApplicationContext(), SpMethods.CARD_LIST_STR));
                    for (int i = 0; i < mjarr.length(); i++)
                        DoschoolApp.cardsList.add(new Person(mjarr.getMJSONObject(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 如果强制联网刷新 或者 数据集为空
                if (DoschoolApp.cardsList.size() == 0) {
                	DoMethods.sleep(1000);
                    MJSONObject result = DoUserSever.UserFriendAndCardListGet(DoschoolApp.thisUser.personId, 1);
                    if (result != null && result.getInt("code", 9) == 0) {
                        // 联网刷新成功
                        MJSONArray mjarr = result.getMJSONArray("data");
                        SpMethods.saveLong(getApplicationContext(), SpMethods.CARD_LIST_UPDATE_TIME, System.currentTimeMillis());
                        SpMethods.saveString(getApplicationContext(), SpMethods.CARD_LIST_STR, mjarr.toString());
                        DoschoolApp.cardsList.clear();
                        for (int i = 0; i < mjarr.length(); i++)
                            DoschoolApp.cardsList.add(new Person(mjarr.getMJSONObject(i)));
                    }
                }
            }
        }).start();
    }
    


    /**
     * 验证用户账号密码有效性
     */
    public void userIdentify() {
        if (User.loadFunId(getApplicationContext()) == null || User.loadFunId(getApplicationContext()).length() == 0)
            MyDialog.popLogoutDialog(zAct_Main.this);
        else
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String funId = User.loadFunId(getApplicationContext());
                    String password = User.loadPassword(getApplicationContext());

                    MJSONObject result = DoUserSever.UserLogin(funId, password);
                    int code = result.getInt("code", 9);
                    // 处理各状态
                    if (code == 2)
                        // 账号密码错误
                        User.clearAutoLoginInfo(getApplicationContext());
                }
            }).start();
    }

    
    //处理返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
        	if(postBlogPopWindow.isShowing()==true)
        	{
        		postBlogPopWindow.dismiss();
        	}
        	else
        	{
        			moveTaskToBack(false);
        	}
        	return true;
        }
		return false;
    }


	
    @Override
	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
	}
    
	public void onResume() {
		super.onResume();
		// =================== MGG'S PART ===================START
		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "SuzbZRlEGULgnh5X1PrOEm8u");
		// =================== MGG'S PART ===================END
		
		if(postBlogPopWindow.isShowing())
			postBlogPopWindow.dismiss();
		
		if (isShowUploadLater) {
			registerUploadLaterReceiver();
		}
		super.onResume();
        // 友盟统计
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(getClass().getName());
    }

	public void onPause() {
        super.onPause();
		if (isShowUploadLater) {
			unregisterUploadLaterReceiver();
		}
        // 友盟统计
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }


	/**
	 * 注册稍后上传的广播
	 */
	public void registerUploadLaterReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(DoschoolApp.UPLOAD_LATER_ACTION_NAME);
		postLaterReceiver = new PostLaterReceiver(taskLayout, this);
		registerReceiver(postLaterReceiver, myIntentFilter);
	}

	/**
	 * 解除稍后上传的广播
	 */
	public void unregisterUploadLaterReceiver() {
		unregisterReceiver(postLaterReceiver);
	}
	
	@Override
	public void onPostSucceed() {
	}

	@Override
	public void startUploadSevice() {
		Intent startServiceIntent = new Intent(this, PostLaterService.class);
		Bundle bundle = new Bundle();
		bundle.putString("operate", "start");
		startServiceIntent.putExtras(bundle);
		startService(startServiceIntent);
	}
    
}
