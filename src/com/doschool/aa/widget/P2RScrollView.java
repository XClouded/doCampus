package com.doschool.aa.widget;

import com.doschool.R;
import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

public class P2RScrollView extends PullToRefreshScrollView {

	public P2RScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public P2RScrollView(Context context) {
		super(context);
		init();
	}

	private void init(){
		//---------------------this.setDividerDrawable(context.getResources().getDrawable(R.drawable.img_transparent));
		this.setMode(Mode.PULL_DOWN_TO_REFRESH);
		this.getLoadingLayoutProxy(false, true).setPullLabel("上拉更多");
		this.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
		this.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
		this.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
		this.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
		this.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
	}
	
	
}
