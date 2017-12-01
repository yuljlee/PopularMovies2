package com.nanodegree.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by u2stay1915 on 8/11/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    public interface TrailerAdapterOnClickHandler {

        void onClick(String key);
    }

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToRoot = false;

        View view = inflater.inflate(layoutIdForListItem, parent, attachToRoot);

        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.mAuthorTextView.setText(mCursor.getString(2));
        holder.mContentTextView.setText(mCursor.getString(3));

        Log.v(TAG, "content -> " + mCursor.getString(3));
        Log.v(TAG, "AuthorText" + mCursor.getString(2));
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mAuthorTextView;
        public final TextView mContentTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_review_author);
            mContentTextView = (TextView) view.findViewById(R.id.tv_review_content);

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            mCursor.moveToPosition(adapterPosition);
            String key = mCursor.getString(1);

        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
