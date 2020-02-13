package com.example.fitnessapp.main;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.keys.KeysUserFragment;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Day;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.User;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FitnessFragment extends Fragment {

    private FitnessViewModel mViewModel;
    private User user;
    private List<Day> days = new ArrayList<>();

    private TextView tvMainDayName;
    private TextView tvMainNumberOfEx;
    private TextView tvMainEsTime;
    private TextSwitcher switcherInnerExName;
    private TextView tvInnerExNameTEXTVIEW;
    private TextSwitcher switcherInnerExNumber;
    private TextView tvInnerExNumberTEXTVIEW;
    private ImageView ivInnerImage;
    private Button btnMainStart;

    //correct day
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    private Date date = new Date();
    private String correctDay;

    //layouts
    private ConstraintLayout mainDayLayout;
    private ConstraintLayout mainDayLayoutDayOff;
    private TextView dayOffName;
    private LottieAnimationView lottieAnimationDayOff;





    public static FitnessFragment newInstance() {
        return new FitnessFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fitness_fragment, container, false);

        tvMainDayName = v.findViewById(R.id.tv_fitness_main_ex_day_name);
        tvMainNumberOfEx = v.findViewById(R.id.tv_fitness_main_ex_day_inner_exNumber);
        tvMainEsTime = v.findViewById(R.id.tv_fitness_main_ex_day_esTime);
        btnMainStart = v.findViewById(R.id.fitness_btn_start);
        switcherInnerExName = v.findViewById(R.id.tv_fitness_main_ex_day_exName);
        switcherInnerExNumber = v.findViewById(R.id.tv_fitness_main_ex_day_exNumber);
        ivInnerImage = v.findViewById(R.id.iv_fitness_main_ex_day_image);

        correctDay = sdf.format(date).toLowerCase();

        mainDayLayout = v.findViewById(R.id.fitness_main_ex_layout);
        mainDayLayoutDayOff = v.findViewById(R.id.fitness_main_ex_layout_dayOff);
        dayOffName = v.findViewById(R.id.fitness_tv_dayoff_dayName);
        lottieAnimationDayOff = v.findViewById(R.id.lottie_dayOff);




        user = (User) getArguments().getSerializable(KeysUserFragment.USER_DATA_TO_FRAGMENT);
        days = user.getDays();

        mainDayEx(days);

        //recyclerView
        final RecyclerView recyclerView = v.findViewById(R.id.fitness_recyclerView);
        FitnessDaysRecyclerAdapter adapter = new FitnessDaysRecyclerAdapter(days,getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //start Button on main ex
        btnMainStart.setOnClickListener(btn->{
            List<Exercise> exercisesData = new ArrayList<>();
            String sendDayName = "";
            for (int i = 0; i < days.size() ; i++) {
                Day day = days.get(i);
                String dayName = day.getDayName();
                if (dayName.equals(correctDay)){
                    exercisesData = day.getExercises();
                    sendDayName = dayName;
                }
            }


            Intent intent = new Intent(getContext(), ExersiceActivity.class);
            intent.putExtra(KeysIntents.EX_LIST, (Serializable) exercisesData);
            intent.putExtra(KeysIntents.DAY_NAME, sendDayName);
            startActivity(intent);
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FitnessViewModel.class);
        // TODO: Use the ViewModel
    }



    private void mainDayEx(List<Day> daysList){

        //if dosent have practice today, change main layout
        List<String> findDayName = new ArrayList<>();
        for (Day day : daysList) {
            findDayName.add(day.getDayName());
        }

        if (!findDayName.contains(correctDay)){
            dayOffName.setText(CustomMethods.convertDateToHebrew(correctDay));
            lottieAnimationDayOff.playAnimation();
            mainDayLayout.setVisibility(View.INVISIBLE);
            mainDayLayoutDayOff.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
            return;
        }

        //have practice
        for (Day day : daysList) {
            if (day.getDayName().equals(correctDay)){
                mainDayLayoutDayOff.setVisibility(View.INVISIBLE);
                mainDayLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
                tvMainDayName.setText(CustomMethods.convertDateToHebrew(day.getDayName()));
                getInnerExParameters(day);
            }
        }
    }

    private void getInnerExParameters(Day day){
        List<Exercise> exercises = day.getExercises();
        List<String> exNameList = new ArrayList<>();
        List<String> exImages = new ArrayList<>();
        List<String> exNumber = new ArrayList<>();
        long restTime = 0;
        for (int i = 0; i <exercises.size() ; i++) {
            exNameList.add(exercises.get(i).getExName());
            exImages.add(exercises.get(i).getImage());
            String exNumberString = "תרגיל " + (i+1);
            exNumber.add(exNumberString);
            restTime += (exercises.get(i).getRest());
            //evrey set add extra 1 min to the esTime
            restTime += (exercises.get(i).getSets()) * 60_000;
        }


        tvMainNumberOfEx.setText(String.valueOf(exercises.size()));

        //es time
        String esTime = CustomMethods.getEsTime(restTime);
        tvMainEsTime.setText(String.valueOf(esTime));


        CountDownTimer countDownTimer = new CountDownTimer(exercises.size() * 3_000, 3000) {
            int counter = 0;
            @Override
            public void onTick(long millisUntilFinished) {

                switcherInnerExName.setText(exNameList.get(counter % exNameList.size()));
                switcherInnerExNumber.setText(exNumber.get(counter % exNumber.size()));

                Picasso.get().load(exImages.get(counter % exImages.size())).into(ivInnerImage);
                counter++;
            }

            @Override
            public void onFinish() {
                counter = 0;
                this.start();

            }
        };

        switcherInnerExNumber.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvInnerExNumberTEXTVIEW = new TextView(getContext());
                tvInnerExNumberTEXTVIEW.setTextColor(getResources().getColor(R.color.lightGreen));
                tvInnerExNumberTEXTVIEW.setTextSize(12);
                tvInnerExNumberTEXTVIEW.setGravity(Gravity.CENTER_HORIZONTAL);
                return tvInnerExNumberTEXTVIEW;
            }
        });
        switcherInnerExName.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvInnerExNameTEXTVIEW = new TextView(getContext());
                tvInnerExNameTEXTVIEW.setTextColor(getResources().getColor(R.color.mainGreen));
                tvInnerExNameTEXTVIEW.setTextSize(14);
                tvInnerExNameTEXTVIEW.setGravity(Gravity.CENTER_HORIZONTAL);
                return tvInnerExNameTEXTVIEW;
            }
        });


        countDownTimer.start();

    }
}
