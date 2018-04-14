package com.mouris.mario.getme.ui.welcome_screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.repositories.GeneralRepository;
import com.mouris.mario.getme.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookLoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = FacebookLoginActivity.class.getSimpleName();

    private static final int FACEBOOK_PROFILE_SIZE = 300;

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private GeneralRepository mGeneralRepository;
    @BindView(R.id.loginButton) LoginButton mLoginButton;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        ButterKnife.bind(this);

        //Firebase Auth code
        mAuth = FirebaseAuth.getInstance();
        mGeneralRepository = GeneralRepository.getInstance();

        //Facebook Login code
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions("public_profile", "email", "user_friends");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(LOG_TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i(LOG_TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(LOG_TAG, "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.i(LOG_TAG, "handleFacebookAccessToken:" + token);
        setLoading(true);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i(LOG_TAG, "signInWithCredential:success");

                        routeToAppropriateActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(LOG_TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    setLoading(false);
                });
    }

    private void routeToAppropriateActivity() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && Profile.getCurrentProfile() != null) {
            mGeneralRepository.getUserById(currentUser.getUid()).observe(this, user -> {
                if (user != null) {
                    //A user object is already created, go inside the app
                    Log.i(LOG_TAG, "User is logged in and registered");
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    //No user object is found, create one
                    Log.i(LOG_TAG, "No user object found");

                    User newUser = new User();
                    newUser.display_name = currentUser.getDisplayName();
                    newUser.facebook_link = Profile.getCurrentProfile().getLinkUri().toString();
                    newUser.profile_picture = Profile.getCurrentProfile()
                            .getProfilePictureUri(FACEBOOK_PROFILE_SIZE, FACEBOOK_PROFILE_SIZE).toString();

                    String currentUserId = Profile.getCurrentProfile().getId();
                    mGeneralRepository.createNewUser(currentUserId, newUser,
                            ((databaseError, databaseReference) -> {

                                if (databaseError == null) {
                                    Intent mainIntent = new Intent(this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                } else {
                                    Log.e(LOG_TAG, "There was an error creating new user object", databaseError.toException());
                                }

                            }));
                }
            });

        } else {
            Log.e(LOG_TAG, "The current user equals null");
        }
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
            mLoginButton.setEnabled(false);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mLoginButton.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
