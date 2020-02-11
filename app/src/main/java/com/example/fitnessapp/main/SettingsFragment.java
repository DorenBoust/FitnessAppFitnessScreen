package com.example.fitnessapp.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fitnessapp.LogActivity;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysUserFragment;
import com.example.fitnessapp.user.User;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private User user;
    private Button btnLogout;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);

        user = (User) getArguments().getSerializable(KeysUserFragment.USER_DATA_TO_FRAGMENT);
        System.out.println("Setting" + user);

        btnLogout = v.findViewById(R.id.settingFregment_btn_logout);

        btnLogout.setOnClickListener(btm->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LogActivity.class));
            getActivity().onBackPressed();
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}
