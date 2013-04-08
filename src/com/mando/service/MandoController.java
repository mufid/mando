package com.mando.service;

import java.util.List;

import android.location.Location;
import android.location.LocationManager;

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

	public static String getLocation(LocationManager locationManager) {
		List<String> providers = locationManager.getProviders(true);

		String res = "";

		for (int i = 0; i < providers.size(); i++) {
			String provider = providers.get(i);
			Location loc = locationManager.getLastKnownLocation(provider);

			res += provider + "\n";

			if (loc == null) {
				res += "null\n";
			} else {
				res += "Latitude : " + loc.getLatitude() + "\nLongitude : "
						+ loc.getLongitude() + "\n";
			}
		}

		return res + "\b";
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
