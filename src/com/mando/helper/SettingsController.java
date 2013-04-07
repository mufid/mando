package com.mando.helper;

import java.util.HashSet;
import java.util.Hashtable;

import android.content.Context;

public class SettingsController {
	private Context c;
	
	public SettingsController(Context c) {
		this.c = c;
	}
	/**
	 * 
	 * @param oldPin
	 * @param newPin
	 * @param newPin2
	 * @return Sebuah hashtable yang berisi key-value pair yang masing-masing
	 * key nya berarti #resource yang ada salah dan String nya adalah
	 * pesan kesalahannya
	 */
	public Hashtable<Integer, String> isValidPIN(String oldPin, String newPin, String newPin2) {
		SettingsHelper.init(c);
	}
}
