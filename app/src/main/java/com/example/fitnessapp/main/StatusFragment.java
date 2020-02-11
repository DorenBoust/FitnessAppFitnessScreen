package com.example.fitnessapp.main;

import androidx.annotation.ColorRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysUserFragment;
import com.example.fitnessapp.user.DietProcessRaw;
import com.example.fitnessapp.user.DietProcessTab;
import com.example.fitnessapp.user.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusFragment extends Fragment implements Serializable {

    private StatusViewModel mViewModel;
    private User user;
    private List<Date> chartDate = new ArrayList<>();
    private List<String> chartDateString = new ArrayList<>();
    private List<Float> chartWeight = new ArrayList<>();
    private List<Float> chartAbdominal = new ArrayList<>();
    private List<Float> chartArm = new ArrayList<>();
    private List<Float> chartBodyFat = new ArrayList<>();

    private TextView tvStartWeight;
    private TextView tvStartArm;
    private TextView tvStartBodyFat;
    private TextView tvStartAbdominal;
    private TextView tvStartDate;
    private TextView tvCurrectGoal;

    private LineChart graph;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_fragment, container, false);


        user = (User) getArguments().getSerializable(KeysUserFragment.USER_DATA_TO_FRAGMENT);
        extractChart(user);

        tvStartWeight = v.findViewById(R.id.statusFragment_tv__startData_weight);
        tvStartArm = v.findViewById(R.id.statusFragment_tv__startData_arm);
        tvStartBodyFat = v.findViewById(R.id.statusFragment_tv_Abdominal);
        tvStartAbdominal = v.findViewById(R.id.statusFragment_tv_bodyFat);
        tvStartDate = v.findViewById(R.id.statusFragment_tv_startData2);
        tvCurrectGoal = v.findViewById(R.id.statusFragment_tv_goal);

        recentData();

        graph = v.findViewById(R.id.statusFragment_graph);

        graph.setDragEnabled(true);
        graph.setScaleEnabled(false);
        dataGraph(chartWeight, tvStartWeight);


        tvStartWeight.setOnClickListener(textview->{
            dataGraph(chartWeight, tvStartWeight);
        });

        tvStartArm.setOnClickListener(textview->{
            dataGraph(chartArm, tvStartArm);
        });

        tvStartBodyFat.setOnClickListener(textview->{
            dataGraph(chartBodyFat, tvStartBodyFat);
        });

        tvStartAbdominal.setOnClickListener(textview->{
            dataGraph(chartAbdominal, tvStartAbdominal);
        });




        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StatusViewModel.class);
    }

    private void extractChart(User user){
        DietProcessTab dietTable = user.getDietTable();
        List<DietProcessRaw> processTable = dietTable.getProcessTable();
        for (DietProcessRaw dietProcessRaw : processTable) {
            chartWeight.add(dietProcessRaw.getWeight());
            chartAbdominal.add(dietProcessRaw.getAbdominal());
            chartArm.add(dietProcessRaw.getArm());
            chartBodyFat.add(dietProcessRaw.getBodyFat());
            chartDate.add(dietProcessRaw.getDate());
            chartDateString.add(dietProcessRaw.getDateString());
        }

    }

    private void dataGraph(List<Float> mainDataList, TextView textView){

        List<TextView> textViewsList = new ArrayList<>();
        textViewsList.add(tvStartWeight);
        textViewsList.add(tvStartArm);
        textViewsList.add(tvStartBodyFat);
        textViewsList.add(tvStartAbdominal);

        for (int i = 0; i < textViewsList.size() ; i++) {
            if (textView.equals(textViewsList.get(i))){
                textViewsList.get(i).setTextColor(getResources().getColor(R.color.mainRed));
            } else {
                textViewsList.get(i).setTextColor(getResources().getColor(R.color.mainGreen));
            }
        }
        ArrayList<Entry> dataGraph = new ArrayList<>();

        if (mainDataList.size() > 5) {
            for (int i = 0; i < 5; i++) {
                dataGraph.add(new Entry(i, mainDataList.get((mainDataList.size() - 5) + i)));
            }

        } else {
            for (int i = 0; i < mainDataList.size(); i++) {
                dataGraph.add(new Entry(i, mainDataList.get(i)));
            }
        }



        LineDataSet set1 = new LineDataSet(dataGraph, "");
        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        set1.setColor(getResources().getColor(R.color.waite));
        set1.setDrawFilled(true);
        set1.setFillColor(getResources().getColor(R.color.lightGreen));
        set1.setDrawValues(false);



        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);

        graph.setData(data);


        //Background
        graph.setBackgroundColor(getResources().getColor(R.color.mainGreen));
        //marker icon
        MarkerImage markerImage = new MarkerImage(getContext(),R.drawable.graph_mark_image);
        markerImage.setOffset(-18f,-18f);
        graph.setMarker(markerImage);




        //hide X and right Y
        graph.getXAxis().setEnabled(false);
        graph.getAxisRight().setEnabled(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.getXAxis().setDrawGridLines(false);


        //hide description and map
        graph.getLegend().setEnabled(false);
        graph.getDescription().setEnabled(false);

        //grid color
        graph.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.lightGreen));
        graph.getAxisLeft().setTextColor(getResources().getColor(R.color.lightGreen));
        graph.setGridBackgroundColor(getResources().getColor(R.color.lightGreen));



        graph.invalidate();
    }

    private void recentData(){
        tvStartWeight.setText(chartWeight.get(chartWeight.size()-1).toString());
        tvStartArm.setText(chartArm.get(chartArm.size()-1).toString());
        tvStartBodyFat.setText(chartBodyFat.get(chartBodyFat.size()-1).toString());
        tvStartAbdominal.setText(chartAbdominal.get(chartAbdominal.size()-1).toString());
        tvStartDate.setText(chartDateString.get(chartDate.size()-1));
        tvCurrectGoal.setText(user.getGoal());
    }
}
