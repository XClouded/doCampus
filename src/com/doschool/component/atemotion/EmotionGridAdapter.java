package com.doschool.component.atemotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.doschool.R;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.GridView;

public class EmotionGridAdapter extends BaseAdapter {
	private final int mScrnWidth = DoschoolApp.widthPixels;
	private final int mScrnHeight = DoschoolApp.heightPixels;
	private final int mDip = DoschoolApp.pxperdp;
	
	private final int EMITION_BOARD_HEIGHT = (int) (mScrnHeight*0.32);
	private final int LINE_PER_PAGE = 4;
	private final int COUNT_PER_LINE = 6;
	
	
	private ArrayList<EmotionBean> ebList;
	Context context;

	ViewPager vp;
	
	public EmotionGridAdapter(Context context, ArrayList<EmotionBean> ebList) {
		this.ebList =ebList;
		this.context=context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		

		ImageView ivEmotion=new ImageView(context);
		ivEmotion.setPadding(mDip*10, mDip*10, mDip*10, mDip*10);
		ivEmotion.setImageResource(ebList.get(position).getResourceId());
		
		LinearLayout layout=new LinearLayout(context);
		layout.setTag(ebList.get(position));
		layout.addView(ivEmotion, mScrnWidth/COUNT_PER_LINE, EMITION_BOARD_HEIGHT/LINE_PER_PAGE);

		return layout;
		
	}

	public int getCount() {
		return ebList.size();
	}

	@Override
	public Object getItem(int position) {
		return ebList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
