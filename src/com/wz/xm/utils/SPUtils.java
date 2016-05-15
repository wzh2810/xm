package com.wz.xm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SPUtils {
	private SharedPreferences	mSp;
	private Editor				mEditor;

	public SPUtils(Context context) {
		mSp = context.getSharedPreferences("zhbj", Context.MODE_PRIVATE);
		mEditor = mSp.edit();
	}

	/**ȡ��string*/
	public String getString(String key, String defValue) {
		return mSp.getString(key, defValue);
	}

	/**ȡ��int*/
	public int getInt(String key, int defValue) {
		return mSp.getInt(key, defValue);
	}

	/**ȡ��boolean*/
	public boolean getBoolean(String key, boolean defValue) {
		return mSp.getBoolean(key, defValue);
	}

	/**����string*/
	public void putString(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	/**����int*/
	public void putInt(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	/**����boolean*/
	public void putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

}

