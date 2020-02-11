package com.example.fitnessapp.main;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysUserFragment;
import com.example.fitnessapp.user.Day;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.User;
import com.squareup.picasso.Picasso;

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

    //correct day
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    private Date date = new Date();
    private String correctDay;

    //layouts
    private ConstraintLayout mainDayLayout;


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
        switcherInnerExName = v.findViewById(R.id.tv_fitness_main_ex_day_exName);
        switcherInnerExNumber = v.findViewById(R.id.tv_fitness_main_ex_day_exNumber);
        ivInnerImage = v.findViewById(R.id.iv_fitness_main_ex_day_image);

        correctDay = sdf.format(date).toLowerCase();

        mainDayLayout = v.findViewById(R.id.fitness_main_ex_layout);




        user = (User) getArguments().getSerializable(KeysUserFragment.USER_DATA_TO_FRAGMENT);
        days = user.getDays();

        mainDayEx(days);







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
            mainDayLayout.setVisibility(View.INVISIBLE);
            return;
        }

        for (Day day : daysList) {
            if (day.getDayName().equals(correctDay)){
                mainDayLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
                switch (day.getDayName()){
                    case "sunday":
                        tvMainDayName.setText("יום ראשון");
                        getInnerExParameters(day);
                        break;
                    case "monday":
                        tvMainDayName.setText("יום שני");
                        getInnerExParameters(day);
                        break;
                    case "tuesday":
                        tvMainDayName.setText("יום שלישי");
                        getInnerExParameters(day);
                        break;
                    case "wednesday":
                        tvMainDayName.setText("יום רביעי");
                        getInnerExParameters(day);
                        break;
                    case "thursday":
                        tvMainDayName.setText("יום חמישי");
                        getInnerExParameters(day);
                        break;
                    case "friday":
                        tvMainDayName.setText("יום שישי");
                        getInnerExParameters(day);
                        break;
                    case "saturday":
                        tvMainDayName.setText("יום שבת");
                        getInnerExParameters(day);
                        break;
                }
            }
        }
    }

    private void getInnerExParameters(Day day){
        List<Exercise> exercises = day.getExercises();
        List<String> exNameList = new ArrayList<>();
        List<String> exImages = new ArrayList<>();
        List<String> exNumber = new ArrayList<>();
        long restTime = 0;
        long exTime = 0;
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

        long hours = TimeUnit.MILLISECONDS.toHours(restTime);
        long min = TimeUnit.MILLISECONDS.toMinutes(restTime) - (hours*60);
        String minString = String.valueOf(min);
        String hourString = String.valueOf(hours);
        String total;

//        tvInnerExName.setText(exNameList.get(0));
//        tvInnerExNumber.setText(exNumber.get(0));
//        Picasso.get().load(exImages.get(0)).into(ivInnerImage);

        if (min < 10){
            total = hourString + ":0" + min;
            tvMainEsTime.setText(String.valueOf(total));
        } else {
            total = hourString + ":" + min;
            tvMainEsTime.setText(String.valueOf(total));
        }


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
