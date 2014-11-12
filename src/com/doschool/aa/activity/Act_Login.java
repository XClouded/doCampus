package com.doschool.aa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.entity.User;
import com.doschool.methods.DoMethods;

/**
 * 注册界面 完成度99%
 * 
 * @author 是我的海
 * 
 */
public class Act_Login extends Act_CommonOld implements OnClickListener {

	EditText etAccount;
	EditText etForget;
	Button btLogin;
	@Override
	public void initData() {
		ACTIONBAR_TITTLE = "登    陆";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(mDip * 16, mDip * 16, mDip * 16, 0);

		etAccount = WidgetFactory.createEditText(this, "请输入学号", 0, 0);
		etAccount.setSingleLine();

		etForget =WidgetFactory.createEditText(this, "请输入密码", 0, 0);
		etForget.setSingleLine();
		etForget.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


		if(!User.loadFunId(getApplicationContext()).equals("CU002"))
		{
		etAccount.setText(User.loadFunId(getApplicationContext()));
		etForget.setText(User.loadPassword(this));
		}
		
		btLogin = WidgetFactory.createButtonWithMainStyle(this, "登       录");
		btLogin.setGravity(Gravity.CENTER);
		btLogin.setOnClickListener(this);

		mParentLayout.addView(etAccount, lp);
		mParentLayout.addView(etForget, lp);
		mParentLayout.addView(btLogin, lp);

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.act_login, menu);
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
		case R.id.forget:
			startActivityForResult(new Intent(Act_Login.this, Act_Forget.class), 0);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (etAccount.getText().toString().length() == 0 || etForget.getText().toString().length() == 0)
			DoMethods.showToast(this, "童鞋，你账号或密码还空着哟");
		else {
			User.saveLoginInfo(getApplicationContext(), etAccount.getText().toString(), etForget.getText().toString(), System.currentTimeMillis());
			setResult(RESULT_OK);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case RESULT_OK:
			User.saveLoginInfo(getApplicationContext(), etAccount.getText().toString(), etForget.getText().toString(), System.currentTimeMillis());
			setResult(RESULT_OK);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}

	}
}
