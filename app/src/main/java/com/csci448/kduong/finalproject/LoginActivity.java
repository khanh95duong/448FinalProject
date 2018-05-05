package com.csci448.kduong.finalproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.net.MalformedURLException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by The Ngo on 3/12/2018.
 */


public class LoginActivity extends AppCompatActivity {

    private TextView mToRegister;
    private Button mSubmitButton;
    private EditText mUserName;
    private EditText mPassword;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        Fabric.with(this, new Crashlytics());

        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        mProgressDialog = new ProgressDialog(this);

        mUserName = (EditText) this.findViewById(R.id.user_name);
        mPassword = (EditText) this.findViewById(R.id.password);

        mSubmitButton = (Button) this.findViewById(R.id.login);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        mToRegister = (TextView) this.findViewById(R.id.to_register);
        mToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    // Log in after checking if everything is correct
    public void userLogin() {
        String username = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        // check to see if username or password is empty, if so, do not move on
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter an email for the username", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_LONG).show();
            return;
        }

        // if everything is good, show a progress bar while the data is uploaded to database (fancy)
        mProgressDialog.setMessage("Login In ...");
        mProgressDialog.show();

        // Sign in
        mFirebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        if(task.isSuccessful()) {
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Email or Password is incorrect, please try again   ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
