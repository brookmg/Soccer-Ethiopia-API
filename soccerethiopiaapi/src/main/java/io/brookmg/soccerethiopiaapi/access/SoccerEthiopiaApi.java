/*
 * Copyright (C) 2018 Brook Mezgebu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.brookmg.soccerethiopiaapi.access;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import io.brookmg.soccerethiopiaapi.data.Player;
import io.brookmg.soccerethiopiaapi.data.Team;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import io.brookmg.soccerethiopiaapi.network.*;
import io.brookmg.soccerethiopiaapi.utils.ThreadPoolProvider;

/**
 * Created by BrookMG on 12/20/2018 in io.brookmg.soccerethiopiaapi.access
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings("unused")
public class SoccerEthiopiaApi {

    private RequestQueue mainRequestQueue;
    private ThreadPoolProvider threadPullProvider = new ThreadPoolProvider();

    /**
     * Main Constructor for Soccer Ethiopia API
     * @param context - used for creating the main request queue. consider using {@code getApplicationContext()}
     * @throws NullPointerException if the context is null
     */
    public SoccerEthiopiaApi(Context context) throws NullPointerException {
        if (context == null) throw new NullPointerException("context cannot be null");
        mainRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Overloaded constructor in-case the user wants to use fragment as parameter
     * @param fragment - #fragment.getActivity() will be used as a context
     * @throws NullPointerException if the context is null
     */
    public SoccerEthiopiaApi(Fragment fragment) throws NullPointerException {
        this(fragment.getActivity());
    }

    /**
     * Main Function to get the latest team ranking
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLatestTeamRanking (StandingFetch.OnStandingDataProcessed processed, OnError error) {
        StandingFetch.fetchLatestStandingData(mainRequestQueue,
                response -> StandingFetch.processFetchedStandingHTML(response , processed, error),
                error);
    }

    /**
     * Main Function to get all the league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLeagueSchedule (LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed , OnError error) {
        LeagueScheduleFetch.getAllLeagueSchedule(mainRequestQueue, processed, error);
    }

    /**
     * Main Function to get all the league schedule for current session
     * @param week - the required week
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLeagueScheduleOfWeek ( int week , LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed , OnError error) {
        LeagueScheduleFetch.getLeagueScheduleOfWeek(week, mainRequestQueue, processed, error);
    }

    /**
     * Main Function to get this week's league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getThisWeekLeagueSchedule ( LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed, OnError error) {
        LeagueScheduleFetch.getThisWeekLeagueSchedule(mainRequestQueue , processed , error);
    }

    /**
     * Main Function to get this week's league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLastWeekLeagueSchedule ( LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed, OnError error) {
        LeagueScheduleFetch.getLastWeekLeagueSchedule(mainRequestQueue , processed , error);
    }

    /**
     * Main Function to get this week's league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getNextWeekLeagueSchedule ( LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed, OnError error) {
        LeagueScheduleFetch.getNextWeekLeagueSchedule(mainRequestQueue , processed , error);
    }

    /**
     * Main Function to get the complete detail of a team
     * @param incomplete - the team that the details should belong to
     * @param teamDetailReady - a callback to handle the processed team detail
     * @param error - a callback to handle any error
     */
    public void getTeamDetail (Team incomplete, TeamDetailsFetch.OnTeamDetailReady teamDetailReady, OnError error) {
        TeamDetailsFetch.getCompleteDetail(incomplete, mainRequestQueue, teamDetailReady, error);
    }

    /**
     * Main Function to get the next game of a specified team in the league schedule
     * @param team - the team that which we want to find the next game of
     * @param callback - a callback to handle the data when the game is found / or not
     * @param onError - a callback to handle any error
     */
    public void getNextGameOfTeam (Team team , LeagueScheduleFetch.OnSingleLeagueScheduleDataProcessed callback, OnError onError) {
        LeagueScheduleFetch.getTeamNextGame(mainRequestQueue , team, callback , onError);
    }

    /**
     * Main Function to get the next game of a specified team in this week's league schedule
     * @param team - the team that which we want to find the next game of
     * @param callback - a callback to handle the data when the game is found / or not
     * @param onError - a callback to handle any error
     */
    public void getNextGameOfTeamInThisWeek (Team team , LeagueScheduleFetch.OnSingleLeagueScheduleDataProcessed callback, OnError onError) {
        LeagueScheduleFetch.getTeamNextGameInThisWeek(mainRequestQueue , team, callback , onError);
    }

    /**
     * Main Function to get the up-to-date top players in the league (in terms of the goal scored)
     * @param onPlayersListReceived - a callback to handle the list of players
     * @param onError - a callback to handle any error
     */
    public void getTopPlayers (TopPlayersFetch.OnPlayersListReceived onPlayersListReceived, OnError onError) {
        TopPlayersFetch.getTopPlayersList( mainRequestQueue, onPlayersListReceived, onError);
    }

    /**
     * Main Function to get details about a specific player
     * @param player - the player in which the detail should belong to
     * @param callback - a callback to handle the processed data
     * @param onError - a callback to handle any error
     */
    public void getPlayerDetail (Player player, PlayerDetailsFetch.OnPlayerDetailProcessed callback, OnError onError) {
        PlayerDetailsFetch.getPlayerDetail(mainRequestQueue, player, callback, onError);
    }

    /**
     * Main Function to get list of latest news
     * @param onNewsDataProcessed - a callback to handle the processed data
     * @param error - a callback to handle any error
     */
    public void getLatestNews(NewsFetch.OnNewsDataProcessed onNewsDataProcessed, OnError error) {
        NewsFetch.getLatestNews(mainRequestQueue, onNewsDataProcessed, error);
    }

}
