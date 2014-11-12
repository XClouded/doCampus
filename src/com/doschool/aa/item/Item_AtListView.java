package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.component.atemotion.AtSpan;
import com.doschool.entity.SimplePerson;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Item_AtListView extends RelativeLayout {


	public static final int ID_HEAD=1;

	private int mScrnWidth =DoschoolApp.widthPixels;
	private int mDip = DoschoolApp.pxperdp;
	ImageView ivHead;
	TextView tvNick;
	CheckBox cbox;
	public AtSpan span;
	
	public Item_AtListView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_at, this);
		ivHead=(ImageView) findViewById(R.id.ivHead);
		tvNick=(TextView) findViewById(R.id.ivNick);
		cbox=(CheckBox) findViewById(R.id.cbox);
		
	}
	
	
	public void setData(SimplePerson pInfo,boolean checked,AtSpan span)
	{
		this.span=span;

		DoschoolApp.newImageLoader.displayImage(pInfo.headUrl, ivHead, DoschoolApp.dioRound);
		tvNick.setText(pInfo.nickName);
		cbox.setChecked(checked);
	}
	
	public boolean isChecked()
	{
		return cbox.isChecked();
	}
}
