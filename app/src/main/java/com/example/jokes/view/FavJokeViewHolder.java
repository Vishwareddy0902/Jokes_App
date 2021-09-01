package com.example.jokes.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jokes.R;

public class FavJokeViewHolder extends RecyclerView.ViewHolder {

    private TextView  txtFavJoke;
    private ImageButton shareButtonFavListItem;


    public FavJokeViewHolder(@NonNull  View itemView) {
        super(itemView);
        txtFavJoke = itemView.findViewById(R.id.txtFavJoke);
        shareButtonFavListItem = itemView.findViewById(R.id.shareButtonFavListItem);
    }

    public TextView getTxtFavJoke() {
        return txtFavJoke;
    }

    public ImageButton getShareButtonFavListItem() {
        return shareButtonFavListItem;
    }
}
