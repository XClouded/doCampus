package com.doschool.component.choosephoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.ProgressOperateImageView;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Microblog;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.methods.FileMethods;
import com.doschool.methods.PathMethods;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

public class Act_PicPreview extends Act_CommonOld {
	ArrayList<ImageView> ivList;
	LinearLayout llBigPic;
	LinearLayout llPicGroup;
	ArrayList<ProgressOperateImageView> photoViewList;
	ViewPager viewPager ;
	private ArrayList<String> mPathList = new ArrayList<String>();
	int order;
	PagerAdapter pagerAdapter;
	@Override
	public void initData() {
		

		mPathList = getIntent().getBundleExtra("bundle").getStringArrayList("pathList");
		order = getIntent().getIntExtra("order", 0);
		
		ACTIONBAR_TITTLE = "";
		FileMethods.createDirectory(PathMethods.getAppSavePath());
		photoViewList = new ArrayList<ProgressOperateImageView>();
		for (int i = 0; i < mPathList.size(); i++) {
			ProgressOperateImageView pv = new ProgressOperateImageView(this, 200);
			photoViewList.add(pv);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.black));
		
		mParentLayout.setBackgroundColor(Color.BLACK);
		viewPager = new ViewPager(this);
		viewPager.setBackgroundColor(Color.BLACK);
		mParentLayout.addView(viewPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		pagerAdapter=new PagerAdapter() {

			public View instantiateItem(ViewGroup container, int position) {
				ProgressOperateImageView photoView = photoViewList.get(position);
				container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				
				if(position==order)
				{
					String tittle="    "+(order+1)+"/"+mPathList.size();
					getActionBar().setTitle(tittle);
				final ProgressOperateImageView POIV = photoViewList.get(order);
				
				
				DoschoolApp.newImageLoader.displayImage(mPathList.get(order), 
						POIV.mImageView, 
						DoschoolApp.dioGallery, 
						new ImageLoadingListener() {

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
				return mPathList.size();
			}
			@Override

			public int getItemPosition(Object object) {

			return POSITION_NONE;

			}
		};
		viewPager.setAdapter(pagerAdapter);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				String tittle="    "+(arg0+1)+"/"+mPathList.size();
				getActionBar().setTitle(tittle);
				
				final ProgressOperateImageView POIV = photoViewList.get(arg0);
				

				DoschoolApp.newImageLoader.displayImage(mPathList.get(arg0), 
						POIV.mImageView, DoschoolApp.dioGallery, new ImageLoadingListener() {

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
		
		viewPager.setCurrentItem(order);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.act_pic_preview, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		Intent it = new Intent();
		Bundle b = new Bundle();
		b.putStringArrayList("pathList", mPathList);
		it.putExtra("bundle", b);
		setResult(RESULT_OK, it);
		finish();
		
		this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		super.onBackPressed();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			Intent it = new Intent();
			Bundle b = new Bundle();
			b.putStringArrayList("pathList", mPathList);
			it.putExtra("bundle", b);
			setResult(RESULT_OK, it);
			finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.save:
			
			new AlertDialog.Builder(Act_PicPreview.this)
			.setTitle("删除照片？")
			.setNegativeButton("取消", null)
			.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					int itemBefore=viewPager.getCurrentItem();
					mPathList.remove(itemBefore);
					viewPager.setAdapter(pagerAdapter);

					if(viewPager.getChildCount()==0)
					{
						Intent it = new Intent();
						Bundle b = new Bundle();
						b.putStringArrayList("pathList", mPathList);
						it.putExtra("bundle", b);
						setResult(RESULT_OK, it);
						finish();
						
						Act_PicPreview.this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
					}
					else if(itemBefore==viewPager.getChildCount())
					{
						viewPager.setCurrentItem(viewPager.getChildCount()-1);
						
					}
					else
					{

						viewPager.setCurrentItem(itemBefore);
					}
				}
			}).show();
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static boolean saveMyBitmap(Bitmap mBitmap) {

		File f = new File(PathMethods.getAppSavePath(), System.currentTimeMillis() + ".jpeg");
		try {
			f.createNewFile();
		} catch (IOException e) {
			return false;
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;

	}

}