package com.doschool.component.choosephoto;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.activity.Act_Register;
import com.doschool.aa.activity.Act_Write;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.DoMethods;
import com.doschool.methods.PathMethods;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * from which to which
 * 
 * @author Sea
 * 
 */
public class Act_PhotoChoose extends Act_CommonOld implements OnClickListener, OnPicCountChangeListener, OnItemClickListener {

	private final static int SCAN_OK = 1;
	private final static int CLICK_CAMERA = 2;
	private String lastPhotoName;
	private boolean isGetImageDone = false;
	private int maxCount=1;
	private boolean autoFinish=false;

	public static final int START_TYPE_GALLERY = 2;
	public static final int START_TYPE_CAMERA = 3;
	private int startType;

	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private ArrayList<String> mSelectList;

	private ProgressDialog mProgressDialog;

	private LinearLayout llFolder;
	private Button btChangeFolder;
	private Button btChangeDefinition;
	private ListView lvFolder;
	private List<ImageFolder> imageFolderList = new ArrayList<ImageFolder>();
	private FolderAdapter mFolderAdapter;

	private GridView photoGridView;
	private ImageGridAdapter PhotoAdapter;
	private PopupWindow postBlogPopWindow;
	private boolean jumpWhenChooseEnough = false;
	private int defination;
	
	
	@Override
	protected void initData() {

		try {
			mSelectList = getIntent().getBundleExtra("bundle").getStringArrayList("pathList");
		} catch (Exception e) {
			mSelectList = new ArrayList<String>();
			e.printStackTrace();
		}

		maxCount = getIntent().getIntExtra("maxCount", 1);
		autoFinish = getIntent().getBooleanExtra("autoFinish", false);
		
		startType = getIntent().getIntExtra("startType", START_TYPE_GALLERY);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.black));
		onPicCountChange(mSelectList.size());

		llFolder = (LinearLayout) findViewById(R.id.llFolder);
		lvFolder = (ListView) findViewById(R.id.lvFolder);
		btChangeFolder = (Button) findViewById(R.id.btChangeFolder);
		btChangeDefinition = (Button) findViewById(R.id.btChangeDefinition);
		photoGridView = (GridView) findViewById(R.id.photoGridView);

		llFolder.setOnClickListener(this);
		btChangeFolder.setOnClickListener(this);
		btChangeDefinition.setOnClickListener(this);
		photoGridView.setOnScrollListener(new PauseOnScrollListener(DoschoolApp.newImageLoader, true, true));
		lvFolder.setOnItemClickListener(this);


		
		if (startType == START_TYPE_GALLERY)
			getImages();
		else
			{
			if (mSelectList.size() < maxCount) {

				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					lastPhotoName = (System.currentTimeMillis() + "").hashCode() + ".jpg";
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), lastPhotoName));
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CLICK_CAMERA);
				} else {
					DoMethods.showToast(Act_PhotoChoose.this, "没有SD卡");
				}
			} else {
				DoMethods.showToast(Act_PhotoChoose.this, "你最多只能选择" + maxCount + "张图片");

			}
			}

	}

	
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("defination", defination);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        defination = bundle.getInt("defination");
    }
	
	
	@Override
	public void onResume() {
//		SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
//		defination=sp.getInt("defination", 0);
//		if(defination==0)
//			btChangeDefinition.setText("标清");
//		else if(defination==1)
//			btChangeDefinition.setText("高清");
//		else if(defination==2)
//			btChangeDefinition.setText("原图");
		super.onResume();
	}

	@Override
	public void onPause() {
        SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
        sp.edit().putInt("defination", defination).commit();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		boolean isexit=false;;
		if (requestCode == CLICK_CAMERA && resultCode == RESULT_OK) {
			
			Log.v("1", "ppppp");
			if (lastPhotoName != null) {
				Log.v("2", "ppppp");
				Uri source = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), lastPhotoName));
				
				String path= Scheme.FILE.wrap(source.getPath());  

				Log.v("path="+path, "ppppp");
				mSelectList.add(path);
				onPicCountChange(mSelectList.size());
				DoMethods.showToast(Act_PhotoChoose.this, "拍摄的图片已经选中！");
				
			}
			
			if(startType==START_TYPE_CAMERA)
			{
				isexit=true;	
				Intent it = new Intent();
				it.putExtra("defination", defination);
				Bundle b = new Bundle();
				b.putStringArrayList("pathList", mSelectList);
				it.putExtra("bundle", b);
				setResult(RESULT_OK, it);
				finish();
			}
			
		}
		
		
		
		if (isGetImageDone == false && isexit==false)
			getImages();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common_confirm, menu);
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
		case R.id.confirm:
			Intent it = new Intent();
			it.putExtra("defination", defination);
			Bundle b = new Bundle();
			b.putStringArrayList("pathList", mSelectList);
			it.putExtra("bundle", b);
			setResult(RESULT_OK, it);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 完成mGruopMap的数据填充
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			DoMethods.showToast(Act_PhotoChoose.this, "暂无外部存储");
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {
			@Override
			public void run() {

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = Act_PhotoChoose.this.getContentResolver();

				// 只查询jpeg和png的图片
//				Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] { "image/jpeg",
//						"image/png" }, MediaStore.Images.Media.DATE_MODIFIED + " desc");
				Cursor mCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
				String pathFirst = null;

				int lastestNum=0;
				
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
					path= Scheme.FILE.wrap(path);  
//					Log.v("GET_IMAGE_PATH="+path, "vvvvb");
					if (pathFirst == null && path != null) {
						pathFirst = path;
					}
					// 获取该图片的父路径名
					String parentName = new File(path).getParentFile().getName();

					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(path);
					}
					
					if(lastestNum<100)
					{
						if (!mGruopMap.containsKey("最新")) {
							List<String> chileList = new ArrayList<String>();
							chileList.add(path);
							mGruopMap.put("最新", chileList);
						} else {
							mGruopMap.get("最新").add(path);
						}
						lastestNum++;
					}
					
					if (!mGruopMap.containsKey("全部")) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put("全部", chileList);
					} else {
						mGruopMap.get("全部").add(path);
					}

				}
//				ImageFolder mImageBean = new ImageFolder();
//				mImageBean.setFolderName("全部");
//				mImageBean.setChecked(true);
//				mImageBean.setImageCounts(mCursor.getCount());
//				mImageBean.setTopImagePath(pathFirst);// 获取该组的第一张图片
//				imageFolderList.add(mImageBean);
				if(VERSION.SDK_INT < 14) {  
					mCursor.close();  
				 }
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
			}
		}).start();

	}

	/**
	 * 完成mGruopMap的数据填充之后，的响应。
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				// 关闭进度条
				isGetImageDone = true;
				mProgressDialog.dismiss();
				imageFolderList.addAll(subGroupOfImage(mGruopMap));
				mFolderAdapter = new FolderAdapter(Act_PhotoChoose.this, imageFolderList);
				lvFolder.setAdapter(mFolderAdapter);
				List<String> childList = mGruopMap.get(imageFolderList.get(0).getFolderName());

				PhotoAdapter = new ImageGridAdapter(Act_PhotoChoose.this, childList, Act_PhotoChoose.this, mSelectList,maxCount,onCameraClick);
				photoGridView.setAdapter(PhotoAdapter);

				break;
			}
		}
	};

	/**
	 * 完成imageFolderList的数据填充
	 */
	private List<ImageFolder> subGroupOfImage(HashMap<String, List<String>> mGruopMap) {
		if (mGruopMap.size() == 0) {
			return null;
		}
		List<ImageFolder> list = new ArrayList<ImageFolder>();

		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageFolder mImageBean = new ImageFolder();
			String key = entry.getKey();
			List<String> value = entry.getValue();

			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片

			
			if(key.equals("全部"))
			{
//				ImageFolder mImageBean = new ImageFolder();
//				mImageBean.setFolderName("全部");
				mImageBean.setChecked(true);
//				mImageBean.setImageCounts(mCursor.getCount());
//				mImageBean.setTopImagePath(pathFirst);// 获取该组的第一张图片
//				imageFolderList.add(mImageBean);
			}
			
			list.add(mImageBean);
		}
		Collections.sort(list, new SpellComparator());
		return list;
	}

	/**
	 * 文件夹的名字按民称排序
	 */
	class SpellComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				
				String[] prioty=new String[]{"camera","Camera","Screenshots","Screenshot","ScreenShots"};
				String s1 = ((ImageFolder) o1).getFolderName();
				String s2 = ((ImageFolder) o2).getFolderName();
				
				if(s1.equals("全部"))
					return -1;
				if(s2.equals("全部"))
					return 1;
				
				if(s1.equals("最新"))
					return -1;
				if(s2.equals("最新"))
					return 1;
				
				for(int i=0;i<prioty.length;i++)
				{
					if(s1.contains(prioty[i]))
					{
						return -1;
					}
					if(s2.contains(prioty[i]))
					{
						return 1;
					}
				}
				// 运用String类的 compareTo（）方法对两对象进行比较
				return s1.compareTo(s2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btChangeFolder:
			if (llFolder.getVisibility() == View.VISIBLE) {
				folderPanelDismiss();
			} else {
				folderPanelShow();
			}
			break;
		case R.id.btChangeDefinition:
			
			new AlertDialog.Builder(this).
			setItems(new String[]{"标清","高清","原图"}, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					defination=which;
					if(which==0)
					{
						btChangeDefinition.setText("标清");
					}
					else if(which==1)
					{
						btChangeDefinition.setText("高清");
					}
					else if(which==2)
					{
						btChangeDefinition.setText("原图");
					}
				}
			}).create().show();
			break;
		case R.id.llFolder:
			folderPanelDismiss();
			break;
		}
	}

	OnClickListener onCameraClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (mSelectList.size() < maxCount) {

				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					lastPhotoName = (System.currentTimeMillis() + "").hashCode() + ".jpg";
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), lastPhotoName));
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CLICK_CAMERA);
				} else {
					DoMethods.showToast(Act_PhotoChoose.this, "没有SD卡");
				}
			} else {
				DoMethods.showToast(Act_PhotoChoose.this, "你最多只能选择" + maxCount + "张图片");

			}
			
		}
	};
	
	@Override
	public void onPicCountChange(int num) {
		ACTIONBAR_TITTLE = "已选择" + num + "张";
		getActionBar().setTitle(ACTIONBAR_TITTLE);
		
		if(autoFinish && num==maxCount)
		{
			Intent it = new Intent();
			it.putExtra("defination", defination);
			Bundle b = new Bundle();
			b.putStringArrayList("pathList", mSelectList);
			it.putExtra("bundle", b);
			setResult(RESULT_OK, it);
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		btChangeFolder.setText(imageFolderList.get(position).getFolderName() + "  ◢");
		List<String> childList = mGruopMap.get(imageFolderList.get(position).getFolderName());
		for (int i = 0; i < imageFolderList.size(); i++) {
			if (i == position)
				imageFolderList.get(i).setChecked(true);
			else
				imageFolderList.get(i).setChecked(false);

		}
		PhotoAdapter = new ImageGridAdapter(Act_PhotoChoose.this, childList, Act_PhotoChoose.this, mSelectList,maxCount,onCameraClick);
		photoGridView.setAdapter(PhotoAdapter);
		mFolderAdapter.notifyDataSetChanged();
		folderPanelDismiss();
	}

	private void folderPanelDismiss() {
		Animation alphaAnim = new AlphaAnimation(1.0f, 0.0f);
		alphaAnim.setDuration(500);
		llFolder.startAnimation(alphaAnim);

		Animation translateAnim = new TranslateAnimation(0, 0, 0, 1000);
		translateAnim.setDuration(500);
		translateAnim.setFillAfter(true);
		lvFolder.startAnimation(translateAnim);
		llFolder.setVisibility(View.GONE);
	}

	private void folderPanelShow() {
		llFolder.setVisibility(View.VISIBLE);
		Animation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
		alphaAnim.setDuration(500);
		llFolder.startAnimation(alphaAnim);

		Animation translateAnim = new TranslateAnimation(0, 0, DoschoolApp.heightPixels, 0);
		translateAnim.setFillAfter(true);
		translateAnim.setDuration(500);
		lvFolder.startAnimation(translateAnim);
	}

}
