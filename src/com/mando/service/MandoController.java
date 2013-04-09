package com.mando.service;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;

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
        // index legends:
        // 0 : forward SMS
        // 1 : receive SMS
        // 2 : get Contacts
        // 3 : help
        // 4 : record sound (not now)
        // 5 : location

        // bentuk SMS valid: <PIN> <perintah> <SMS>

        // data accuiring:
        ArrayList<String> commands = new ArrayList<String>();
        ArrayList<Boolean> isActive = new ArrayList<Boolean>();

        String result = "";

        // dummy, replace it with real commands
        commands.add("forward");
        commands.add("ambil");
        commands.add("kontak");
        commands.add("tolong");
        commands.add("suara");
        commands.add("lokasi");

        // dummy juga
        for (int i = 0; i < 6; i++)
            isActive.add(true);

        // dummy juga
        String PIN = "1234";

        String[] words = s.split(" ");
        if (words.length < 2)
            return;
        if (!words[0].equals(PIN))
            return;

        // forward SMS:
        // <PIN> <perintah> <nomor tujuan> <SMS>
        if (words[1].equals(commands.get(0)) && isActive.get(0)) {
            if (words.length < 4)
                return; // invalid forward
            try {
                int numInt = Integer.parseInt(words[2]);
                String num = words[2];
                String msg = "";
                for (int i = 3; i < words.length; i++)
                    msg += words[i] + " ";

                // call forward SMS
                SMS sms = new SMS(num, msg);
                forwardSMS(sms);
            } catch (Exception e) {
                return; // invalid SMS
            }
        }

        // receive SMS:
        // <PIN> <perintah> <jumlah SMS diambil>
        if (words[1].equals(commands.get(1)) && isActive.get(1)) {
            if (words.length != 3)
                return; // invalid SMS
            try {
                int count = Integer.parseInt(words[2]);

                // call recieve SMS
                // receiveSMS(count);
            } catch (Exception e) {
                return; // invalid SMS
            }
        }

        // get Contacs:
        // <PIN> <perintah> <nama kontak>
        if (words[1].equals(commands.get(2)) && isActive.get(2)) {
            if (words.length < 3)
                return; // invalid SMS
            String name = "";
            for (int i = 2; i < words.length; i++)
                name += words[i] + " ";
            result = getContacts(name);
        }

        // get Help:
        // <PIN> <perintah>
        if (words[1].equals(commands.get(3)) && isActive.get(3)) {
            if (words.length != 2)
                return; // invalid SMS
            result = getHelp(commands, isActive);
        }

        // get Location:
        // <PIN> <perintah>
        if (words[1].equals(commands.get(5)) && isActive.get(5)) {
            if (words.length != 2)
                return; // invalid SMS
            getLocation();
        }

        SMS sms = new SMS("081511967009", result);

        if (result.length() > 0)
            sendSMS(sms);

    }

    public static String getHelp(ArrayList<String> commands,
            ArrayList<Boolean> isActive) {
        String msg = "help: ";
        if (isActive.get(0)) // forward sms
            msg += "\nForward SMS:\n<PIN> " + commands.get(0)
                    + " <No.Tujuan> <SMS>\n";
        if (isActive.get(1)) // ambil sms
            msg += "\nretieve SMS:\n<PIN> " + commands.get(1)
                    + " <Jumlah SMS yang akan diambil>\n";
        if (isActive.get(2)) // kontak
            msg += "\nget contact:\n<PIN> " + commands.get(2)
                    + " <Nama kontak>\n";
        if (isActive.get(3)) // help
            msg += "\nhelp:\n<PIN> " + commands.get(3) + "\n";
        if (isActive.get(4)) // suara
            msg += "\nrecord sound:\n<PIN> " + commands.get(4)
                    + " <waktu rekam(detik)>\n";
        if (isActive.get(5)) // lokasi
            msg += "\nget location:\n<PIN> " + commands.get(5) + "\n";

        return msg;
    }

    private static boolean match(String a, String b) {
        int j = 0;
        // pattern matching
        for (int i = 0; i < a.length() && j + 1 < b.length(); i++)
            if (a.charAt(i) == b.charAt(j))
                j++;
            else
                j = 0;
        return j + 1 == b.length();
    }

    private static void sendSMS(SMS sms) {
        String phoneNumber = sms.getNumber();
        String message = sms.getMessage();

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
                null);
    }

    public static String getContacts(String x) {
        String msg = "";

        // list of name + number in contacts
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> num = new ArrayList<String>();

        name.add("joko susilo");
        name.add("Mr. mando");

        num.add("0815235456");
        num.add("9001");

        int N = name.size();
        for (int i = 0; i < N; i++) {
            if (match(name.get(i) + " ", x))
                msg += name.get(i) + " [ " + num.get(i) + " ]\n";

        }
        if (msg.length() == 0)
            return "Kontak tidak ditemukan";
        return msg;
    }

    public static String getLocation() {
        return "";
    }

    public static SMS[] getSMS(int jumlah, Context context) {
        SMS[] res = new SMS[10];
        Uri smsUri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(smsUri,
                new String[] { "address", "body" }, null, null, "date DESC");

        for (int i = 0; i < jumlah && cursor.moveToNext(); i++) {
            String addressNum = cursor.getString(0);
            Uri addrNameUri = Uri.withAppendedPath(
                    PhoneLookup.CONTENT_FILTER_URI, Uri.encode(addressNum));
            Cursor addr = context.getContentResolver()
                    .query(addrNameUri,
                            new String[] { PhoneLookup.DISPLAY_NAME }, null,
                            null, null);
            String addressName;

            if (addr.getCount() > 0) {
                addr.moveToNext();
                addressName = addr.getString(0) + " (" + addressNum + ")";
            } else {
                addressName = addressNum;
            }

            String body = cursor.getString(1);

            res[i] = new SMS(addressName, body);
        }

        return res;
    }

    public static String forwardSMS(SMS sms) {
        sendSMS(sms);

        return "";
    }
}
