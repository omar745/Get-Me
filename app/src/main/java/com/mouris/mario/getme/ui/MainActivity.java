package com.mouris.mario.getme.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.ui.welcome_screens.FacebookLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.logout_button)
    void logoutButtonClicked() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent facebookLoginIntent = new Intent(this, FacebookLoginActivity.class);
        facebookLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(facebookLoginIntent);
        finish();
    }
}
