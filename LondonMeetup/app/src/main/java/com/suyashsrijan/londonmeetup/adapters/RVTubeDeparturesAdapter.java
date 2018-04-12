package com.suyashsrijan.londonmeetup.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suyashsrijan.londonmeetup.API.tfl.models.TubeDeparture;
import com.suyashsrijan.londonmeetup.R;
import com.suyashsrijan.londonmeetup.utils.BitmapUtils;
import com.suyashsrijan.londonmeetup.utils.Utils;

import java.util.ArrayList;

public class RVTubeDeparturesAdapter extends RecyclerView.Adapter<RVTubeDeparturesAdapter.TubeDeparturesViewHolder>{

    public static class TubeDeparturesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView goingTowards;
        TextView stationName;
        ImageView lineLogo;
        TextView arrivalTime;

        TubeDeparturesViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.departureCard);
            goingTowards = itemView.findViewById(R.id.textViewGoingTowards);
            stationName = itemView.findViewById(R.id.textViewStationName);
            lineLogo = itemView.findViewById(R.id.imageViewLine);
            arrivalTime = itemView.findViewById(R.id.textViewTimeToArrive);
        }
    }

    private ArrayList<TubeDeparture> departures;
    private Context context;

    public RVTubeDeparturesAdapter(ArrayList<TubeDeparture> departures){
        this.departures = departures;
    }

    @Override
    public TubeDeparturesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tube_departure_card_view, viewGroup, false);
        TubeDeparturesViewHolder pvh = new TubeDeparturesViewHolder(v);
        context = viewGroup.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(TubeDeparturesViewHolder tubeDeparturesViewHolder, int i) {
        tubeDeparturesViewHolder.goingTowards.setText(departures.get(i).getGoingTowards());
        tubeDeparturesViewHolder.stationName.setText(departures.get(i).getDestinationName());
        tubeDeparturesViewHolder.lineLogo.setImageDrawable(BitmapUtils.getDrawableForTubeLine(context, departures.get(i).getLineName()));
        int timeToArrive = Utils.getTimeToArriveInMinutes(departures.get(i).getArrivalTime());
        if (timeToArrive == 1) {
            tubeDeparturesViewHolder.arrivalTime.setText(R.string.only_1_min);
        } else if (timeToArrive == 0) {
            tubeDeparturesViewHolder.arrivalTime.setText(R.string.less_than_1_min);
        } else  tubeDeparturesViewHolder.arrivalTime.setText(String.format("%s mins", String.valueOf(timeToArrive)));
    }

    @Override
    public int getItemCount() {
        return departures.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}