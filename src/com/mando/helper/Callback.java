package com.mando.helper;

/**
 * Kelas ini digunakan untuk keperluan yang asinkron,
 * seperti misalnya merekam suara
 * @author Mufid
 *
 */
public abstract class Callback {
	protected abstract void onSuccess();
	protected abstract void onFailure();
}
