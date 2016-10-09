package com.lichfaker.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author LichFaker
 *
 */
public class PreferenceUtils {

	private static PreferenceUtils mInstance = null;
	private static final String SP_FILE_NAME = "config";

	private static SharedPreferences mPreference = null;

	public static PreferenceUtils getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new PreferenceUtils(context);
		}
		return mInstance;
	}

	private PreferenceUtils(Context context) {
		mPreference = context.getSharedPreferences(SP_FILE_NAME,
				Activity.MODE_PRIVATE);
	}

	public void save(String key, Object value) {
		Editor e = mPreference.edit();
		if (value instanceof String) {
			e.putString(key, (String) value);
		} else if (value instanceof Integer) {
			e.putInt(key, (int) value);
		} else if (value instanceof Boolean) {
			e.putBoolean(key, (boolean) value);
		} else if (value instanceof Float) {
			e.putFloat(key, (float) value);
		}
		e.commit();
	}
	
	public String getString(String key, String defValue) {
		return mPreference.getString(key, defValue);
	}
	
	public int getInt(String key, int defValue) {
		return mPreference.getInt(key, defValue);
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		return mPreference.getBoolean(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return mPreference.getFloat(key, defValue);
	}
    
}
