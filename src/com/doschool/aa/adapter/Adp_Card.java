package com.doschool.aa.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.doschool.aa.item.Item_CardsListView;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Person;

public class Adp_Card extends Adp_Standard {
	private ArrayList<Person> cardsDataSet; 
	private Context context;
	
	public Adp_Card(Context context,ArrayList<Person> friendsDataSet){
		this.context = context;
		this.cardsDataSet = friendsDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			Item_CardsListView A =new Item_CardsListView(context);
	    	convertView =A;
	     }
	     ((Item_CardsListView) convertView).setDataRefresh(cardsDataSet.get(position));
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
		return cardsDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cardsDataSet.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
