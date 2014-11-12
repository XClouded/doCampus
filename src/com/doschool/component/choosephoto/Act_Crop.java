package com.doschool.component.choosephoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.doschool.app.MySession;
import com.doschool.methods.BitmapIOMethod;
import com.doschool.methods.DoMethods;
import com.doschool.methods.PathMethods;
import com.edmodo.cropper.CropImageView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
public class Act_Crop extends Act_CommonOld {

	private int ratioX = 1;
	private int ratioY = 1;
	private int defination = 0;
	private boolean fixed = false;
	private static String sourcePath;
	private static final int ON_TOUCH = 1;
	private static final int ROTATE_NINETY_DEGREES = 90;

	Bitmap croppedImage;

	CropImageView cropImageView;

	@Override
	protected void initData() {
		ACTIONBAR_TITTLE = "裁剪图片";
		fixed = getIntent().getBooleanExtra("fixed", false);
		if (fixed) {
			ratioX = getIntent().getIntExtra("ratioX", 1);
			ratioY = getIntent().getIntExtra("ratioY", 1);
			defination = getIntent().getIntExtra("defination", 0);
		}
		sourcePath = getIntent().getStringExtra("sourcePath");

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.black));
		setContentView(R.layout.act_crop);
		cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		cropImageView.setFixedAspectRatio(fixed);
		cropImageView.setAspectRatio(ratioX, ratioY);

		Bitmap photo1 = BitmapIOMethod.compressImageFromFile(sourcePath, defination);
		cropImageView.setImageBitmap(photo1);

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
			Bitmap photo1 = cropImageView.getCroppedImage();
			String path=BitmapIOMethod.compressBmpToFile(photo1, defination);
			Intent it = new Intent();
			it.putExtra("crop_file", path);
			setResult(RESULT_OK, it);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
