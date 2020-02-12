package com.example.fitnessapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Exercise;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExersiceActivity extends AppCompatActivity {

    private ImageView btnNext;
    private ImageView btnBack;
    private TextView tvDay;
    private int counterEx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exersice);

        btnBack = findViewById(R.id.iv_ex_activity_title_backGreen);
        btnNext = findViewById(R.id.iv_ex_activity_title_nextGreen);
        tvDay = findViewById(R.id.tv_ex_activity_title_day);





        //get data
        Intent intent = getIntent();
        List<Exercise> exercises = (List<Exercise>) intent.getSerializableExtra(KeysIntents.EX_LIST);
        String dayName = intent.getStringExtra(KeysIntents.DAY_NAME);


        //regular component
        tvDay.setText(CustomMethods.convertDateToHebrew(dayName));


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

            //display arrow
            if (counterEx == exercises.size()-1){
                btnNext.setVisibility(View.INVISIBLE);
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

            //display arrow
            if (counterEx == 0){
                btnNext.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.INVISIBLE);
            }
        });


    }

}
