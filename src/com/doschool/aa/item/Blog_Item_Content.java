package com.doschool.aa.item;

import java.util.zip.Inflater;

import com.doschool.R;
import com.doschool.aa.activity.Act_PicGallery;
import com.doschool.aa.widget.ProgressImageView;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Blog;
import com.doschool.clicklistener.Click_Topic;
import com.doschool.entity.Microblog;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * 几乎完美
 * 
 * @author 是我的海
 */
public class Blog_Item_Content extends LinearLayout {

	private int mDip = DoschoolApp.pxperdp;
	private int mScrnWidth = DoschoolApp.widthPixels;

	private boolean isTrans;
	public boolean isOneBlog;
	private Microblog blogData;

	private LinearLayout llTopic;
	private TextView tvTopic, tvTransNick;
	private LinearLayout llTransFrom;
	public TextView tvContent;
	private LinearLayout llPicture;
	String paste;
	private OnClickListener piccClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int order = (Integer) v.getTag();
			Intent intent = new Intent(getContext(), Act_PicGallery.class);
			intent.putExtra("order", order);
			intent.putExtra("data", blogData);
			getContext().startActivity(intent);
		}
	};

	public Blog_Item_Content(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_blog_content, this);
		llTopic = (LinearLayout) findViewById(R.id.llTopic);
		tvTopic = (TextView) findViewById(R.id.tvTopic);
		llTransFrom = (LinearLayout) findViewById(R.id.llTransFrom);
		tvTransNick = (TextView) findViewById(R.id.tvTransNick);
		llPicture = (LinearLayout) findViewById(R.id.llPicture);
		tvContent = (TextView) findViewById(R.id.tvContent);
		tvContent.setAutoLinkMask(Linkify.ALL);
		tvContent.setMovementMethod(LinkMovementMethod.getInstance());

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Blog_Content);
		isTrans = typedArray.getBoolean(R.styleable.Blog_Content_is_trans, false);
		typedArray.recycle();


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

		if (isTrans) {
			llTopic.setVisibility(View.GONE);
			this.setBackgroundResource(R.color.light_light_grey);
		} else {
			llTransFrom.setVisibility(View.GONE);
		}
		
		
		
	}

	// 注入数据
	public void updateUI(Microblog data) {
		this.blogData = data;

		if (blogData == null) {
			this.setVisibility(View.GONE);
		} else {

			this.setVisibility(View.VISIBLE);
			if (isTrans) {
				Click_Blog click_Blog = new Click_Blog(getContext(), blogData);
				this.setOnClickListener(click_Blog);
				tvTransNick.setText(ConvertMethods.getShowName(blogData.author));
			}

			if (!isTrans && blogData.topic != null && blogData.topic.topic.length() > 0) {
				llTopic.setVisibility(View.VISIBLE);
				Click_Topic click_Topic = new Click_Topic(getContext(), blogData.topic);
				llTopic.setOnClickListener(click_Topic);
				tvTopic.setText(blogData.topic.topic);
			} else {
				llTopic.setVisibility(View.GONE);
			}

			int padding = mDip * 4;
			int PicLayoutWidth = (int) ((mScrnWidth - 16 * mDip) * 0.8);
			
			if (blogData.blogContent != null && blogData.blogContent.length() > 0)
				tvContent.setText(ConvertMethods.stringToSpannableString(blogData.blogContent, getContext()));
			else
				tvContent.setText("该微博内容丢失了");

			paste = ConvertMethods.removeTextTag(tvContent.getText().toString());
			if(!isOneBlog)
				tvContent.setOnClickListener(new Click_Blog(getContext(), blogData));

			
			llPicture.removeAllViews();
			
			RelativeLayout picBox=null;
			LayoutInflater inflate=LayoutInflater.from(getContext());
			
			switch (blogData.imageUrlList.size()) {
			case 0:
				break;
			case 1:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_1,null);
				break;
			case 2:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_2,null);
				break;
			case 3:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_3,null);
				break;
			case 4:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_4,null);
				break;
			case 5:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_5,null);
				break;
			case 6:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_6,null);
				break;
			case 7:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_7,null);
				break;
			case 8:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_8,null);
				break;
			case 9:
				picBox=(RelativeLayout) inflate.inflate(R.layout.picshowbox_9,null);
				break;
			default:
				break;
			}
			
				if(picBox!=null)
				{

					llPicture.addView(picBox);
					for(int i=0;i<picBox.getChildCount();i++)
					{
						final ProgressImageView ivImage = (ProgressImageView) picBox.getChildAt(i);
						ivImage.setPadding(0, 0, mDip*4, mDip*4);
						ivImage.setTag(i);
						ivImage.setOnClickListener(piccClickListener);
						ivImage.mImageView.setScaleType(ScaleType.CENTER_CROP);

						int imageWidth = 100;
						LinearLayout.LayoutParams lp = new LayoutParams(imageWidth, imageWidth);
						DoschoolApp.newImageLoader.displayImage(blogData.imageUrlList.get(i), ivImage.mImageView, DoschoolApp.dioSquare, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								ivImage.mCircleProgress.setProgress(0);
								ivImage.mCircleProgress.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							}

							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								ivImage.mCircleProgress.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri, View view, int current, int total) {
								Log.v("onProgressUpdate" + "current" + current + " total" + total, "onProgressUpdate");
								ivImage.mCircleProgress.setProgress(100 * current / (total + 1));
							}
						});
						
						
					}
					
				}

			}
			}
		}
