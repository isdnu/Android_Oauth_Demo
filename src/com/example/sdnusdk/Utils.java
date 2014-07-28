package com.example.sdnusdk;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
	//这个相当于一个文件名,
	public static final String SDNUSDKDEMO = "SDNU_SDK_DEMO";
	public static final String TOKENVALUE= "TOKEN_VALUE";	
	
	private static SharedPreferences  preferences;
	public static  String getTokenValue(Context mContext ,String key,int mode){
		 preferences = mContext.getSharedPreferences(SDNUSDKDEMO,mode);
		 String value=preferences.getString(key, "");
		 return value;		
	}
	
	public static  void saveTokenValue(Context mContext ,String value,String key,int mode){
		 preferences = mContext.getSharedPreferences(SDNUSDKDEMO,mode);
		 preferences.edit().putString(key, value).commit();
	}

}
