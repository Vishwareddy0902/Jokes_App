package com.example.jokes.model;

public class Joke {

    private String jokeText;
    private Boolean jokeIsLiked;

    public Joke(String jokeText, Boolean jokeIsLiked) {
        this.jokeText = jokeText;
        this.jokeIsLiked = jokeIsLiked;
    }

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }

    public Boolean isJokeLiked() {
        return jokeIsLiked;
    }

    public void setJokeLiked(Boolean jokeIsLiked) {
        this.jokeIsLiked = jokeIsLiked;
    }
}
