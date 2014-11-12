package com.doschool.aa.activity;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 注册界面
 * 完成度99%
 * @author 是我的海
 *
 */
public class Act_Register extends Act_CommonOld {
	
	private EditText etNick, etPassword,etPasswordAgain;
	private String funId,funPasswd,usrName,usrSex, usrNick, usrPasswd;

	@Override
	public void initData() {

		funId=getIntent().getStringExtra("funId");
		funPasswd=getIntent().getStringExtra("funPasswd");
		usrName=getIntent().getStringExtra("usrName");
		usrSex=getIntent().getStringExtra("usrSex");
		ACTIONBAR_TITTLE="注册";
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		etNick=WidgetFactory.createEditText(this, "输入昵称：2-12个字", 0, 0);
		etNick.setSingleLine();
		etNick.setMaxEms(12);
		
		etPassword=WidgetFactory.createEditText(this, "输入密码：6-12位", 0, 0);
		etPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD); 
		
		etPasswordAgain=WidgetFactory.createEditText(this, "确认密码", 0, 0);
		etPasswordAgain.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD); 
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(mDip * 16, mDip * 16, mDip * 16, 0);
		
		mParentLayout.addView(etNick, lp);
		mParentLayout.addView(etPassword, lp);
		mParentLayout.addView(etPasswordAgain, lp);
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
			usrNick = etNick.getText().toString();
			if (usrNick.length() < 2 || usrNick.length() > 12) {
				DoMethods.showToast(Act_Register.this, "昵称应在2～12个字");
				return super.onOptionsItemSelected(item);
			}
			usrPasswd = etPassword.getText().toString();
			if (usrPasswd.length() < 6) {
				DoMethods.showToast(Act_Register.this, "您的密码太短");
				return super.onOptionsItemSelected(item);
			}
			if (!usrPasswd.equals(etPasswordAgain.getText().toString())) {
				DoMethods.showToast(Act_Register.this, "两次密码不一致");
				return super.onOptionsItemSelected(item);
			}
			new RegisterTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	class RegisterTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		MJSONObject jResult;
		
		protected void onPreExecute() {
		
			//转圈Dialogue
			progressDialog=new ProgressDialog(Act_Register.this);
			progressDialog.setMessage("小安正在登记你哟");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		

		@Override
		protected Void doInBackground(Void... arg0) {
			jResult = DoUserSever.UserRegister(funId, usrName, usrNick, usrPasswd, usrSex);
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			int code = jResult.getInt("code", 9);
			String toast = jResult.getString("data");
			if (code == 0) {

				MobclickAgent.onEvent(Act_Register.this, "event_reg_succ");
				SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
				sp.edit().putString("funId", funId).commit();
				sp.edit().putString("usrPasswd", usrPasswd).commit();
				DoMethods.showToast(Act_Register.this, "注册成功");
				User.saveLoginInfo(Act_Register.this, funId, usrPasswd,System.currentTimeMillis());
				
				Intent intent = new Intent();
				intent.setClass(Act_Register.this, Act_Wele.class);
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			} else {

				MobclickAgent.onEvent(Act_Register.this, "event_reg_fail");
				if (toast.length() == 0) {
					if (code == 1)
						toast = "用户已经注册过";
					else if (code == 2)
						toast = "该昵称已经存在";
					else if (code == 9)
						toast = "网络服务错误";
				}
				DoMethods.showToast(Act_Register.this, toast);
			}
			
			
			
		}
	}

}
