package com.doschool.aa.activity.Oneblog;
//package com.doschool.aa.component.Oneblog;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.view.Gravity;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.ScaleAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//import com.doschool.R;
//import com.doschool.aa.activity.Act_Trans;
//import com.doschool.aa.widget.MyTextView;
//import com.doschool.app.DoschoolApp;
//import com.doschool.app.MySession;
//import com.doschool.app.TextSize;
//import com.doschool.entity.Microblog;
//import com.doschool.webserver.DoBlogSever;
//import com.doschool.zz.MJSONObject;
//
//public class Oneblog_Info_Bottom extends LinearLayout {
//	private final static int ID_ZAN = 0;
//	private final static int ID_TRANS = 1;
//	private final static int ID_CMT = 2;
//
//	private int cardWidth = (int) (DoschoolApp.widthPixels * 0.98);
//	private int padding = DoschoolApp.pxperdp;
//	LinearLayout llTrans;
//	LinearLayout llZan;
//	LinearLayout llComment;
//	MyTextView tvCommentCount;
//	MyTextView tvTransCount;
//	MyTextView tvZan;
//	private ImageView ivComment, ivTrans;
//	ImageView ivZan;
//
//	private Microblog blogData;
//
//	public Oneblog_Info_Bottom(Context context) {
//		super(context);
//		this.setPadding(0, padding, 0, padding);
//		this.setGravity(Gravity.CENTER_VERTICAL);
//		this.setOrientation(LinearLayout.HORIZONTAL);
//
//		// 赞
//		llZan = new LinearLayout(getContext());
//		llZan.setTag(ID_ZAN);
//		llZan.setGravity(Gravity.CENTER);
//		this.addView(llZan, (int) (cardWidth * 0.33), (int) (cardWidth * 0.108));
//		llZan.setBackgroundResource(R.drawable.btn_style_nocomment);
//
//		ivZan = new ImageView(getContext());
//		llZan.addView(ivZan, (int) (cardWidth * 0.048), (int) (cardWidth * 0.036));
//		tvZan = new MyTextView(getContext(), "", R.color.normal_grey, TextSize.BLOG_TIMECOUNT);
//		tvZan.setTag(1111);
//		llZan.addView(tvZan, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//		// 转发数
//		llTrans = new LinearLayout(getContext());
//		llTrans.setTag(ID_TRANS);
//		llTrans.setGravity(Gravity.CENTER);
//		this.addView(llTrans, (int) (cardWidth * 0.34), (int) (cardWidth * 0.108));
//		this.setBackgroundResource(R.drawable.btn_style_nocomment);
//
//		ivTrans = new ImageView(getContext());
//		llTrans.addView(ivTrans, (int) (cardWidth * 0.048), (int) (cardWidth * 0.036));
//		ivTrans.setImageDrawable(getResources().getDrawable(R.drawable.img_trans_little));
//		setTvTransCount(new MyTextView(getContext(), " ", R.color.normal_grey, TextSize.BLOG_TIMECOUNT));
//
//		llTrans.addView(getTvTransCount(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//		// 评论数
//		llComment = new LinearLayout(getContext());
//		llComment.setTag(ID_CMT);
//		llComment.setGravity(Gravity.CENTER);
//		this.addView(llComment, (int) (cardWidth * 0.33), (int) (cardWidth * 0.108));
//		llComment.setBackgroundResource(R.drawable.btn_style_nocomment);
//
//		ivComment = new ImageView(getContext());
//		llComment.addView(ivComment, (int) (cardWidth * 0.048), (int) (cardWidth * 0.036));
//		ivComment.setImageDrawable(getResources().getDrawable(R.drawable.img_comment_little));
//		setTvCommentCount(new MyTextView(getContext(), " ", R.color.normal_grey, TextSize.BLOG_TIMECOUNT));
//
//		llComment.addView(getTvCommentCount(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//	}
//
//	public void setData(Microblog data) {
//		blogData = data;
//		getTvTransCount().setText(" " + blogData.transCount);
//		getTvCommentCount().setText(" " + blogData.commentCount);
//		if (blogData.isZaned == 0)
//			ivZan.setImageDrawable(getResources().getDrawable(R.drawable.img_zan_no));
//		else
//			ivZan.setImageDrawable(getResources().getDrawable(R.drawable.img_zan_yes));
//		tvZan.setText(" " + blogData.zanCount);
//		tvZan.setText(" " + blogData.zanCount);
//		MClickListener mClickListener = new MClickListener();
//		llZan.setOnClickListener(mClickListener);
//		llTrans.setOnClickListener(mClickListener);
//		llComment.setOnClickListener(mClickListener);
//	}
//
//	public MyTextView getTvCommentCount() {
//		return tvCommentCount;
//	}
//
//	public void setTvCommentCount(MyTextView tvCommentCount) {
//		this.tvCommentCount = tvCommentCount;
//	}
//
//	public MyTextView getTvTransCount() {
//		return tvTransCount;
//	}
//
//	public void setTvTransCount(MyTextView tvTransCount) {
//		this.tvTransCount = tvTransCount;
//	}
//
//	class MClickListener implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			if (blogData != null) {
//				switch ((Integer) v.getTag()) {
//				case ID_ZAN:
//					if (blogData.isZaned == 1)
//						Toast.makeText(getContext(), "你已经赞过了！", Toast.LENGTH_SHORT).show();
//					else
//						new ZanTask().execute();
//					break;
//				case ID_TRANS:
//					Intent intent = new Intent(getContext(), Act_Trans.class);
//					MySession.getSession().put("microblog", blogData);
//					((Activity) getContext()).startActivityForResult(intent, 1);
//					((Activity) getContext()).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//					break;
//				case ID_CMT:
//					break;
//				}
//			}
//
//		}
//	}
//
//	public class ZanTask extends AsyncTask<Void, Void, Void> {
//
//		// ProgressDialog progressDialog;
//		boolean isFinish = true;
//		MJSONObject jResult;
//
//		protected void onPreExecute() {
//			blogData.isZaning = true;
//			Animation bb = new ScaleAnimation(0.7f, 1.5f, 0.7f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//			bb.setDuration(400);
//			bb.setRepeatMode(Animation.REVERSE);
//			bb.setRepeatCount(Animation.INFINITE);
//			ivZan.startAnimation(bb);
//			ivZan.setClickable(false);
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			jResult = DoBlogSever.MicroblogZanSend(DoschoolApp.thisUser.personId, blogData.blogId);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//
//			ivZan.setClickable(true);
//			blogData.isZaning = false;
//			ivZan.clearAnimation();
//			int code = jResult.getInt("code", 9);
//			String toast = jResult.getString("data");
//			if (code == 0) {
//				blogData.isZaned = 1;
//				blogData.zanCount++;
//				Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
//				tvZan.setText(blogData.zanCount + "");
//				ivZan.setImageResource(R.drawable.img_zan_yes);
//			} else {
//				if (toast.length() == 0) {
//					if (code == 1)
//						toast = "无此用户";
//					else if (code == 2)
//						toast = "无此微博";
//					else if (code == 3)
//						toast = "已经赞过";
//					else if (code == 9)
//						toast = "网络服务错误";
//				}
//				Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
//			}
//
//		}
//	}
//}
