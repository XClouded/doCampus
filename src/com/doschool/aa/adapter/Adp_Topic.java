package com.doschool.aa.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.doschool.aa.item.Blog_Item;
import com.doschool.aa.item.Item_Topic;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Microblog;
import com.doschool.entity.Topic;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Adp_Topic extends Adp_Standard {
	private Context context;
	private ArrayList<Topic> dataSet;
	
	public Adp_Topic(Context context,ArrayList<Topic> sayDataSet){
		this.context = context;
		this.dataSet = sayDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	     if (convertView == null) {
	    	 Item_Topic A =new Item_Topic(context);
	    	convertView =A;
	     }
	     ((Item_Topic) convertView).updateUI(dataSet.get(position),position);
	     return convertView;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataSet.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
