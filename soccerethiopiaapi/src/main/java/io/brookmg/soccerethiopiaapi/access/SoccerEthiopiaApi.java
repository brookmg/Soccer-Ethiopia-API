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
import android.support.v4.app.Fragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import io.brookmg.soccerethiopiaapi.network.LeagueScheduleFetch;
import io.brookmg.soccerethiopiaapi.network.StandingFetch;

/**
 * Created by BrookMG on 12/20/2018 in io.brookmg.soccerethiopiaapi.access
 * inside the project SoccerEthiopia .
 */
public class SoccerEthiopiaApi {

    private RequestQueue mainRequestQueue;

    /**
     * Main Constructor for Soccer Ethiopia API
     * @param context - used for creating the main request queue
     */
    public SoccerEthiopiaApi(Context context) {
        mainRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Overloaded constructor in-case the user wants to use fragment as parameter
     * @param fragment - #fragment.getActivity() will be used as a context
     */
    public SoccerEthiopiaApi(Fragment fragment) {
        this(fragment.getActivity());
    }

    /**
     * Main Function to get the latest team ranking
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLatestTeamRanking (StandingFetch.OnStandingDataProcessed processed, StandingFetch.OnError error) {
        StandingFetch.fetchLatestStandingData(mainRequestQueue,
                response -> StandingFetch.processFetchedStandingHTML(response , processed, error),
                error);
    }

    /**
     * Main Function to get all the league schedule for current session
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLeagueSchedule (LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed , StandingFetch.OnError error) {
        LeagueScheduleFetch.getAllLeagueSchedule(mainRequestQueue, processed, error);
    }

    /**
     * Main Function to get all the league schedule for current session
     * @param week - the required week
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any error
     */
    public void getLeagueScheduleOfWeek ( int week , LeagueScheduleFetch.OnLeagueScheduleDataProcessed processed , StandingFetch.OnError error) {
        LeagueScheduleFetch.getLeagueScheduleOfWeek(week, mainRequestQueue, processed, error);
    }

}
