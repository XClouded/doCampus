package com.doschool.clicklistener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.doschool.R;
import com.doschool.aa.activity.Act_TopicBlogList;
import com.doschool.app.MySession;
import com.doschool.entity.Topic;
import com.umeng.analytics.MobclickAgent;

/**
 * 点击事件：点微博，跳转到某个微博
 * 功能完成：100%
 * 异常处理：100%
 * @author 是我的海
 */

public class Click_Topic implements OnClickListener {
	Context ctx;
	Topic data;

	public Click_Topic(Context ctx, Topic data) {
		super();
		this.ctx = ctx;
		this.data = data;
	}

	@Override
	public void onClick(View v) {

		MobclickAgent.onEvent(ctx, "event_click_topic");
		MySession.getSession().put("topic", data);
		Intent intent = new Intent(ctx, Act_TopicBlogList.class);
		ctx.startActivity(intent);
		((Activity) ctx).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
}
