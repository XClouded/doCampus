package com.doschool.methods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.doschool.R;
import com.doschool.app.DoschoolApp;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class DoMethods {

	/**
	 * 获取app的版本号VersionCode
	 */
	public static int getVersionCode(Context context)
	{  
	    try {  
	    	PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
	        return pi.versionCode;  
	    } catch (NameNotFoundException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	        return 0;  
	    }  
	}  
	
	public static String getRandomHintForWrite(Context ctx) {
		String a[] = ctx.getResources().getStringArray(R.array.BlogWriteHintList);
		return a[new Random().nextInt(a.length)];
	}

	public static String getRandomBlogContentWhenNoPic(Context ctx) {
		String a[] = ctx.getResources().getStringArray(R.array.BlogContentWhenNopicList);
		return a[new Random().nextInt(a.length)];
	}
	
	
	public static String getRandomTransContentWhenNoWords(Context ctx) {
		String a[] = ctx.getResources().getStringArray(R.array.TransContentWhenNoWordsList);
		return a[new Random().nextInt(a.length)];
	}
	
	
	public static void showToast(Context ctx, String text) {
		if (ctx != null && text != null) {
			try {
				Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String deleteEmptyLine(String input) {
		String[] ss = input.split("[ \n]+");
		Matcher m = Pattern.compile("[ \n]+").matcher(input);

		int i = 0;
		while (m.find()) {
			if (m.group().contains("\n"))
				ss[i] += "\n";
			else
				ss[i] += " ";
			i++;
		}
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < ss.length; k++) {
			sb.append(ss[k]);
		}

		Log.v("Dong", "then==" + sb.toString());
		return sb.toString();
	}


	public static void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	public static void sleep(int mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
