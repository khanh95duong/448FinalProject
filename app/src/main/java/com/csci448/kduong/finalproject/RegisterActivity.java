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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import io.fabric.sdk.android.Fabric;

/**
 * Created by The Ngo on 4/22/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    // data for registering user
    private Button mRegister;
    private TextView mToLogin;
    private EditText mUserName;
    private EditText mPassword;

    // other variables
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        Fabric.with(this, new Crashlytics());

        // authenticating the database
        mFirebaseAuth = FirebaseAuth.getInstance();

        //progress dialog
        mProgressDialog = new ProgressDialog(this);

        mUserName = (EditText) this.findViewById(R.id.register_user_name);
        mPassword = (EditText) this.findViewById(R.id.register_password);

        mRegister = (Button) this.findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mToLogin = (TextView) this.findViewById(R.id.to_login);
        mToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // close the activity
            }
        });

    }

    //Register user after checking if everything is correct
    public void registerUser() {

        // get string from the edit texts, not sure why there is trim, it is from the guide
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
        mProgressDialog.setMessage("Registering User ...");
        mProgressDialog.show();

        // adding user to the database
        mFirebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        if(task.isSuccessful()) {
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Could not register the user", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}