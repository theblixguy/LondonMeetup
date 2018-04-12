package com.suyashsrijan.londonmeetup.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.suyashsrijan.londonmeetup.API.tfl.TfLAPI;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ApiResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.TubeDeparture;
import com.suyashsrijan.londonmeetup.R;
import com.suyashsrijan.londonmeetup.adapters.RVTubeDeparturesAdapter;

import java.util.ArrayList;

public class TubeDeparturesActivity extends AppCompatActivity {

    private String stopId;
    private Stoptypes stopType;
    private Modes mode;
    private TfLAPI TfLApi = new TfLAPI(this);
    private ProgressDialog loadingDeparturesPd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tube_departures);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        stopId = getIntent().getStringExtra("stopId");

        final RecyclerView rv = findViewById(R.id.recyclerViewTubeDepartures);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        loadingDeparturesPd = new ProgressDialog(TubeDeparturesActivity.this);
        loadingDeparturesPd.setMessage("Loading departures...");
        loadingDeparturesPd.setIndeterminate(true);
        loadingDeparturesPd.setCancelable(false);
        loadingDeparturesPd.show();

        TfLApi.getTubeDepartures(stopId, new ApiResultCallback<TubeDeparture>() {
            @Override
            public void response(Context context, final ArrayList<TubeDeparture> output) {
                rv.setAdapter(new RVTubeDeparturesAdapter(output));
                rv.startLayoutAnimation();
                if (loadingDeparturesPd != null) {
                    loadingDeparturesPd.dismiss();
                }
            }

            @Override
            public void error(Context context, Exception exception) {
                if (loadingDeparturesPd != null) {
                    loadingDeparturesPd.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Error");
                builder.setMessage("There was an error while loading departures: " + exception.getMessage());
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
