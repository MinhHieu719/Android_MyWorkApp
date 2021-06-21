package com.example.mywork.fragment_main.fragment_mywork;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.mywork.Database;
import com.example.mywork.Model.Work;
import com.example.mywork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMwStatistic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMwStatistic extends Fragment {
    String[] status = {"Finished", "Unfinished"};
    int[] amount = {0, 0};
    AnyChartView anyChartView;

    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMwStatistic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMwStatistic.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMwStatistic newInstance(String param1, String param2) {
        FragmentMwStatistic fragment = new FragmentMwStatistic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_mw_statistic, container, false);

        anyChartView = view.findViewById(R.id.anyChartView);

        setUpPieChart();

        return view;
    }

    public void setUpPieChart() {
        Database database = new Database(getActivity(), "work.DB", null, 1);
        Cursor dataWork = database.getdata("SELECT * FROM work");
        while(dataWork.moveToNext()) {
            if(dataWork.getInt(1) == 1) {
                amount[0]++;
            } else {
                amount[1]++;
            }
        }

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i = 0; i < status.length; i++) {
            dataEntries.add(new ValueDataEntry(status[i], amount[i]));

        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    @Override
    public void onResume() {
        setUpPieChart();
        super.onResume();
    }
}