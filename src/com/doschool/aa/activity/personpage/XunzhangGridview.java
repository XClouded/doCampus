package com.doschool.aa.activity.personpage;

import java.util.ArrayList;

import com.doschool.aa.widget.MyDialog;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Xunzhang;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class XunzhangGridview extends GridView {

	//存放各功能图片  
    private ArrayList<Xunzhang> xunPList;
	protected int parentWidth = DoschoolApp.widthPixels;
	protected int padding = DoschoolApp.pxperdp;
	
	public XunzhangGridview(Context context) {
		super(context);
		
        this.setNumColumns(6);
    }  
	
	public void setAdapter(ArrayList<Xunzhang> xunList)
	{
		this.xunPList=xunList;
		this.setAdapter(new ImageAdapter(getContext(),this.xunPList));
	}
	
	public void callRefresh(ArrayList<Xunzhang> xunList)
	{
		this.setAdapter( xunList);
		android.view.ViewGroup.LayoutParams lp=this.getLayoutParams();
		lp.height=parentWidth/6*((xunList.size()-1)/6+1);
		this.setLayoutParams(lp);
	}
	
public class ImageAdapter extends BaseAdapter{
	
    private Context mContext;
    ArrayList<Xunzhang> xunList;
    int mposition;
    
    public ImageAdapter(Context c,ArrayList<Xunzhang> xunList){  
        mContext = c;  
        this.xunList=xunList;
    }  
    @Override  
    public int getCount() {  
        return xunList.size();  
    }  

    @Override  
    public Object getItem(int position) {  
        return position;  
    }  

    @Override  
    public long getItemId(int position) {  
        return position;  
    }  

    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {
    	mposition=position;
    	if(convertView==null)
    	{
    		ImageView iv=new ImageView(mContext);
    		 iv.setLayoutParams(new LayoutParams(parentWidth/6, parentWidth/6));
    		 iv.setPadding(padding*3, padding*3, padding*3, padding*3);
    		 convertView=iv;
    	}
    	convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyDialog.popXunZhang(mContext, xunList.get(mposition));
				
			}
		});
    	DoschoolApp.newImageLoader.displayImage(xunList.get(position).pic, (ImageView) convertView, DoschoolApp.dioSquare);
    	Log.v("aaa", "eeee");
        return convertView; 
    }  
      
}  
}
