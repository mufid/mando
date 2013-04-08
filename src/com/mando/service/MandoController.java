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
		// Saya nemu cara buat dapetin lokasi dari semua sumber yang ada,
		// kalau-kalau GPSnya lagi mati.
		// Modifikasi dari kode di http://stackoverflow.com/a/4736240

		List<String> locProviders = locationManager.getProviders(true);
		Location location;

		for (int i = locProviders.size() - 1; i >= 0; i--) {
			location = locationManager
					.getLastKnownLocation(locProviders.get(i));
			if (location != null) {
				return "Latitude : " + location.getLatitude()
						+ "\nLongitude : " + location.getLongitude();
			}
		}

		return "Lokasi gagal didapat";
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
