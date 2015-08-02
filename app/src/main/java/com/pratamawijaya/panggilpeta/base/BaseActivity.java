package com.pratamawijaya.panggilpeta.base;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.pratamawijaya.panggilpeta.R;

/**
 * Created by pratama on 2/18/15.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    /**
     * get actionbar toolbar
     *
     * @return
     */
    protected Toolbar getActionBarToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }
}
