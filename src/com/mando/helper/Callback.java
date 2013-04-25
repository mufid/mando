package com.mando.helper;

import android.content.Context;

/**
 * Kelas ini digunakan untuk keperluan yang asinkron,
 * seperti misalnya merekam suara
 * @author Mufid
 *
 */
public abstract class Callback {
    protected Context c;
    public Callback(Context c) {
        this.c = c;
    }
	protected abstract void onSuccess();
	protected abstract void onFailure();
}
