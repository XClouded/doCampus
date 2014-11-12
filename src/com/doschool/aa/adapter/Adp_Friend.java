package com.doschool.aa.adapter;

import java.util.ArrayList;

import com.doschool.aa.item.Item_FriendsListView;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.SimplePerson;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Adp_Friend extends Adp_Standard {
	private ArrayList<SimplePerson> friendsDataSet; 
	private Context context;
	
	public Adp_Friend(Context context,ArrayList<SimplePerson> friendsDataSet){
		this.context = context;
		this.friendsDataSet = friendsDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
        if (convertView == null) {
        	Item_FriendsListView A =new Item_FriendsListView(context);
	    	convertView =A;
	     }
        
	     ((Item_FriendsListView) convertView).setDataRefresh(friendsDataSet.get(position));
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
		return friendsDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friendsDataSet.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
