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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 注册界面 完成度99%
 * 
 * @author 是我的海
 * 
 */
public class Act_Forget extends Act_CommonOld {

	private static final int STEP_CHECK = 1;
	private static final int STEP_CHANGE = 2;

	private EditText etFunId, etFunPassword, etPassword, etPasswordAgain;
	private String funId, funPasswd, usrPasswd;

	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "忘记密码";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		etFunId = WidgetFactory.createEditText(this, "输入学号", 0, 0);
		etFunId.setSingleLine();

		etFunPassword = WidgetFactory.createEditText(this, "输入教务密码", 0, 0);
		etFunPassword.setSingleLine();
		etFunPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		etPassword = WidgetFactory.createEditText(this, "输入新密码：6-12位", 0, 0);
		etPassword.setSingleLine();
		etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		etPasswordAgain = WidgetFactory.createEditText(this, "确认新密码", 0, 0);
		etPasswordAgain.setSingleLine();
		etPasswordAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		TextView mtvTip = WidgetFactory.createTextView(this, "社团及学生组织忘记密码后\n请联系doBell团队", getResources().getColor(R.color.fzd_grey), 0);
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(mDip * 16, mDip * 16, mDip * 16, 0);
		mParentLayout.addView(etFunId, lp);
		mParentLayout.addView(etFunPassword, lp);
		mParentLayout.addView(etPassword, lp);
		mParentLayout.addView(etPasswordAgain, lp);

		mParentLayout.addView(mtvTip, lp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.act_forget, menu);
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
			funId = etFunId.getText().toString().toUpperCase();
			funPasswd = etFunPassword.getText().toString();
			usrPasswd = etPassword.getText().toString();
			if (usrPasswd.length() < 3) {
				DoMethods.showToast(Act_Forget.this, "您的密码太短了");
			} else if (!usrPasswd.equals(etPasswordAgain.getText().toString())) {
				DoMethods.showToast(Act_Forget.this, "两次密码不一致");
			} else
				new FindPasswordTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class FindPasswordTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;
		int Step;

		protected void onPreExecute() {

			// 转圈Dialogue
			progressDialog = new ProgressDialog(Act_Forget.this);
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
			jObj = DoUserSever.UserCheck(funId, funPasswd);
			code = jObj.getInt("code", 9);
			if (code == 0 || code == 2)
				;
			else
				return code;

			// 注册用户
			Step = STEP_CHANGE;
			jObj = DoUserSever.UserAlterPassword(funId, usrPasswd);
			code = jObj.getInt("code", 9);
			if (code == 0) {
				SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
				sp.edit().putString("funId", funId).commit();
				sp.edit().putString("usrPasswd", usrPasswd).commit();
			}
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (Step == STEP_CHECK) {
				if (result == 1)
					DoMethods.showToast(Act_Forget.this, "学号或教务密码出错");
				else
					DoMethods.showToast(Act_Forget.this, "教务系统出现异常(⊙o⊙)…");
			} else if (Step == STEP_CHANGE) {
				if (result == 0) {
					DoMethods.showToast(Act_Forget.this, "修改成功O(∩_∩)O~~");
					User.saveLoginInfo(Act_Forget.this, funId, usrPasswd, System.currentTimeMillis());
					Intent intent = new Intent();
					intent.setClass(Act_Forget.this, Act_Wele.class);
					setResult(RESULT_OK);
					finish();
					overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				} else
					DoMethods.showToast(Act_Forget.this, "服务君出问题了咕╯﹏╰");
			}

		}
	}
}
