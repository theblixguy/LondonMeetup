package com.suyashsrijan.londonmeetup.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suyashsrijan.londonmeetup.R;
import com.suyashsrijan.londonmeetup.models.Friend;

import java.util.ArrayList;

public class RVFriendsAdapter extends RecyclerView.Adapter<RVFriendsAdapter.TubeDeparturesViewHolder>{

    public static class TubeDeparturesViewHolder extends RecyclerView.ViewHolder {
        CardView friendCard;
        TextView friendName;
        TextView friendMobileNumber;

        TubeDeparturesViewHolder(View itemView) {
            super(itemView);
            friendCard = itemView.findViewById(R.id.friendCard);
            friendName = itemView.findViewById(R.id.friendName);
            friendMobileNumber = itemView.findViewById(R.id.friendMobileNumber);
        }
    }

    ArrayList<Friend> friends;

    public RVFriendsAdapter(ArrayList<Friend> friends){
        this.friends = friends;
    }

    @Override
    public TubeDeparturesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_card_view, viewGroup, false);
        TubeDeparturesViewHolder pvh = new TubeDeparturesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TubeDeparturesViewHolder tubeDeparturesViewHolder, int i) {
        tubeDeparturesViewHolder.friendName.setText(friends.get(i).getFriendName());
        tubeDeparturesViewHolder.friendMobileNumber.setText(friends.get(i).getFriendMobileNumber());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}