package com.doschool.aa.activity;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.CircleHorizontal;
import com.doschool.aa.widget.ProgressImageView;
import com.doschool.aa.widget.ProgressOperateImageView;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class Act_About extends Act_CommonOld {
	ArrayList<String> urlList;
	LinearLayout llBigPic;
	LinearLayout llPicGroup;
	ArrayList<ProgressImageView> photoViewList;
	CircleHorizontal loadingCircle;
	ViewPager viewPager;
	
	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "关于我们";
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		photoViewList=new ArrayList<ProgressImageView>();
		urlList=new ArrayList<String>();
		loadingCircle=new CircleHorizontal(this, Color.BLACK);
		loadingCircle.setText("正在加载...");
		mParentLayout.setGravity(Gravity.CENTER);
		mParentLayout.addView(loadingCircle);
		mParentLayout.setBackgroundResource(R.color.light_greyblue3);
		viewPager=new ViewPager(getApplicationContext());
		mParentLayout.addView(viewPager,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		viewPager.setAdapter(new PagerAdapter() {

			public View instantiateItem(ViewGroup container, int position) {
				ProgressImageView photoView =new ProgressImageView(Act_About.this, 200);
				photoView.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
				photoViewList.add(position, photoView);
				if(position==0)
				{
					final ProgressImageView POIV = photoViewList.get(0);
					DoschoolApp.newImageLoader.displayImage(urlList.get(0), POIV.mImageView, DoschoolApp.dioSquare, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							POIV.mCircleProgress.setProgress(0);
							POIV.mCircleProgress.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							POIV.mCircleProgress.setVisibility(View.INVISIBLE);

						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							Log.v("onProgressUpdate" + "current" + current + " total" + total, "onProgressUpdate");
							POIV.mCircleProgress.setProgress(100 * current / (total + 1));
						}
					});
				}
				
				
				container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				return photoView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public int getCount() {
				return urlList.size();
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				final ProgressImageView POIV = photoViewList.get(arg0);
				DoschoolApp.newImageLoader.displayImage(urlList.get(arg0), POIV.mImageView, DoschoolApp.dioSquare, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						POIV.mCircleProgress.setProgress(0);
						POIV.mCircleProgress.setVisibility(View.VISIBLE);

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						POIV.mCircleProgress.setVisibility(View.INVISIBLE);

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
						Log.v("onProgressUpdate" + "current" + current + " total" + total, "onProgressUpdate");
						POIV.mCircleProgress.setProgress(100 * current / (total + 1));
					}
				});
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		new RefreshTopicListTask().execute();
	}
	
	public void setRefreshing()
	{
		loadingCircle.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.GONE);
	}
	
	public void setRefreshComplete()
	{
		loadingCircle.setVisibility(View.GONE);
		viewPager.getAdapter().notifyDataSetChanged();
		viewPager.setVisibility(View.VISIBLE);
	}
	
	

	public class RefreshTopicListTask extends AsyncTask<Void, Void, Integer> {
		
		//后台从网络拉一列数据，进行添加
		@Override
		protected Integer doInBackground(Void... params) {
			
			
			
			 // 先读本地
            try {
                MJSONArray mjarr = new MJSONArray(SpMethods.loadString(getApplicationContext(), SpMethods.ABOUT_STR_LIST));
                urlList.clear();
                for (int i = 0; i < mjarr.length(); i++)
                	urlList.add(mjarr.getMJSONObject(i).getString("site"));
                viewPager.getAdapter().notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 如果强制联网刷新 或者 数据集为空
                MJSONObject result = DoUserSever.CommonPicGet(2);
                if (result != null && result.getInt("code", 9) == 0) {
                    // 联网刷新成功
                    MJSONArray mjarr = result.getMJSONArray("data");
//                    DongSpMethods.saveLong(getApplicationContext(), DongSpMethods.FRIEND_LIST_UPDATE_TIME, System.currentTimeMillis());
                    SpMethods.saveString(getApplicationContext(), SpMethods.ABOUT_STR_LIST, mjarr.toString());
                    urlList.clear();
                    for (int i = 0; i < mjarr.length(); i++)
                    	urlList.add(mjarr.getMJSONObject(i).getString("site"));
            
		}
			return mDip;
		
		}
		
		
		

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}




		//更新界面
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			setRefreshComplete();
		}

		
	}
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
