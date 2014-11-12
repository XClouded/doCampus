package com.doschool.aa.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class NetWorkUtils {

	public static String remoteServer(String postURL, String postParams) {
		String jsonStr = post(postURL, postParams);
		JSONObject o = null;
		if (jsonStr == null || jsonStr.equals("")) {
			return "EnetWork";
		}
		try {
			o = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
			return "Ejson";
		}

		try {
			if (o.getString("msg").equals("PASSWORD WRONG")) {
				return "Epass";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "Ejson";
		}

		try {
			if (!o.getString("msg").equals("SUCCESS")) {
				return "Emsg";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "Ejson";
		}
		return jsonStr;
	}
	
	
	
	private static String md5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
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

	
	private static final String TAG = "NetWork";

	private static  String AES(String params,String time,int mode) throws Exception{
		
        String key = md5(time+"IloveDobell");
        key = key.substring(0, 32).toLowerCase();
        Log.i(TAG,"md5:key:"+key);
        String iv = "WeAreTheBestInWD";
//        iv = iv.substring(0, 16).toLowerCase();
        Log.i(TAG,"md5:iv:"+iv);
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
        if(mode==0){
        	cipher.init(Cipher.ENCRYPT_MODE, keyspec,ivspec);
        }
        else{
        	cipher.init(Cipher.DECRYPT_MODE, keyspec,ivspec);

        }
        byte[] result = null;
        result = cipher.doFinal(plaintext);
        Log.i("AESAA",new String(result));
        if(mode==0){
        	params = new String(Base64.encode(result,Base64.NO_WRAP)).replace("+", "-").replace("/", "_").replace("=", "");
        	Log.i(TAG,params);
        }
        return params;
	}
	
	private static String getTime(){
		return postWithTime("http://doschool1.duapp.com/AES/getTime.php","","");
	}
	
	public static String post(String function, String params) {
		String time = getTime();
		String aesp=null;
		try {
			Log.i(TAG,params);
			if(function.contains("NOAES") || function.contains("getWeek") )
				aesp = params;
			else
				aesp="st="+AES(params,time,0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return postWithTime(function,aesp,time);
	}
	
	private static String postWithTime(String function, String params,String time) {
		if(!time.equals("")){
			params+="&time="+time;
		}
		HttpURLConnection connection = null;				
		Log.i(TAG, "call---->" + function  );
		try {
			Log.i(TAG, "call---->"  + function  );
			Log.i(TAG, "with---->" + params);
			URL url = new URL( function );
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			OutputStreamWriter streamWriter = new OutputStreamWriter(
					connection.getOutputStream() );
			streamWriter.write(params);
			streamWriter.flush();
			streamWriter.close();

			BufferedReader bufferReader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String returnString = "";
			String temp;
			while ((temp = bufferReader.readLine()) != null) {
				returnString += temp;
			}
			bufferReader.close();
			
			Log.i(TAG, "returnNOAES-->" + returnString);
			if (returnString.startsWith("\ufeff")) {
				returnString = returnString.substring(1);
			}
			return returnString.trim();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "error--->");
			return "";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}


}
