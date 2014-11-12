package com.doschool.aa.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.doschool.aa.item.Item_MessageListView;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Message;

public class Adp_Messages extends Adp_Standard {
	private ArrayList<Message> MessageDataSet; 
	private Context context;
	
	public Adp_Messages(Context context,ArrayList<Message> MessageDataSet){
		this.context = context;
		this.MessageDataSet = MessageDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			Item_MessageListView A =new Item_MessageListView(context);
	    	convertView =A;
	     }
	     ((Item_MessageListView) convertView).setDataRefresh(MessageDataSet.get(position), mBusy);
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
		return MessageDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MessageDataSet.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
