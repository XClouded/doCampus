//package com.doschool.aa.activity;
//
//import com.doschool.R;
//import com.doschool.aa.widget.MyDialog;
//import com.doschool.aa.widget.SlidingButton;
//import com.doschool.app.DoschoolApp;
//import com.doschool.tasklistener.Click_Person;
//import com.umeng.analytics.MobclickAgent;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.ImageView;
//import android.widget.Toast;
//import android.widget.ImageView.ScaleType;
//import android.widget.LinearLayout;
//
//public class Fgm_SlideMenu extends Fragment implements OnClickListener {
//
//	private final static String TAG="Dong_Fgm_SlideMenu";
//	private final static boolean DEBUG = true;
//	
//	protected int parentWidth = DoschoolApp.widthPixels;
//	protected int parentHeight = DoschoolApp.heightPixels;
//	protected int mDip = DoschoolApp.pxperdp;
//	
//	protected final static int ID_BLOG = 0;
//	protected final static int ID_FRIEND = 1;
//	protected final static int ID_MSG = 2;
//	protected final static int ID_TOOL = 3;
//	protected final static int ID_SETTING = 4;
//	
//	
//	LinearLayout llParent;
//	ImageView headLayout;
//	LinearLayout MenuGroup;
//	SlidingButton ibtBlog,ibtFriend,ibtMsg,ibtSetting,ibtTool;
//	SlidingButton[] slbList;
//	
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//		if(DEBUG)Log.v(TAG, "onCreateView");
//		
//		llParent=new LinearLayout(getActivity());
//		llParent.setOrientation(LinearLayout.VERTICAL);
//		llParent.setGravity(Gravity.CENTER);
//		llParent.setBackgroundResource(R.drawable.ssb_below_head);
//		
//		LinearLayout llHead=new LinearLayout(getActivity());
//		llHead.setBackgroundResource(R.drawable.ssb_below_head);
//		llHead.setOrientation(LinearLayout.VERTICAL);
//		llHead.setGravity(Gravity.CENTER);
//		llParent.addView(llHead,LayoutParams.MATCH_PARENT,mDip*80);
//	
//				headLayout=new ImageView(getActivity());
//				headLayout.setScaleType(ScaleType.FIT_XY);
//				DoschoolApp.newImageLoader.displayImage(DoschoolApp.thisUser.headUrl, headLayout, DoschoolApp.dioRound);
//				Click_Person listener=new Click_Person(getActivity(), DoschoolApp.thisUser);
//				headLayout.setOnClickListener(listener);
//				llHead.addView(headLayout,mDip*48,mDip*48);
//				
//
//		MenuGroup=new LinearLayout(getActivity());
//		MenuGroup.setOrientation(LinearLayout.VERTICAL);
//		MenuGroup.setGravity(Gravity.CENTER);
//		llParent.addView(MenuGroup,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);	
//		
//
//		ibtBlog=new SlidingButton(getActivity(), R.drawable.ssb_blog);
//		ibtBlog.setId(ID_BLOG);
//		ibtBlog.setOnClickListener(this);
//		MenuGroup.addView(ibtBlog, LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//		
//		ibtFriend=new SlidingButton(getActivity(), R.drawable.ssb_friend);
//		ibtFriend.setId(ID_FRIEND);
//		ibtFriend.setOnClickListener(this);
//		MenuGroup.addView(ibtFriend, LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//		
//		
//		ibtMsg=new SlidingButton(getActivity(), R.drawable.ssb_msg);
//		ibtMsg.setId(ID_MSG);
//		ibtMsg.setOnClickListener(this);
//		MenuGroup.addView(ibtMsg, LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//		
//		
//		ibtTool=new SlidingButton(getActivity(), R.drawable.ssb_tool);
//		ibtTool.setId(ID_TOOL);
//		ibtTool.setOnClickListener(this);
//		MenuGroup.addView(ibtTool, LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//		
//		ibtSetting=new SlidingButton(getActivity(), R.drawable.ssb_setting);
//		ibtSetting.setId(ID_SETTING);
//		ibtSetting.setOnClickListener(this);
//		MenuGroup.addView(ibtSetting, LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//		
//		slbList=new SlidingButton[5];
//		slbList[0]=ibtBlog;
//		slbList[1]=ibtFriend;
//		slbList[2]=ibtMsg;
//		slbList[3]=ibtTool;
//		slbList[4]=ibtSetting;
//		slbList[getFragmentIndex()].showLeftLine();
//		
//		
//		return llParent;
//	}
//	
//	public zAct_Main getMainActivity()
//	{
//		return (zAct_Main) getActivity();
//		
//	}
//	
//	
//	public void callRefresh()
//	{
//		DoschoolApp.newImageLoader.displayImage(DoschoolApp.thisUser.headUrl, headLayout, DoschoolApp.dioRound);
//	}
//	
//	public void onResume() {
//	    super.onResume();
//	    callRefresh();
//	}
//
//	@Override
//	public void onClick(View v) {
//		int order=v.getId();
//			if(DoschoolApp.isGuest())
//			{
//				if(order==1 || order==2)
//				{
//
//					MyDialog.popURGuest(getActivity());
//				return;
//				}
//			}
//		getMainActivity().ShowWhichPage(order, 0);
//		
//		getActivity().invalidateOptionsMenu();
//		
//		
//		slbList[getFragmentIndex()].hideLeftLine();
//		getMainActivity().getSlidingMenu().showContent();
//		setFragmentIndex(order);
//		slbList[order].showLeftLine();
//		
//		switch(order)
//		{
//		case 0:
//			MobclickAgent.onEvent(getActivity(), "event_smenu_blog");
//			break;
//		case 1:
//			MobclickAgent.onEvent(getActivity(), "event_smenu_friend");
//			break;
//		case 3:
//			MobclickAgent.onEvent(getActivity(), "event_smenu_msg");
//			break;
//		case 4:
//			MobclickAgent.onEvent(getActivity(), "event_smenu_tool");
//			break;
//		case 5:
//			MobclickAgent.onEvent(getActivity(), "event_smenu_setting");
//			break;
//		}
//		
//		
//		
//		getMainActivity().refreshTaskBar();
//	}
//
//
//	
//	
//	
//	private int setFragmentIndex(int index) {
//		return getMainActivity().fragmentIndex=index;
//	}
//
//	private int getFragmentIndex() {
//		return getMainActivity().fragmentIndex;
//	}
//}
