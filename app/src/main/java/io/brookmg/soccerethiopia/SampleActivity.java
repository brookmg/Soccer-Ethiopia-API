package io.brookmg.soccerethiopia;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import io.brookmg.soccerethiopiaapi.access.SoccerEthiopiaApi;
import io.brookmg.soccerethiopiaapi.data.LeagueScheduleItem;
import io.brookmg.soccerethiopiaapi.data.NewsItem;
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

        apiEntry.getTopPlayers(
                players -> Log.v("players" , Arrays.toString(players.toArray())),
                error -> Log.e("players_error", error));

        apiEntry.getTopPlayers(
                players -> {
                    if (players.size() > 0) {
                        apiEntry.getPlayerDetail(players.get(0),
                                player -> {
                                    if (!player.getCurrentTeam().isComplete()) {
                                        apiEntry.getTeamDetail(player.getCurrentTeam(), team -> {
                                            player.setCurrentTeam(team);
                                            Log.v("player_detailed", player.toString());
                                        }, error -> {});
                                    } else {
                                        Log.v("player_detailed", player.toString());
                                    }
                                },
                                error -> Log.e("player_detailed" , error));
                    }
                },
                error -> Log.e("players_error", error));

        apiEntry.getLatestNews(news -> {
            for (NewsItem item : news) Log.v("news_fetch", item.toString());
        }, error -> Log.e("news_fetch", error));

    }
}
