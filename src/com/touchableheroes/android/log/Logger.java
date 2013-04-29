package com.touchableheroes.android.log;

import android.util.Log;


/**
 * @author Andreas Siebert, ask@touchableheroes.com
 */
public class Logger {

	
	public static void debug(final String msg) {
		Log.d( "APP", msg);
	}

	public static void info(final String msg) {
		Log.d("APP",  msg);
	}

	public static void error(final String msg) {
		Log.e("APP",  msg);
	}

	public static void critical(final String msg) {
		Log.e("APP", "CRITICAL ERROR: " + msg);
	}

	public static void exception(final String msg, final Throwable ex) {
		Log.e("APP", msg, ex);
	}

	public static boolean isDebug() {
		return false;
	}
}