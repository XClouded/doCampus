package com.doschool.component.choosephoto;

import java.util.List;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.PathMethods;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter{
	private List<ImageFolder> list;
	protected LayoutInflater mInflater;
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public FolderAdapter(Context context, List<ImageFolder> list){
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_picchoose_folder, null);
			convertView.setTag(viewHolder);
			viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.ivCover);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
			viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageFolder mFolderBean = list.get(position);
		

		
		
		viewHolder.tvName.setText(mFolderBean.getFolderName());
		viewHolder.tvCount.setText("("+Integer.toString(mFolderBean.getImageCounts())+")");
		viewHolder.ivCover.setTag(mFolderBean.getTopImagePath());
		DoschoolApp.newImageLoader.displayImage(mFolderBean.getTopImagePath(), viewHolder.ivCover,DoschoolApp.dioSquareSmall);
		viewHolder.checkbox.setChecked(mFolderBean.isChecked());
		
		return convertView;
	}
	
	static class ViewHolder{
		public ImageView ivCover;
		public TextView tvName;
		public TextView tvCount;
		public CheckBox checkbox;
	}

}
