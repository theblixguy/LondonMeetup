package com.suyashsrijan.londonmeetup.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem

import com.suyashsrijan.londonmeetup.oldcode.API.tfl.TfLAPI
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ApiResultCallback
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeDeparture
import com.suyashsrijan.londonmeetup.R
import com.suyashsrijan.londonmeetup.adapters.RVTubeDeparturesAdapter

import java.util.ArrayList

class TubeDeparturesActivity : AppCompatActivity() {

    private var stopId: String? = null
    private val stopType: Stoptypes? = null
    private val mode: Modes? = null
    private val TfLApi = TfLAPI(this)
    private var loadingDeparturesPd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tube_departures)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        stopId = intent.getStringExtra("stopId")

        val rv = findViewById<RecyclerView>(R.id.recyclerViewTubeDepartures)
        val llm = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.layoutManager = llm

        loadingDeparturesPd = ProgressDialog(this@TubeDeparturesActivity)
        loadingDeparturesPd!!.setMessage("Loading departures...")
        loadingDeparturesPd!!.isIndeterminate = true
        loadingDeparturesPd!!.setCancelable(false)
        loadingDeparturesPd!!.show()

        TfLApi.getTubeDepartures(stopId!!, object : ApiResultCallback<TubeDeparture> {
            override fun response(context: Context, output: ArrayList<TubeDeparture>) {
                rv.adapter = RVTubeDeparturesAdapter(output)
                rv.startLayoutAnimation()
                if (loadingDeparturesPd != null) {
                    loadingDeparturesPd!!.dismiss()
                }
            }

            override fun error(context: Context, exception: Exception) {
                if (loadingDeparturesPd != null) {
                    loadingDeparturesPd!!.dismiss()
                }
                val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                builder.setTitle("Error")
                builder.setMessage("There was an error while loading departures: " + exception.message)
                builder.setPositiveButton("Close") { dialogInterface, i -> dialogInterface.dismiss() }
                builder.show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
