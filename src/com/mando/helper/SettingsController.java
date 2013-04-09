package com.mando.helper;

import java.util.Hashtable;

import android.content.Context;

import com.mando.R;

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
	 *         key nya berarti #resource yang ada salah dan String nya adalah
	 *         pesan kesalahannya
	 */
	public Hashtable<Integer, String> checkValidPIN(String oldPin, String newPin,
			String newPin2) {
		SettingsHelper.init(c);
		Hashtable<Integer, String> ht = new Hashtable<Integer, String>();
		// Check the old pin
		if (getCurrentPIN(true) != null && getCurrentPIN(true).length() > 0
				&& !getCurrentPIN(true).equals(oldPin)) {
			ht.put(R.id.pin_current,
					c.getString(R.string.setting_pin_wrongcurrentpin));
		}
		// Check if the required field filled out
		if (newPin.length() == 0) {
			ht.put(R.id.pin_new, String.format(
					c.getString(R.string.setting_fieldrequired),
					R.string.setting_pin_baru));
		}
		// Cek apakah panjang PIN benar-benar 4
		if (newPin.length() != 4) {
			ht.put(R.id.pin_new, c.getString(R.string.setting_pin_lengthmismatch));
		}
		// Check if the required field filled out
		if (newPin2.length() == 0) {
			ht.put(R.id.pin_newagain, String.format(
					c.getString(R.string.setting_fieldrequired),
					R.string.setting_pin_barulagi));
		}
		// Check if the required field filled out
		if (!newPin2.equals(newPin)) {
			ht.put(R.id.pin_newagain,
					c.getString(R.string.setting_pin_notmatch));
		}
		return ht;
	}
	
	public void setPIN(String pin) {
		SettingsHelper.init(c);
		SettingsHelper.store("pin", pin);
	}

	public String getCurrentPIN() {
		String pin = getCurrentPIN(true);
		return pin != null && pin.length() == 4 ? pin : "1234";
	}
	
	public String getCurrentPIN(boolean strict) {
		SettingsHelper.init(c);
		return SettingsHelper.read("pin");
	}
}
