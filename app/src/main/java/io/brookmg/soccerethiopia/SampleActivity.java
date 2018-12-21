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
        new SoccerEthiopiaApi(this).getLatestTeamRanking(ranking -> {
            for (RankItem item : ranking) {
                Log.v("data" , item.getTeamName() + ", " + item.getTeamIcon() + ", " + item.getRank()
                        + ", " + item.getPlayedGames() + ", " + item.getWonGames() + ", " + item.getDrawGames()
                        + ", " + item.getLostGames()
                );
            }
        }, error -> Log.e("Error" , error));
    }
}
