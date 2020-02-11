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

public class FourthIntroFragment extends Fragment {

    private FourthIntroViewModel mViewModel;
    private LottieAnimationView lottieAnimationView;
    private TextView textView;

    public static FourthIntroFragment newInstance() {
        return new FourthIntroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fourth_intro_fragment, container, false);

        lottieAnimationView = v.findViewById(R.id.lottieAnimationView_fourth);
        textView = v.findViewById(R.id.tv_intro_block4);
        lottieAnimationView.playAnimation();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FourthIntroViewModel.class);
        // TODO: Use the ViewModel
    }

}
