package com.example.fitnessapp.loginAndRegisterAnimated;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginAniFragment extends Fragment {

    private LoginAniViewModel mViewModel;
    private TextView tvToRegister;
    private Button btnLogin;
    private TextInputLayout etUser;
    private TextInputLayout etPass;
    private FirebaseAuth fAuth;
    private LottieAnimationView loginAnimation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_ani_fragment, container, false);

        tvToRegister = v.findViewById(R.id.tv_register_click_to_login);
        btnLogin = v.findViewById(R.id.btn_login_enter);
        etUser = v.findViewById(R.id.username_login);
        etPass = v.findViewById(R.id.password_login);
        fAuth = FirebaseAuth.getInstance();
        loginAnimation = v.findViewById(R.id.login_animation);

        btnLogin.setOnClickListener(btn->{


            String user = etUser.getEditText().getText().toString();
            String pass = etPass.getEditText().getText().toString();

            validationField();

            fAuthChecking(etUser,etPass);

        });


        tvToRegister.setOnClickListener(view->{
            getFragmentManager().beginTransaction().
                    setCustomAnimations(R.anim.enter_top_to_bottom,R.anim.exite_bottom_to_top,R.anim.enter_bottom_to_top,R.anim.exite_top_to_bottom).
                    replace(R.id.mFragment, new RegisterAniFragment()).commit();

        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginAniViewModel.class);
        // TODO: Use the ViewModel
    }

    private void validationField(){

        String user = etUser.getEditText().getText().toString();
        String pass = etPass.getEditText().getText().toString();

        if (TextUtils.isEmpty(user)){
            etUser.setError("שדה חובה");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            etUser.setError("נא להזין כתובת מייל תיקנית");
        } else {
            etUser.setError(null);
        }

        if (TextUtils.isEmpty(pass)){
            etPass.setError("שדה חובה");
        } else {
            etPass.setError(null);
        }


    }

    private void fAuthChecking(TextInputLayout etUser, TextInputLayout etPass){

        String user = etUser.getEditText().getText().toString();
        String pass = etPass.getEditText().getText().toString();

        if (Objects.equals(etUser.getError(), null) && Objects.equals(etPass.getError(), null)) {
            btnLogin.setVisibility(View.INVISIBLE);
            loginAnimation.setVisibility(View.VISIBLE);
            loginAnimation.playAnimation();
            fAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);

                        getActivity().onBackPressed();


                    } else {
                        etPass.setError("שם המשתמש או הסיסמא שגויים");
                        etUser.setError("שם המשתמש או הסיסמא שגויים");
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.faidin);
                        btnLogin.setAnimation(animation);
                        btnLogin.setVisibility(View.VISIBLE);
                        loginAnimation.setVisibility(View.INVISIBLE);

                    }
                }
            });


        }
    }


}
