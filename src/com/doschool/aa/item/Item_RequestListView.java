package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.FriendRequest;
import com.doschool.methods.ConvertMethods;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 投票广场ListView的Item布局
 * 
 * @author Sea
 */

public class Item_RequestListView extends LinearLayout {

	private Context ctx;
	
	FriendRequest requestData;
	
	public Button btNo,btYes;
	public TextView tvResult;
	ImageView ivHead;
	TextView tvNick;
	LinearLayout llOperate;
	TextView tvTime;
	TextView tvReason;
	
	public void setDataRefresh(FriendRequest request,boolean isLoadOnlyFromCache){
		this.requestData=request;
		DoschoolApp.newImageLoader.displayImage(requestData.launchPerson.headUrl, ivHead, DoschoolApp.dioRound);
		
		tvNick.setText(ConvertMethods.getShowName(request.launchPerson));
		tvTime.setText(ConvertMethods.dateLongToDiyStr(request.time));
		if(request.reason.length()==0)
			tvReason.setText("这家伙很懒，没填理由");
		else
			tvReason.setText(request.reason);
		if(request.state==1)
		{
			tvResult.setVisibility(View.GONE);
			llOperate.setVisibility(View.VISIBLE);
		}
		else if(request.state==2)
		{
			tvResult.setText("已经拒绝");
			tvResult.setTextColor(getResources().getColor(R.color.zd_gery));
			tvResult.setVisibility(View.VISIBLE);
			llOperate.setVisibility(View.GONE);
			
		}
		else
		{
			tvResult.setText("已经接受");
			tvResult.setTextColor(getResources().getColor(R.color.light_green));
			tvResult.setVisibility(View.VISIBLE);
			llOperate.setVisibility(View.GONE);
		}
//		this.setOnClickListener(new Click_Person(ctx, new Person(request.userId, request.nickName, request.headUrl, 0, 0,"")));
		
	}
	
	public Item_RequestListView(Context context) {
		super(context);
		this.ctx=context;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_request, this);
		ivHead=(ImageView) findViewById(R.id.ivHead);
		tvNick=(TextView) findViewById(R.id.tvNick);
		tvTime=(TextView) findViewById(R.id.tvTime);
		tvReason=(TextView) findViewById(R.id.tvReason);
		llOperate=(LinearLayout) findViewById(R.id.llOperate);
		btYes=(Button) findViewById(R.id.btYes);
		btNo=(Button) findViewById(R.id.btNo);
		tvResult=(TextView) findViewById(R.id.tvResult);
		
	}
	

}
