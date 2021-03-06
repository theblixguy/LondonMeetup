package com.suyashsrijan.londonmeetup.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusDeparture
import com.suyashsrijan.londonmeetup.R
import com.suyashsrijan.londonmeetup.utils.BitmapUtils
import com.suyashsrijan.londonmeetup.utils.Utils

import java.util.ArrayList

class RVBusDeparturesAdapter(private val departures: ArrayList<BusDeparture>) : RecyclerView.Adapter<RVBusDeparturesAdapter.TubeDeparturesViewHolder>() {
    private var context: Context? = null

    class TubeDeparturesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cardView: CardView
        internal var goingTowards: TextView
        internal var destinationName: TextView
        internal var lineLogo: ImageView
        internal var arrivalTime: TextView

        init {
            cardView = itemView.findViewById(R.id.departureCard)
            goingTowards = itemView.findViewById(R.id.textViewGoingTowards)
            destinationName = itemView.findViewById(R.id.textViewDestination)
            lineLogo = itemView.findViewById(R.id.imageViewLine)
            arrivalTime = itemView.findViewById(R.id.textViewTimeToArrive)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TubeDeparturesViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.bus_departure_card_view, viewGroup, false)
        val pvh = TubeDeparturesViewHolder(v)
        context = viewGroup.context
        return pvh
    }

    override fun onBindViewHolder(tubeDeparturesViewHolder: TubeDeparturesViewHolder, i: Int) {
        tubeDeparturesViewHolder.goingTowards.text = departures[i].goingTowards
        tubeDeparturesViewHolder.destinationName.text = departures[i].destinationName
        tubeDeparturesViewHolder.lineLogo.setImageBitmap(BitmapUtils.getBitmapForBusLine(context!!, departures[i].lineName))
        val timeToArrive = Utils.getTimeToArriveInMinutes(departures[i].arrivalTime)
        if (timeToArrive == 1) {
            tubeDeparturesViewHolder.arrivalTime.setText(R.string.only_1_min)
        } else if (timeToArrive == 0) {
            tubeDeparturesViewHolder.arrivalTime.setText(R.string.less_than_1_min)
        } else
            tubeDeparturesViewHolder.arrivalTime.text = String.format("%s mins", timeToArrive.toString())
    }

    override fun getItemCount(): Int {
        return departures.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

}