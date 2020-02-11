package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessapp.intro.FirstIntroFragment;
import com.example.fitnessapp.intro.FourthIntroFragment;
import com.example.fitnessapp.intro.SecIntroFragment;
import com.example.fitnessapp.intro.ThirdIntroFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private Button btnNext;
    private Button btnBack;
    private TextView btnSkip;
    private Button btnStart;
    private static int count = 1;
    private static int clickedOnNext = 0;

    private List<Fragment> fragments = new ArrayList<>();
    private Animation animFaidOut;
    private Animation animFaidIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getSupportFragmentManager().beginTransaction().replace(R.id.introMFragment, new FirstIntroFragment()).commit();

        btnNext = findViewById(R.id.intro_btn_next);
        btnBack = findViewById(R.id.intro_btn_back);
        btnSkip = findViewById(R.id.intro_btn_skip);
        btnStart = findViewById(R.id.intro_btn_start);

        fragments.add(new FirstIntroFragment());
        fragments.add(new SecIntroFragment());
        fragments.add(new ThirdIntroFragment());
        fragments.add(new FourthIntroFragment());


        btnNext.setOnClickListener(v -> {
            btnNextSetup();
        });

        btnBack.setOnClickListener(v -> {
            btnBackSetup();
        });

        btnSkip.setOnClickListener(v -> {
            btnSkipSetup();
        });

        btnStart.setOnClickListener(v -> {
            btnSkipSetup();
        });
    }

    private void btnNextSetup(){
        clickedOnNext++;
        if (count < 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.introMFragment, fragments.get(count % fragments.size())).commit();
            count++;

            if (count == 4) {
                animFaidOut = AnimationUtils.loadAnimation(this, R.anim.faidout);
                btnNext.setAnimation(animFaidOut);
                btnNext.setVisibility(View.INVISIBLE);

                animFaidIn = AnimationUtils.loadAnimation(this,R.anim.faidin);
                btnStart.setAnimation(animFaidIn);
                btnStart.setVisibility(View.VISIBLE);

            }

            if (count == 2){
                animFaidIn = AnimationUtils.loadAnimation(this,R.anim.faidin);
                btnBack.setAnimation(animFaidIn);
                btnBack.setVisibility(View.VISIBLE);
            }

            if (clickedOnNext == 1){
                animFaidIn = AnimationUtils.loadAnimation(this,R.anim.faidin);
                btnSkip.startAnimation(animFaidIn);
                btnSkip.setVisibility(View.VISIBLE);
            }


            System.out.println(count);
        }

    }

    private void btnBackSetup(){
        if (count > 1) {
            count--;
            getSupportFragmentManager().beginTransaction().replace(R.id.introMFragment, fragments.get(count % fragments.size() - 1)).commit();

            if (count <= 1) {
                animFaidOut = AnimationUtils.loadAnimation(this, R.anim.faidout);
                btnBack.setAnimation(animFaidOut);
                btnBack.setVisibility(View.INVISIBLE);
            }

            if (count == 3) {
                animFaidIn = AnimationUtils.loadAnimation(this, R.anim.faidin);
                btnNext.setAnimation(animFaidIn);
                btnNext.setVisibility(View.VISIBLE);

                animFaidOut = AnimationUtils.loadAnimation(this, R.anim.faidout);
                btnStart.setAnimation(animFaidOut);
                btnStart.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void btnSkipSetup(){
        Intent loginIntent = new Intent(this, LogActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.faidin, R.anim.faidout);

    }

}
