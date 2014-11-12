package com.doschool.aa.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.activity.Act_PersonPage.ChangeBackTask;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.app.MySession;
import com.doschool.component.choosephoto.Act_Crop;
import com.doschool.component.choosephoto.Act_PhotoChoose;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.methods.PathMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.umeng.analytics.MobclickAgent;

public class Act_EditInfo extends Act_CommonOld implements OnClickListener {

	private static final int ID_CHANGE_HEAD = 2;
	private static final int REQ_CHANGE_HEAD = 2;
	private static final int REQ_CROP_HEAD = 4;

	private static final int AFTER_TAKEPHOTO = 3;
	private static final int CROP_PICTURE = 4;
	private static final int AFTER_CHOOSEPHOTO = 5;

	TvEtLayout llNick, llPhone, llQQ, llEmail, llIntro;
	private String nick, phone, qq, email, intro;
	private ImageView headImg;
	String head_file;
	boolean isPicChanged = false;

	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "修改资料";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mParentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		// 头像
		headImg = new ImageView(this);
		headImg.setId(ID_CHANGE_HEAD);
		headImg.setOnClickListener(this);
		DoschoolApp.newImageLoader.displayImage(DoschoolApp.thisUser.headUrl, headImg, DoschoolApp.dioRound);
		

		headImg.setScaleType(ScaleType.FIT_XY);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(mDip*64, mDip*64);
		lp1.setMargins(0, mDip * 12, 0, mDip * 12);
		mParentLayout.addView(headImg, lp1);

		TvEtLayout llName = new TvEtLayout(this, "姓名：", DoschoolApp.thisUser.trueName);
		llName.setEditable(false);
		llName.editText.setText(DoschoolApp.thisUser.trueName);
		mParentLayout.addView(llName);

		llNick = new TvEtLayout(this, "昵称：", DoschoolApp.thisUser.nickName);
		llNick.editText.setMaxEms(12);
		llNick.editText.setHint("每天只能修改一次名称");
		llNick.editText.setSingleLine();
		llNick.editText.setText(DoschoolApp.thisUser.nickName);
		long lastTime = SpMethods.loadLong(this, SpMethods.LAST_RENAME_TIME, 0);
		long xiangcha = System.currentTimeMillis() - lastTime;
		if (xiangcha / 1000 / 3600  < 24)
			llNick.editText.setEnabled(false);
		mParentLayout.addView(llNick);

		llPhone = new TvEtLayout(this, "手机：", DoschoolApp.thisUser.phoneNumber);
		llPhone.editText.setMaxEms(12);
		llPhone.editText.setSingleLine();
		llPhone.editText.setText(DoschoolApp.thisUser.phoneNumber);
		llPhone.editText.setInputType(InputType.TYPE_CLASS_PHONE);
		mParentLayout.addView(llPhone);

		llQQ = new TvEtLayout(this, "QQ：", DoschoolApp.thisUser.qq);
		llQQ.editText.setMaxEms(20);
		llQQ.editText.setSingleLine();
		llQQ.editText.setText(DoschoolApp.thisUser.qq);
		llQQ.editText.setInputType(InputType.TYPE_CLASS_PHONE);
		mParentLayout.addView(llQQ);

		llEmail = new TvEtLayout(this, "邮箱：", DoschoolApp.thisUser.email);
		llEmail.editText.setMaxEms(40);
		llEmail.editText.setSingleLine();
		llEmail.editText.setText(DoschoolApp.thisUser.email);
		mParentLayout.addView(llEmail);

		llIntro = new TvEtLayout(this, "简介：", DoschoolApp.thisUser.intro);
		llIntro.editText.setMaxEms(100);
		llIntro.editText.setLines(3);
		llIntro.editText.setText(DoschoolApp.thisUser.intro);
		mParentLayout.addView(llIntro);

	}

	
	
	
	class TvEtLayout extends LinearLayout {

		EditText editText;
		TextView textView;

		public TvEtLayout(Context context, String tvStr, String etStr) {
			super(context);
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.setPadding(mDip*16, mDip * 4, mDip*16, mDip * 4);
			textView = WidgetFactory.createTextView(context, tvStr, 0, 0);
			this.addView(textView);
			editText = WidgetFactory.createEditText(context, etStr, 0, 0);
			this.addView(editText, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}

		public void setEditable(boolean editable) {
			editText.setEnabled(editable);
		}
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
			nick = llNick.editText.getText().toString();
			phone = llPhone.editText.getText().toString();
			qq = llQQ.editText.getText().toString();
			email = llEmail.editText.getText().toString();
			intro = llIntro.editText.getText().toString();

			new RegisterTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class RegisterTask extends AsyncTask<Void, Void, Integer> {
		String headName = "";
		ProgressDialog progressDialog;

		protected void onPreExecute() {

			// 转圈Dialogue
			progressDialog = new ProgressDialog(Act_EditInfo.this);
			progressDialog.setMessage("正在修改你的资料!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {

			int code;

			if (isPicChanged) {
				MJSONObject jObj = DoUserSever.UserHeadUpload(head_file);
				code = jObj.getInt("code", 9);
				if (code == 0)
					headName = jObj.getString("data");
				else
					return 1;
			}

			MJSONObject jObj2 = DoUserSever.UserInfoUpdate(DoschoolApp.thisUser.personId, nick, headName, phone, qq, email, intro);
			code = jObj2.getInt("code", 9);
			if (code != 0)
				return 2;

			MJSONObject jObj3 = DoUserSever.UserCompleteInfoGet(DoschoolApp.thisUser.personId, DoschoolApp.thisUser.personId);
			code = jObj3.getInt("code", 9);
			if (code == 0)
				return 4;
			else
				return 3;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			progressDialog.cancel();
			if (result == 1)
				DoMethods.showToast(Act_EditInfo.this, "头像上传出错");
			else if (result == 2)
				DoMethods.showToast(Act_EditInfo.this, "资料上传出错");
			else if (result == 3)
				DoMethods.showToast(Act_EditInfo.this, "拉取资料出错");
			else {
				if (!nick.equals(DoschoolApp.thisUser.nickName))
					;
				SpMethods.saveLong(getApplicationContext(), SpMethods.LAST_RENAME_TIME, System.currentTimeMillis());
				DoMethods.showToast(Act_EditInfo.this, "资料修改成功！");
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}

		}
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case ID_CHANGE_HEAD:
			Intent it=new Intent(Act_EditInfo.this, Act_PhotoChoose.class);
			it.putExtra("maxCount", 1);
			it.putExtra("tittle", "选择头像");
			it.putExtra("autoFinish", true);
			startActivityForResult(it,REQ_CHANGE_HEAD);
			break;
		}

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		

		if (resultCode == RESULT_OK) {
			
			switch (requestCode) {
			
			case REQ_CHANGE_HEAD:
				ArrayList<String> pathList=data.getBundleExtra("bundle").getStringArrayList("pathList");
				int defination=data.getIntExtra("defination", 0);
				
				Intent it=new Intent(this, Act_Crop.class);
				it.putExtra("fixed", true);
				it.putExtra("defination", defination);
				it.putExtra("ratioX", 1);
				it.putExtra("ratioY", 1);
				it.putExtra("sourcePath", pathList.get(0));
				startActivityForResult(it,REQ_CROP_HEAD);
				break;
			case REQ_CROP_HEAD:
				
//				head_file=(File) MySession.getSession().get("crop_file");
				head_file=data.getStringExtra("crop_file");
				DoschoolApp.newImageLoader.displayImage(head_file, headImg, DoschoolApp.dioRound);
				head_file=Scheme.FILE.crop(head_file);
				isPicChanged=true;
				break;
			default:
				break;
			}
		}
		
		
	}

	
}
