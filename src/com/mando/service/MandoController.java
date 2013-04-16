package com.mando.service;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;

import com.mando.R;
import com.mando.helper.SMS;
import com.mando.helper.SettingsController;

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
    public static Context c;

    public static void processSMS(String s, String n, Context context) {
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

        c = context;

        // Baca pin dari setting
        SettingsController settings = new SettingsController(context);
        String PIN = settings.getCurrentPIN();

        String[] words = s.split(" ");
        if (words.length < 2)
            return;
        if (!words[0].equals(PIN))
            return;

        // forward SMS:
        // <PIN> <perintah> <nomor tujuan> <SMS>
        if (words[1].equalsIgnoreCase(settings.getCommandString(0))
                && settings.getCommandActive(0)) {
            if (words.length < 4)
                return; // invalid forward
            try {
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
        if (words[1].equalsIgnoreCase(settings.getCommandString(1))
                && settings.getCommandActive(1)) {
            if (words.length != 3)
                return; // invalid SMS
            try {
                int count = Integer.parseInt(words[2]);

                // call recieve SMS
                receiveSMS(count, n);
            } catch (Exception e) {
                return; // invalid SMS
            }
        }

        // get Contacs:
        // <PIN> <perintah> <nama kontak>
        if (words[1].equalsIgnoreCase(settings.getCommandString(2))
                && settings.getCommandActive(2)) {
            if (words.length < 3)
                return; // invalid SMS
            String name = "";
            for (int i = 2; i < words.length; i++)
                name += words[i] + " ";
            result = getContacts(name);
        }

        // get Help:
        // <PIN> <perintah>
        if (words[1].equalsIgnoreCase(settings.getCommandString(3))
                && settings.getCommandActive(3)) {
            if (words.length != 2)
                return; // invalid SMS
            result = getHelp(commands, isActive);
        }

        // get Location:
        // <PIN> <perintah>
        if (words[1].equalsIgnoreCase(settings.getCommandString(5))
                && settings.getCommandActive(5)) {
            if (words.length != 2)
                return; // invalid SMS
            getLocation();
        }

        SMS sms = new SMS(n, result);

        if (result.length() > 0)
            sendSMS(sms);

    }

    public static String receiveSMS(int x, String n) {

        SMS sms[] = getSMS(x, c);

        for (int i = 0; i < sms.length; i++) {
            sendSMS(sms[i]);
        }
        return "";
    }

    public static String getHelp(ArrayList<String> commands,
            ArrayList<Boolean> isActive) {
        SettingsController s = new SettingsController(c);
        String msg = "help: ";
        if (isActive.get(0)) // forward sms
            msg += "\n" + c.getString(R.string.command_forwardsms)
                    + ":\n<PIN> " + s.getCommandString(0)
                    + " <No.Tujuan> <SMS>\n";
        if (isActive.get(1)) // ambil sms
            msg += "\n" + c.getString(R.string.command_ambilsms) + ":\n<PIN> "
                    + s.getCommandString(1)
                    + " <Jumlah SMS yang akan diambil>\n";
        if (isActive.get(2)) // kontak
            msg += "\n" + c.getString(R.string.command_contact) + ":\n<PIN> "
                    + s.getCommandString(2) + " <Nama kontak>\n";
        if (isActive.get(3)) // help
            msg += "\n" + c.getString(R.string.command_help) + ":\n<PIN> "
                    + s.getCommandString(3) + "\n";
        if (isActive.get(4)) // suara
            msg += "\n" + c.getString(R.string.command_record) + ":\n<PIN> "
                    + s.getCommandString(4) + " <waktu rekam(detik)>\n";
        if (isActive.get(5)) // lokasi
            msg += "\n" + c.getString(R.string.command_loc) + ":\n<PIN> "
                    + s.getCommandString(5) + "\n";

        return msg;
    }

    private static boolean match(String a, String b) {
        int j = 0;
        // pattern matching
        for (int i = 0; i < a.length() && j + 1 < b.length(); i++)
            if (a.toLowerCase().charAt(i) == b.toLowerCase().charAt(j))
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

        // retrieve contact
        String condition = ContactsContract.Data.MIMETYPE + " = "
                + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "";
        String[] Projection = { ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        String sort = ContactsContract.Data.DISPLAY_NAME;

        Cursor cur = c.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, Projection, null, null,
                sort + " ASC");

        String lastName = "";
        while (cur.moveToNext()) {
            int nameField = cur
                    .getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
            int numField = cur
                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (nameField == -1 || numField == -1)
                continue;

            if (lastName.equals(cur.getString(nameField)))
                continue;

            if (cur.getString(numField) == null
                    || !cur.getString(numField).matches("^[+]?[0-9 -()]+"))
                continue;

            lastName = cur.getString(nameField);

            name.add(cur.getString(nameField));
            num.add(cur.getString(numField));

        }

        int N = name.size();
        for (int i = 0; i < N; i++) {
            if (match(name.get(i), x))
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
        SMS[] res = new SMS[jumlah];
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

            String body = String.format("Dari %s: %s", addressName,
                    cursor.getString(1));// cursor.getString(1);

            if (i == 0) {
                // TODO : Kalau command udah dihapus, gak perlu pake ini lagi.
                continue;
            }

            res[i] = new SMS(addressNum, body);
        }

        return res;
    }

    public static String forwardSMS(SMS sms) {
        sendSMS(sms);
        return "";
    }
}
