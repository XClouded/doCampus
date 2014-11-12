package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.SimplePerson;
import com.doschool.entity.Topic;
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

public class Item_Topic extends LinearLayout {

	SimplePerson personData;
	TextView ivTopic;
	ImageView ivLine;
	
	public Item_Topic(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_topic, this);
		ivLine=(ImageView) findViewById(R.id.ivLine);
		ivTopic=(TextView) findViewById(R.id.ivTopic);
	}

	
	public void updateUI(Topic topic,int position){
		
		
//		this.personData=person;
//		this.setOnClickListener(new Click_Person(getContext(), personData));
		if(position%2==0)
		{
			ivLine.setImageResource(R.color.light_greyblue);
			ivTopic.setBackgroundResource(R.color.white);
		}
		else
		{
			ivLine.setImageResource(R.color.light_orange);
			ivTopic.setBackgroundResource(R.color.bg_grey);
		}
		ivTopic.setText(topic.topic);
//		DoschoolApp.newImageLoader.displayImage(person.headUrl, ivLeftHead, DoschoolApp.dioRound);
	}

}
