package com.doschool.aa.adapter;

import java.util.ArrayList;

import com.doschool.aa.item.Item_ScriptListView;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Scrip;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Adp_Scrip extends Adp_Standard implements OnClickListener {
	private ArrayList<Scrip> requestDataSet; 
	private Context context;
	
	
	public Adp_Scrip(Context context,ArrayList<Scrip> friendsDataSet){
		this.context = context;
		this.requestDataSet = friendsDataSet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		  if (convertView == null) {
			  Item_ScriptListView A =new Item_ScriptListView(context);
		    	convertView =A;
		    }
		     ((Item_ScriptListView) convertView).setDataRefresh(requestDataSet.get(position), mBusy);
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
		// TODO Auto-generated method stub
		
	}

}
