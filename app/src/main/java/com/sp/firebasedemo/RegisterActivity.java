package com.sp.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/*
/* To Sigin using email and password
/* First we need to register user
/* Add the dependency for the Firebase Authentication Android library to your module (app-level) Gradle file (usually app/build.gradle):
/* implementation 'com.google.firebase:firebase-auth:19.2.0'
/*https://firebase.google.com/docs/auth/android/password-auth
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText emailReg, passwordReg;
    private Button register;
    // instantiate FirebaseAuth
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailReg = findViewById(R.id.emailid);
        passwordReg = findViewById(R.id.passwordid);
        register = findViewById(R.id.login);
        // get an object
        auth = FirebaseAuth.getInstance();
        registerClick();
    }

    public void registerClick(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = emailReg.getText().toString();
                String password_txt = passwordReg.getText().toString();

                if (TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt)){
                   String msg = "Empty Username or Password";
                    toastMsg(msg);

                }else if (password_txt.length()<6){
                    String msg = "Password is too short";
                    toastMsg(msg);
                }else
                    registerUser(email_txt,password_txt);
            }
        });
    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void registerUser(String email_txt, String password_txt) {
        // To create username and password
        auth.createUserWithEmailAndPassword(email_txt,password_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    String msg = "Registration Successful";
                    toastMsg(msg);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }else {
                    String msg = "Registration Unsuccessful";
                    toastMsg(msg);
                }
            }
        });
    }
}
