package com.pratamawijaya.panggilpeta.base;

import android.app.Application;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pratamawijaya.panggilpeta.BuildConfig;
import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;
import timber.log.Timber;

/**
 * Created by pratama on 2/18/15.
 */
public class BaseApplication extends Application {
  private RequestQueue requestQueue;
  private static BaseApplication instance;
  private static final int TIMEOUT_MS = 30000; // 50second

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
    try {
      Realm.getInstance(this);
    } catch (RealmMigrationNeededException expected) {
      Realm.deleteRealmFile(this); // Delete old realm
    }

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  /**
   * get instance application
   */
  public static synchronized BaseApplication getInstance() {
    return instance;
  }

  /**
   * get requestQueue from volley
   */
  public RequestQueue getRequestQueue() {
    if (requestQueue == null) requestQueue = Volley.newRequestQueue(instance);
    return requestQueue;
  }

  /**
   * Add request to queue using specific tag
   */
  public <T> void addToRequestQueue(Request<T> request, String tag) {
    // set retry policy
    Log.d("debug", request.getUrl());
    request.setRetryPolicy(
        new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    getRequestQueue().add(request);
  }

  /**
   * cancelling all pending request by TAG
   */
  public void cancelPendingRequest(Object tag) {
    if (requestQueue != null) requestQueue.cancelAll(tag);
  }
}
