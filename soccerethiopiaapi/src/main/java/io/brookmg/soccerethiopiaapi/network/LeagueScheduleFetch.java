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

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import io.brookmg.soccerethiopiaapi.data.LeagueItemStatus;
import io.brookmg.soccerethiopiaapi.data.LeagueScheduleItem;
import io.brookmg.soccerethiopiaapi.utils.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BrookMG on 12/29/2018 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings("unused")
public class LeagueScheduleFetch {

    /**
     * Interface to serve as a callback for {@link LeagueScheduleFetch#fetchUpdatedLeagueSchedule(RequestQueue, OnRawLeagueScheduleData, StandingFetch.OnError)}
     */
    public interface OnRawLeagueScheduleData {
        void onResponse(String response);
    }

    /**
     * Interface to serve as a callback for functions :
     * {@link LeagueScheduleFetch#processFetchedLeagueSchedule(String, OnLeagueScheduleDataProcessed, StandingFetch.OnError)}
     * {@link LeagueScheduleFetch#getThisWeekSchedule(OnLeagueScheduleDataProcessed, StandingFetch.OnError)}
     * {@link LeagueScheduleFetch#getLeagueScheduleOfWeek(int, OnLeagueScheduleDataProcessed, StandingFetch.OnError)}
     * {@link LeagueScheduleFetch#getAllLeagueSchedule(OnLeagueScheduleDataProcessed, StandingFetch.OnError)}
     */
    public interface OnLeagueScheduleDataProcessed {
        void onProcessed(ArrayList<LeagueScheduleItem> items);
    }

    /**
     * A method to get the latest raw data from base website
     * @param queue - The Volley queue to work on
     * @param callback - The callback to call when response is returned
     * @param onError - callback function for error handling
     */
    public static void fetchUpdatedLeagueSchedule (RequestQueue queue , OnRawLeagueScheduleData callback, StandingFetch.OnError onError) {
        // FIXME: 12/30/2018 This fetches only the current week's schedule... We need a method to press some buttons before letting the processor process the response
        queue.add(new StringRequest(Request.Method.GET , Constants.CLUB_STANDING_BASE_URL, callback::onResponse , error -> onError.onError(error.toString())));
    }

    /**
     * A method to process all the league schedule items from the raw data
     * @param response - Raw data
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processFetchedLeagueSchedule (String response , OnLeagueScheduleDataProcessed callback, StandingFetch.OnError onError) {
        if (response.equals("[ERROR!]")) {
            onError.onError("error in response.");
        }

        Log.e("_RESPONSE" , response);

        int currentWeek = 0;
        ArrayList<LeagueScheduleItem> items = new ArrayList<>();

        try {
            Document $ = Jsoup.parse(response);
            Elements tables = $.getElementsByClass("tablepress-id-006");
            for (Element table : tables) {
                //for each table meaning for each week... each week is stored in a table with a classname of tablepress-id-006
                Elements rows = table.getElementsByTag("tr");
                currentWeek++;
                String currentDate = "";

                for (Element row : rows) {
                    //each row could be either a schedule item or a date for the following schedule items
                    if (row.attributes().get("class").contains("row-2 odd") && !row.equals(rows.get(0))) {
                        currentDate = row.text();
                    } else if(!row.equals(rows.get(0))) {
                        int gs = getGameStatus(row.getElementsByTag("td").get(1).text());
                        Map<String, Integer> detail = new HashMap<>();

                        if (gs == LeagueItemStatus.STATUS_POSTPONED || gs == LeagueItemStatus.STATUS_NORMAL) {
                            detail.put(row.getElementsByTag("td").get(0).text(), 0);
                            detail.put(row.getElementsByTag("td").get(2).text(), 0);
                        } else if (gs == LeagueItemStatus.STATUS_TOOK_PLACE) {
                            detail.put(row.getElementsByTag("td").get(0).text(), Integer.parseInt(row.getElementsByTag("td").get(1).text().split("-")[0]));
                            detail.put(row.getElementsByTag("td").get(2).text(), Integer.parseInt(row.getElementsByTag("td").get(1).text().split("-")[1]));
                        }

                        items.add(new LeagueScheduleItem(
                                currentWeek, currentDate, gs, detail
                        ));
                    }
                }

            }
        } catch (Exception error) {
            onError.onError(error.toString());
            return;
        }

        callback.onProcessed(items);
    }

    private static @LeagueItemStatus.GameStatus int getGameStatus (String data) {
        if (data.contains("PP")) return LeagueItemStatus.STATUS_POSTPONED;
        else if(data.contains("-")) return LeagueItemStatus.STATUS_TOOK_PLACE;
        else return LeagueItemStatus.STATUS_NORMAL;
    }

    /**
     * A method to get this week's league schedule
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getThisWeekSchedule (OnLeagueScheduleDataProcessed callback , StandingFetch.OnError onError){

    }

    /**
     * A method to get league schedule on a specific week
     * @param week - on which week
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getLeagueScheduleOfWeek (int week , OnLeagueScheduleDataProcessed callback , StandingFetch.OnError onError) {
        if (week <= 0) return;



    }

    /**
     * A method to get all the league schedule
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getAllLeagueSchedule (OnLeagueScheduleDataProcessed callback , StandingFetch.OnError onError){

    }
}
