package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Blog;
import com.doschool.entity.Message;
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
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 投票广场ListView的Item布局
 * 
 * @author Sea
 */

public class Item_MessageListView extends LinearLayout {

	private Context ctx;
	Message messageData;
	
	ImageView ivLeftHead;
	ImageView ivType;
	TextView tvNickName;
	TextView tvTime;
	TextView tvContent;
	TextView tvBlog;
	String paste;
	String pasteRoot;
	
	public void setDataRefresh(Message message,boolean isLoadOnlyFromCache){
		this.messageData=message;
		DoschoolApp.newImageLoader.displayImage(messageData.person.headUrl, ivLeftHead, DoschoolApp.dioRound);
		tvNickName.setText(ConvertMethods.getShowName(messageData.person));
		
		String operate="";
		if(message.type==1)
			operate="在微博中@了你";
		else if(message.type==2)
			operate="在微博中回复了你";
		else if(message.type==3 || message.type==9)
			operate="转发了你的微博";
		else if(message.type==4)
			operate="删除了你的微博";
		else if(message.type==5)
			operate="评论时@了你";
		else if(message.type==6)
			operate="回复了你的评论";
		else if(message.type==7)
			operate="删除了你的评论";
		else if(message.type==8)
			operate="赞了你";
		
		

		if(message.type==1 || message.type==4 || message.type==7 || message.type==8)
		{	//隐藏
			tvContent.setVisibility(View.GONE);
		}
		else if(messageData.type==2 || message.type==5 || message.type==6)
		{	//显示newContent
			tvContent.setVisibility(View.VISIBLE);
			if(messageData.newContent==null || messageData.newContent.length()==0)
				tvContent.setText("内容被删除了T_T");
			else
				tvContent.setText(ConvertMethods.stringToSpannableString(messageData.newContent, getContext()));
		}
		else if(messageData.type==3 || messageData.type==9)
		{
			//显示newBlog
			tvContent.setVisibility(View.VISIBLE);
			try {
				if(messageData.newBlog.blogContent==null || messageData.newBlog.blogContent.length()==0)
					tvContent.setText("内容被删除了T_T");
				else
				tvContent.setText(ConvertMethods.stringToSpannableString(messageData.newBlog.blogContent, getContext()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			tvContent.setVisibility(View.VISIBLE);
			tvContent.setText("内容不存在");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		if(message.type==1 || message.type==2 || message.type==3 || message.type==4 ||  message.type==5 ||  message.type==7 || message.type==8 || message.type==9)
		{	//显示根微博
			tvBlog.setVisibility(View.VISIBLE);
			if(messageData.rootBlog.blogContent==null || messageData.rootBlog.blogContent.length()==0)
				tvContent.setText("内容被删除了T_T");
			else
			tvBlog.setText(ConvertMethods.stringToSpannableString(messageData.rootBlog.blogContent, getContext()));
		}
		else if(message.type==6)
		{	//显示评论
			tvBlog.setVisibility(View.VISIBLE);
			if(messageData.oldContent==null || messageData.oldContent.length()==0)
				tvContent.setText("内容被删除了T_T");
			else
			tvBlog.setText(ConvertMethods.stringToSpannableString(messageData.oldContent, getContext()));
		}
		else
		{
			tvBlog.setVisibility(View.GONE);
		}
		
		
		
		
		
		
		
		if(message.type==1 || message.type==5)
		{
			ivType.setImageResource(R.drawable.sicon_msg_at);
			
		}
		else if(message.type==2 || message.type==6)
		{
			ivType.setImageResource(R.drawable.sicon_msg_cmt);
		}
		else if(message.type==3 || message.type==9)
		{
			ivType.setImageResource(R.drawable.sicon_msg_tran);
		}else if(message.type==8)
		{
			ivType.setImageResource(R.drawable.sicon_msg_zan);
		}else
		{
			ivType.setImageResource(R.color.transparent);
		}
		
		tvTime.setText(ConvertMethods.dateLongToDiyStr(messageData.time)+"  "+operate);
		
		
		if(message.type==3)
		{
			tvBlog.setOnClickListener(new Click_Blog(ctx, messageData.newBlog));
			tvContent.setOnClickListener(new Click_Blog(ctx, messageData.newBlog));
			this.setOnClickListener(new Click_Blog(ctx, messageData.newBlog));
		}
		else
		{
			tvContent.setOnClickListener(new Click_Blog(ctx, messageData.newBlog));
			tvContent.setOnClickListener(new Click_Blog(ctx, messageData.rootBlog));
			this.setOnClickListener(new Click_Blog(ctx, messageData.rootBlog));
		}
		pasteRoot = ConvertMethods.removeTextTag(tvBlog.getText().toString());
		paste = ConvertMethods.removeTextTag(tvContent.getText().toString());

	
	}
	
	public Item_MessageListView(Context context) {
		super(context);
		// 本身初始化
		this.ctx=context;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_msg, this);
		ivLeftHead=(ImageView) findViewById(R.id.ivHead);
		ivType=(ImageView) findViewById(R.id.ivType);
		tvNickName=(TextView) findViewById(R.id.tvNick);
		tvTime=(TextView) findViewById(R.id.tvTime);
		tvContent=(TextView) findViewById(R.id.tvContent);
		tvBlog=(TextView) findViewById(R.id.tvBlog);

		tvContent.setAutoLinkMask(Linkify.ALL); 
		tvContent.setMovementMethod(LinkMovementMethod.getInstance()); 
		tvBlog.setAutoLinkMask(Linkify.ALL); 
		tvBlog.setMovementMethod(LinkMovementMethod.getInstance()); 
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
		tvBlog.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				MobclickAgent.onEvent(getContext(), "event_longclick_tocopy");
				cmb.setText(pasteRoot);
				DoMethods.showToast(getContext(), "文字已经复制到剪贴板");
				return true;
			}
		});
	}
}
