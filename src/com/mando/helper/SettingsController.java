package com.mando.helper;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;

import com.mando.R;

public class SettingsController {
    private Context c;
    /**
     * Perintah-perintah yang muncul ke pengguna 0: TODO 1: 2:
     */
    private int[] visibleCommands = { 0, 1, 2, 4, 5, 6 };

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
    public Hashtable<Integer, String> checkValidPIN(String oldPin,
            String newPin, String newPin2) {
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
                    c.getString(R.string.setting_pin_baru)));
        }
        // Cek apakah panjang PIN benar-benar 4
        if (newPin.length() != 4) {
            ht.put(R.id.pin_new,
                    c.getString(R.string.setting_pin_lengthmismatch));
        }
        // Check if the required field filled out
        if (newPin2.length() == 0) {
            ht.put(R.id.pin_newagain, String.format(
                    c.getString(R.string.setting_fieldrequired),
                    c.getString(R.string.setting_pin_barulagi)));
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

    public void initCommand() {
        SettingsHelper.init(c);

        SettingsHelper.store("command-0", "teruskan");
        SettingsHelper.store("command-1", "ambil");
        SettingsHelper.store("command-2", "kontak");
        SettingsHelper.store("command-3", "tolong");
        SettingsHelper.store("command-4", "suara");
        SettingsHelper.store("command-5", "lokasi");
        SettingsHelper.store("command-6", "twitter");

        SettingsHelper.store("commandactive", "1111111");
    }

    public boolean getCommandActive(int i) {
        SettingsHelper.init(c);
        String activebin = SettingsHelper.read("commandactive");
        if (activebin == null || activebin.length() == 0
                || i >= activebin.length()) {
            initCommand();
            return true;
        }

        return (activebin.charAt(i) == '1');
    }

    public void setCommandActive(int i, boolean active) {
        StringBuilder x = new StringBuilder();
        SettingsHelper.init(c);
        String activebin = SettingsHelper.read("commandactive");
        x.append(activebin);
        char activechar = active ? '1' : '0';

        if (i >= x.length()) {
            for (int j = 0; j < i; j++) {
                x.append(j == i - 1 ? activechar : '0');
            }
        }

        x.setCharAt(i, activechar);

        SettingsHelper.store("commandactive", x.toString());
    }

    public String getCommandString(int i) {
        SettingsHelper.init(c);
        String activebin = SettingsHelper.read("command-" + i);
        if (activebin == null)
            initCommand();
        activebin = SettingsHelper.read("command-" + i);
        return activebin;
    }

    public boolean setCommandString(int i, String command) {
        if (command.length() == 0)
            return false;
        SettingsHelper.init(c);
        SettingsHelper.store("command-" + i, command);
        return true;
    }

    public String getCommandName(int i) {
        switch (i) {
        case 0:
            return c.getString(R.string.command_forwardsms);
        case 1:
            return c.getString(R.string.command_ambilsms);
        case 2:
            return c.getString(R.string.command_contact);
        case 3:
            return c.getString(R.string.command_help);
        case 4:
            return c.getString(R.string.command_record);
        case 5:
            return c.getString(R.string.command_loc);
        case 6:
            return c.getString(R.string.command_twitter);
        }
        return null;
    }

    /**
     * Mendapatkan nama-nama command yang hanya visible ke pengguna
     * 
     * @return
     */
    public ArrayList<Pair<Integer, String>> getCommandName() {

        ArrayList<Pair<Integer, String>> retval = new ArrayList<Pair<Integer, String>>();
        for (int i : visibleCommands) {
            retval.add(new Pair<Integer, String>(i, getCommandName(i)));
        }
        return retval;
    }
    
    public void setTwitterUsername(String username) {
        SettingsHelper.init(c);
        SettingsHelper.store("command-twitter-uname", username);
    }
    public void setTwitterTokenPair(String token, String tokenSecret) {
        SettingsHelper.init(c);
        SettingsHelper.store("command-twitter-token", token);
        SettingsHelper.store("command-twitter-token-secret", tokenSecret);
    }
    public Pair<String, String> getTwitterTokenPair() {
        SettingsHelper.init(c);
        String token = SettingsHelper.read("command-twitter-token");
        String tokensecret = SettingsHelper.read("command-twitter-token-secret");
        if (token == null || token.length() == 0) return null;
        return new Pair<String, String>(token, tokensecret);
    }
    public String getTwitterUsername() {
        SettingsHelper.init(c);
        String uname = SettingsHelper.read("command-twitter-uname");
        if (uname == null || uname.length() == 0) return null;
        return uname;
    }
    
    public void deleteTwitterConnection() {
        SettingsHelper.init(c);
        SettingsHelper.store("command-twitter-uname", "");
        SettingsHelper.store("command-twitter-token", "");
    }
}
