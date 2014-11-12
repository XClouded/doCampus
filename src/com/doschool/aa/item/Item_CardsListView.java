package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.aa.widget.MyDialog;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.Person;
import com.doschool.methods.ConvertMethods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 投票广场ListView的Item布局
 * 
 * @author Sea
 */

public class Item_CardsListView extends LinearLayout {

	Person personData;
	TextView tvNickName;
	ImageView ivLeftHead;

	public Item_CardsListView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_card, this);
		ivLeftHead = (ImageView) findViewById(R.id.ivHead);
		tvNickName = (TextView) findViewById(R.id.ivNick);
	}

	public void setDataRefresh(Person person) {

		this.personData = person;

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyDialog.popHdHeadDialog(getContext(), personData);

			}
		});
		ivLeftHead.setOnClickListener(new Click_Person(getContext(), personData));

		tvNickName.setText(ConvertMethods.getShowName(person));
		DoschoolApp.newImageLoader.displayImage(person.headUrl, ivLeftHead, DoschoolApp.dioRound);
	}

}
