package com.pratamawijaya.panggilpeta.helper;

import android.content.Context;
import android.content.SharedPreferences;
import timber.log.Timber;

/**
 * Created by pratama on 3/19/15.
 */
public class PreferencesUtils {

  private static SharedPreferences sp;
  public static final String PREF_NAME = "jalanjalan";

  public static final String USER_SESSION = "user_session";
  public static final String USER_NAME = "user_name";
  public static final String USER_ISLOGIN = "user_login";
  public static final String USER_ISLOGIN_FB = "user_login_fb";
  public static final String USER_ISLOGIN_GOOGLE = "user_login_google";
  public static final String USER_AVATAR = "user_avatar";
  public static final String USER_EMAIL = "user_email";
  public static final String USER_ID = "user_id";
  public static final String REG_GCM_ID = "reg_gcm";
  public static final String NAV_DRAWER_OPEN = "nav_drawer_open";
  public static final String IS_FIRST = "first";

  public static String getToken(Context context) {
    return getDataStringFromSP(context, USER_SESSION, "");
  }

  public static String getUserID(Context context) {
    return getDataStringFromSP(context, USER_ID, "");
  }

  /**
   * clear all sp
   */
  public static void clearSP(Context context) {
    Timber.d("clear sp");
    context.getSharedPreferences(PREF_NAME, 0).edit().clear().commit();
  }

  public static void setDataIntTOSP(Context context, String to, int data) {
    Timber.d("set data int to SP " + to + " data :" + data);
    sp = context.getSharedPreferences(PREF_NAME, 0);
    SharedPreferences.Editor editor = sp.edit();
    editor.putInt(to, data);
    editor.commit();
  }

  public static int getDataIntFromSP(Context context, String from, int defaultValue) {
    sp = context.getSharedPreferences(PREF_NAME, 0);
    return sp.getInt(from, defaultValue);
  }

  public static void setDataBooleanToSP(Context context, String to, boolean data) {
    Timber.d("setDataBooleanToSP " + data);
    sp = context.getSharedPreferences(PREF_NAME, 0);
    SharedPreferences.Editor editor = sp.edit();
    editor.putBoolean(to, data);
    editor.commit();
  }

  public static boolean getDataBooleanFromSP(Context context, String from, boolean defaultValue) {
    sp = context.getSharedPreferences(PREF_NAME, 0);
    return sp.getBoolean(from, defaultValue);
  }

  public static void setDataDoubleToSP(Context context, String to, long data) {
    Timber.d("setDataDoubleToSp " + data);
    sp = context.getSharedPreferences(PREF_NAME, 0);
    SharedPreferences.Editor editor = sp.edit();
    editor.putLong(to, data);
    editor.commit();
  }

  public static double getDataDoubleFromSP(Context context, String from, long defaultVal) {
    sp = context.getSharedPreferences(PREF_NAME, 0);
    Timber.d("data ->" + sp.getLong(from, defaultVal));
    return sp.getLong(from, defaultVal);
  }

  public static void setDataStringToSP(Context context, String to, String data) {
    Timber.d("setDataStringToSP " + to + " data :" + data);
    sp = context.getSharedPreferences(PREF_NAME, 0);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(to, data);
    editor.commit();
  }

  public static String getDataStringFromSP(Context context, String from, String defaultValue) {
    sp = context.getSharedPreferences(PREF_NAME, 0);
    Timber.d("data ->" + sp.getString(from, defaultValue));
    return sp.getString(from, defaultValue);
  }
}
