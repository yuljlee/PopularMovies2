package com.nanodegree.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class holds data for movie details
 */

public class Movie implements Parcelable {

    private String title;
    private String posterUrl;
    private String releaseDate;
    private String voteAverage;
    private String overview;

    private static final String MOIVE_POSTER_URL =
            "http://image.tmdb.org/t/p/w342";

    public Movie (String title, String posterUrl, String releaseDate
            , String voteAverage, String overview) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    private Movie (Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return MOIVE_POSTER_URL + posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(overview);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
