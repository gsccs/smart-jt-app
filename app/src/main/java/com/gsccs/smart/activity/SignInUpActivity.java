package com.gsccs.smart.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.gsccs.smart.R;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.LoginHttps;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *  注册登录模块
 *  Created by x.d zhang on 2016/11/19.
 */
public class SignInUpActivity extends  BaseActivity{

    @Bind(R.id.username_edit)
    EditText accountEdit;
    @Bind(R.id.password_edit)
    EditText passwordEdit;
    @Bind(R.id.sign_commit_button)
    Button signCommitButton;
    @Bind(R.id.sign_change_button)
    Button signChangeButton;
    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.login_form)
    ScrollView loginForm;
    @Bind(R.id.smscode_edit)
    EditText smscodelEdit;
    @Bind(R.id.password_confirm_edit)
    EditText passwordConfirmEdit;
    @Bind(R.id.username_signup_form)
    LinearLayout usernameSignupForm;

    @Bind(R.id.smscode_btn)
    Button smscodeButton;
    //是否登录操作
    private Boolean isSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinup_activity);
        ButterKnife.bind(this);
        //isSignIn
        isSignIn = getIntent().getBooleanExtra(BaseConst.IS_SIGN_IN, false);
        Log.d("isSignIn",String.valueOf(isSignIn));
        if (isSignIn) {
            usernameSignupForm.setVisibility(View.INVISIBLE);
            signCommitButton.setText(R.string.action_sign_in);
            signChangeButton.setText(R.string.action_sign_up);
        }

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSign();
                    return true;
                }
                return false;
            }
        });

        signCommitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSign();
            }
        });

        signChangeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("signChangeButton",String.valueOf(isSignIn));
                if (isSignIn){      //当前为登录状态
                    isSignIn = false;
                    usernameSignupForm.setVisibility(View.VISIBLE);
                    signCommitButton.setText(R.string.action_sign_up);
                    signChangeButton.setText(R.string.action_sign_in);
                }else {
                    isSignIn = true;
                    usernameSignupForm.setVisibility(View.INVISIBLE);
                    signCommitButton.setText(R.string.action_sign_in);
                    signChangeButton.setText(R.string.action_sign_up);
                }
            }
        });

        smscodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsCode();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.colorPrimary));
        loginProgress.setIndeterminateDrawable(threeBounce);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }


    private void sendSmsCode(){
        // Reset errors.
        accountEdit.setError(null);
        String account = accountEdit.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(account)) {
            accountEdit.setError(getString(R.string.error_field_required));
            focusView = accountEdit;
            cancel = true;
        }

        if (!isAccountValid(account)){
            accountEdit.setError(getString(R.string.error_invalid_account));
            focusView = accountEdit;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            LoginHttps.getInstance(this).sendSmS(account,refreshHandler);
        }

    }

    private void attemptSign() {

        // Reset errors.
        accountEdit.setError(null);
        passwordEdit.setError(null);

        // Store values at the time of the login attempt.
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirmPassword = passwordConfirmEdit.getText().toString();
        String smscode = smscodelEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(account)) {
            accountEdit.setError(getString(R.string.error_field_required));
            focusView = accountEdit;
            cancel = true;
        }

        if (!isAccountValid(account)){
            accountEdit.setError(getString(R.string.error_invalid_account));
            focusView = accountEdit;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            passwordEdit.setError(getString(R.string.error_invalid_password));
            focusView = passwordEdit;
            cancel = true;
        }
        if(!isSignIn){      //注册操作
            if (!isPasswordValid(confirmPassword)) {
                passwordConfirmEdit.setError(getString(R.string.error_invalid_password));
                focusView = passwordConfirmEdit;
                cancel = true;
            }

            if (!password.equals(confirmPassword)) {
                passwordConfirmEdit.setError(getString(R.string.error_invalid_confirm_password));
                focusView = passwordConfirmEdit;
                cancel = true;
            }

            if (!isSmscodeValid(smscode)) {
                smscodelEdit.setError(getString(R.string.error_invalid_smscode));
                focusView = smscodelEdit;
                cancel = true;
            }
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //mAuthTask = new SignTask(username, password, email);
            //mAuthTask.execute((Void) null);
            Log.d("username",account+"|"+password);

            actionSign(account, password, smscode);
        }
    }

    private void actionSign(String username, String password, String smscode){
        if (isSignIn){
            LoginHttps.getInstance(this).userLogin(username,password,refreshHandler);
        }else {
            LoginHttps.getInstance(this).register(username,password,smscode,refreshHandler);
        }
    }

    private boolean isAccountValid(String username) {
       //return username.length() > 2 && StringUtils.isLetter(username.charAt(0));
        return username.length() ==11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 1;
    }

    private boolean isSmscodeValid(String smscode){
        return smscode.length() ==6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        } else {
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void actionStart(Activity welcomeContext, Boolean isSignIn) {
        Intent intent = new Intent(welcomeContext, SignInUpActivity.class);
        intent.putExtra("isSignIn", isSignIn);
        welcomeContext.startActivity(intent);
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_USER_LOGIN_PHONE:
                    showProgress(false);
                    Intent intent = new Intent(SignInUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    //LocalBroadcastManager.getInstance(SignInUpActivity.this).sendBroadcast(new Intent(Constant.ACTION_RELOGIN));
                    finish();

                    //passwordEdit.setError(getString(R.string.error_network));
                    //passwordEdit.requestFocus();
                    break;
                case BaseConst.WHAT_USER_REGISTER:
                    showProgress(false);
                    isSignIn = false;       //转到登录操作
                    signChangeButton.callOnClick();
                    break;
                case BaseConst.WHAT_SMS_REGISTER:

                    break;
                default:
                    showProgress(false);
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}

