package com.example.grecords;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

public class MainActivity extends AppCompatActivity {



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        Fragment fragment = new main_fragment();
        FragmentManager fm = getSupportFragmentManager();

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Handle nav_profile
                    return true;
                }
                return false;

            }
        });

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();


        SearchBar searchBar = findViewById(R.id.search_bar);
        SearchView searchView = findViewById(R.id.search_view);

        searchBar.setOnClickListener(v -> searchView.show());

        searchView.addTransitionListener((view1, oldState, newState) -> {
            if (newState == SearchView.TransitionState.SHOWING) {
                // Optional: preload anything when the view shows
            } else if (newState == SearchView.TransitionState.SHOWN) {
                // Now we can safely start polling or watching text
                searchView.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // 👇 Set the text in the SearchBar to match input
                        searchBar.setText(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

            }
        });





































        bottomNavigation.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Handle nav_profile reselection
                }
            }
        });




    }
}