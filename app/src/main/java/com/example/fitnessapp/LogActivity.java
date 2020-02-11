package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fitnessapp.loginAndRegisterAnimated.LoginAniFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogActivity extends AppCompatActivity {

    TextView tvLogin;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        fAuth = FirebaseAuth.getInstance();

        //if user are login open main activity if not open login fragment
        if (fAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.faidin, R.anim.faidout);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.faidin,R.anim.faidout).replace(R.id.mFragment, new LoginAniFragment()).commit(); //TODO:NOT WORKING!!
        }

    }

}
