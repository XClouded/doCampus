package com.doschool.aa.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.doschool.R;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.activity.Act_OneBlog;
import com.doschool.aa.activity.Act_Trans;
import com.doschool.aa.widget.MyDialog;
import com.doschool.app.ActivityName;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.entity.Microblog;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoBlogSever;
import com.doschool.zother.MJSONObject;

public class Blog_Item_Bottom extends LinearLayout {

	private final static int ID_ZAN = 0;
	private final static int ID_TRANS = 1;
	private final static int ID_CMT = 2;

	LinearLayout llTrans;
	LinearLayout llZan;
	LinearLayout llCmt;
	TextView tvCmtCount;
	TextView tvTransCount;
	TextView tvZanCount;
	ImageView ivZan;

	private Microblog blogData;
	
	public Blog_Item_Bottom(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_blog_footer, this);
		llZan = (LinearLayout) findViewById(R.id.llZan);
		llTrans = (LinearLayout) findViewById(R.id.llTrans);
		llCmt = (LinearLayout) findViewById(R.id.llCmt);
		tvCmtCount = (TextView) findViewById(R.id.tvCmtCount);
		tvTransCount = (TextView) findViewById(R.id.tvTransCount);
		tvZanCount = (TextView) findViewById(R.id.tvZanCount);
		ivZan = (ImageView) findViewById(R.id.ivZan);

		llZan.setTag(ID_ZAN);
		llTrans.setTag(ID_TRANS);
		llCmt.setTag(ID_CMT);
	}

	public void updateUI(Microblog data,OnClickListener oncmtClick) {
		blogData = data;

		tvTransCount.setText(" " + blogData.transCount);
		tvCmtCount.setText(" " + blogData.commentCount);
		tvZanCount.setText(" " + blogData.zanCount);

		MClickListener mClickListener = new MClickListener();
		llZan.setOnClickListener(mClickListener);
		llTrans.setOnClickListener(mClickListener);
		if(oncmtClick==null)
			llCmt.setOnClickListener(mClickListener);
		else
			llCmt.setOnClickListener(oncmtClick);

		ivZan.setClickable(true);
		if (blogData.isZaning) {
			Animation bb = new ScaleAnimation(0.7f, 1.5f, 0.7f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			bb.setDuration(400);
			bb.setRepeatMode(Animation.REVERSE);
			bb.setRepeatCount(Animation.INFINITE);
			ivZan.setClickable(false);
			ivZan.startAnimation(bb);
		} else
			ivZan.clearAnimation();
		if (blogData.isZaned == 0)
			ivZan.setImageDrawable(getResources().getDrawable(R.drawable.icon_like));
		else
			ivZan.setImageDrawable(getResources().getDrawable(R.drawable.icon_like_selected));
	}

	class MClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(DoschoolApp.isGuest())
			{

				MyDialog.popURGuest(getContext());
				return;
			}
			if (blogData != null) {
				switch ((Integer) v.getTag()) {
				case ID_ZAN:
					if (blogData.isZaned == 1)
						DoMethods.showToast(getContext(), "你已经赞过了！");
					else
						new ZanTask(v).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					break;
				case ID_TRANS:
					Intent intent = new Intent(getContext(), Act_Trans.class);
					MySession.getSession().put("microblog", blogData);
					((Activity) getContext()).startActivityForResult(intent, ActivityName.CODE_ACT_TRANSNLOG);
					((Activity) getContext()).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					break;
				case ID_CMT:
					if (blogData != null && blogData.status != 9) {
						Intent intent2 = new Intent(getContext(), Act_OneBlog.class);
						intent2.putExtra("isOpenKeyboard", true);
						MySession.getSession().put("microblog", blogData);
						((Activity) getContext()).startActivity(intent2);
						((Activity) getContext()).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					}
					break;
				}
			}

		}
	}

	public class ZanTask extends AsyncTask<Void, Void, Void> {

		boolean isFinish = true;
		View view;
		MJSONObject jResult;

		public ZanTask(View v) {
			this.view = v;
		}

		protected void onPreExecute() {
			blogData.isZaning = true;
			view.setClickable(false);
			Animation bb = new ScaleAnimation(0.7f, 1.5f, 0.7f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			bb.setDuration(400);
			bb.setRepeatMode(Animation.REVERSE);
			bb.setRepeatCount(Animation.INFINITE);
			ivZan.startAnimation(bb);
		}

		@Override
		protected Void doInBackground(Void... params) {
			jResult = DoBlogSever.MicroblogZanSend(DoschoolApp.thisUser.personId, blogData.blogId);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			blogData.isZaning = false;
			ivZan.clearAnimation();
			int code = jResult.getInt("code", 9);
			String toast = jResult.getString("data");
			if (code == 0) {
				blogData.isZaned = 1;
				blogData.zanCount++;
//				Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
				tvZanCount.setText(blogData.zanCount + "");
				ivZan.setImageResource(R.drawable.sicon_blog_zaned);
			} else {
				if (toast.length() == 0) {
					if (code == 1)
						toast = "无此用户";
					else if (code == 2)
						toast = "无此微博";
					else if (code == 3)
						toast = "已经赞过";
					else if (code == 9)
						toast = "网络服务错误";
				}
				DoMethods.showToast(getContext(), toast);
				
			}
			view.setClickable(true);
		}
	}
	public TextView getCommentCountTextView() {
		// TODO Auto-generated method stub
		return tvCmtCount;
	}

	public TextView getTransCountTextView() {
		// TODO Auto-generated method stub
		return tvTransCount;
	}
}
