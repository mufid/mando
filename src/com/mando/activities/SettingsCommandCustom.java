package com.mando.activities;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthSupport;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsController;
import com.mando.mailer.EmailSettings;
import com.mando.R;

public class SettingsCommandCustom extends SherlockActivity {
    private static final int FROM_TWITTER_LOGIN_WINDOW = 0;
    private static final int FROM_EMAIL_SETTINGS = 1;
    
    RequestToken mRequestToken;
    Twitter mTwitter = null;
    
    private void refreshTwitterStatus() {
        SettingsController s = new SettingsController(this);
        String uname = s.getTwitterUsername();
        String button_string;
        String twitter_desc;
        if (uname == null) {
            button_string = getString(R.string.twitter_connect);
            twitter_desc = getString(R.string.twitter_notconnected);
        } else {
            button_string = getString(R.string.twitter_disconnect);
            twitter_desc = String.format(getString(R.string.twitter_connected), uname);
        }
        
        Button twitterButton = (Button) findViewById(R.id.customcommand_twitterconnect);
        TextView twitterDesc = (TextView) findViewById(R.id.customcommand_twitterconnectionstatus);
        
        twitterDesc.setText(twitter_desc);
        twitterButton.setText(button_string);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);

        Intent i = getIntent();
        SettingsController s = new SettingsController(this);

        int commandID = i.getExtras().getInt("id");
        
        // Special custom command
        switch (commandID) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 5:
        case 7:
            setContentView(R.layout.activity_settings_commandcustom);
            break;
        case 4:
        case 8:
            setContentView(R.layout.activity_settings_email);
            Button emailButton = (Button) findViewById(R.id.customcommand_emailsettings);
            emailButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    startActivityForResult(new Intent(SettingsCommandCustom.this, SettingsEmail.class), FROM_EMAIL_SETTINGS);
                }
            });
            refreshEmailSettings();
            break;
        case 6: // Twitter
            // Oh well, Android ICS Network Thread Problem
            if (android.os.Build.VERSION.SDK_INT > 9) {
                android.os.StrictMode.ThreadPolicy policy = new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
                android.os.StrictMode.setThreadPolicy(policy);
            }
            
            setContentView(R.layout.activity_settings_twitter);
            Button twitterButton = (Button) findViewById(R.id.customcommand_twitterconnect);
            refreshTwitterStatus();
            twitterButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    SettingsController s = new SettingsController(getApplicationContext());
                    if (s.getTwitterUsername() != null) {
                        s.deleteTwitterConnection();
                        refreshTwitterStatus();
                        return;
                    }
                    ConfigurationBuilder confbuilder = new ConfigurationBuilder();
                    Configuration conf = confbuilder
                        .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                        .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                        .build();
                    mTwitter = new TwitterFactory(conf).getInstance();
                    mTwitter.setOAuthAccessToken(null);
                    try {
                        mRequestToken = mTwitter.getOAuthRequestToken(SettingsTwitterLogin.MANDO_TWITTER_CALLBACKURL);
                        Intent intent = new Intent(SettingsCommandCustom.this, SettingsTwitterLogin.class);
                        intent.putExtra(SettingsTwitterLogin.TWITTER_AUTH_URI, mRequestToken.getAuthorizationURL());
                        startActivityForResult(intent, FROM_TWITTER_LOGIN_WINDOW);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            });
            break;
        }
        
        final EditText ed = ((EditText) findViewById(R.id.customcommand_edittext));
        ed.setText(s.getCommandString(commandID));
        TextView tv = (TextView) findViewById(R.id.customcommand_titlelabel);
        tv.setText(String.format(getString(R.string.setting_customcommandfor),
                s.getCommandName(commandID)));
        ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (commandStringValid(ed.getText().toString())) {
                    ed.setError(null);
                } else {
                    ed.setError("Perintah hanya boleh berisi angka dan huruf");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) { /* do nothing */ }

            @Override
            public void afterTextChanged(Editable s) { /* do nothing */ }
        });
        

    }

    boolean commandStringValid(String what) {
        return what.matches("^[0-9A-Za-z]+$");
    }

    @Override
    protected void onDestroy() {
        // Cek apakah ada perubah
        Intent i = getIntent();
        SettingsController s = new SettingsController(this);
        int commandID = i.getExtras().getInt("id");
        String currentcommand = s.getCommandString(commandID);
        EditText ed = ((EditText) findViewById(R.id.customcommand_edittext));
        String typedcommand = ed.getText().toString();
        if (!typedcommand.equals(currentcommand)
                && commandStringValid(typedcommand)) {
            s.setCommandString(commandID, typedcommand);
            Toast.makeText(
                    this,
                    String.format(
                            getString(R.string.setting_customcommand_changed),
                            s.getCommandName(commandID)), Toast.LENGTH_LONG)
                    .show();
        }
        super.onDestroy();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
        case FROM_TWITTER_LOGIN_WINDOW:
            if (resultCode == RESULT_OK) {
                AccessToken accessToken = null;
                
                String oauthVerifier = intent.getExtras().getString(SettingsTwitterLogin.TWITTER_VERIFIER);
                try {
                    accessToken = mTwitter.getOAuthAccessToken(mRequestToken, oauthVerifier);
                    SettingsController s = new SettingsController(this);
                    s.setTwitterTokenPair(accessToken.getToken(), accessToken.getTokenSecret());
                    s.setTwitterUsername(mTwitter.getScreenName());
                    Toast.makeText(this, "Mando berhasil terhubung ke Twitter", Toast.LENGTH_SHORT).show();
                    
                    refreshTwitterStatus();
                } catch (TwitterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Mando gagal tersambung ke Twitter", Toast.LENGTH_LONG).show();
            }
            break;
        case FROM_EMAIL_SETTINGS:
            refreshEmailSettings();
            break;
        default: // do nothing
        }
    }

    private void refreshEmailSettings() {
        TextView tv = (TextView) findViewById(R.id.customcommand_status);
        SettingsController s = new SettingsController(this);
        EmailSettings em = s.getEmailSettings();
        if (em.username.length() == 0) {
            tv.setText(R.string.email_notset);
        } else {
            tv.setText("Terkonfigurasi atas " + em.username);
        }
    }
}
