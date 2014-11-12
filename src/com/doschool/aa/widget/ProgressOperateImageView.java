package com.doschool.aa.widget;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.widget.RelativeLayout;

public class ProgressOperateImageView extends RelativeLayout {

	public PhotoView mImageView;
	public CircleProgress mCircleProgress;
	
	public ProgressOperateImageView(Context context,int progressSize) {
		super(context);
		mImageView=new PhotoView(getContext());
		RelativeLayout.LayoutParams lpImage=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(mImageView, lpImage);

		mCircleProgress=new CircleProgress(getContext(),progressSize);
		RelativeLayout.LayoutParams lpProgress=new LayoutParams(progressSize, progressSize);
		lpProgress.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(mCircleProgress, lpProgress);
	}

}
