package com.doschool.aa.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.activity.Act_Wele.getInfoTask;
import com.doschool.aa.item.Blog_Item;
import com.doschool.aa.item.Item_FriendsListView;
import com.doschool.aa.item.Item_PeopleListView;
import com.doschool.aa.item.Item_RequestListView;
import com.doschool.aa.widget.CircleVirticle;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.FriendRequest;
import com.doschool.entity.Microblog;
import com.doschool.entity.Person;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Adp_Request extends Adp_Standard implements OnClickListener {
	private ArrayList<FriendRequest> requestDataSet; 
	private Context context;
	Adp_Request thisSelf;
	
	private final static int ID_YES = 3;
	private final static int ID_NO = 2;
	
	public Adp_Request(Context context,ArrayList<FriendRequest> friendsDataSet){
		thisSelf=this;
		this.context = context;
		this.requestDataSet = friendsDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		  if (convertView == null) {
			  Item_RequestListView A =new Item_RequestListView(context);
		    	convertView =A;
		     }
		     ((Item_RequestListView) convertView).setDataRefresh(requestDataSet.get(position), mBusy);
		    
		     if(requestDataSet.get(position).state==1)
				{
		    	 ((Item_RequestListView) convertView).btYes.setId(ID_YES);
		    	 ((Item_RequestListView) convertView).btYes.setTag(requestDataSet.get(position).requestId);
		    	 ((Item_RequestListView) convertView).btYes.setOnClickListener(this);
		    	 ((Item_RequestListView) convertView).btNo.setId(ID_NO);
		    	 ((Item_RequestListView) convertView).btNo.setTag(requestDataSet.get(position).requestId);
		    	 ((Item_RequestListView) convertView).btNo.setOnClickListener(this);
				}
		     

		     if(position==0)
		     {
		    	 convertView.setPadding(0, DoschoolApp.pxperdp*36, 0, 0);
		     }
		     else
		     {

		    	 convertView.setPadding(0, DoschoolApp.pxperdp*0, 0, 0);
		     }
		     return convertView;
		
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return requestDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return requestDataSet.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public void onClick(View v) {
		switch((Integer)v.getId())
		{
		case ID_YES:
			new LoginTask((Integer) v.getTag(), ID_YES).execute();
			break;
		case ID_NO:
			new LoginTask((Integer) v.getTag(), ID_NO).execute();
			break;
		}
		
	}
	
	//-2-登陆
		public class LoginTask extends AsyncTask<Void, Void, MJSONObject> {
			
			ProgressDialog progressDialog;
			int applyId,handleCode;
			
			public LoginTask(int applyId,int handleCode)
			{
				this.applyId=applyId;
				this.handleCode=handleCode;
			}
			
			protected void onPreExecute() {
				//转圈Dialogue
				progressDialog=new ProgressDialog(context);
				progressDialog.setMessage("正在处理好友关系!");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
			
			@Override
			protected MJSONObject doInBackground(Void... params) {
				MJSONObject jObj = DoUserSever.UserApplyHandle(applyId, handleCode);
				return jObj;
			}
			@Override
			protected void onPostExecute(MJSONObject result) {
				//获取状态值
				int code =result.getInt("code",9);
			
				//处理各状态
				if (code == 0)		
				{
					for(int i=0;i<requestDataSet.size();i++)
					{
						if(requestDataSet.get(i).requestId==applyId)
							requestDataSet.get(i).state=handleCode;
					}
					thisSelf.notifyDataSetInvalidated();
					
				}
				else
				{
					DoMethods.showToast(context, "网络出错了");
					
				}
				progressDialog.cancel();
			}
		}
	
	
}
