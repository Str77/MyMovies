package com.example.mymovies.data;

public class Trailer {

    private String youtubeURLTrailer;
    private String name;

    public Trailer(String key, String name) {
        this.youtubeURLTrailer = key;
        this.name = name;
    }

    public String getYoutubeURLTrailer() {
        return youtubeURLTrailer;
    }

    public String getName() {
        return name;
    }
}
