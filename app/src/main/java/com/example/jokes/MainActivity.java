package com.example.jokes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.arasthel.asyncjob.AsyncJob;
import com.example.jokes.controller.CardsDataAdapter;
import com.example.jokes.controller.JokeLikeListener;
import com.example.jokes.model.Joke;
import com.example.jokes.model.JokeManager;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JokeLikeListener {

    CardStack mCardStack;
    CardsDataAdapter mCardAdapter;
    List<Joke> allJokes = new ArrayList<>();
    JokeManager mJokeManager;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJokeManager = new JokeManager(this);

        mCardStack = findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.jokes_card);
        mCardStack.setStackMargin(20);

        mSensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });

        mCardAdapter = new CardsDataAdapter(MainActivity.this,0);

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        try {

                            JSONObject rootObject = new JSONObject(loadJSONFromAsset());

                            JSONArray fatJokes = rootObject.getJSONArray("fat");
                            addJsonArrayToArrayList(fatJokes,allJokes);

                            JSONArray stupidJokes = rootObject.getJSONArray("stupid");
                            addJsonArrayToArrayList(fatJokes,allJokes);

                            JSONArray uglyJokes = rootObject.getJSONArray("ugly");
                            addJsonArrayToArrayList(fatJokes,allJokes);

                            JSONArray nastyJokes = rootObject.getJSONArray("nasty");
                            addJsonArrayToArrayList(fatJokes,allJokes);

                            JSONArray odorJokes = rootObject.getJSONArray("odor");
                            addJsonArrayToArrayList(fatJokes,allJokes);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {

                        for (Joke joke : allJokes){

                            mCardAdapter.add(joke.getJokeText());

                        }

                        mCardStack.setAdapter(mCardAdapter);

                    }
                }).create().start();






    }

    private void handleShakeEvent() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        Collections.shuffle(allJokes);
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        mCardAdapter.clear();
                        mCardAdapter =new CardsDataAdapter(MainActivity.this,0);
                        for (Joke joke : allJokes){

                            mCardAdapter.add(joke.getJokeText());

                        }

                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("jokes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void addJsonArrayToArrayList(JSONArray jsonArray, List<Joke> arrayList){
        try {

            if (jsonArray != null){
                for (int i=0; i <jsonArray.length();i++){
                    arrayList.add(new Joke(jsonArray.getString(i), false));
                }
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }


    @Override
    public void jokeIsLiked(Joke joke) {
        if(joke.isJokeLiked()){
            mJokeManager.saveJoke(joke);
        }
        else {
            mJokeManager.deleteJoke(joke);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(MainActivity.this, FavJokesActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,SensorManager.SENSOR_DELAY_UI );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }
}