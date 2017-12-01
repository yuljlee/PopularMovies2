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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private final Context mContext;
    private final TrailerAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public interface TrailerAdapterOnClickHandler {

        void onClick(String key);
    }

    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToRoot = false;

        View view = inflater.inflate(layoutIdForListItem, parent, attachToRoot);

        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        //String posterUrl = NetworkUtils.MOIVE_POSTER_URL + mCursor.getString(3);
        holder.mTrailerTextView.setText(mCursor.getString(2));

        Log.v(TAG, "TrailerText -> " + mCursor.getString(2));

    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTrailerTextView;
        public final ImageView mPlayImage;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTextView = (TextView) view.findViewById(R.id.tv_trailer_title);
            mPlayImage = (ImageView) view.findViewById((R.id.img_play));

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String key = mCursor.getString(1);
            mClickHandler.onClick(key);
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
