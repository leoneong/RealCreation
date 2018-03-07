package com.example.realcreation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChooseMfaContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private final String TAG="LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private AlertDialog userDialog;
    private ProgressDialog waitDialog;

    // Screen fields
    private EditText inUsername;
    private EditText inPassword;

    //Continuations
    private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;
    private ForgotPasswordContinuation forgotPasswordContinuation;
    private NewPasswordContinuation newPasswordContinuation;
    private ChooseMfaContinuation mfaOptionsContinuation;

    // User Details
    private String username;
    private String password;

    CognitoCredentialsProvider credentialsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppHelper.init(getApplicationContext());
        inUsername = findViewById(R.id.editTextUserId);
        inPassword = findViewById(R.id.editTextUserPassword);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:d0b27010-f1c0-4bdf-8f19-3a920f552a7b", // Identity pool ID
                Regions.US_EAST_1 // Region
        );

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check whether it is signed in already no need go through OnCreate
    }

    // Button Function
    public void logIn(View view) {
        signIn();
    }

    // This is what happens when you already signed in
    private void findCurrent() {

        // Try Cognito first
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        if (username != null) {
            AppHelper.setUser(username);
            inUsername.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }

        if (username != null) {
            AppHelper.setUser(username);
            inUsername.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    // This is sign in process when clicking the button
    private void signIn() {
        username = inUsername.getText().toString();
        if(username == null || username.length() < 1) {
            TextView label = findViewById(R.id.username);
            label.setText(inUsername.getHint()+" cannot be empty");
            return;
        }

        AppHelper.setUser(username);

        password = inPassword.getText().toString();
        if(password == null || password.length() < 1) {
            TextView label = (TextView) findViewById(R.id.passwordID);
            label.setText(inPassword.getHint()+" cannot be empty");
            return;
        }

        showWaitDialog("Signing in...");
        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
    }

    // Launch to the userActivity and close the SignIn
    private void launchUser() {
        Intent userActivity = new Intent(this, MainActivity.class);
        userActivity.putExtra("name", username);
        startActivityForResult(userActivity, 4);
        finish();
    }

    // Whenever the user is authenticated or going through log in,  this will execute
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            // Setting up the information to the AppHelper
            Log.d(TAG, " -- Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);

            Map<String, String> logins = new HashMap<>();
            logins.put("cognito-idp.us-east-1.amazonaws.com/us-east-1_zSLbuwVxl", cognitoUserSession.getIdToken().getJWTToken());
            credentialsProvider.setLogins(logins);
            AppHelper.setCognitoCredentialsProvider(credentialsProvider);
            closeWaitDialog();
            launchUser();
        }

        // This also happens when sign in success
        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            closeWaitDialog();
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            closeWaitDialog();
            Log.d(TAG, "Accessed MFA");

        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            TextView label = (TextView) findViewById(R.id.username);
            label.setText("Sign-in failed");

            label = (TextView) findViewById(R.id.username);
            label.setText("Sign-in failed");

            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            /**
             * For Custom authentication challenge, implement your logic to present challenge to the
             * user and pass the user's responses to the continuation.
             */
            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
                // This is the first sign-in attempt for an admin created user
                newPasswordContinuation = (NewPasswordContinuation) continuation;
                Log.d(TAG, "NEW PASSWORD REQUIRED");
                closeWaitDialog();
            } else if ("SELECT_MFA_TYPE".equals(continuation.getChallengeName())) {
                closeWaitDialog();
                Log.d(TAG, "SELECT_MFA_TYPE");
            }
        }
    };

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        Log.d(TAG, "Get User Authentication");
        if (username != null) {
            this.username = username;
            AppHelper.setUser(username);
        }
        if (this.password == null) {
            inUsername.setText(username);
            password = inPassword.getText().toString();
            if (password == null) {
                TextView label = (TextView) findViewById(R.id.passwordID);
                label.setText(inPassword.getHint() + " enter password");
                return;
            }

            if (password.length() < 1) {
                TextView label = (TextView) findViewById(R.id.passwordID);
                label.setText(inPassword.getHint() + " enter password");
                return;
            }
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

}
