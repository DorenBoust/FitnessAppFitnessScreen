package com.example.fitnessapp.models;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.fitnessapp.loginAndRegisterAnimated.RegisterAniFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsynUserJSON extends AsyncTask<String, Integer, String> {

    MutableLiveData<String> mLiveData;


    public AsynUserJSON(MutableLiveData<String> mLiveData) {
        this.mLiveData = mLiveData;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String finalUrl = "http://appfitness.boust.me/wp-json/acf/v3/trainers?appConnection=" + strings[0];
            URL url = new URL(finalUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            return sb.toString();


        }catch (IOException e){

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        mLiveData.setValue(s);
    }
}
