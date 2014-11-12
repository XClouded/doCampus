package com.doschool.clicklistener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.doschool.R;
import com.doschool.aa.activity.Act_OneBlog;
import com.doschool.app.MySession;
import com.doschool.entity.Microblog;

/**
 * 点击事件：点微博，跳转到某个微博
 * 功能完成：100%
 * 异常处理：100%
 * @author 是我的海
 */

public class Click_Blog implements OnClickListener {
	Context ctx;
	Microblog blogData;

	public Click_Blog(Context ctx, Microblog data) {
		super();
		this.ctx = ctx;
		this.blogData = data;
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent(ctx, Act_OneBlog.class);
		if(blogData!=null && blogData.blogId>0)
		{
			MySession.getSession().put("microblog", blogData);
			ctx.startActivity(intent);
			((Activity) ctx).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
	}
}
