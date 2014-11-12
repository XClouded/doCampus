package com.doschool.aa.activity.Oneblog;

import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.activity.Act_OneBlog;
import com.doschool.aa.widget.CircleHorizontal;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Comment;
import com.doschool.entity.Microblog;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoBlogSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;




import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class Oneblog_Cmtbox extends LinearLayout {
	
	/******** 尺寸大小 ****************************************/
	private int parentWidth=(int) (DoschoolApp.widthPixels*0.98);
	private int dip=DoschoolApp.pxperdp;
	
	/******** 界面组件 ****************************************/
	public CircleHorizontal loadingCircle;
	LinearLayout llComment;
	Button mbtRetry;
	
	/******** 数据 ****************************************/
	Microblog blogData;
	ArrayList<Comment> commentList;
	Act_OneBlog containerAct;
	
	
	public Oneblog_Cmtbox(Context context) {
		super(context);
		commentList=new ArrayList<Comment>();
		this.containerAct=(Act_OneBlog) context;

		//2 本身的设置
		this.setBackgroundResource(R.drawable.bg_blog_card);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		//4、添加重试按钮
		mbtRetry=WidgetFactory.createButton(getContext(), "", R.drawable.icon_network_error, 0, 16);
		this.addView(mbtRetry,dip*72,dip*72);
		
		//5、添加评论区
		llComment=new LinearLayout(getContext());
		llComment.setOrientation(LinearLayout.VERTICAL);
		this.addView(llComment,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		
		//3、添加加载圈
		loadingCircle=new CircleHorizontal(getContext(), "刷新中...");
		LinearLayout.LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, dip*12, 0, dip*18);
		this.addView(loadingCircle,lp);
	}
	
	
	/******** 调用刷新任务 ****************************************/
	public void callRefresh(Microblog blogData,boolean isMore)
	{
		this.blogData =blogData;
		new RefreshTask(isMore).execute();
	}
	
	
	/******** 刷新任务 ****************************************/
	class RefreshTask extends AsyncTask<Void, Void, Void>
	{
		boolean isMore;	
		MJSONObject jResult;
		public RefreshTask(boolean isMore)
		{
			this.isMore=isMore;
		}
		
		@Override
		protected void onPreExecute() {
			//加载状态,转圈圈
			loadingCircle.setVisibility(View.VISIBLE);
			mbtRetry.setVisibility(View.GONE);
			containerAct.mScrollView.getRefreshableView().fullScroll(ScrollView.FOCUS_DOWN);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
				int lastBID=0;
				if(isMore!=false && commentList.size()>0)
					lastBID=commentList.get(commentList.size()-1).commentId;
				jResult = DoBlogSever.MicroblogCommentListGet(blogData.blogId, lastBID, 30,DoschoolApp.thisUser.personId);
				return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			
			int code = jResult.getInt("code", 9);
			String toast = jResult.getString("data");
			if (code == 0) {

				if(isMore)
				{
					int beforeSize=commentList.size();
					MJSONArray jArr = jResult.getMJSONArray("data");
					for (int i = 0; i < jArr.length(); i++)
						commentList.add(new Comment(jArr.getMJSONObject(i)));
						
						for(int i=beforeSize;i<commentList.size();i++)
						{
							Oneblog_CmtBox_item ipo=new Oneblog_CmtBox_item(containerAct,commentList.get(i),blogData,i+1);
							if(i==commentList.size()-1)
								ipo.ivDivide.setVisibility(View.INVISIBLE);
							ipo.setTag(i);
							llComment.addView(ipo, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						}
						loadingCircle.setVisibility(View.GONE);
						llComment.setVisibility(View.VISIBLE);
						mbtRetry.setVisibility(View.GONE);
						if(commentList.size()>blogData.commentCount)
							containerAct.blogInfoLayout.setCommentCount(commentList.size());
				}
				else
				{
					commentList=new ArrayList<Comment>();
					MJSONArray jArr = jResult.getMJSONArray("data");
					for (int i = 0; i < jArr.length(); i++)
						commentList.add(new Comment(jArr.getMJSONObject(i)));
					llComment.removeAllViews();
					for(int i=0;i<commentList.size();i++)
					{
						Oneblog_CmtBox_item ipo=new Oneblog_CmtBox_item(containerAct,commentList.get(i),blogData,i+1);
						if(i==commentList.size()-1)
							ipo.ivDivide.setVisibility(View.INVISIBLE);
						ipo.setTag(i);
						llComment.addView(ipo, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					}
					loadingCircle.setVisibility(View.GONE);
					llComment.setVisibility(View.VISIBLE);
					mbtRetry.setVisibility(View.GONE);
//					containerAct.blogInfoLayout.setCommentCount(commentList.size());
				}

				
				if(commentList.size()==0)					//如果没有评论
				{
					loadingCircle.setVisibility(View.GONE);
					llComment.setVisibility(View.GONE);
					mbtRetry.setVisibility(View.VISIBLE);
					mbtRetry.setBackgroundResource(R.drawable.icon_no_data);
					mbtRetry.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new RefreshTask(false).execute();
						}
					});
				}

//				containerAct.mScrollView.getRefreshableView().fullScroll(ScrollView.FOCUS_DOWN);
			
			} else {
				
				loadingCircle.setVisibility(View.GONE);
				llComment.setVisibility(View.GONE);
				mbtRetry.setVisibility(View.VISIBLE);
				mbtRetry.setBackgroundResource(R.drawable.icon_network_error);
				mbtRetry.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new RefreshTask(false).execute();
					}
				});
				
				if (toast.length() == 0) {
					if (code == 1)
						toast = "无此微博";
					else if (code == 9)
						toast = "网络服务错误";
				}
				DoMethods.showToast(getContext(), toast);
			}
			
		}
		
	}
	
}
