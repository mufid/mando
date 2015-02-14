package com.mando.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mando.helper.SettingsController;
import com.mando.mailer.EmailServerType;
import com.mando.mailer.EmailSettings;
import com.mando.R;

public class SettingsEmail extends SherlockActivity implements OnNavigationListener {
    
    enum View {
        Gmail, CustomSMTP
    }
    View currentView = View.Gmail;
    EmailSettings em;
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 0, 0, R.string.save)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
    
    public String getTextOf(int resId) {
        EditText ed = (EditText) findViewById(resId);
        return ed.getText().toString();
    }
    
    public void setTextOf(int resId, String text) {
        EditText ed = (EditText) findViewById(resId);
        ed.setText(text);
    }
    
    @Override
    public boolean onMenuItemSelected(int id, MenuItem item) {
        switch (id) {
        case 0:
            em.username = getTextOf(R.id.username);
            em.password = getTextOf(R.id.password);
            SettingsController x = new SettingsController(this);
            if (em.server.equals(EmailServerType.CustomSMTP)) {
                em.port = getTextOf(R.id.serverport);
                em.serverAddr = getTextOf(R.id.serveraddr);
                Spinner yyy = (android.widget.Spinner) findViewById(R.id.spinner1);
                int pos_sp = yyy.getSelectedItemPosition();
                em.isSSL = pos_sp > 0;
                em.isTLS = pos_sp == 1;
            }
            x.saveEmailSettings(em);
            finish();
            break;
        }
        
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);
        SettingsController x = new SettingsController(this);
        em = x.getEmailSettings();

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.email_mechanism, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        getSupportActionBar().setSelectedNavigationItem(em.server == EmailServerType.CustomSMTP ? 1 : 0);
    }
    
    protected void renderLayout() {
        switch (em.server) {
        case GMail:
            setContentView(R.layout.activity_settings_emailgmail);
            
            setTextOf(R.id.username, em.username);
            setTextOf(R.id.password, em.password);
            break;
        case CustomSMTP:
            setContentView(R.layout.activity_settings_emailcustomsmtp);
            
            setTextOf(R.id.username, em.username);
            setTextOf(R.id.password, em.password);
            setTextOf(R.id.serveraddr, em.serverAddr);
            setTextOf(R.id.serverport, em.port);
            Spinner yyy = (android.widget.Spinner) findViewById(R.id.spinner1);
            if (em.isTLS) {
                yyy.setSelection(1);
            } else if (em.isSSL) {
                yyy.setSelection(2);
            } else {
                yyy.setSelection(0);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        Log.e("mando", "Posisi: " + itemPosition);
        switch (itemPosition) {
        case 0:
            em.server = EmailServerType.GMail;
            renderLayout();
            break;
        case 1:
            em.server = EmailServerType.CustomSMTP;
            renderLayout();
            break;
        }
        return true;
    }
    
    
}