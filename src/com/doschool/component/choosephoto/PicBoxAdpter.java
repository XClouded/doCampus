package com.doschool.component.choosephoto;

import java.util.ArrayList;
import java.util.List;
import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.PathMethods;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class PicBoxAdpter extends BaseAdapter {
	protected int mScrnWidth = DoschoolApp.widthPixels;
	protected int mDip = DoschoolApp.pxperdp;
	
	private List<String> pathList;
	Context context;

	public PicBoxAdpter(Context context, List<String> list) {
		this.pathList =new ArrayList<String>();
		for (String string : list) {

			pathList.add(string);
		}
		pathList.add("ADD");
		this.context=context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LinearLayout ll=new LinearLayout(context);
		
		if(position==pathList.size()-1)
		{

			ImageView ivImage = new ImageView(context);
			ivImage.setTag(position);
			ivImage.setImageResource(R.drawable.btn_addpic);
			ivImage.setScaleType(ScaleType.FIT_XY);
			ll.addView(ivImage,(mScrnWidth-32*mDip-4*mDip*5)/5,(mScrnWidth-32*mDip-4*mDip*5)/5);
			return ll;
		}
		else
		{

			final ImageView ivImage = new ImageView(context);

			DoschoolApp.newImageLoader.displayImage(pathList.get(position), ivImage,DoschoolApp.dioSquareSmall);
			ivImage.setTag(position);
			ivImage.setScaleType(ScaleType.FIT_XY);
			ll.addView(ivImage,(mScrnWidth-32*mDip-4*mDip*5)/5,(mScrnWidth-32*mDip-4*mDip*5)/5);
			return ll;
		}

	}

	public int getCount() {
		return pathList.size();
	}

	@Override
	public Object getItem(int position) {
		return pathList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
