package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;




import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

/**
 * 投票广场ListView的Item布局
 * 
 * @author Sea
 */

public class Item_PeopleListView extends LinearLayout {

	private Context ctx;
	private int parentWidth = (int) (DoschoolApp.widthPixels * 1.0);
	private int padding = DoschoolApp.pxperdp;
	private int HEAD_SIZE = (int) (parentWidth*0.10);
	SimplePerson personData;
	Button mbtOperate;
	
	public Item_PeopleListView(Context context, SimplePerson person) {
		super(context);
		// 本身初始化
		this.ctx=context;
		this.personData=person;
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(padding, padding, padding, padding);

		//头像
		ImageView ivLeftHead = new ImageView(getContext());

		DoschoolApp.newImageLoader.displayImage(person.headUrl, ivLeftHead, DoschoolApp.dioRound);
		ivLeftHead.setPadding(padding, padding, padding, padding);
		this.addView(ivLeftHead,HEAD_SIZE, HEAD_SIZE);
		
		//用户名
		TextView tvNickName = WidgetFactory.createTextView(context, person.nickName, android.R.color.black, 0);
		tvNickName.setPadding(padding, padding, padding, padding);
		this.addView(tvNickName, (int) (parentWidth*0.65), LinearLayout.LayoutParams.WRAP_CONTENT);
		
		mbtOperate=WidgetFactory.createButton(context, "", R.drawable.btn_style_nocomment, 0, 0);

		mbtOperate.setPadding(0, 0, 0, 0);
		refreshButton();
		this.addView(mbtOperate,(int) (parentWidth*0.20), LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setOnClickListener(new Click_Person(context, personData));
	
	}

	public void refreshButton()
	{
		if(personData.personId==DoschoolApp.thisUser.personId)
		{
			mbtOperate.setText("自己");
			mbtOperate.setBackgroundDrawable(null);
		}
		if(personData.isMyFriend==true)
		{
			mbtOperate.setText("已是好友");
			mbtOperate.setBackgroundDrawable(null);
		}
		else if(personData.doISendFriendRequest)
		{
			mbtOperate.setText("已发请求");
			mbtOperate.setBackgroundDrawable(null);
		}
		else
		{
			mbtOperate.setText("加为好友");
			mbtOperate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					popFriendRequestDialog();
				}
			});
		}
	}
	
	
	//好友申请，删除，等待的弹窗
		public void popFriendRequestDialog() {
			final EditText metReason=WidgetFactory.createEditText(ctx, "填写理由", R.color.black, 0);
					
			String reason=SpMethods.loadString(getContext(), SpMethods.MAKE_FRIEND_REASON);
			metReason.setText(reason);
			
			new AlertDialog.Builder(ctx).
			setTitle("向"+personData.nickName+"发出好友申请？").
			setView(metReason).
			setNegativeButton("取消", null).
			setPositiveButton("发送申请", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int i) {
	            	SpMethods.saveString(getContext(), SpMethods.MAKE_FRIEND_REASON, metReason.getText().toString());
	            	new sendFriendRequestTask(metReason.getText().toString()).execute();
	            }
	        }).show();
		}
		
		
		
		/**** 发送好友请求任务**********/
		class sendFriendRequestTask extends AsyncTask<Void, Void, Integer> {

			ProgressDialog progressDialog;
			String reason;
			
			public sendFriendRequestTask(String rs)
			{
				reason=rs;
			}
			
			protected void onPreExecute(String rs) {
				// 转圈Dialogue
				reason=rs;
				progressDialog = new ProgressDialog(ctx);
				progressDialog.setMessage("正在发出申请!");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected Integer doInBackground(Void... params) {

				return DoUserSever.UserApplySend(DoschoolApp.thisUser.personId, personData.personId,reason).getInt("data",9);

			}

			@Override
			protected void onPostExecute(Integer state) {
				//progressDialog.cancel();
				// 处理各状态
				if (state != 9) {
					DoMethods.showToast(ctx, "搞定啦！");
					personData.friendState = state;
					personData.refreshFlags();
//					UglyTrans.adptPerson.notifyDataSetInvalidated();
				} else {
					DoMethods.showToast(ctx, "网络出错T_T");
					popFriendRequestDialog();
				}

			}
		}
}
