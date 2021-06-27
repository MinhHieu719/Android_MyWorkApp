package com.example.mywork.fragment_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mywork.R;
import com.example.mywork.fragment_main.fragment_mywork.ViewPagerMyworkAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMyWork#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMyWork extends Fragment {
    public ViewPager vpMywork;
    public TabLayout tlMywork;
    private View view;

    public ViewPagerMyworkAdapter viewPagerMyworkAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMyWork() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentToDoList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMyWork newInstance(String param1, String param2) {
        FragmentMyWork fragment = new FragmentMyWork();
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
        view = inflater.inflate(R.layout.fragment_bn_mywork, container, false);

        vpMywork = view.findViewById(R.id.vpMywork);
        tlMywork = view.findViewById(R.id.tlMywork);

        viewPagerMyworkAdapter = new ViewPagerMyworkAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpMywork.setAdapter(viewPagerMyworkAdapter);
        tlMywork.setupWithViewPager(vpMywork);

        tlMywork.getTabAt(0).setIcon(R.drawable.ic_baseline_list_alt_24);
        tlMywork.getTabAt(1).setIcon(R.drawable.ic_baseline_pie_chart_24);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}