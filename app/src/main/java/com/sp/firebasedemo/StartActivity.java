package com.sp.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        register = findViewById(R.id.registerBtn);
        login = findViewById(R.id.loginBtn);
        registerBtn();
        loginBtn();
    }

    public void registerBtn(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    public void loginBtn(){
//        String email = "";
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.sendPasswordResetEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            toastMsg("Reset link sent to your email");
//
//                        } else {
//                            toastMsg("Unable to send reset mail");
//                        }
//                    }
//                });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
                finish();
            }
        });

//        String password = "";
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.getCurrentUser().updatePassword(password)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            toastMsg("Password changes successfully");
//                            finish();
//                        } else {
//                            toastMsg("password not changed");
//
//                        }
//                    }
//                });
    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
        }
    }
}
