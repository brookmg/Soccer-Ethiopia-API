package io.brookmg.soccerethiopia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import io.brookmg.soccerethiopiaapi.access.SoccerEthiopiaApi;
import io.brookmg.soccerethiopiaapi.data.RankItem;
import io.brookmg.soccerethiopiaapi.network.StandingFetch;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        new SoccerEthiopiaApi(this).getLatestTeamRanking(ranking -> {}, error -> Log.e("Error" , error));
    }
}
