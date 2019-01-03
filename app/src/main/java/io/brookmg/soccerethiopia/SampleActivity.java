package io.brookmg.soccerethiopia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import io.brookmg.soccerethiopiaapi.access.SoccerEthiopiaApi;
import io.brookmg.soccerethiopiaapi.data.LeagueScheduleItem;
import io.brookmg.soccerethiopiaapi.data.RankItem;

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

        new SoccerEthiopiaApi(this).getLeagueSchedule(scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        }, error -> Log.e("Error_League" , error));


        new SoccerEthiopiaApi(this).getLeagueScheduleOfWeek(5,
                scheduleItems -> {
                    for (LeagueScheduleItem item : scheduleItems) {
                        Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
                    }
                },
                error -> Log.e("Error_League" , error));

    }
}
