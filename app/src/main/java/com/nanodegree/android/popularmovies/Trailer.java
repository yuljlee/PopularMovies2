package com.nanodegree.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class holds data for movie details
 */

public class Trailer implements Parcelable {

    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    private static final String MOIVE_POSTER_URL =
            "http://image.tmdb.org/t/p/w342";

    public Trailer (String key, String name, String site
            , String size, String type) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    private Trailer (Parcel in) {
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return MOIVE_POSTER_URL + name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
