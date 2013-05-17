package com.mando;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mando.helper.SettingsController;
import com.mando.mailer.EmailServerType;
import com.mando.mailer.EmailSettings;

public class SettingsEmail extends SherlockActivity implements OnNavigationListener {
    
    enum View {
        Gmail, CustomSMTP
    }
    View currentView = View.Gmail;
    
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
            SettingsController x = new SettingsController(this);
            EmailSettings em = new EmailSettings();
            em.username = getTextOf(R.id.username);
            em.password = getTextOf(R.id.password);
            em.server = EmailServerType.GMail;
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
        setContentView(R.layout.activity_settings_emailgmail);
        SettingsController x = new SettingsController(this);
        EmailSettings m = x.getEmailSettings();
        setTextOf(R.id.username, m.username);
        setTextOf(R.id.password, m.password);

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.email_mechanism, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
}