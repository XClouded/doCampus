package com.doschool.methods;

import java.io.File;

import android.os.Environment;

public class PathMethods {



	public static String getAppRootPath() {
		if (PathMethods.hasSDCard()) {
			return PathMethods.getPhoneRootFilePath() + "com.doschool.missan/";
		} else {
			return PathMethods.getPhoneRootFilePath() + "com.doschool.missan/";
		}
	}

	public static String getAppFileSavePath() {
		if (PathMethods.hasSDCard()) {
			return PathMethods.getAppRootPath() + "downloadfiles/";
		} else {
			return PathMethods.getAppRootPath() + "downloadfiles/";
		}
	}

	public static String getAppTempPath() {
		if (PathMethods.hasSDCard()) {
			return PathMethods.getAppRootPath() + "temp/";
		} else {
			return PathMethods.getAppRootPath() + "temp/";
		}
	}

	public static String getAppSavePath() {
		if (PathMethods.hasSDCard()) {
			return PathMethods.getAppRootPath() + "save/";
		} else {
			return PathMethods.getAppRootPath() + "save/";
		}
	}

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static void deleteFile(String path, String fileName) {
		if (hasSDCard()) {
			try {
				File folder = new File(path);
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().split("\\.")[0].equals(fileName)) {
						files[i].delete();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static String getPhoneRootFilePath() {
		if (hasSDCard()) {
			// filePath:/sdcard/
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		} else {
			// filePath: /data/data/
			return Environment.getDataDirectory().getAbsolutePath() + "/data/";
		}
	}
}
