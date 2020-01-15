package com.sp.firebasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
/* To Sigin using email and password
/* Once we register we can now sign
/* Add the dependency for the Firebase Authentication Android library to your module (app-level) Gradle file (usually app/build.gradle):
/* implementation 'com.google.firebase:firebase-auth:19.2.0'
/*https://firebase.google.com/docs/auth/android/password-auth
 */
public class LoginActivity extends AppCompatActivity {
    private EditText email,pwd;
    private Button login;
    // Instantiate firebaseauth
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailid);
        pwd = findViewById(R.id.passwordid);
        login = findViewById(R.id.login);
        // create an object
        auth = FirebaseAuth.getInstance();
        clickLogin();
    }

    public void clickLogin(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Email = email.getText().toString();
                String txt_Pwd = pwd.getText().toString();
                loginUser(txt_Email,txt_Pwd);
            }
        });
    }

    private void loginUser(String txt_email, String txt_pwd) {
        // call the object and provide it with email id and password
        auth.signInWithEmailAndPassword(txt_email, txt_pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String msg = "Login Successful";
                toastMsg(msg);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
