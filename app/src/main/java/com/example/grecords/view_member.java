package com.example.grecords;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class view_member extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.view_member, container, false);


        ExtendedFloatingActionButton addbtn = view.findViewById(R.id.add_member);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);

                TextInputEditText nameInput = dialogView.findViewById(R.id.edit_name);
                TextInputEditText startDateInput = dialogView.findViewById(R.id.edit_start_date);
                TextInputEditText endDateInput = dialogView.findViewById(R.id.edit_end_date);

                startDateInput.setOnClickListener(s -> showDatePicker(startDateInput));
                endDateInput.setOnClickListener(s -> showDatePicker(endDateInput));

                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Add Member")
                        .setView(dialogView)
                        .setPositiveButton("Add", (dialog, which) -> {
                            String name = nameInput.getText().toString().trim();
                            String start = startDateInput.getText().toString().trim();
                            String end = endDateInput.getText().toString().trim();

                            // Handle the values
                            Toast.makeText(getContext(), "Added: " + name+start+end, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
            private void showDatePicker(TextInputEditText target) {
                Calendar calendar = Calendar.getInstance();
                Context context = new ContextThemeWrapper(getContext(), R.style.Base_Theme_GRecords);
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        (view, year, month, day) -> {
                            String selectedDate = (month + 1) + "/" + day + "/" + year;
                            target.setText(selectedDate);
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }

        });

        ExtendedFloatingActionButton actMbtn = view.findViewById(R.id.active_fab);
        ExtendedFloatingActionButton ovrdMbtn = view.findViewById(R.id.overdue_fab);

        actMbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new active_member())
                        .addToBackStack(null)
                        .commit();
            }
        });
        ovrdMbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new overdue_member())
                        .addToBackStack(null)
                        .commit();
            }
        });











        return view;
    }
}