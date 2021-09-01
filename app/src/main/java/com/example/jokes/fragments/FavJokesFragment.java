package com.example.jokes.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jokes.R;
import com.example.jokes.controller.FavJokesListAdapter;
import com.example.jokes.model.Joke;
import com.example.jokes.model.JokeManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FavJokesFragment extends Fragment {

    RecyclerView mRecyclerView;
    FavJokesListAdapter mFavJokesListAdapter;
    JokeManager mJokeManager;
    private List<Joke> mJokeList = new ArrayList<>();

    private Joke deletedJoke;


    public FavJokesFragment() {
        // Required empty public constructor
    }


    public static FavJokesFragment newInstance() {
        FavJokesFragment fragment = new FavJokesFragment();

        return fragment;
    }

    @Override
    public void onAttach(@NonNull  Context context) {
        super.onAttach(context);

        mJokeManager = new JokeManager(context);
        mJokeList.clear();
        if(mJokeManager.retrieveJokes().size()>0){
            for(Joke joke : mJokeManager.retrieveJokes()){
                mJokeList.add(joke);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_jokes, container, false);
        if (view != null){
            mRecyclerView = view.findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mFavJokesListAdapter = new FavJokesListAdapter(mJokeList,getContext());
            mRecyclerView.setAdapter(mFavJokesListAdapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallBack);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }

        return view;

    }

  ItemTouchHelper.SimpleCallback mSimpleCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
          return false;
      }

      @Override
      public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
          final int position = viewHolder.getAdapterPosition();
          switch (direction){
              case ItemTouchHelper.LEFT:
              case ItemTouchHelper.RIGHT:

                  deletedJoke = mJokeList.get(position);
                  mJokeManager.deleteJoke(deletedJoke);
                  mJokeList.remove(position);
                  mFavJokesListAdapter.notifyItemRemoved(position);
                  mFavJokesListAdapter.notifyDataSetChanged();

                  Snackbar.make(mRecyclerView,"Joke is removed",Snackbar.LENGTH_LONG)
                          .setAction("Undo", new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  mJokeList.add(position,deletedJoke);
                                  mJokeManager.saveJoke(deletedJoke);
                                  mFavJokesListAdapter.notifyItemInserted(position);
                              }
                          }).show();

                  break;

          }

      }
  };

}