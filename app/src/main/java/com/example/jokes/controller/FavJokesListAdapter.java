package com.example.jokes.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jokes.R;
import com.example.jokes.model.Joke;
import com.example.jokes.view.FavJokeViewHolder;

import java.util.List;

public class FavJokesListAdapter extends RecyclerView.Adapter<FavJokeViewHolder> {

    private List<Joke> mJokeList;
    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public FavJokesListAdapter(List<Joke> jokeList, Context context) {
        mJokeList = jokeList;
        mContext = context;
    }

    @Override
    public FavJokeViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_jokes_list,parent,false);
        return new FavJokeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull  FavJokeViewHolder holder, int position) {

        String jokeText = mJokeList.get(position).getJokeText();
        holder.getTxtFavJoke().setText(jokeText);


        holder.getShareButtonFavListItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareBody = jokeText;
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Joke");
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);

                mContext.startActivity(Intent.createChooser(intent ,"Share via"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return mJokeList.size();
    }
}
