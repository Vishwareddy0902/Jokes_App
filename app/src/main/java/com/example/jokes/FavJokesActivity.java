package com.example.jokes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.jokes.fragments.FavJokesFragment;

public class FavJokesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_jokes);

        FavJokesFragment favJokesFragment = FavJokesFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fav_jokes_container ,favJokesFragment).commit();

    }
}