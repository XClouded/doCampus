package com.doschool.aa.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.doschool.aa.item.Item_PeopleListView;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;

public class Adp_SearchPerson extends Adp_Standard {
	private ArrayList<SimplePerson> friendsDataSet; 
	private Context context;
	
	public Adp_SearchPerson(Context context,ArrayList<SimplePerson> friendsDataSet){
		this.context = context;
		this.friendsDataSet = friendsDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item_PeopleListView itemView = new Item_PeopleListView(context,friendsDataSet.get(position));
		convertView=(Item_PeopleListView) itemView;
		
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
