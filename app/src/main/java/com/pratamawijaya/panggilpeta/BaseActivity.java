package com.pratamawijaya.panggilpeta;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by pratama on 2/18/15.
 */
public class BaseActivity extends AppCompatActivity {

  private Toolbar toolbar;

  @Override public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    getActionBarToolbar();
  }

  /**
   * get actionbar toolbar
   */
  protected Toolbar getActionBarToolbar() {
    return toolbar;
  }
}
