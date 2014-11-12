package com.doschool.aa.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.doschool.aa.item.Blog_Item;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Microblog;



import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Adp_Blog extends Adp_Standard {
	private Context context;
	private ArrayList<Microblog> sayDataSet;
	
	HashMap hashMap=new HashMap<Integer, Integer>();
	
	public Adp_Blog(Context context,ArrayList<Microblog> sayDataSet){
		this.context = context;
		this.sayDataSet = sayDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	     if (convertView == null) {
	    	Blog_Item A =new Blog_Item(context);
	    	convertView =A;
	     }
	     ((Blog_Item) convertView).updateUI(sayDataSet.get(position));
	     
	     
	     if(position==0)
	     {
	    	 convertView.setPadding(0, DoschoolApp.pxperdp*40, 0, 0);
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
		return sayDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sayDataSet.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
