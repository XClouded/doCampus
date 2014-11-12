package com.doschool.aa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ProgressImageView extends RelativeLayout {

	public ImageView mImageView;
	public CircleProgress mCircleProgress;
	
	public ProgressImageView(Context context,int progressSize) {
		super(context);
		init(progressSize);
	}

	public ProgressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(200);
	}

	public void init(int progressSize)
	{
		mImageView=new ImageView(getContext());
		RelativeLayout.LayoutParams lpImage=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(mImageView, lpImage);

		mCircleProgress=new CircleProgress(getContext(),progressSize);
		RelativeLayout.LayoutParams lpProgress=new LayoutParams(progressSize, progressSize);
		lpProgress.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(mCircleProgress, lpProgress);
	}
	
	
	
}
