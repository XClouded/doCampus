package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.component.atemotion.AtSpan;
import com.doschool.entity.SimplePerson;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Item_SettingListView extends RelativeLayout {

	public ImageView ivIcon;
	public TextView tvText;
	public CheckBox cbox;
	
	public Item_SettingListView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_at, this);
		ivIcon=(ImageView) findViewById(R.id.ivHead);
		tvText=(TextView) findViewById(R.id.ivNick);
		cbox=(CheckBox) findViewById(R.id.cbox);
		
	}
	
	
	public void setData(int iconRes,String text,boolean cbVisible)
	{
		ivIcon.setImageResource(iconRes);
		tvText.setText(text);
		if(cbVisible==true)
			cbox.setVisibility(View.VISIBLE);
		else
			cbox.setVisibility(View.GONE);
	}
	
	public boolean isChecked()
	{
		return cbox.isChecked();
	}
}
