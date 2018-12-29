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

package io.brookmg.soccerethiopiaapi.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import io.brookmg.soccerethiopiaapi.data.LeagueScheduleItem;
import io.brookmg.soccerethiopiaapi.utils.Constants;

import java.util.ArrayList;

/**
 * Created by BrookMG on 12/29/2018 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings("unused")
public class LeagueScheduleFetch {

    public interface OnRawLeagueScheduleData {
        void onResponse(String response);
    }

    public interface OnLeagueScheduleDataProcessed {
        void onProcessed(ArrayList<LeagueScheduleItem> items);
    }

    public static void fetchUpdatedLeagueSchedule (RequestQueue queue , OnRawLeagueScheduleData callback, StandingFetch.OnError onError) {
        queue.add(new StringRequest(Request.Method.GET , Constants.CLUB_STANDING_BASE_URL, callback::onResponse , error -> onError.onError(error.toString())));
    }

    public static void processFetchedLeagueSchedule (String response , OnLeagueScheduleDataProcessed callback, StandingFetch.OnError onError) {
        if (response.equals("[ERROR!]")) {
            onError.onError("error in response.");
        }

    }
}
