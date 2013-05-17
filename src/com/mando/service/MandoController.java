package com.mando.service;

import java.io.IOException;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.mando.R;
import com.mando.helper.Callback;
import com.mando.helper.CallbackLocation;
import com.mando.helper.GradualMessage;
import com.mando.helper.SMS;
import com.mando.helper.SettingsController;
import com.mando.helper.SettingsHelper;
import com.mando.mailer.EmailSettings;
import com.mando.mailer.Mailer;

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

    public static void processSMS(String s, String phoneNum, Context context) {
        // Ini yang ada di UCRS pertama
        // index legends:
        // 0 : forward SMS
        // 1 : receive SMS
        // 2 : get Contacts
        // 3 : help
        // 4 : record sound (not now)
        // 5 : location
        // 6 : Twitter
        // 7 : Anti-theft

        // bentuk SMS valid: <PIN> <perintah> <SMS>

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
                receiveSMS(count, phoneNum);
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

        // Get some help
        // <PIN> <perintah>
        if (words[1].equalsIgnoreCase(settings.getCommandString(3))
                && settings.getCommandActive(3)) {
            if (words.length != 2)
                return; // invalid SMS
            result = getHelp(phoneNum);
        }
        
        // Do rekam suara
        // <PIN> <perintah>
        if (words[1].equalsIgnoreCase(settings.getCommandString(4))
                && settings.getCommandActive(4)) {
            if (words.length != 2)
                return; // invalid SMS
            // do rekan suara here
            recordSound(new Callback(c, phoneNum) {

                @Override
                public void onSuccess(String successMessage) {
                    send("Pengiriman berhasil");
                }

                @Override
                public void onFailure() {
                    send("Gagal mengirim surel atau sdcard tidak terpasang");
                }
                
            });
        }

        // get Location:
        // <PIN> <perintah>
        if (words[1].equalsIgnoreCase(settings.getCommandString(5))
                && settings.getCommandActive(5)) {
            if (words.length != 2)
                return; // invalid SMS
            getLocation(new CallbackLocation(c, phoneNum) {

                @Override
                public void onSuccess(String locationLat, String locationName) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void onFailure() {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void onSuccess(String locationLat) {
                    // TODO Auto-generated method stub
                    
                }
                
            });
        }

        // get twitter:
        // <PIN> <perintah> <tweetnya?
        if (words[1].equalsIgnoreCase(settings.getCommandString(6))
                && settings.getCommandActive(6)) {
            if (words.length < 3)
                return; // invalid perintah twitter
            try {
                String num = words[2];
                String msg = "";
                for (int i = 3; i < words.length; i++)
                    msg += words[i] + " ";
                Toast.makeText(c, "Masuk ke twitter", Toast.LENGTH_LONG);
                // call forward SMS
                tweet(msg, new Callback(c, phoneNum) {
                    @Override
                    public void onSuccess(String msg) {
                        send("Tweet berhasil dikirim");
                    }

                    @Override
                    public void onFailure() {
                        send("Tweet gagal dikirim");
                    }
                });
            } catch (Exception e) {
                return; // invalid SMS
            }
        }

        // Hapus SMS terakhir atau SMS perintah
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        deleteLastSMS();

        SMS sms = new SMS(phoneNum, result);

        if (result.length() > 0) {
            if (SettingsHelper.isDebug())
                Toast.makeText(c, "Will be sent: " + result, Toast.LENGTH_LONG)
                        .show();
            sendSMS(sms);
        }

    }

    private static void tweet(String msg, final Callback cb) {
        // 0: Twitter token
        // 1: Twitter message
        AsyncTask<String, Void, Void> x = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                Log.e("mando", "Oke, jalan kok");
                try {
                    ConfigurationBuilder confbuilder = new ConfigurationBuilder();
                    Configuration conf = confbuilder
                            .setOAuthConsumerKey(
                                    c.getString(R.string.twitter_consumer_key))
                            .setOAuthConsumerSecret(
                                    c.getString(R.string.twitter_consumer_secret))
                            .build();
                    Twitter mTwitter = new TwitterFactory(conf).getInstance();

                    String accessToken = params[0];
                    String accessTokenSecret = params[1];
                    if (accessToken == null || accessTokenSecret == null) {
                        cb.onFailure();
                        return null;
                    }
                    mTwitter.setOAuthAccessToken(new AccessToken(accessToken,
                            accessTokenSecret));

                    mTwitter.updateStatus(params[2]);
                    Log.e("mando", "access token: " + accessToken);
                    Log.e("mando", "access accessTokenSecret: " + accessTokenSecret);
                    Log.e("mando", "msg: " + params[2]);
                    cb.onSuccess(null);
                } catch (TwitterException e) {
                    cb.onFailure();
                }
                return null;
            }

        };
        Log.e("mando", "Wait..");
        SettingsController s = new SettingsController(c);
        Pair<String, String> tokenpair = s.getTwitterTokenPair();
        if (s.getTwitterUsername().length() == 0) {
            cb.onFailure();
        } else {
            x.execute(tokenpair.first, tokenpair.second, msg);
        }
    }

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private static MediaRecorder mRecorder = null;

    private static MediaPlayer mPlayer = null;

    public static void recordSound(final Callback cb) {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        
        Log.e("mando", "save to: " + mFileName);
        final EmailSettings mailSettings = new SettingsController(c).getEmailSettings();
        AsyncTask<String, Void, Void> x = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                Log.e("Masuk rekam cuy","Rekam");
               
                try {
                    startRecording();
                    Thread.sleep(10000);
                    stopRecording();
                    Mailer sender = new Mailer(mailSettings);
                    sender.sendMail("Mando Audio",   
                            "Hai, makasih sudah menggunakkan Mando. Terlampir hasil rekamannya.",   
                            mailSettings.username,   
                            mailSettings.username,
                            mFileName);   
                    cb.onSuccess(null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    stopRecording();
                    cb.onFailure();
                } 
                
                return null;
            }

        };
     
        x.execute();
    }

    private static void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private static void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private static void deleteLastSMS() {
        // TODO Auto-generated method stub
        Toast.makeText(c, "mulai hapus", 1).show();

        try {
            // mLogger.logInfo("Deleting SMS from inbox");
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor cur = c.getContentResolver().query(
                    uriSms,
                    new String[] { "_id", "thread_id", "address", "person",
                            "date", "body" }, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                long idM = 0;
                do {
                    long id = cur.getLong(0);
                    if (idM < id)
                        idM = id;
                    long threadId = cur.getLong(1);
                    String address = cur.getString(2);
                    String body = cur.getString(5);
                    // mLogger.logInfo("Deleting SMS with id: " + threadId);
                } while (cur.moveToNext());
                c.getContentResolver().delete(
                        Uri.parse("content://sms/" + idM), null, null);
                Toast.makeText(c, "Hapus SMS id " + idM, 1).show();

            }
        } catch (Exception e) {
            // mLogger.logError("Could not delete SMS from inbox: " +
            // e.getMessage());
            Toast.makeText(c,
                    "Could not delete SMS from inbox: " + e.getMessage(), 1)
                    .show();

        }
    }

    public static String receiveSMS(int x, String n) {

        SMS sms[] = getSMS(x, c);

        for (int i = 0; i < sms.length; i++) {
            sendSMS(sms[i]);
        }
        return "";
    }

    public static String getHelp(String phoneNumber) {
        SettingsController s = new SettingsController(c);
        // Karena terlalu panjang, gunakan gradual message
        GradualMessage gm = new GradualMessage(phoneNumber);
        gm.addGradualMessage("help: ");
        if (s.getCommandActive(0)) // forward sms
            gm.addGradualMessage("\n"
                    + c.getString(R.string.command_forwardsms) + ":\n<PIN> "
                    + s.getCommandString(0) + " <No.Tujuan> <SMS>\n");
        if (s.getCommandActive(1)) // ambil sms
            gm.addGradualMessage("\n" + c.getString(R.string.command_ambilsms)
                    + ":\n<PIN> " + s.getCommandString(1)
                    + " <Jumlah SMS yang akan diambil>\n");
        
        if (s.getCommandActive(2)) // kontak
            gm.addGradualMessage("\n" + c.getString(R.string.command_contact)
                    + ":\n<PIN> " + s.getCommandString(2) + " <Nama kontak>\n");
        
        if (s.getCommandActive(3)) // help
            gm.addGradualMessage("\n" + c.getString(R.string.command_help)
                    + ":\n<PIN> " + s.getCommandString(3) + "\n");
        
        if (s.getCommandActive(4)) // suara
            gm.addGradualMessage("\n" + c.getString(R.string.command_record)
                    + ":\n<PIN> " + s.getCommandString(4)
                    + " <waktu rekam(detik)>\n");
        if (s.getCommandActive(5)) // lokasi
            gm.addGradualMessage("\n" + c.getString(R.string.command_loc)
                    + ":\n<PIN> " + s.getCommandString(5) + "\n");

        if (s.getCommandActive(6)) // twitter
            gm.addGradualMessage("\n" + c.getString(R.string.command_twitter)
                    + ":\n<PIN> " + s.getCommandString(6) + "\n");

        gm.flush();

        return "";
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

    public static String getLocation(CallbackLocation callbackLocation) {

        return "";
    }

    public static SMS[] getSMS(int jumlah, Context context) {
        SMS[] res = new SMS[jumlah];
        Uri smsUri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(smsUri,
                new String[] { "address", "body" }, null, null, "date DESC");

        for (int i = 0; i < jumlah + 1 && cursor.moveToNext(); i++) {
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
            res[i - 1] = new SMS(addressNum, body);

            addr.close();
        }

        cursor.close();
        return res;
    }

    public static String forwardSMS(SMS sms) {
        sendSMS(sms);
        return "";
    }

    public static String dering() {
        // TODO: Lakukan rutin di sini

        return "Berhasil Menderingkan";
    }

    public static String mutakhirkanTwitter(String pesan) {

        return "Status twitter berhasil dimutakhirkan";
    }
}
