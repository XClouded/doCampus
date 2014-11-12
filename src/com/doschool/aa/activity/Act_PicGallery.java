package com.doschool.aa.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
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
import com.doschool.methods.DoMethods;
import com.doschool.methods.FileMethods;
import com.doschool.methods.PathMethods;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

public class Act_PicGallery extends Act_CommonOld {
	public Microblog thisBlogdata;
	ArrayList<ImageView> ivList;
	int order;
	LinearLayout llBigPic;
	LinearLayout llPicGroup;
	ArrayList<ProgressOperateImageView> photoViewList;

	
	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "";
		thisBlogdata = (Microblog) getIntent().getSerializableExtra("data");
		order = getIntent().getIntExtra("order", 0);
		FileMethods.createDirectory(PathMethods.getAppSavePath());
		photoViewList = new ArrayList<ProgressOperateImageView>();
		for (int i = 0; i < thisBlogdata.imageUrlListHD.size(); i++) {
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
		ViewPager viewPager = new ViewPager(this);
		viewPager.setBackgroundColor(Color.BLACK);
		mParentLayout.addView(viewPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewPager.setAdapter(new PagerAdapter() {

			public View instantiateItem(ViewGroup container, int position) {
				ProgressOperateImageView photoView = photoViewList.get(position);
				container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				
				if(position==order)
				{
					String tittle="    "+(order+1)+"/"+thisBlogdata.imageUrlListHD.size();
					getActionBar().setTitle(tittle);
				final ProgressOperateImageView POIV = photoViewList.get(order);
				DoschoolApp.newImageLoader.displayImage(thisBlogdata.imageUrlListHD.get(order), POIV.mImageView, DoschoolApp.dioGallery, new ImageLoadingListener() {

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
				return thisBlogdata.imageUrlListHD.size();
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				String tittle="    "+(arg0+1)+"/"+thisBlogdata.imageUrlListHD.size();
				getActionBar().setTitle(tittle);
				
				final ProgressOperateImageView POIV = photoViewList.get(arg0);
				DoschoolApp.newImageLoader.displayImage(thisBlogdata.imageUrlListHD.get(arg0), POIV.mImageView, DoschoolApp.dioGallery, new ImageLoadingListener() {

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
		inflater.inflate(R.menu.act_gallery, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			this.finish();
			this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.save:
			new savePicture().execute();
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

	class savePicture extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog progressDialog;

		protected void onPreExecute() {
			// 转圈Dialogue

			progressDialog = new ProgressDialog(Act_PicGallery.this);
			progressDialog.setMessage("极速保存图片中!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			Bitmap bm = DoschoolApp.newImageLoader.loadImageSync(thisBlogdata.imageUrlListHD.get(order));
			return saveMyBitmap(bm);

		}

		@Override
		protected void onPostExecute(Boolean state) {
			progressDialog.dismiss();
			if (state)
				DoMethods.showToast(Act_PicGallery.this, "文件已经被报存到SD卡中com.doschool.missan/save目录下");
			else
				DoMethods.showToast(Act_PicGallery.this, "保存失败了...");

		}
	}

}