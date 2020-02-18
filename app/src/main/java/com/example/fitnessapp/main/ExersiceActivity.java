package com.example.fitnessapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.ExerciseHistory;
import com.example.fitnessapp.user.ExersixeOneRawHistory;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static com.example.fitnessapp.models.AppNotification.CHANNEL_1_ID;

public class ExersiceActivity extends AppCompatActivity {

    //navigation ex btn
    private ImageView btnNext;
    private ImageView btnBack;
    private Button btnFinishEx;
    private boolean crash = true;

    private TextView tvSets;
    private TextView tvRepit;
    private TextView tvNote;
    private TextView tvNoteTitle;
    private TextView tvExName;
    private TextView tvExNumber;
    private TextView tvDay;
    private ImageView ivMainImage;
    private int counterEx = 0;


    //timer
    private TextView tvTimer;
    private Button btnTimerStop;
    private Button btnTimerStart;
    private Button btnTimerResume;
    private Button btnTimerClear;
    private long timerTime;
    private CountDownTimer timerCount;
    private boolean mTimerRunning;
    private long mTimerLeft;

    //notification
    private NotificationManagerCompat notificationManager;

    //recyclerview
    private RecyclerView recyclerViewComponent;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exersice);

        btnBack = findViewById(R.id.iv_ex_activity_title_backGreen);
        btnNext = findViewById(R.id.iv_ex_activity_title_nextGreen);
        btnFinishEx = findViewById(R.id.btn_finish_ex);
        tvDay = findViewById(R.id.tv_ex_activity_title_day);
        tvSets = findViewById(R.id.tv_ex_activity_details_set);
        tvRepit = findViewById(R.id.tv_ex_activity_details_repit);
        tvNote = findViewById(R.id.tv_ex_activity_details_notes);
        tvNoteTitle = findViewById(R.id.textView11);
        tvExName = findViewById(R.id.tv_ex_activity_title_exName);
        tvExNumber = findViewById(R.id.tv_ex_activity_title_exNumber);
        ivMainImage = findViewById(R.id.iv_ex_activity_details_exImage);

        tvTimer = findViewById(R.id.ex_activity_timer);
        btnTimerStart = findViewById(R.id.ex_activity_timerStart);
        btnTimerStop = findViewById(R.id.ex_activity_timerStop);
        btnTimerResume = findViewById(R.id.ex_activity_timerResume);
        btnTimerClear = findViewById(R.id.ex_activity_timerClear);
        notificationManager = NotificationManagerCompat.from(this);

        recyclerViewComponent = findViewById(R.id.tv_ex_activity_details_recycler);



        //get data
        Intent intent = getIntent();
        List<Exercise> exercises = (List<Exercise>) intent.getSerializableExtra(KeysIntents.EX_LIST);
        String dayName = intent.getStringExtra(KeysIntents.DAY_NAME);

        timerTime = exercises.get(counterEx).getRest();
        mTimerLeft = timerTime;



        //regular component
        regularComponents(exercises,dayName);

        //recycler
        Exercise exercise = exercises.get(counterEx);
        RecyclerView recyclerView = findViewById(R.id.tv_ex_activity_details_recycler);
        ExersiceFieldRecyclerAdapter adapter = new ExersiceFieldRecyclerAdapter(exercise,getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //navigation arrow btn
        btnNext.setOnClickListener(v->{
            if (crash) {

                boolean dialogstatus = true;

                //get the recyclerview edit text and export to firebase store
                RecyclerView.Adapter adapter1 = recyclerViewComponent.getAdapter();
                int itemCount = adapter1.getItemCount();
                System.out.println(itemCount);

                List<ExersixeOneRawHistory> exersixeOneRawHistories = new ArrayList<>();
                for (int i = 0; i < itemCount ; i++) {
                    RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerViewComponent.findViewHolderForAdapterPosition(i);
                    View view = recyclerView.getChildAt(i);

                    EditText etRepit = (EditText) view.findViewById(R.id.ex_activity_recycler_repit);
                    Integer repit = null;
                    if (!etRepit.getText().toString().equals("")) {
                        repit = Integer.parseInt(etRepit.getText().toString());
                    }

                    EditText etKG = (EditText) view.findViewById(R.id.ex_activity_recycler_kg);
                    Double kg = null;
                    if (!etKG.getText().toString().equals("")) {
                        kg = Double.parseDouble(etKG.getText().toString());
                    }

                    if (kg != null && repit != null) {
                        exersixeOneRawHistories.add(new ExersixeOneRawHistory((i + 1), repit, kg));
                        if (i == itemCount - 1){
                            dialogstatus = false;
                        }
                    }

                }

                if (dialogstatus){
                    showDialog();
                    return;
                }


                System.out.println(exersixeOneRawHistories);

                ExerciseHistory exerciseHistory = new ExerciseHistory("17/02/2019", exersixeOneRawHistories);
                List<ExerciseHistory> exerciseHistoryList = new ArrayList<>();
                exerciseHistoryList.add(exerciseHistory);

                ExerciseHistoryToFIreBase exerciseHistoryToFIreBase = new ExerciseHistoryToFIreBase(exerciseHistoryList);

                System.out.println(exerciseHistory);
                Task<Void> saveOnDB = fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid())
                        .collection(tvExName.getText().toString()).document("12.12.12").set(exerciseHistoryToFIreBase);



                crash = false;

                //change recycler

                Exercise exerciseIN = exercises.get(++counterEx);
                ExersiceFieldRecyclerAdapter adapterIN = new ExersiceFieldRecyclerAdapter(exerciseIN, getLayoutInflater());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapterIN);

                //regular Component
                regularComponents(exercises, dayName);

                //display arrow
                if (counterEx == exercises.size() - 1) {

                    btnNext.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidout));
                    btnNext.setVisibility(View.INVISIBLE);
                    btnFinishEx.setAnimation(AnimationUtils.loadAnimation(this,R.anim.faidin));
                    btnFinishEx.setVisibility(View.VISIBLE);
                } else {
                    btnNext.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ex_activity_next_button));
                }

//                if (counterEx == 1){
//                    btnBack.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidin));
//                    btnBack.setVisibility(View.VISIBLE);
//                }

                btnNext.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        crash = true;
                    }
                }, 500);
            }



        });

        btnFinishEx.setOnClickListener(v->{
            boolean dialogstatus = true;

            //get the recyclerview edit text and export to firebase store
            RecyclerView.Adapter adapter1 = recyclerViewComponent.getAdapter();
            int itemCount = adapter1.getItemCount();
            System.out.println(itemCount);

            List<ExersixeOneRawHistory> exersixeOneRawHistories = new ArrayList<>();
            for (int i = 0; i < itemCount ; i++) {
                RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerViewComponent.findViewHolderForAdapterPosition(i);
                View view = recyclerView.getChildAt(i);

                EditText etRepit = (EditText) view.findViewById(R.id.ex_activity_recycler_repit);
                Integer repit = null;
                if (!etRepit.getText().toString().equals("")) {
                    repit = Integer.parseInt(etRepit.getText().toString());
                }

                EditText etKG = (EditText) view.findViewById(R.id.ex_activity_recycler_kg);
                Double kg = null;
                if (!etKG.getText().toString().equals("")) {
                    kg = Double.parseDouble(etKG.getText().toString());
                }

                if (kg != null && repit != null) {
                    exersixeOneRawHistories.add(new ExersixeOneRawHistory((i + 1), repit, kg));
                    if (i == itemCount - 1){
                        dialogstatus = false;
                    }
                }

            }

            if (dialogstatus){
                showDialog();
                return;
            }


            System.out.println(exersixeOneRawHistories);

            ExerciseHistory exerciseHistory = new ExerciseHistory("17/02/2019", exersixeOneRawHistories);
            List<ExerciseHistory> exerciseHistoryList = new ArrayList<>();
            exerciseHistoryList.add(exerciseHistory);

            ExerciseHistoryToFIreBase exerciseHistoryToFIreBase = new ExerciseHistoryToFIreBase(exerciseHistoryList);

            System.out.println(exerciseHistory);
            Task<Void> saveOnDB = fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid())
                    .collection(tvExName.getText().toString()).document("12.12.12").set(exerciseHistoryToFIreBase);



            finish();

        });

        //timer
        btnTimerStart.setOnClickListener(btn->{
            if (mTimerRunning){
                stopTimer();
            }else {
                startTimer();
            }
        });
        btnTimerStop.setOnClickListener(btn->{
            stopTimer();
        });
        btnTimerClear.setOnClickListener(btn->{
            clearTimer();
        });
        btnTimerResume.setOnClickListener(btn->{
            startTimer();
            btnTimerClear.setVisibility(View.INVISIBLE);
            btnTimerResume.setVisibility(View.INVISIBLE);
        });
        updateTimerText();


    }



    private void startTimer(){
        timerCount = new CountDownTimer(mTimerLeft,1_000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerLeft = millisUntilFinished;
                updateTimerText();
                btnTimerStart.setVisibility(View.INVISIBLE);
                btnTimerStop.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                btnTimerStart.setVisibility(View.VISIBLE);
                btnTimerStop.setVisibility(View.INVISIBLE);
                clearTimer();
                sendTimerNotification();

            }
        }.start();

        mTimerRunning = true;
        btnTimerStart.setVisibility(View.INVISIBLE);
        btnTimerStop.setVisibility(View.VISIBLE);
    }
    private void stopTimer(){
        timerCount.cancel();
        mTimerRunning = false;
        btnTimerStop.setVisibility(View.INVISIBLE);
        btnTimerResume.setVisibility(View.VISIBLE);
        btnTimerClear.setVisibility(View.VISIBLE);



    }
    private void clearTimer(){
        mTimerLeft = timerTime;
        updateTimerText();
        btnTimerResume.setVisibility(View.INVISIBLE);
        btnTimerClear.setVisibility(View.INVISIBLE);
        btnTimerStart.setVisibility(View.VISIBLE);
    }
    private void updateTimerText(){
        int minute = (int) (mTimerLeft / 1000) / 60;
        int seconds = (int) (mTimerLeft / 1000) % 60;

        String timerLeftFprmat = String.format(Locale.getDefault(),"%02d:%02d", minute,seconds);
        tvTimer.setText(timerLeftFprmat);
    }
    public void  sendTimerNotification(){


        Drawable drawable = ivMainImage.getDrawable();
        Bitmap picNotificatio = ((BitmapDrawable)drawable).getBitmap();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_ex_timer)
                .setContentTitle(tvExName.getText())
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picNotificatio)
                        .bigLargeIcon(picNotificatio))
                .setContentText("המנוחה נגמרה, הגיע הזמן לחזור להתאמן!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(getResources().getColor(R.color.mainGreen))
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1,notification);

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
        //timer
        if (mTimerRunning) {
            timerCount.cancel();
        }
        timerTime = exercises.get(counterEx).getRest();
        mTimerLeft = timerTime;
        mTimerRunning = false;
        btnTimerStop.setVisibility(View.INVISIBLE);
        btnTimerStart.setVisibility(View.VISIBLE);
        updateTimerText();


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

    private void showDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.finish_ex_alert_dialog, null);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(v).create();

        alertDialog.show();
    }

}
