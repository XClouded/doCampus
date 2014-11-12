package com.doschool.aa.activity;

import com.doschool.R;
import com.doschool.aa.aa.Act_CommonOld;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.methods.DoMethods;
import com.doschool.network.DoUserSever;
import com.doschool.zother.MJSONObject;
import com.umeng.analytics.MobclickAgent;

import android.app.ProgressDialog;
import android.content.Intent;
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
public class Act_UserCheck extends Act_CommonOld {
	
	private EditText etFunId, etFunPassword;
	private String funId, funPasswd;
	
	@Override
	public void initData() {
		ACTIONBAR_TITTLE="注册";
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//教务账号密码
		etFunId=WidgetFactory.createEditText(this, "输入学号", 0, 0);
		etFunId.setSingleLine();
		
		etFunPassword=WidgetFactory.createEditText(this, "输入教务密码", 0, 0);
		etFunPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);  
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(mDip * 16, mDip * 16, mDip * 16, 0);
		mParentLayout.addView(etFunId, lp);
		mParentLayout.addView(etFunPassword, lp);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.act_usercheck, menu);
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
		case R.id.next:
			funId = etFunId.getText().toString().toUpperCase();
			funPasswd = etFunPassword.getText().toString();
			if(funId.length()<1)
			{
				DoMethods.showToast(Act_UserCheck.this, "你还没填学号呐⊙▽⊙。");
			}
			else if(funPasswd.length()<1)
			{
				DoMethods.showToast(Act_UserCheck.this, "你还没填密码呐⊙▽⊙。");
			}
			else
			{
				new CheckTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	class CheckTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		String usrName;
		String usrSex;
		MJSONObject jResult;
		
		protected void onPreExecute() {
		
			//转圈Dialogue
			progressDialog=new ProgressDialog(Act_UserCheck.this);
			progressDialog.setMessage("正在检测你是否是安大学生");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		

		@Override
		protected Void doInBackground(Void... arg0) {
			jResult = DoUserSever.UserCheck(funId, funPasswd);
			return null;

			
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			int code = jResult.getInt("code", 9);
			String toast = jResult.getString("data");
			if (code == 0) {
				MobclickAgent.onEvent(Act_UserCheck.this, "event_check_succ");
				MJSONObject jdata=jResult.getMJSONObject("data");
				usrName = jdata.getString("name");
				usrSex = jdata.getString("sex");
				Intent it=new Intent(Act_UserCheck.this, Act_Register.class);
				it.putExtra("funId", funId);
				it.putExtra("funPasswd", funPasswd);
				it.putExtra("usrName", usrName);
				it.putExtra("usrSex", usrSex);
				startActivity(it);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				finish();
			} else {
				MobclickAgent.onEvent(Act_UserCheck.this, "event_check_fail");
				if (toast.length() == 0) {
					if (code == 1)
						toast = "学号或密码错误";
					else if (code == 2)
						toast = "该用户已经注册";
					else if (code == 3)
						toast = "学校不存在";
					else if (code == 9)
						toast = "网络服务错误";
				}
				DoMethods.showToast(Act_UserCheck.this, toast);
			}
			
		}
	}
	

}
