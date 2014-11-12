package com.doschool.aa.activity.Oneblog;

import com.doschool.R;
import com.doschool.aa.activity.Act_OneBlog;
import com.doschool.aa.activity.Act_Register;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.Comment;
import com.doschool.entity.Microblog;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.umeng.analytics.MobclickAgent;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 微博里面，每个评论item的布局
 * @author 是我的海
 */
public class Oneblog_CmtBox_item extends RelativeLayout {

	ImageView ivHead;
	public ImageView ivDivide;
	
	public TextView tvSubNick,mtvObjNick;
	Comment comment;
	Act_OneBlog containerAct;
	TextView tvContent;
	String paste;
	public Oneblog_CmtBox_item(Context context,Comment cmt,Microblog microblog,int FloorNo) {
		super(context);
		containerAct=(Act_OneBlog) context;
		this.comment=cmt;

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_blog_cmt, this);
		//内容
		tvContent=(TextView) findViewById(R.id.tvCmt);
		tvContent.setText(ConvertMethods.stringToSpannableString(comment.content, getContext()));
		tvContent.setAutoLinkMask(Linkify.ALL); 
		tvContent.setMovementMethod(LinkMovementMethod.getInstance()); 
		ivDivide=(ImageView) findViewById(R.id.ivDivide);
		
		
		tvContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				containerAct.onCmtItemClick(comment.subPerson.personId, comment.subPerson.nickName);
			}
		});
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				containerAct.onCmtItemClick(comment.subPerson.personId, comment.subPerson.nickName);
			}
		});
		ivHead=(ImageView) findViewById(R.id.ivHead);
		DoschoolApp.newImageLoader.displayImage(comment.subPerson.headUrl, ivHead, DoschoolApp.dioRound);
		ivHead.setOnClickListener(new Click_Person(getContext(),comment.subPerson));
		//谁
		tvSubNick=(TextView) findViewById(R.id.tvSub);
		tvSubNick.setText(comment.subPerson.nickName);
		if(cmt.subPerson.personId==microblog.author.personId)
			tvSubNick.setText("楼主");
		else
		{
			for(int i=0;i<DoschoolApp.cardsList.size();i++)
			{
				if(cmt.subPerson.personId==DoschoolApp.cardsList.get(i).personId)
				{
					tvSubNick.setText(DoschoolApp.cardsList.get(i).trueName);
					break;
				}
			}
		}
		
		
		//谁
		mtvObjNick=(TextView) findViewById(R.id.tvObj);
		mtvObjNick.setText(comment.objPerson.nickName);
		if(cmt.objPerson.personId==microblog.author.personId)
			mtvObjNick.setText("楼主");
		else
		{
			for(int i=0;i<DoschoolApp.cardsList.size();i++)
			{
				if(cmt.objPerson.personId==DoschoolApp.cardsList.get(i).personId)
				{
					mtvObjNick.setText(DoschoolApp.cardsList.get(i).trueName);
					break;
				}
					
			}
		}
		
		TextView mtvFloor=(TextView) findViewById(R.id.tvFloor);
		mtvFloor.setText(FloorNo+"楼");

		paste = ConvertMethods.removeTextTag(tvContent.getText().toString());
		tvContent.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				MobclickAgent.onEvent(getContext(), "event_longclick_tocopy");
				
				cmb.setText(paste);
				DoMethods.showToast(getContext(), "文字已经复制到剪贴板");
				return true;
			}
		});

		
	}

}
