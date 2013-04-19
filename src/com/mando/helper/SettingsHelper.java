package com.mando.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsHelper {
	private static Context context;
	private static String PREFERENCES_NAME = "MandoSettings";
	
    // If in Debug, centang this
    private static boolean isDebug = true;
    
    public static void init(Context c) {
        context = c;
    }

	public static boolean isDebug() {
	    return SettingsHelper.isDebug;
	}
	
	/**
	 * Menyimpan string ke Persistent Storage. Jangan lupa untuk melakukan
	 * init terlebih dahulu
	 * @param key
	 * @param value
	 */
	public static void store(String key, String value) {
		//Modification of the Preferences
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor     Prefeditor = prefs.edit();
		Prefeditor.putString(key, value);
		Prefeditor.commit();
	}
	
	/**
	 * Membaca dari Persisten Storage. Mengembalikan <code>null</code>
	 * apabila preferences belum pernah dibuat. Jangan lupa untuk melakukan
	 * init terlebih dahulu
	 * @param key
	 * @return
	 */
	public static String read(String key) {
		//Reading values from the Preferences
		SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return prefs.getString(key, null);
	}
}
