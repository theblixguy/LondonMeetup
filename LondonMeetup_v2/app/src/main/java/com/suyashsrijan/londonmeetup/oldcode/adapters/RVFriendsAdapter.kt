package com.suyashsrijan.londonmeetup.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.suyashsrijan.londonmeetup.R
import com.suyashsrijan.londonmeetup.models.Friend

import java.util.ArrayList

class RVFriendsAdapter(internal var friends: ArrayList<Friend>) : RecyclerView.Adapter<RVFriendsAdapter.TubeDeparturesViewHolder>() {

    class TubeDeparturesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var friendCard: CardView
        internal var friendName: TextView
        internal var friendMobileNumber: TextView

        init {
            friendCard = itemView.findViewById(R.id.friendCard)
            friendName = itemView.findViewById(R.id.friendName)
            friendMobileNumber = itemView.findViewById(R.id.friendMobileNumber)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TubeDeparturesViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.friend_card_view, viewGroup, false)
        val pvh = TubeDeparturesViewHolder(v)
        return pvh
    }

    override fun onBindViewHolder(tubeDeparturesViewHolder: TubeDeparturesViewHolder, i: Int) {
        tubeDeparturesViewHolder.friendName.text = friends[i].friendName
        tubeDeparturesViewHolder.friendMobileNumber.text = friends[i].friendMobileNumber
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

}