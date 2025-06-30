package com.example.grecords;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class main_fragment extends Fragment {

    Button view_member_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        view_member_btn = view.findViewById(R.id.main_frag_fab);
        view_member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new view_member())
                        .addToBackStack(null)
                        .commit();
            }
        });














        // Inflate the layout for this fragment
        return view;
    }
}