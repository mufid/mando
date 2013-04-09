package com.mando.service;

import android.location.LocationManager;

import com.mando.helper.CallbackLocation;
import com.mando.helper.SMS;

/**
 * Kerjain yang parser SMS
 * 
 * @author Mufid
 * 
 */
public class MandoController {

	/**
	 * Parsing SMS yang masuk sekaligus menjalankan fungsi SMS yang masuk
	 * 
	 * @param s
	 */
	public static void processSMS(String s) {
		// Ini yang ada di UCRS pertama
	}

	public static String getContacts(String s) {
		// TODO
		return null;
	}

	public static String getLocation(LocationManager locationManager,
			CallbackLocation locationListener) {
		final long maxTime = 1000 * 5 * 60;
		final long interval = 1000 * 1;

		String provider = LocationManager.GPS_PROVIDER;
		locationManager
				.requestLocationUpdates(provider, 0, 0, locationListener);

		return "";
	}

	public static String forwardSMS(SMS message) {
		// TODO
		return null;
	}

	public static String receiveSMS(int count) {
		// TODO
		return null;
	}

	public static String recordSound() {
		return null;
	}
}
