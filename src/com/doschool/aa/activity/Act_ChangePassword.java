package com.doschool.aa.activity;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 注册界面 完成度99%
 * 
 * @author 是我的海
 * 
 */
public class Act_ChangePassword extends Act_CommonOld {

	private static final int STEP_CHECK = 1;
	private static final int STEP_CHANGE = 2;

	private EditText etOld, etNew, etPasswordAgain;
	private String funId, oldPasswd, newPasswd;

	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "修改密码";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		etOld = WidgetFactory.createEditText(this, "输入原密码", 0, 0);
		etOld.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		etNew = WidgetFactory.createEditText(this, "输入新密码：6-12位", 0, 0);
		etNew.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		etPasswordAgain = WidgetFactory.createEditText(this, "确认新密码", 0, 0);
		etPasswordAgain.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(mDip * 16, mDip * 16, mDip * 16, 0);
		mParentLayout.addView(etOld, lp);
		mParentLayout.addView(etNew, lp);
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
			this.overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
			break;
		case R.id.confirm:
			oldPasswd = etOld.getText().toString();
			newPasswd = etNew.getText().toString();
			if (oldPasswd.length() < 3) {
				DoMethods.showToast(this, "您的原密码不可能这么短");
			} else if (newPasswd.length() < 3) {
				DoMethods.showToast(this, "您的密码太短了,太短了...");
			} else if (!newPasswd.equals(etPasswordAgain.getText().toString())) {
				DoMethods.showToast(this, "两次密码不一致");
			} else
				new FindPasswordTask()
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class FindPasswordTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;
		int Step;

		protected void onPreExecute() {

			// 转圈Dialogue
			progressDialog = new ProgressDialog(Act_ChangePassword.this);
			progressDialog.setMessage("正在修改密码");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			MJSONObject jObj = null;
			int code;
			// 检测用户
			Step = STEP_CHECK;
			jObj = DoUserSever.UserLogin(DoschoolApp.thisUser.funId, oldPasswd);
			code = jObj.getInt("code", 9);
			if (code == 0)
				;
			else
				return code;

			// 注册用户
			Step = STEP_CHANGE;
			jObj = DoUserSever.UserAlterPassword(DoschoolApp.thisUser.funId,
					newPasswd);
			code = jObj.getInt("code", 9);
			if (code == 0) {
				SharedPreferences sp = getSharedPreferences("autoLogin",
						MODE_PRIVATE);
				sp.edit().putString("funId", funId).commit();
				sp.edit().putString("usrPasswd", newPasswd).commit();
			}
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (Step == STEP_CHECK) {
				if (result == 9)
					DoMethods.showToast(Act_ChangePassword.this, "服务器君出问题了");
				else
					DoMethods.showToast(Act_ChangePassword.this, "原密码错误T_T");
			} else if (Step == STEP_CHANGE) {
				if (result == 0) {
					DoMethods.showToast(Act_ChangePassword.this, "修改成功╮(╯▽╰)╭");
					User.saveLoginInfo(getApplicationContext(), funId,
							newPasswd, System.currentTimeMillis());
					setResult(RESULT_OK);
					finish();
					overridePendingTransition(R.anim.in_from_left,
							R.anim.out_to_right);
				} else
					DoMethods.showToast(Act_ChangePassword.this, "服务器君出问题了T_T");
			}

		}
	}

}
