package com.nanodegree.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class holds data for review data
 */

public class Review implements Parcelable {

    private String reviewId;
    private String author;
    private String content;
    private String review_url;

    private static final String MOIVE_POSTER_URL =
            "http://image.tmdb.org/t/p/w342";

    public Review (String reviewId, String author, String content
            , String review_url) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.review_url = review_url;
    }

    private Review (Parcel in) {
        reviewId = in.readString();
        author = in.readString();
        content = in.readString();
        review_url = in.readString();
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return review_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(review_url);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
