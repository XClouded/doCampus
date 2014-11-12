package com.doschool.component.choosephoto;

import java.util.ArrayList;
import java.util.List;
import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.DoMethods;
import com.doschool.methods.PathMethods;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter {

	protected LayoutInflater mInflater;
	private ArrayList<String> mSelectMap; // 用来存储图片的选中情况
	private OnPicCountChangeListener onPicCountChangeListener;
	private List<String> pathList;
	Context context;
	int maxCount;
	OnClickListener onCameraClick;

	public ImageGridAdapter(Context context, List<String> list, OnPicCountChangeListener onPicCountChangeListener, ArrayList<String> mSelectMap,int maxCount,OnClickListener onCameraClick) {
		this.context=context;
		this.pathList = list;
		this.onPicCountChangeListener = onPicCountChangeListener;
		this.mSelectMap = mSelectMap;
		this.mInflater = LayoutInflater.from(context);
		this.maxCount=maxCount;
		this.onCameraClick=onCameraClick;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_picchoose_image, null);
			convertView.setTag(viewHolder);
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageview);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		if(position==0)
		{
			viewHolder.mImageView.setImageResource(R.drawable.gallery_camera);
			viewHolder.mImageView.setOnClickListener(onCameraClick);
		}
		else
		{
			final String actualPath = new String(pathList.get(position-1));
					
			boolean found=false;
			for (String item : mSelectMap) {
				if(item.equals(actualPath))
				{
					viewHolder.mCheckBox.setChecked(true);
					found=true;
					break;
				}
			}
			if(found==false)
			{
				viewHolder.mCheckBox.setChecked(false);
			}
		
			
			viewHolder.mImageView.setTag(actualPath);
			DoschoolApp.newImageLoader.displayImage(actualPath, viewHolder.mImageView,DoschoolApp.dioSquareSmall);

			
			viewHolder.mImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if (viewHolder.mCheckBox.isChecked() == false) {
						if(mSelectMap.size()>=maxCount)
						{
							DoMethods.showToast(v.getContext(), "你最多只能选择"+maxCount+"张图片");
							return;
						}
						viewHolder.mCheckBox.setChecked(true);
						DoMethods.addAnimation(viewHolder.mCheckBox);
						mSelectMap.add(actualPath);
					} else {
						viewHolder.mCheckBox.setChecked(false);
						DoMethods.addAnimation(viewHolder.mCheckBox);
						for (String item : mSelectMap) {
							if(item.equals(actualPath))
							{
								DoMethods.addAnimation(viewHolder.mCheckBox);
								viewHolder.mCheckBox.setChecked(false);
								mSelectMap.remove(item);
								break;
							}
						}
					}
					onPicCountChangeListener.onPicCountChange(mSelectMap.size());
				}
			});
			
		}
		
		return convertView;
	}

	public static class ViewHolder {
		public ImageView mImageView;
		public CheckBox mCheckBox;
	}

	@Override
	public int getCount() {
		return pathList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if(position==0)
			return null;
		else
			return pathList.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
