package com.mando.helper;

import android.content.Context;

public class NullCallback extends Callback {

    public NullCallback(Context c, String pn) {
        super(c, pn);
    }
    
    public NullCallback() {
        super(null, null);
    }

    @Override
    public void onSuccess(String successMessage) { }

    @Override
    public void onFailure() { }

}
