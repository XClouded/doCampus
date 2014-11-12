package com.doschool.aa.activity.personpage;

import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.activity.Act_PersonPage;
import com.doschool.aa.activity.Act_Wele;
import com.doschool.aa.widget.MyDialog;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Person;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OperateGridview extends GridView {

	//存放各功能图片  
    private ArrayList<String> operateStrList;
    Person personData;
	protected int parentWidth = DoschoolApp.widthPixels;
	protected int padding = DoschoolApp.pxperdp;
	Act_PersonPage act;
	
	public OperateGridview(Context context) {
		super(context);
		this.act=(Act_PersonPage) context;
        this.setNumColumns(2);
    }  
	
	public void setAdapter(ArrayList<String> operateStrList)
	{
		this.operateStrList=operateStrList;
		this.setAdapter(new ImageAdapter(getContext(),operateStrList));
	}
	
	public void callRefresh(Person pData)
	{
		operateStrList=new ArrayList<String>();
		this.personData=pData;
		if(personData.isMySelf==true)
		{
			operateStrList.add("我要注销");
			operateStrList.add("修改资料");
			
		}else
		{
		
			if(personData.cardState==-100)
				operateStrList.add("发送名片No");
			else if(personData.doISendCard)
				operateStrList.add("已发名片");
			else
				operateStrList.add("发送名片");
			
			if(personData.friendState==-100)
				operateStrList.add("好友申请No");
			else if(personData.isMyFriend)
				operateStrList.add("删除好友");
			else if(personData.doISendFriendRequest)
				operateStrList.add("已发申请NO");
			else
				operateStrList.add("好友申请");
			

			if(personData.isMyCard)
				operateStrList.add("查看名片");
			else
				operateStrList.add("查看名片No");
			
			if(personData.isMyFriend)
				operateStrList.add("发小纸条");
			else
				operateStrList.add("发小纸条No");
			
			
			
			
		}
		this.setAdapter( operateStrList);
		
		
	}
	
class ImageAdapter extends BaseAdapter{
	
    private Context mContext;
    ArrayList<String> operateStrList;
    
    public ImageAdapter(Context c,ArrayList<String> operateStrList){  
        mContext = c;
        this.operateStrList=operateStrList;
    }  
    @Override  
    public int getCount() {  
        return operateStrList.size();  
    }  

    @Override  
    public Object getItem(int position) {  
        return position;  
    }  

    @Override  
    public long getItemId(int position) {  
        return position;  
    }  

    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {

    	if(convertView==null)
    	{
    		LinearLayout llOuter=new LinearLayout(mContext);
        	llOuter.setGravity(Gravity.CENTER);
        	llOuter.setLayoutParams(new LayoutParams((int) (parentWidth*0.5), (int) (parentWidth*0.15)));
        		Button btOperate=WidgetFactory.createButton(mContext, operateStrList.get(position).substring(0, 4), R.drawable.btn_style_personpage_operate, 0, 16);

        		btOperate.setTag(operateStrList.get(position));
        		btOperate.setLayoutParams(new LayoutParams((int) (parentWidth*0.48), (int) (parentWidth*0.13)));
        		llOuter.addView(btOperate);
        	convertView=llOuter;
        	
        	if(operateStrList.get(position).length()>4)
        	{
        		btOperate.setTextColor(Color.GRAY);
        		btOperate.setBackgroundResource(R.drawable.btn_style_personpage_operate_unclick);
        	}
        	
        	btOperate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(DoschoolApp.isGuest())
					{

						MyDialog.popURGuest(getContext());
						return;
					}
				
					
					
					
					Button btOperate=(Button)v;
					String tag=(String) btOperate.getTag();
					Log.v(""+tag, "btOperate.getTag()");
					if(tag.equals("发小纸条"))
						act.popSendScripDialog();
					else if(tag.equals("发小纸条No"))
						DoMethods.showToast(act, "你们不是好友，不能发纸条哦");
					
					else if(tag.equals("删除好友"))
						act.popFriendRequestDialog();
					else if(tag.equals("好友申请"))
						act.popFriendRequestDialog();
					else if(tag.equals("已发申请"))
						DoMethods.showToast(act, "你已经发过好友请求，请等待对方操作");
					
					else if(tag.equals("发送名片"))
						act.popCardSendDialog();
					else if(tag.equals("已发名片"))
						DoMethods.showToast(act, "你已经向Ta发过名片了");
					
					else if(tag.equals("查看名片"))
						MyDialog.popHdHeadDialog(act, personData);
					else if(tag.equals("查看名片No"))
						DoMethods.showToast(act, "你没有他的名片，所以不能查看");
					else if(tag.equals("修改资料"))
					{
						act.startActivityForResult(new Intent(act, Act_EditInfo.class),ActivityName.CODE_ACT_EDITINFO);
						act.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					}
					else if(tag.equals("我要注销"))
					{
						
						new AlertDialog.Builder(getContext()).setTitle("注销").setNegativeButton("取消", null)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								SpMethods.saveString(getContext(), "LAST_USER_FUNID","");
								NotificationManager mNotificationManager = (NotificationManager) getContext()
								.getSystemService(Context.NOTIFICATION_SERVICE);
								mNotificationManager.cancelAll();

								MobclickAgent.onEvent(getContext(), "event_setting_logout");
								User.clearAutoLoginInfo(getContext());
								DoschoolApp.mDBHelper.removeAll();
								getContext().startActivity(new Intent(getContext(), Act_Wele.class));
								((Activity) getContext()).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
								((Activity) getContext()).finish();
								
							}
						}).show();
					}
				}
			});
    	}
    	else
    	{
    		Button btOperate=(Button) ((LinearLayout)convertView).getChildAt(0);
    		btOperate.setText(operateStrList.get(position).substring(0, 4));
    		btOperate.setTag(operateStrList.get(position));
    	}
    		
        	
        return convertView; 
    }  
      
}  
}
