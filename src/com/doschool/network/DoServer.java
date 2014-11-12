package com.doschool.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;

import android.util.Base64;
import android.util.Log;

import com.doschool.app.DoschoolApp;
import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONObject;

/**
 * 服务器助手类
 * 
 * @author xxx
 * 
 */
public class DoServer {
	public final static boolean DEBUG = true;
	public final static String TAG = "DoServer";
	public final static int SCHOOLID = 1;
	public final static int TERMINALID = 1;
	public final static int VERSIONCODE = DoschoolApp.versionCode;

	public static final int NETWORK_ERROR = 9;

	public static MJSONObject callSevice(String folder, String function, String params) {

		// 获得网址前缀
		if (DoschoolApp.SERVER_URL == null) {
			try {
				MJSONObject jobj = format(new MJSONObject(httpPost("http://commsev4all.duapp.com", "common", "SchoolConfigGet", "schoolId=" + SCHOOLID + " & terminalId=" + TERMINALID + " & version="
						+ VERSIONCODE)));
				DoschoolApp.SERVER_URL = jobj.getJSONObject("data").getString("url");
			} catch (JSONException e1) {
				DoschoolApp.SERVER_URL = null;
				return networkError();
			}
		}

		// 获得时间;
		if (DoschoolApp.isTimeGet == false) {
			try {
				long serverTime = Long.valueOf(httpPost("http://commsev4all.duapp.com", "common", "getTime", ""));
				long phoneTime = System.currentTimeMillis() / 1000;
				DoschoolApp.SERVER_TIME_FWQ_LINGXIAN_PHONE = serverTime - phoneTime;
				DoschoolApp.isTimeGet = true;

			} catch (Exception e) {
				DoschoolApp.isTimeGet = false;
				return networkError();
			}
		}

		// 加密
		long phoneTime = System.currentTimeMillis() / 1000;
		String time = (phoneTime + DoschoolApp.SERVER_TIME_FWQ_LINGXIAN_PHONE) + "";
		String aesp = null;
		try {
			params = params + "&schoolId=" + SCHOOLID;
			aesp = "st=" + AES(params, time, 0);
			aesp += "&time=" + time;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (DEBUG)
			Log.d(TAG, "posturl-->" + DoschoolApp.SERVER_URL + "/" + folder + "/" + function + ".php");
		if (DEBUG)
			Log.d(TAG, "params-->" + params);
		// 调用服务
		try {
			return format(new MJSONObject(httpPost(DoschoolApp.SERVER_URL, folder, function, aesp)));
		} catch (JSONException e) {
			return networkError();
		}
	}

	public static MJSONObject callFileSevice(String folder, String function, String filePath) {

		// 获得时间戳
		String url = "";
		try {
			MJSONObject jobj = format(new MJSONObject(httpPost("http://commsev4all.duapp.com", "common", "SchoolConfigGet", "schoolId=" + SCHOOLID + " & terminalId=" + TERMINALID + " & version="
					+ VERSIONCODE)));
			url = jobj.getJSONObject("data").getString("url");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return httpFile(url, folder, function, filePath);

	}

	
	protected static String httpPost(String severUrl, String folder, String function, String params) {

		HttpURLConnection connection = null;

		try {
			URL url = new URL(severUrl + "/" + folder + "/" + function + ".php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
			streamWriter.write(params);
			streamWriter.flush();
			streamWriter.close();

			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String returnString = "";
			String temp;
			while ((temp = bufferReader.readLine()) != null) {
				returnString += temp;
			}
			bufferReader.close();

			if (returnString.startsWith("\ufeff")) {
				returnString = returnString.substring(1);
			}

			if (DEBUG)
				Log.d(TAG, "return-->" + returnString.trim());
			return returnString.trim();
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG)
				Log.d(TAG, "return-->" + "error");
			DoschoolApp.SERVER_URL = null;
			DoschoolApp.isTimeGet = false;
			return "";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	protected static MJSONObject httpFile(String severUrl, String folder, String function, String filePath) {
		// 换行
		String lineEnd = "\r\n";
		// 附加短线
		String twoHyphens = "--";
		// 分界符
		String boundary = "----WebKitFormBoundarydyVd4OxlaM9Ud3oP";
		String fileName = filePath.substring(filePath.lastIndexOf("/"));

		HttpURLConnection connection = null;
		try {
			Log.d(TAG, "call---->" + severUrl + "/" + folder + "/" + function + ".php");
			Log.d(TAG, "with---->" + filePath);
			URL url = new URL(severUrl + "/" + folder + "/" + function + ".php");
			// 打开文件
			FileInputStream fileInputStream = new FileInputStream(new File(filePath));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// connection.setChunkedStreamingMode(1024);
			// 设置附加参数
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);

			// 新建流
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			// 写文件块信息
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fileName + "\"" + lineEnd);
			dos.writeBytes("Content-Type: image/jpeg" + lineEnd + lineEnd);
			// 写文件
			int bytesAvailable = fileInputStream.available();
			byte[] buffer = new byte[bytesAvailable];
			int bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
			while (bytesRead > 0) {

				Log.d(TAG, "bytesAvailable-->" + bytesAvailable / 1024);
				Log.d(TAG, "bytesRead-->" + bytesRead / 1024);
				dos.write(buffer, 0, bytesAvailable);
				bytesAvailable = fileInputStream.available();
				bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
			}
			fileInputStream.close();
			dos.writeBytes(lineEnd);
			// 写结束分界符
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// 更新关闭流
			dos.flush();
			dos.close();

			// 开始链接并取得返回数据
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer sb2 = new StringBuffer();
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				sb2.append(tmp);
			}
			br.close();
			Log.d(TAG, "return-->" + sb2);
			return format(new MJSONObject(sb2.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "error--->");
			return networkError();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	

	public static MJSONObject format(MJSONObject jObj) {
		try {
			String codeStr = jObj.getString("code");
			codeStr = codeStr.substring(codeStr.length() - 1, codeStr.length());
			int code = Integer.decode(codeStr);
			jObj.put("code", code);
			if (jObj.getString("data").equals("null"))
				jObj.put("data", "");
			return jObj;
		} catch (Exception e) {
			return networkError();
		}
	}

	public static MJSONObject networkError() {
		MJSONObject jObj = null;
		try {
			jObj = new MJSONObject();
			jObj.put("code", NETWORK_ERROR);
		} catch (JSONException e) {
		}
		return jObj;
	}

	public static String encoding(String a) {
		try {
			return URLEncoder.encode(a, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			return "";
		}
	}

	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String AES(String params, String time, int mode) throws Exception {

		String key = md5(time + "IloveDobell");
		key = key.substring(0, 32).toLowerCase();
		String iv = "WeAreTheBestInWD";
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		int blockSize = cipher.getBlockSize();

		byte[] dataBytes = params.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}

		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
		if (mode == 0) {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

		} else {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

		}
		byte[] result = null;
		result = cipher.doFinal(plaintext);
		// 加密
		if (mode == 0) {
			params = new String(Base64.encode(result, Base64.NO_WRAP)).replace("+", "-").replace("/", "_").replace("=", "");
		}
		return params;
	}

}
