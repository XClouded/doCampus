package com.doschool.aa.item;

import java.util.Random;

import com.doschool.R;
import com.doschool.aa.activity.Act_EditInfo;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.Scrip;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.network.DoUserSever;




import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 投票广场ListView的Item布局
 * 
 * @author Sea
 */

public class Item_ScriptListView extends LinearLayout {

	Scrip scripData;
	
	RelativeLayout leftLayout;
	LinearLayout rightLayout;
	ImageView leftHead,rightHead;
	TextView leftNick,rightNick;
	TextView leftTime,rightTime;
	TextView leftContent,rightContent;
	
	
	public void setDataRefresh(Scrip scrip,boolean isLoadOnlyFromCache){

		this.scripData=scrip;
		OnClickListener click=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isFriend=false;
				for(int i=0;i<DoschoolApp.friendList.size();i++)
				{
					if(DoschoolApp.friendList.get(i).personId==scripData.author.personId)
					{
						popSendScripDialog();
						isFriend=true;
					}
				}
				if(isFriend==false)
					DoMethods.showToast(getContext(), "你们不是好友，不能发纸条哦");
			}
		};
		
		if(scripData.author.personId==DoschoolApp.thisUser.personId)
		{
			this.setOnClickListener(null);
			leftLayout.setVisibility(View.GONE);
			rightLayout.setVisibility(View.VISIBLE);
			rightNick.setText(ConvertMethods.getShowName(scripData.toPerson));

			rightTime.setText(ConvertMethods.dateLongToDiyStr(ConvertMethods.dateStrToLong(scripData.sendTime))+"悄悄递给");
			if(scripData.content.length()==0)
				rightContent.setText("");
			else
				rightContent.setText(scripData.content);
		}
		else
		{

			this.setOnClickListener(click);
			rightLayout.setVisibility(View.GONE);
			leftLayout.setVisibility(View.VISIBLE);
			DoschoolApp.newImageLoader.displayImage(scripData.author.headUrl, leftHead, DoschoolApp.dioRound);
			leftHead.setOnClickListener(new Click_Person(getContext(), scripData.author));
			
			leftNick.setText(ConvertMethods.getShowName(scripData.author));

			leftTime.setText(ConvertMethods.dateLongToDiyStr(ConvertMethods.dateStrToLong(scripData.sendTime)) );
			if(scripData.content.length()==0)
				leftContent.setText("");
			else
				leftContent.setText(scripData.content);
		}
		
		

		
	}
	
	
	public void popSendScripDialog() {

		final EditText metContent=WidgetFactory.createEditText(getContext(), "纸条内容", R.color.black, 0);
		
		String content=SpMethods.loadString(getContext(), SpMethods.SEND_SCRIPT_CONTENT);
		metContent.setText(content);
		
		
		new AlertDialog.Builder(getContext()).
		setTitle("小纸条").
		setView(metContent).
		setNegativeButton("取消", null).
		setPositiveButton("扔过去", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
            	if(metContent.getText().toString().length()>0)
            	{
            		SpMethods.saveString(getContext(), SpMethods.SEND_SCRIPT_CONTENT, metContent.getText().toString());
            		new sendScripTask(metContent.getText().toString()).execute();
            	}
            	else
            	{
            		popSendScripDialog();
            	}
            }
        }).show();


	}
	
	
	class sendScripTask extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progressDialog;
		String content;

		public sendScripTask(String rs)
		{
			content=rs;
		}
		
		protected void onPreExecute() {
			// 转圈Dialogue
			
			progressDialog = new ProgressDialog(getContext());
			progressDialog.setMessage("正在偷偷扔小纸条!");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			return DoUserSever.UserScripSend(DoschoolApp.thisUser.personId, scripData.author.personId,content).getInt("code",9);

		}

		@Override
		protected void onPostExecute(Integer state) {
			progressDialog.dismiss();
			// 处理各状态
			if (state ==0) {
				
				SpMethods.saveString(getContext(), SpMethods.SEND_SCRIPT_CONTENT, "");

				DoMethods.showToast(getContext(), "发送成功！");
				
			} else {
				DoMethods.showToast(getContext(), "网络出错T_T");
				popSendScripDialog();
			}

		}
	}
	
	public Item_ScriptListView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_script, this);
		leftLayout=(RelativeLayout) findViewById(R.id.leftLayout);
		rightLayout=(LinearLayout) findViewById(R.id.rightLayout);
		leftHead=(ImageView) findViewById(R.id.leftHead);
		leftNick=(TextView) findViewById(R.id.leftNick);
		rightNick=(TextView) findViewById(R.id.rightNick);
		leftContent=(TextView) findViewById(R.id.leftContent);
		rightContent=(TextView) findViewById(R.id.rightContent);
		leftTime=(TextView) findViewById(R.id.leftTime);
		rightTime=(TextView) findViewById(R.id.rightTime);

		leftContent.setAutoLinkMask(Linkify.ALL); 
		leftContent.setMovementMethod(LinkMovementMethod.getInstance()); 
		rightContent.setAutoLinkMask(Linkify.ALL); 
		rightContent.setMovementMethod(LinkMovementMethod.getInstance()); 
	}
	

}
