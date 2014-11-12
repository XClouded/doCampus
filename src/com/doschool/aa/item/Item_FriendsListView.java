package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.SimplePerson;
import com.doschool.methods.ConvertMethods;




import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 投票广场ListView的Item布局
 * 
 * @author Sea
 */

public class Item_FriendsListView extends LinearLayout {

	SimplePerson personData;
	TextView tvNickName;
	ImageView ivLeftHead;
	
	public Item_FriendsListView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_friend, this);
		ivLeftHead=(ImageView) findViewById(R.id.ivHead);
		tvNickName=(TextView) findViewById(R.id.ivNick);
	}

	
	public void setDataRefresh(SimplePerson person){
		this.personData=person;
		this.setOnClickListener(new Click_Person(getContext(), personData));
		tvNickName.setText(ConvertMethods.getShowName(person));
		DoschoolApp.newImageLoader.displayImage(person.headUrl, ivLeftHead, DoschoolApp.dioRound);
	}

}
