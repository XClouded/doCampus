package com.doschool.methods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.doschool.app.DoschoolApp;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class BitmapIOMethod {
	public static Bitmap compressImageFromFile(String srcPath, int defination) {
		if (defination == 2)
			return compressImageFromFilePrivate(srcPath, 1600, 4800);
		else if (defination == 1)
			return compressImageFromFilePrivate(srcPath, 1200, 1200);
		else
			return compressImageFromFilePrivate(srcPath, 600, 600);
	}

	private static Bitmap compressImageFromFilePrivate(String srcPath, int ww, int hh) {
		
		Log.v("srcPath="+srcPath, "vvvvb");
		srcPath=srcPath.replace("file://", "");
		
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	public static String compressBmpToFile(Bitmap bmp, int defination) {
		File file = new File(PathMethods.getAppTempPath(), System.currentTimeMillis() + DoschoolApp.BTF);
		int maxBytes = 50;
		if (defination == 2)
			maxBytes = 400;
		else if (defination == 1)
			maxBytes = 150;
		else
			maxBytes = 60;
		return compressBmpToFile(bmp, file, maxBytes);
	}

	private static String compressBmpToFile(Bitmap bmp, File file, int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;// 个人喜欢从80开始,
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > kb) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			if (options == 20)
				break;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Scheme.FILE.wrap(file.getPath());
	}
}
