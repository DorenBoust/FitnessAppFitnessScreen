package com.example.fitnessapp.intro;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fitnessapp.R;

public class SecIntroFragment extends Fragment {

    private SecIntroViewModel mViewModel;
    private LottieAnimationView lottieAnimationView;
    private TextView textView;

    public static SecIntroFragment newInstance() {
        return new SecIntroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sec_intro_fragment, container, false);

        lottieAnimationView = v.findViewById(R.id.lottieAnimationView_sec);
        textView = v.findViewById(R.id.tv_intro_block2);
        lottieAnimationView.playAnimation();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SecIntroViewModel.class);
        // TODO: Use the ViewModel
    }

}
