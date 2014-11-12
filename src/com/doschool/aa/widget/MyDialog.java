package com.doschool.aa.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import com.doschool.R;
import com.doschool.aa.activity.Act_Wele;
//import com.doschool.aa.tools.ActivityYunDongHui;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.Person;
import com.doschool.entity.User;
import com.doschool.entity.Xunzhang;

public class MyDialog {
	

	public static void popHdHeadDialog(Context ctx,Person personInfo) {
		new AlertDialog.Builder(ctx).setTitle(personInfo.nickName+"的名片").
		setItems(new String[] { 
				"姓名："+personInfo.trueName, 
//				"院系："+personInfo.trueName, 
//				"姓名："+personInfo.trueName, 
				"性别："+personInfo.sex, 
				"手机："+personInfo.phoneNumber, 
				"QQ："+personInfo.qq, 
				"邮箱："+personInfo.email, 
				"签名："+personInfo.intro}, null).
		setNegativeButton("关闭", null).show();
		
	}
	

	public static void popXunZhang(Context ctx,Xunzhang xunInfo) {
		
		
		AlertDialog dialog=new AlertDialog.Builder(ctx)
		.setTitle(xunInfo.name)
		.setMessage(xunInfo.intro)
		.setNegativeButton("关闭", null).show();
		
	}
	
	
	
	static Context ctx;
	Intent it;
	
	
	public static void popLogoutDialog(Context ictx) {
		ctx=ictx;
			new AlertDialog.Builder(ctx)
			.setTitle("账号异常")
			.setMessage("您的密码可能被修改了，请重新登录")
			.setNegativeButton("重新登录", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					User.clearAutoLoginInfo(ctx);
					ctx.startActivity(new Intent(ctx, Act_Wele.class));
					((Activity) ctx).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
					((Activity) ctx).finish();
				}
			}).show();
			
		}
	public static void popURGuest(Context ictx) {
		ctx=ictx;
			new AlertDialog.Builder(ctx)
			.setTitle("提示")
			.setMessage("您现在是游客模式，无法进行该操作")
			.setNegativeButton("我知道了", null)
			.setPositiveButton("去注册登录", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					User.clearAutoLoginInfo(ctx);
					ctx.startActivity(new Intent(ctx, Act_Wele.class));
					((Activity) ctx).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
					((Activity) ctx).finish();
				}
			}).show();
			
		}
	
}
