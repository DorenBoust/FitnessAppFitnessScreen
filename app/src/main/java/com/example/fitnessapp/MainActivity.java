package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysUserFragment;
import com.example.fitnessapp.main.ArticlesFragment;
import com.example.fitnessapp.main.DietFragment;
import com.example.fitnessapp.main.FitnessFragment;
import com.example.fitnessapp.main.SettingsFragment;
import com.example.fitnessapp.main.StatusFragment;
import com.example.fitnessapp.main.StatusViewModel;
import com.example.fitnessapp.user.AsyncJSON;
import com.example.fitnessapp.user.DietProcessRaw;
import com.example.fitnessapp.user.DietProcessTab;
import com.example.fitnessapp.user.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MutableLiveData<User> userJsonLiveData = new MutableLiveData<>();
    private User userObject;
    private ConstraintLayout splash;
    private ConstraintLayout menuBar;


    private int lastClicked = 1;
    private boolean iconSwitch = false;

    private AnimatedVectorDrawableCompat avd;
    private AnimatedVectorDrawable avd2;

    private ImageView iconStatus;
    private ImageView iconLineStatus;
    private boolean statusClicked = false;

    private ImageView iconFitness;
    private ImageView iconLineFitness;
    private boolean fitnessClicked = false;

    private ImageView iconDiet;
    private ImageView iconLineDiet;
    private boolean dietClicked = false;

    private ImageView iconArticles;
    private ImageView iconLineArticles;
    private boolean articlesClicked = false;

    private ImageView iconSetting;
    private ImageView iconLineSetting;
    private boolean settingClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonParser();

        splash = findViewById(R.id.splash);
        menuBar = findViewById(R.id.menuBar);

        iconStatus = findViewById(R.id.menu_icon_status);
        iconLineStatus = findViewById(R.id.iconline_status);
        iconFitness = findViewById(R.id.menu_icon_fitness);
        iconLineFitness = findViewById(R.id.iconline_fitness);
        iconStatus.setImageDrawable(getResources().getDrawable(R.drawable.status_animation));
        iconDiet = findViewById(R.id.menu_icon_diet);
        iconLineDiet = findViewById(R.id.iconline_diet);
        iconArticles = findViewById(R.id.menu_icon_articals);
        iconLineArticles = findViewById(R.id.iconline_articals);
        iconSetting = findViewById(R.id.menu_icon_settings);
        iconLineSetting = findViewById(R.id.iconline_setting);


        userJsonLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userObject = user;
                Bundle bundle = new Bundle();
                bundle.putSerializable(KeysUserFragment.USER_DATA_TO_FRAGMENT, userObject);
                StatusFragment statusFragment = new StatusFragment();
                statusFragment.setArguments(bundle);


                splash.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.faidout));
                splash.setVisibility(View.INVISIBLE);
                menuBar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.faidin));
                menuBar.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.faidin,R.anim.faidout).replace(R.id.mainFragment, statusFragment).commit();

            }
        });






        iconStatus.setOnClickListener(v->{
            if (!statusClicked){
                mainIconAnimation(iconStatus,iconLineStatus,iconSwitch,R.drawable.status_animation,R.drawable.status_stati);

                lastClicked = 1;
                statusClicked = true;
                fitnessClicked = false;
                dietClicked = false;
                settingClicked = false;
                articlesClicked = false;


            }

        });


        iconFitness.setOnClickListener(v->{
            if (!fitnessClicked) {
                mainIconAnimation(iconFitness,iconLineFitness, iconSwitch, R.drawable.fitness_animation, R.drawable.fitness_stati);


                lastClicked = 2;
                fitnessClicked = true;
                statusClicked = false;
                dietClicked = false;
                settingClicked = false;
                articlesClicked = false;


            }
        });

        iconDiet.setOnClickListener(v->{
            if (!dietClicked) {
                mainIconAnimation(iconDiet,iconLineDiet,iconSwitch, R.drawable.diet_animation, R.drawable.diet_stati);

                lastClicked = 3;
                dietClicked = true;
                statusClicked = false;
                fitnessClicked = false;
                settingClicked = false;
                articlesClicked = false;

            }
        });

        iconArticles.setOnClickListener(v->{
            if ((!articlesClicked)){
                mainIconAnimation(iconArticles,iconLineArticles,iconSwitch,R.drawable.articels_animation,R.drawable.articels_stati);

                lastClicked = 4;
                articlesClicked = true;
                statusClicked = false;
                dietClicked = false;
                fitnessClicked = false;
                settingClicked = false;


            }
        });

        iconSetting.setOnClickListener(v->{
            if (!settingClicked) {
                mainIconAnimation(iconSetting, iconLineSetting,iconSwitch, R.drawable.menu_animation, R.drawable.menu_static);

                lastClicked = 5;
                settingClicked = true;
                dietClicked = false;
                statusClicked = false;
                fitnessClicked = false;
                articlesClicked = false;

            }
        });



    }

    private void jsonParser(){

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        String userID = fAuth.getUid();
        DocumentReference documentReference = fStore.collection(KeysFirebaseStore.COLLECTION_USER).document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String integrationCode = documentSnapshot.getString(KeysFirebaseStore.INTEGRATION_CODE);
                    String email = documentSnapshot.getString(KeysFirebaseStore.EMAIL);
                    new AsyncJSON(userJsonLiveData).execute(integrationCode, email);
                }else {
                    Toast.makeText(MainActivity.this, "Doc not exesite", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void mainIconAnimation(ImageView icon,ImageView iconLine, boolean iconSwitch, int drawableResFirstAnimation, int drawableResSecAnimation){



        List<Drawable> statiImages = new ArrayList<>();
        statiImages.add(getResources().getDrawable(R.drawable.status_stati));
        statiImages.add(getResources().getDrawable(R.drawable.fitness_stati));
        statiImages.add(getResources().getDrawable(R.drawable.diet_stati));
        statiImages.add(getResources().getDrawable(R.drawable.articels_stati));
        statiImages.add(getResources().getDrawable(R.drawable.menu_static));

        List<ImageView> iconsNames = new ArrayList<>();
        iconsNames.add(iconStatus);
        iconsNames.add(iconFitness);
        iconsNames.add(iconDiet);
        iconsNames.add(iconArticles);
        iconsNames.add(iconSetting);

        List<ImageView> iconLines = new ArrayList<>();
        iconLines.add(iconLineStatus);
        iconLines.add(iconLineFitness);
        iconLines.add(iconLineDiet);
        iconLines.add(iconLineArticles);
        iconLines.add(iconLineSetting);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new StatusFragment());
        fragments.add(new FitnessFragment());
        fragments.add(new DietFragment());
        fragments.add(new ArticlesFragment());
        fragments.add(new SettingsFragment());




        //set icon images static
        for (int i = 0; i < statiImages.size() ; i++) {
            if (icon != iconsNames.get(i))
            iconsNames.get(i).setImageDrawable(statiImages.get(i));
        }


        //Animation icon Lines
        for (int i = 0; i < iconLines.size() ; i++) {
            if (iconLine != iconLines.get(i)){
                faidoutIconLine(iconLines.get(i));
            }else {
                faidinIconLine(iconLines.get(i));

                Bundle bundle = new Bundle();
                bundle.putSerializable(KeysUserFragment.USER_DATA_TO_FRAGMENT, userObject);
                Fragment fragment = fragments.get(i);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.faidin,R.anim.faidout,R.anim.faidin,R.anim.faidout).replace(R.id.mainFragment, fragment).commitNow();

            }
        }


        //do the animation
        if(!iconSwitch){
            icon.setImageDrawable(getResources().getDrawable(drawableResFirstAnimation));
            Drawable drawable = icon.getDrawable();

            if(drawable instanceof AnimatedVectorDrawableCompat){
                avd = (AnimatedVectorDrawableCompat) drawable;
                avd.start();
            }else if (drawable instanceof AnimatedVectorDrawable){
                avd2 = (AnimatedVectorDrawable) drawable;
                avd2.start();
            }

            iconSwitch = true;

        }else {
            icon.setImageDrawable(getResources().getDrawable(drawableResSecAnimation));
            Drawable drawable = icon.getDrawable();

            if(drawable instanceof AnimatedVectorDrawableCompat){
                avd = (AnimatedVectorDrawableCompat) drawable;
                avd.start();
            }else if (drawable instanceof AnimatedVectorDrawable){
                avd2 = (AnimatedVectorDrawable) drawable;
                avd2.start();
            }

            iconSwitch = false;

        }
    }

    private void faidinIconLine(ImageView iconLine){
        iconLine.setVisibility(View.VISIBLE);
        iconLine.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidin));

    }

    private void faidoutIconLine(ImageView iconLine){

        switch (lastClicked) {
            case 1:
                iconLineStatus.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 2:
                iconLineFitness.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 3:
                iconLineDiet.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 4:
                iconLineArticles.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 5:
                iconLineSetting.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
        }
        iconLine.setVisibility(View.INVISIBLE);
    }


}
