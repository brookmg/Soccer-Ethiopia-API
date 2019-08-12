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
import androidx.test.annotation.Beta;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import io.brookmg.soccerethiopiaapi.data.NewsItem;
import io.brookmg.soccerethiopiaapi.data.NewsItemV2;
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
    private Boolean contentShouldBeCached;

    /**
     * Main Constructor for Soccer Ethiopia API
     * @param context - used for creating the main request queue. consider using {@code getApplicationContext()}
     * @throws NullPointerException if the context is null
     */
    public SoccerEthiopiaApi(@NonNull Context context) {
        this(context, true);
    }

    /**
     * Main Constructor for Soccer Ethiopia API
     * @param context - used for creating the main request queue. consider using {@code getApplicationContext()}
     * @param shouldCache - to specify whether to receive cached content or not, which is by default true
     */
    public SoccerEthiopiaApi(@NonNull Context context, boolean shouldCache) {
        mainRequestQueue = Volley.newRequestQueue(context);
        contentShouldBeCached = shouldCache;
    }

    /**
     * Overloaded constructor in-case the user wants to use fragment as parameter
     * @param fragment {@code fragment.getActivity()} will be used as a context
     * @deprecated using contexts with lifecycle might lead to some leaks for now. Use {@code SoccerEthiopiaApi(getApplicationContext())} instead
     */
    @Deprecated
    public SoccerEthiopiaApi(Fragment fragment) {
        this(fragment.getActivity());
    }

    /**
     * Main Function to get the latest team ranking
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLatestTeamRanking (StandingFetch.OnStandingDataProcessed processed, OnError error) {
        StandingFetch.fetchLatestStandingData(mainRequestQueue,
                contentShouldBeCached,
                response -> StandingFetch.processFetchedStandingHTML(response , processed, error),
                error);
    }

    /**
     * Main Function to get the latest team ranking
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     * @param moreDetailed - whether to include details like `goalAgainst` for each RankItem
     */
    public void getLatestTeamRanking (StandingFetch.OnStandingDataProcessed processed, OnError error, boolean moreDetailed) {
        StandingFetch.fetchLatestStandingData(mainRequestQueue,
                contentShouldBeCached,
                response -> StandingFetch.processFetchedStandingHTML(response , moreDetailed, processed, error),
                error);
    }

    /**
     * Main Function to get all the league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLeagueSchedule (LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed , OnError error) {
        LeagueScheduleFetch.getAllLeagueSchedule(mainRequestQueue, contentShouldBeCached, processed, error);
    }

    /**
     * Main Function to get all the league schedule for current session
     * @param week - the required week
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLeagueScheduleOfWeek ( int week , LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed , OnError error) {
        LeagueScheduleFetch.getLeagueScheduleOfWeek(week, mainRequestQueue, contentShouldBeCached, processed, error);
    }

    /**
     * Main Function to get this week's league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getThisWeekLeagueSchedule ( LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed, OnError error) {
        LeagueScheduleFetch.getThisWeekLeagueSchedule(mainRequestQueue, contentShouldBeCached, processed , error);
    }

    /**
     * Main Function to get this week's league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLastWeekLeagueSchedule ( LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed, OnError error) {
        LeagueScheduleFetch.getLastWeekLeagueSchedule(mainRequestQueue, contentShouldBeCached,  processed , error);
    }

    /**
     * Main Function to get this week's league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getNextWeekLeagueSchedule ( LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed, OnError error) {
        LeagueScheduleFetch.getNextWeekLeagueSchedule(mainRequestQueue , contentShouldBeCached, processed , error);
    }

    /**
     * Main Function to get the complete detail of a team
     * @param incomplete - the team that the details should belong to
     * @param teamDetailReady - a callback to handle the processed team detail
     * @param error - a callback to handle any error
     */
    public void getTeamDetail (Team incomplete, TeamDetailsFetch.OnTeamDetailReady teamDetailReady, OnError error) {
        TeamDetailsFetch.getCompleteDetail(incomplete, mainRequestQueue, contentShouldBeCached, teamDetailReady, error);
    }

    /**
     * Main Function to get the next game of a specified team in the league schedule
     * @param team - the team that which we want to find the next game of
     * @param callback - a callback to handle the data when the game is found / or not
     * @param onError - a callback to handle any error
     */
    public void getNextGameOfTeam (Team team , LeagueScheduleFetch.OnSingleLeagueScheduleDataProcessed callback, OnError onError) {
        LeagueScheduleFetch.getTeamNextGame(mainRequestQueue , contentShouldBeCached, team, callback , onError);
    }

    /**
     * Main Function to get the next game of a specified team in this week's league schedule
     * @param team - the team that which we want to find the next game of
     * @param callback - a callback to handle the data when the game is found / or not
     * @param onError - a callback to handle any error
     */
    public void getNextGameOfTeamInThisWeek (Team team , LeagueScheduleFetch.OnSingleLeagueScheduleDataProcessed callback, OnError onError) {
        LeagueScheduleFetch.getTeamNextGameInThisWeek(mainRequestQueue , contentShouldBeCached, team, callback , onError);
    }

    /**
     * Main Function to get the up-to-date top players in the league (in terms of the goal scored)
     * @param onPlayersListReceived - a callback to handle the list of players
     * @param onError - a callback to handle any error
     */
    public void getTopPlayers (TopPlayersFetch.OnPlayersListReceived onPlayersListReceived, OnError onError) {
        TopPlayersFetch.getTopPlayersList( mainRequestQueue, contentShouldBeCached, onPlayersListReceived, onError);
    }

    /**
     * Main Function to get details about a specific player
     * @param player - the player in which the detail should belong to
     * @param callback - a callback to handle the processed data
     * @param onError - a callback to handle any error
     */
    public void getPlayerDetail (Player player, PlayerDetailsFetch.OnPlayerDetailProcessed callback, OnError onError) {
        PlayerDetailsFetch.getPlayerDetail(mainRequestQueue, contentShouldBeCached, player, callback, onError);
    }

    /**
     * Main Function to get list of latest news
     * @param onNewsDataProcessed - a callback to handle the processed data
     * @param error - a callback to handle any error
     */
    public void getLatestNews(NewsFetch.OnNewsDataProcessed onNewsDataProcessed, OnError error) {
        NewsFetch.getLatestNews(mainRequestQueue, contentShouldBeCached, onNewsDataProcessed, error);
    }

    /**
     * Main Function to get list of latest news
     * @param onNewsDataProcessed - a callback to handle the processed data
     * @param error - a callback to handle any error
     */
    @Beta
    public void getLatestNewsV2(NewsFetchV2.OnNewsDataProcessed onNewsDataProcessed, OnError error) {
        NewsFetchV2.getLatestNews(mainRequestQueue, contentShouldBeCached, onNewsDataProcessed, error);
    }

    /**
     * Main Function to get the content of a specific news
     * @param item the news item you want to fetch the content of
     * @param onNewsItemProcessed a callback to handle the processed news item
     * @param error a callback to handle any error
     */
    public void getNewsItemContent(NewsItem item, NewsFetch.OnNewsItemProcessed onNewsItemProcessed, OnError error) {
        NewsFetch.getNewsItem(mainRequestQueue, contentShouldBeCached, item, onNewsItemProcessed, error);
    }

    @Beta
    public void getAuthorName(NewsItemV2 item, NewsFetchV2.OnRawNewsDataReceived onAuthorReceived) {
        NewsFetchV2.getAuthorName(mainRequestQueue, item.getAuthorId(), onAuthorReceived);
    }

    @Beta
    public void getCategoryName(NewsItemV2 item, NewsFetchV2.OnRawNewsDataReceived onCategoryReceived) {
        NewsFetchV2.getCategoryName(mainRequestQueue, item.getAuthorId(), onCategoryReceived);
    }

    @Beta
    public void getMediaLink(NewsItemV2 item, NewsFetchV2.OnRawNewsDataReceived onMediaReceived) {
        NewsFetchV2.getImageMediaLink(mainRequestQueue, item.getNewsMediaId(), onMediaReceived);
    }

    public RequestQueue getRequestQueue() {
        return mainRequestQueue;
    }
}
