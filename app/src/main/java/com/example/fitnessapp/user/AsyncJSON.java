package com.example.fitnessapp.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsyncJSON extends AsyncTask<String, Integer, User>{

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private MutableLiveData<User> integrationCodeLiveData = new MutableLiveData<>();

    public AsyncJSON(MutableLiveData<User> integrationCodeLiveData) {
        this.integrationCodeLiveData = integrationCodeLiveData;
    }

    @Override
    protected User doInBackground(String... strings) {

        List<Day> days = new ArrayList<>();
        List<DietProcessRaw> dietRawList = new ArrayList<>();

        try {
            URL url = new URL("http://appfitness.boust.me/wp-json/acf/v3/trainers?appConnection=" + strings[0]);
            System.out.println(url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null){
                sb.append(line);
            }

            String json = sb.toString();

            JSONArray rootJSONArray = new JSONArray(json);
            JSONObject rootJSONObject = rootJSONArray.getJSONObject(0);

            JSONObject acf = rootJSONObject.getJSONObject("acf");

            String name = acf.getString("name");
            String bDay = acf.getString("bDay");
            Date bDayDate = stringToDate(bDay,-1900);

            String height = acf.getString("height");
            double heightDouble = Double.parseDouble(height);

            String job = acf.getString("job");
            String phoneNumber = acf.getString("phoneNumber");
            String email = acf.getString("email");
            String goal = acf.getString("goal");
            String limitation = acf.getString("limitation");

            JSONObject training = acf.getJSONObject("training");
            String[] daysName = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};

            for (int i = 0; i < daysName.length ; i++) {
                JSONObject day = training.getJSONObject(daysName[i]);

                String numberOfExercies = day.getString("numberOfExercies");
                int numberOfExerciesInt = Integer.parseInt(numberOfExercies);
                System.out.println(daysName[i] + " " + numberOfExerciesInt);
                List<Exercise> exercises = new ArrayList<>();
                for (int j = 0; j < numberOfExerciesInt ; j++) {
                    String exNumber = "ex" + (j+1);
                    JSONObject ex = day.getJSONObject(exNumber);

                    String exNameJson = ex.getString("exName");
                    String[] exSplit = exNameJson.split("/");
                    String exJSON = "https://appfitness.boust.me/wp-json/acf/v3/exercises?name=" + exSplit[exSplit.length-1];

                    URL urlEx = new URL(exJSON);
                    HttpURLConnection conEx = (HttpURLConnection) urlEx.openConnection();

                    InputStream inputStreamEx = conEx.getInputStream();
                    BufferedReader readerEx = new BufferedReader(new InputStreamReader(inputStreamEx));

                    StringBuilder sbEx = new StringBuilder();
                    String lineEx = null;

                    while ((lineEx = readerEx.readLine()) != null){
                        sbEx.append(lineEx);
                    }

                    String jsonEx = sbEx.toString();
                    System.out.println(jsonEx);

                    JSONArray rootJSONArrayEx = new JSONArray(jsonEx);
                    JSONObject rootJSONObjectEx = rootJSONArrayEx.getJSONObject(0);

                    JSONObject acfEx = rootJSONObjectEx.getJSONObject("acf");

                    String exName = acfEx.getString("name");
                    String exNameFinal = exName.replace("-", " ");

                    String imageUrl = acfEx.getString("image");
                    String videoUrl = acfEx.getString("video");


                    String set = ex.getString("sets");
                    int setInt = Integer.parseInt(set);

                    String repitition = ex.getString("repitition");
                    String rest = ex.getString("rest");
                    long restLong = millisectTimer(rest);

                    String notes = ex.getString("הערות");

                    Exercise exercise = new Exercise(exNameFinal,setInt,repitition,restLong,notes,imageUrl,videoUrl);
                    exercises.add(exercise);

                }
                if (numberOfExerciesInt != 0) {
                    days.add(new Day(daysName[i], exercises));
                }

            }

            JSONObject process = acf.getJSONObject("prosess");
            JSONObject table = process.getJSONObject("table");
            JSONArray body = table.getJSONArray("body");

            for (int z = 0; z < body.length() ; z++) {
                JSONArray jsonArrayBody = body.getJSONArray(z);

                JSONObject dateProcessJson = jsonArrayBody.getJSONObject(0);
                String dateProcessString = dateProcessJson.getString("c");

                Date dateConvert = stringToDate(dateProcessString,100);

                JSONObject weightProcessJson = jsonArrayBody.getJSONObject(1);
                String weightProcessString = weightProcessJson.getString("c");
                float weightProcessDouble = Float.parseFloat(weightProcessString);

                JSONObject abdominalProcessJson = jsonArrayBody.getJSONObject(2);
                String abdominalProcessString = abdominalProcessJson.getString("c");
                float abdominalProcessDouble = Float.parseFloat(abdominalProcessString);

                JSONObject armProcessJson = jsonArrayBody.getJSONObject(3);
                String armProcessString = armProcessJson.getString("c");
                float armProcessDouble = Float.parseFloat(armProcessString);

                JSONObject bodyFatProcessJson = jsonArrayBody.getJSONObject(4);
                String bodyFatProcessString = bodyFatProcessJson.getString("c");
                float bodyProcessDouble = Float.parseFloat(bodyFatProcessString);

                DietProcessRaw dietProcessRaw = new DietProcessRaw(dateConvert,weightProcessDouble,abdominalProcessDouble,armProcessDouble,bodyProcessDouble, dateProcessString);
                dietRawList.add(dietProcessRaw);

            }

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();

            DietProcessTab dietProcessTab = new DietProcessTab(dietRawList);

            User user = new User(name,strings[0],bDayDate,heightDouble,job,phoneNumber,email,strings[1],goal,limitation,days, dietProcessTab);


            //Save on FireBaseStore
            Task<Void> documentReference2 = fStore.collection(KeysFirebaseStore.COLLECTION_USER).document(fAuth.getUid()).set(user);

            return user;

        }catch (IOException e){
            System.out.println("Error user JSON");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        System.out.println(user);
        integrationCodeLiveData.setValue(user);
    }

    private Date stringToDate(String string, int year){
        String[] dateSplit = string.split("/");
        int dateDay = Integer.parseInt(dateSplit[0]);
        int dateMonth = Integer.parseInt(dateSplit[1])-1;
        int dateYear = Integer.parseInt(dateSplit[2])+year;

        Date dateConvert = new Date(dateYear,dateMonth,dateDay);

        return dateConvert;
    }

    private long millisectTimer(String string){
        String[] split = string.split(":");
        String min = split[0];
        String sec = split[1];

        long minLong = Long.parseLong(min) * 60 * 1000;
        long secLong = Long.parseLong(sec) * 1000;

        long total = minLong + secLong;

        return total;

    }

}
