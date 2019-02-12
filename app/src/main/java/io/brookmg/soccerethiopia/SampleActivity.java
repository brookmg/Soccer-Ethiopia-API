package io.brookmg.soccerethiopia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import io.brookmg.soccerethiopiaapi.access.SoccerEthiopiaApi;
import io.brookmg.soccerethiopiaapi.data.LeagueScheduleItem;
import io.brookmg.soccerethiopiaapi.data.RankItem;
import io.brookmg.soccerethiopiaapi.utils.Constants;

import java.util.Arrays;

public class SampleActivity extends AppCompatActivity {

    SoccerEthiopiaApi apiEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        apiEntry = new SoccerEthiopiaApi(this);

        apiEntry.getLatestTeamRanking(ranking -> {
            for (RankItem item : ranking) {
                Log.v("data" , item.getTeam().getTeamFullName() + ", " + item.getTeam().getTeamLogo() + ", " + item.getRank()
                        + ", " + item.getPlayedGames() + ", " + item.getWonGames() + ", " + item.getDrawGames()
                        + ", " + item.getLostGames()
                );
            }
        }, error -> Log.e("Error" , error));

        apiEntry.getLeagueSchedule(scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        }, error -> Log.e("Error_League" , error));


        apiEntry.getLeagueScheduleOfWeek(5,
                scheduleItems -> {
                    for (LeagueScheduleItem item : scheduleItems) {
                        Log.v("data_league_week_5" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
                    }
                },
                error -> Log.e("Error_League" , error));

        apiEntry.getThisWeekLeagueSchedule(
                scheduleItems -> {
                    for (LeagueScheduleItem item : scheduleItems) {
                        Log.v("data_this_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
                    }
                },
                error -> Log.e("Error_League" , error));

        apiEntry.getLastWeekLeagueSchedule(
                scheduleItems -> {
                    for (LeagueScheduleItem item : scheduleItems) {
                        Log.v("data_last_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
                    }
                },
                error -> Log.e("Error_League" , error));

        apiEntry.getNextWeekLeagueSchedule(
                scheduleItems -> {
                    for (LeagueScheduleItem item : scheduleItems) {
                        Log.v("data_next_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
                    }
                },
                error -> Log.e("Error_League" , error));

        apiEntry.getNextGameOfTeam(Constants.ETHIOPIA_BUNA ,
                item -> Log.v("data_next_game" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus()),
                error -> Log.e("Error_Game" , error));

//        for (Team t : teams)
//            apiEntry
//                    .getTeamDetail(t ,
//                            team -> Log.v("data_team_detail" , team.toString()),
//                            error -> Log.e("Error_Team" , error));

        apiEntry.getTopPlayers(this,
                players -> Log.v("players" , Arrays.toString(players.toArray())),
                error -> Log.e("players_error", error));

        apiEntry.getTopPlayers(this,
                players -> {
                    if (players.size() > 0) {
                        apiEntry.getPlayerDetail(this, players.get(0),
                                player -> {
                                    if (!player.getCurrentTeam().isComplete()) {
                                        apiEntry.getTeamDetail(player.getCurrentTeam(), player::setCurrentTeam, error -> {});
                                    }
                                    Log.v("player_detailed", player.toString());
                                },
                                error -> Log.e("player_detailed" , error));
                    }
                },
                error -> Log.e("players_error", error));

    }
}
