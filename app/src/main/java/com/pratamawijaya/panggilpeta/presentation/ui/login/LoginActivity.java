package com.pratamawijaya.panggilpeta.presentation.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.pratamawijaya.panggilpeta.R;

public class LoginActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btn_login)void clickLogin(){
    Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
  }
}
