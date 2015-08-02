package com.pratamawijaya.panggilpeta.views;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.pratamawijaya.panggilpeta.R;
import com.pratamawijaya.panggilpeta.base.BaseApplication;
import com.pratamawijaya.panggilpeta.helper.PreferencesUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import timber.log.Timber;

public class LoginViewActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  /* Request code used to invoke sign in user interactions. */
  private static final int RC_SIGN_IN = 0;

  /* Client used to interact with Google APIs. */
  private GoogleApiClient mGoogleApiClient;

  /* Is there a ConnectionResult resolution in progress? */
  private boolean mIsResolving = false;

  /* Should we automatically resolve ConnectionResults when possible? */
  private boolean mShouldResolve = false;

  private static final String URL = "https://www.yogyes.com/id/yogyakarta-travel-guide/map/";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_view_activity);
    getWindow().getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    ButterKnife.bind(this);

    // Build GoogleApiClient with access to basic profile
    mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(new Scope(Scopes.PROFILE))
        .build();

    testString();
  }

  private void testString() {
    StringRequest request =
        new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
          @Override public void onResponse(String s) {
            //Timber.d("hasil : " + s);

            Document doc = Jsoup.parse(s);

            Elements content = doc.getElementsByTag("script");
            Timber.d("banyak data :" + content.size());
            for (Element e : content)
              Timber.d("hasil : " + e.toString());
          }
        }, new Response.ErrorListener() {
          @Override public void onErrorResponse(VolleyError volleyError) {

          }
        });

    BaseApplication.getInstance().addToRequestQueue(request, "");
  }

  @Override protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override protected void onStop() {
    super.onStop();
    mGoogleApiClient.disconnect();
  }

  @OnClick(R.id.btnSignIn) public void onBtnSignInClick() {
    mShouldResolve = true;
    mGoogleApiClient.connect();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      // If the error resolution was not successful we should not resolve further.
      if (resultCode != RESULT_OK) {
        mShouldResolve = false;
      }

      mIsResolving = false;
      mGoogleApiClient.connect();
    }
  }

  @Override public void onConnected(Bundle bundle) {
    mShouldResolve = false;

    if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

      Timber.d("login");
      Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

      // set login
      PreferencesUtils.setDataBooleanToSP(this, PreferencesUtils.USER_ISLOGIN, true);

      // set name
      PreferencesUtils.setDataStringToSP(this, PreferencesUtils.USER_NAME,
          currentPerson.getDisplayName());

      // set email
      PreferencesUtils.setDataStringToSP(this, PreferencesUtils.USER_EMAIL,
          Plus.AccountApi.getAccountName(mGoogleApiClient));

      // set Photo
      // remove size 50px
      PreferencesUtils.setDataStringToSP(this, PreferencesUtils.USER_AVATAR,
          currentPerson.getImage().getUrl().replace("?sz=50", ""));

      Timber.d("id :" + currentPerson.getId());
      Timber.d("aboutme : " + currentPerson.getAboutMe());
      Timber.d("birthday :" + currentPerson.getBirthday());
      Timber.d("current location : " + currentPerson.getCurrentLocation());
      Timber.d("photos url :" + currentPerson.getUrl());
      Timber.d("email : " + Plus.AccountApi.getAccountName(mGoogleApiClient));

      //startActivity(new Intent(this, HomeViewActivity.class));
      Toast.makeText(LoginViewActivity.this, "User : " + currentPerson.getDisplayName(),
          Toast.LENGTH_SHORT).show();
    } else {
      Timber.d("not login");
      PreferencesUtils.setDataBooleanToSP(this, PreferencesUtils.USER_ISLOGIN, false);
    }
  }

  @Override public void onConnectionSuspended(int i) {

  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {
    if (!mIsResolving && mShouldResolve) {
      if (connectionResult.hasResolution()) {
        try {
          connectionResult.startResolutionForResult(this, RC_SIGN_IN);
          mIsResolving = true;
        } catch (IntentSender.SendIntentException e) {
          Timber.e("Could not resolve ConnectionResult." + e.toString());
          mIsResolving = false;
          mGoogleApiClient.connect();
        }
      } else {
        // Could not resolve the connection result, show the user an
        // error dialog.
        //showErrorDialog(connectionResult);
      }
    } else {
      // Show the signed-out UI
      //showSignedOutUI();
    }
  }
}
