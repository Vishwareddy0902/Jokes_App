package com.example.jokes.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jokes.R;
import com.example.jokes.model.Joke;

public class CardsDataAdapter extends ArrayAdapter<String> {

    private Boolean clicked = true ;
    private JokeLikeListener mJokeLikeListener;
    private Joke mJoke;
    SharedPreferences mSharedPreferences;

    public CardsDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mJokeLikeListener =(JokeLikeListener) context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        TextView v = (contentView.findViewById(R.id.content));
        v.setText(getItem(position));


        ImageButton likeButton = contentView.findViewById(R.id.likeButton);

        if (mSharedPreferences.contains(getItem(position))){
            likeButton.setImageResource(R.drawable.like_filled);
            clicked = false;
        }
        else {
            clicked = true;
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clicked){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(likeButton);
                    likeButton.setImageResource(R.drawable.like_filled);
                    clicked = false;
                    mJoke = new Joke(getItem(position), true);
                }
                else{
                    likeButton.setImageResource(R.drawable.like_empty);
                    clicked = true;
                    mJoke = new Joke(getItem(position), false);
                }
                mJokeLikeListener.jokeIsLiked(mJoke);

            }
        });

        ImageButton shareButton = contentView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareBody = v.getText().toString();
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Joke");
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);

                v.getContext().startActivity(Intent.createChooser(intent ,"Share via"));

            }
        });

        return contentView;

    }

}

