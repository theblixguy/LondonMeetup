package com.suyashsrijan.londonmeetup.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suyashsrijan.londonmeetup.R;

public class BikeInfoBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private String mBikePointName;
    private int mBikesAvailable;
    private int mBikesTaken;

    public static BikeInfoBottomSheetDialogFragment newInstance(String bikePointName, int bikesAvailable, int bikesTaken) {
        BikeInfoBottomSheetDialogFragment f = new BikeInfoBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString("bikePointName", bikePointName);
        args.putInt("bikesAvailable", bikesAvailable);
        args.putInt("bikesTaken", bikesTaken);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBikePointName = getArguments().getString("bikePointName", "Unavailable");
        this.mBikesAvailable = getArguments().getInt("bikesAvailable", 0);
        this.mBikesTaken = getArguments().getInt("bikesTaken", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bike_info_bottom_sheet, container, false);
        TextView mTextViewBikePointName = v.findViewById(R.id.textViewBikePointName);
        TextView mTextViewNumBikes = v.findViewById(R.id.textViewNumBikes);
        TextView mTextViewBikesTaken = v.findViewById(R.id.textViewBikesTaken);
        mTextViewBikePointName.setText(mBikePointName);
        mTextViewNumBikes.setText(String.format(getString(R.string.num_bikes_available), String.valueOf(mBikesAvailable)));
        mTextViewBikesTaken.setText(String.format(getString(R.string.num_bikes_taken), String.valueOf(mBikesTaken)));
        return v;
    }

}
