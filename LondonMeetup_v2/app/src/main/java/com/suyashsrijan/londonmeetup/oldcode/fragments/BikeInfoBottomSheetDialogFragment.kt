package com.suyashsrijan.londonmeetup.fragments

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.suyashsrijan.londonmeetup.R

class BikeInfoBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var mBikePointName: String? = null
    private var mBikesAvailable: Int = 0
    private var mBikesTaken: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mBikePointName = arguments!!.getString("bikePointName", "Unavailable")
        this.mBikesAvailable = arguments!!.getInt("bikesAvailable", 0)
        this.mBikesTaken = arguments!!.getInt("bikesTaken", 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.bike_info_bottom_sheet, container, false)
        val mTextViewBikePointName = v.findViewById<TextView>(R.id.textViewBikePointName)
        val mTextViewNumBikes = v.findViewById<TextView>(R.id.textViewNumBikes)
        val mTextViewBikesTaken = v.findViewById<TextView>(R.id.textViewBikesTaken)
        mTextViewBikePointName.text = mBikePointName
        mTextViewNumBikes.text = String.format(getString(R.string.num_bikes_available), mBikesAvailable.toString())
        mTextViewBikesTaken.text = String.format(getString(R.string.num_bikes_taken), mBikesTaken.toString())
        return v
    }

    companion object {

        fun newInstance(bikePointName: String, bikesAvailable: Int, bikesTaken: Int): BikeInfoBottomSheetDialogFragment {
            val f = BikeInfoBottomSheetDialogFragment()
            val args = Bundle()
            args.putString("bikePointName", bikePointName)
            args.putInt("bikesAvailable", bikesAvailable)
            args.putInt("bikesTaken", bikesTaken)
            f.arguments = args
            return f
        }
    }

}
