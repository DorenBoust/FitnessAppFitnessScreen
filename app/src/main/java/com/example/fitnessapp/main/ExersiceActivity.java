package com.example.fitnessapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Exercise;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExersiceActivity extends AppCompatActivity {

    private ImageView btnNext;
    private ImageView btnBack;
    private TextView tvSets;
    private TextView tvRepit;
    private TextView tvNote;
    private TextView tvNoteTitle;
    private TextView tvExName;
    private TextView tvExNumber;
    private TextView tvDay;
    private ImageView ivMainImage;
    private int counterEx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exersice);

        btnBack = findViewById(R.id.iv_ex_activity_title_backGreen);
        btnNext = findViewById(R.id.iv_ex_activity_title_nextGreen);
        tvDay = findViewById(R.id.tv_ex_activity_title_day);
        tvSets = findViewById(R.id.tv_ex_activity_details_set);
        tvRepit = findViewById(R.id.tv_ex_activity_details_repit);
        tvNote = findViewById(R.id.tv_ex_activity_details_notes);
        tvNoteTitle = findViewById(R.id.textView11);
        tvExName = findViewById(R.id.tv_ex_activity_title_exName);
        tvExNumber = findViewById(R.id.tv_ex_activity_title_exNumber);
        ivMainImage = findViewById(R.id.iv_ex_activity_details_exImage);





        //get data
        Intent intent = getIntent();
        List<Exercise> exercises = (List<Exercise>) intent.getSerializableExtra(KeysIntents.EX_LIST);
        String dayName = intent.getStringExtra(KeysIntents.DAY_NAME);

        //regular component
        regularComponents(exercises,dayName);

        //recycler
        Exercise exercise = exercises.get(0);
        RecyclerView recyclerView = findViewById(R.id.tv_ex_activity_details_recycler);
        ExersiceFieldRecyclerAdapter adapter = new ExersiceFieldRecyclerAdapter(exercise,getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnNext.setOnClickListener(v->{
            //change recycler
            Exercise exerciseIN = exercises.get(++counterEx);
            ExersiceFieldRecyclerAdapter adapterIN = new ExersiceFieldRecyclerAdapter(exerciseIN,getLayoutInflater());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterIN);

            //regular Component
            regularComponents(exercises,dayName);

            //display arrow
            if (counterEx == exercises.size()-1){

                btnNext.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidout));
                btnNext.setVisibility(View.INVISIBLE);
                return;
            } else {
                btnNext.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ex_activity_next_button));
            }
            if (counterEx != 0){
                btnBack.setVisibility(View.VISIBLE);
            }
        });


        btnBack.setOnClickListener(v->{
            //change recycler
            Exercise exerciseIN = exercises.get(--counterEx);
            ExersiceFieldRecyclerAdapter adapterIN = new ExersiceFieldRecyclerAdapter(exerciseIN,getLayoutInflater());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterIN);

            //regular Component
            regularComponents(exercises,dayName);


            //display arrow
            if (counterEx == 0){
                btnBack.setAnimation(AnimationUtils.loadAnimation(this,R.anim.faidout));
                btnBack.setVisibility(View.INVISIBLE);
                return;
            } else {
                btnBack.setAnimation(AnimationUtils.loadAnimation(this,R.anim.ex_activity_back_button));
            }
            btnNext.setVisibility(View.VISIBLE);
        });


    }

    private void regularComponents(List<Exercise> exercises, String dayName){
        Picasso.get().load(exercises.get(counterEx).getImage()).into(ivMainImage);
        tvDay.setText(CustomMethods.convertDateToHebrew(dayName));
        tvSets.setText(String.valueOf(exercises.get(counterEx).getSets()));
        tvRepit.setText(String.valueOf(exercises.get(counterEx).getRepitition()));
        tvExName.setText(String.valueOf(exercises.get(counterEx).getExName()));
        String tvExNumberString = "תרגיל " + (counterEx + 1) + "/" + exercises.size();
        tvExNumber.setText(tvExNumberString);
        tvNote.setText(String.valueOf(exercises.get(counterEx).getNotes()));
        noteColor(exercises,counterEx);

    }

    private void noteColor(List<Exercise> exercises, int counterEx){
        if (!exercises.get(counterEx).getNotes().equals("אין")){
            tvNote.setTextColor(getResources().getColor(R.color.mainRed));
            tvNoteTitle.setTextColor(getResources().getColor(R.color.mainRed));
        } else {
            tvNote.setTextColor(getResources().getColor(R.color.waite));
            tvNoteTitle.setTextColor(getResources().getColor(R.color.waite));

        }

    }

}
