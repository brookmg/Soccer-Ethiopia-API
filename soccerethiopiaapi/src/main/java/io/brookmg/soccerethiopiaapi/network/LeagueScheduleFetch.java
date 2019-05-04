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
import io.brookmg.soccerethiopiaapi.data.LeagueItemStatus;
import io.brookmg.soccerethiopiaapi.data.LeagueScheduleItem;
import io.brookmg.soccerethiopiaapi.data.Team;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import io.brookmg.soccerethiopiaapi.utils.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.brookmg.soccerethiopiaapi.utils.Utils.getTeamFromTeamName;

/**
 * Created by BrookMG on 12/29/2018 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LeagueScheduleFetch {

    /**
     * Interface to serve as a callback for {@link LeagueScheduleFetch#fetchUpdatedLeagueSchedule(RequestQueue, OnRawLeagueScheduleData, OnError)}
     */
    public interface OnRawLeagueScheduleData {
        void onResponse(String response);
    }

    /**
     * Interface to serve as a callback for functions :
     * {@link LeagueScheduleFetch#processFetchedLeagueSchedule(String, OnLeagueScheduleDataProcessed, OnError)}
     * {@link LeagueScheduleFetch#processThisWeekLeagueSchedule(String, OnLeagueScheduleDataProcessed, OnError)}
     * {@link LeagueScheduleFetch#getLeagueScheduleOfWeek(int, RequestQueue, OnLeagueScheduleDataProcessed, OnError)}
     * {@link LeagueScheduleFetch#getAllLeagueSchedule(RequestQueue, OnLeagueScheduleDataProcessed, OnError)}
     */
    public interface OnLeagueScheduleDataProcessed {
        void onProcessed(ArrayList<LeagueScheduleItem> items);
    }

    /**
     * Interface to serve as a callback for functions :
     * {@link LeagueScheduleFetch#getTeamNextGameInThisWeek(RequestQueue, Team, OnSingleLeagueScheduleDataProcessed, OnError)}
     * {@link LeagueScheduleFetch#getTeamNextGame(RequestQueue, Team, OnSingleLeagueScheduleDataProcessed, OnError)}
     */
    public interface OnSingleLeagueScheduleDataProcessed {
        void onProcessed(LeagueScheduleItem item);
    }

    /**
     * A method to get the latest raw data from base website
     * @param queue - The Volley queue to work on
     * @param callback - The callback to call when response is returned
     * @param onError - callback function for error handling
     */
    public static void fetchUpdatedLeagueSchedule (RequestQueue queue , OnRawLeagueScheduleData callback, OnError onError) {
        queue.add(new CachedStringRequest(Request.Method.GET , Constants.PREMIER_LEAGUE_SCHEDULE_BASE_URL, callback::onResponse , error -> onError.onError(error.toString())));
    }

    /**
     * A method to process all the league schedule items from the raw data
     * @param response - Raw data
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processFetchedLeagueSchedule (String response , OnLeagueScheduleDataProcessed callback, OnError onError) {
        if (response.equals("[ERROR!]")) {
            onError.onError("error in response.");
        }

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
                        Map<Team, Integer> detail = new HashMap<>();

                        if (gs == LeagueItemStatus.STATUS_POSTPONED || gs == LeagueItemStatus.STATUS_NORMAL) {
                            detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(0).text()), 0);
                            detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(2).text()), 0);
                        } else if (gs == LeagueItemStatus.STATUS_TOOK_PLACE) {
                            detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(0).text()), Integer.parseInt(row.getElementsByTag("td").get(1).text().split("-")[0]));
                            detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(2).text()), Integer.parseInt(row.getElementsByTag("td").get(1).text().split("-")[1]));
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

        items = new ArrayList<>(noDuplicates(items));   //remove duplicates
        callback.onProcessed(items);
    }

    /**
     * A method to compute the current status of the game from the provided data
     * @param data - data about the game ( between two team names )
     * @return [ {@link LeagueItemStatus#STATUS_NORMAL} ,
     *         {@link LeagueItemStatus#STATUS_POSTPONED} ,
     *         {@link LeagueItemStatus#STATUS_CANCELLED} ,
     *         {@link LeagueItemStatus#STATUS_TOOK_PLACE} ] one of these
     */
    private static @LeagueItemStatus.GameStatus int getGameStatus (String data) {
        if (data.contains("PP")) return LeagueItemStatus.STATUS_POSTPONED;
        else if(data.contains("-")) return LeagueItemStatus.STATUS_TOOK_PLACE;
        else return LeagueItemStatus.STATUS_NORMAL;
    }

    /**
     * A method to return a clone of a given arrayList but without the duplicate items
     * @param initial - initial data-set with all the possible duplicates
     * @return {@link ArrayList} from the initial data-set with all the duplicates removed
     */
    private static ArrayList<LeagueScheduleItem> noDuplicates (ArrayList<LeagueScheduleItem> initial) {
        ArrayList<LeagueScheduleItem> returnable = new ArrayList<>();
        for (LeagueScheduleItem item : initial) {
            boolean found = false;
            for (LeagueScheduleItem item_2 : returnable) {
                if ((item_2.getGameDate() + item_2.getGameDetail()).equals((item.getGameDate() + item.getGameDetail()))) found = true;
            }
            if (!found) returnable.add(item);
        }
        return returnable;
    }

    /**
     * A method to get this week's league schedule
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processThisWeekLeagueSchedule(String response , OnLeagueScheduleDataProcessed callback , OnError onError){
        if (response.equals("[ERROR!]")) {
            onError.onError("error in response.");
        }

        int currentWeek = 0;
        ArrayList<LeagueScheduleItem> items = new ArrayList<>();

        final ArrayList<LeagueScheduleItem> allData = new ArrayList<>();
        processFetchedLeagueSchedule(response, allData::addAll, error -> onError.onError("Couldn't process all the data correctly"));

       // final ArrayList<LeagueScheduleItem> noDuplicateList = new ArrayList<>(noDuplicates(allData)); // unnecessary because it's already done in in processFetchedLeagueSchedule method

        try {
            Document $ = Jsoup.parse(response);
            Elements tables = $.getElementsByClass("tablepress-id-006");
            for (Element table : tables) {
                //for each table meaning for each week... each week is stored in a table with a classname of tablepress-id-006
                Element parent = table.parent();
                if (!parent.attributes().get("style").trim().toLowerCase().equals("display: none;")) {
                    Elements rows = table.getElementsByTag("tr");

                    String currentDate = "";

                    for (Element row : rows) {
                        //each row could be either a schedule item or a date for the following schedule items
                        if (row.attributes().get("class").contains("row-2 odd") && !row.equals(rows.get(0))) {
                            currentDate = row.text();

                            if (currentWeek == 0) {
                                for (LeagueScheduleItem i : allData) {
                                    if (i.getGameDate().equals(currentDate))
                                        currentWeek = i.getGameWeek();
                                }
                            }

                        } else if(!row.equals(rows.get(0))) {
                            int gs = getGameStatus(row.getElementsByTag("td").get(1).text());
                            Map<Team, Integer> detail = new HashMap<>();

                            if (gs == LeagueItemStatus.STATUS_POSTPONED || gs == LeagueItemStatus.STATUS_NORMAL) {
                                detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(0).text()), 0);
                                detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(2).text()), 0);
                            } else if (gs == LeagueItemStatus.STATUS_TOOK_PLACE) {
                                detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(0).text()), Integer.parseInt(row.getElementsByTag("td").get(1).text().split("-")[0]));
                                detail.put(getTeamFromTeamName(row.getElementsByTag("td").get(2).text()), Integer.parseInt(row.getElementsByTag("td").get(1).text().split("-")[1]));
                            }

                            items.add(new LeagueScheduleItem(
                                    currentWeek, currentDate, gs, detail
                            ));
                        }
                    }
                }
            }
        } catch (Exception error) {
            onError.onError(error.toString());
            return;
        }

        items = new ArrayList<>(noDuplicates(items));   //remove duplicates
        callback.onProcessed(items);
    }

    /**
     * A method to get last week's league schedule
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processLastWeekLeagueSchedule(String response, OnLeagueScheduleDataProcessed callback, OnError onError) {
        if (response.equals("[ERROR!]")) {
            onError.onError("error in response.");
        }

        int currentWeek = 0;
        ArrayList<LeagueScheduleItem> items = new ArrayList<>();

        final ArrayList<LeagueScheduleItem> allData = new ArrayList<>();
        processFetchedLeagueSchedule(response, allData::addAll, error -> onError.onError("Couldn't process all the data correctly"));

        try {
            Document $ = Jsoup.parse(response);
            Elements tables = $.getElementsByClass("tablepress-id-006");
            for (Element table : tables) {
                //for each table meaning for each week... each week is stored in a table with a classname of tablepress-id-006
                Element parent = table.parent();
                if (!parent.attributes().get("style").trim().toLowerCase().equals("display: none;")) {
                    Elements rows = table.getElementsByTag("tr");

                    for (Element row : rows) {
                        //each row could be either a schedule item or a date for the following schedule items
                        if (row.attributes().get("class").contains("row-2 odd") && !row.equals(rows.get(0))) {
                            if (currentWeek == 0) {
                                for (LeagueScheduleItem i : allData) {
                                    if (i.getGameDate().equals(row.text())) currentWeek = i.getGameWeek();
                                }
                            }
                        }
                    }

                    for (LeagueScheduleItem d : allData) {
                        if (d.getGameWeek() == currentWeek - 1) items.add(d);
                    }
                }
            }
        } catch (Exception e) {
            onError.onError(e.toString());
        }

        items = new ArrayList<>(noDuplicates(items));
        callback.onProcessed(items);
    }

    /**
     * A method to get next week's league schedule
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processNextWeekLeagueSchedule(String response, OnLeagueScheduleDataProcessed callback, OnError onError) {
        if (response.equals("[ERROR!]")) {
            onError.onError("error in response.");
        }

        int currentWeek = 0;
        ArrayList<LeagueScheduleItem> items = new ArrayList<>();

        final ArrayList<LeagueScheduleItem> allData = new ArrayList<>();
        processFetchedLeagueSchedule(response, allData::addAll, error -> onError.onError("Couldn't process all the data correctly"));

        try {
            Document $ = Jsoup.parse(response);
            Elements tables = $.getElementsByClass("tablepress-id-006");
            for (Element table : tables) {
                //for each table meaning for each week... each week is stored in a table with a classname of tablepress-id-006
                Element parent = table.parent();
                if (!parent.attributes().get("style").trim().toLowerCase().equals("display: none;")) {
                    Elements rows = table.getElementsByTag("tr");

                    for (Element row : rows) {
                        //each row could be either a schedule item or a date for the following schedule items
                        if (row.attributes().get("class").contains("row-2 odd") && !row.equals(rows.get(0))) {
                            if (currentWeek == 0) {
                                for (LeagueScheduleItem i : allData) {
                                    if (i.getGameDate().equals(row.text())) currentWeek = i.getGameWeek();
                                }
                            }
                        }
                    }

                    for (LeagueScheduleItem d : allData) {
                        if (d.getGameWeek() == currentWeek + 1) items.add(d);
                    }
                }
            }
        } catch (Exception e) {
            onError.onError(e.toString());
        }

        items = new ArrayList<>(noDuplicates(items));
        callback.onProcessed(items);
    }

    /**
     * A method to get league schedule on a specific week
     * @param week - on which week
     * @param queue - the volley queue to place the requests in
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getLeagueScheduleOfWeek (int week , RequestQueue queue, OnLeagueScheduleDataProcessed callback , OnError onError) {
        if (week <= 0) return;
        ArrayList<LeagueScheduleItem> returnedItems = new ArrayList<>();
        fetchUpdatedLeagueSchedule(queue , raw_data -> processFetchedLeagueSchedule(raw_data , list -> {
            for (LeagueScheduleItem item : list)
                if (item.getGameWeek() == week)
                    returnedItems.add(item);
            callback.onProcessed(returnedItems);
        }, onError), onError);

    }

    /**
     * A method to get all the league schedule
     * @param queue - volley queue to put the request in
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getAllLeagueSchedule (RequestQueue queue, OnLeagueScheduleDataProcessed callback , OnError onError){
        fetchUpdatedLeagueSchedule(queue , raw_data -> processFetchedLeagueSchedule(raw_data , callback, onError), onError);
    }

    /**
     * A method to get this week's league schedule
     * @param queue - volley queue to put the request in
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getThisWeekLeagueSchedule (RequestQueue queue, OnLeagueScheduleDataProcessed callback , OnError onError) {
        fetchUpdatedLeagueSchedule(queue, response -> processThisWeekLeagueSchedule(response , callback, onError), onError);
    }

    /**
     * A method to get last week's league schedule
     * @param queue - volley queue to put the request in
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getLastWeekLeagueSchedule (RequestQueue queue, OnLeagueScheduleDataProcessed callback , OnError onError) {
        fetchUpdatedLeagueSchedule(queue, response -> processLastWeekLeagueSchedule(response , callback, onError), onError);
    }

    /**
     * A method to get next week's league schedule
     * @param queue - volley queue to put the request in
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void getNextWeekLeagueSchedule (RequestQueue queue, OnLeagueScheduleDataProcessed callback , OnError onError) {
        fetchUpdatedLeagueSchedule(queue, response -> processNextWeekLeagueSchedule(response , callback, onError), onError);
    }

    public static void getTeamNextGameInThisWeek (RequestQueue requestQueue, Team team, OnSingleLeagueScheduleDataProcessed callback, OnError onError) {
        getThisWeekLeagueSchedule(requestQueue, items -> {
            boolean found = false;
            for (LeagueScheduleItem item : items) {
                if (item.teamExists(team)) {
                    //The team is in this game, let's check if the game has already took place or not
                    if (item.getGameStatus() == LeagueItemStatus.STATUS_NORMAL){
                        callback.onProcessed(item);
                        found = true;
                    }
                }
            }
            if (!found) onError.onError("Team " + team + " couldn't be found in this week's schedule");
        }, onError);
    }

    public static void getTeamNextGame (RequestQueue requestQueue, Team team, OnSingleLeagueScheduleDataProcessed callback, OnError onError) {
        getAllLeagueSchedule(requestQueue, items -> {
            boolean found = false;
            for (LeagueScheduleItem item : items) {
                if (item.teamExists(team)) {
                    //The team is in this game, let's check if the game has already took place or not
                    if (item.getGameStatus() == LeagueItemStatus.STATUS_NORMAL){
                        callback.onProcessed(item);
                        found = true;
                    }
                }
            }
            if (!found) onError.onError("Team " + team + " couldn't be found in the future schedule list");
        }, onError);
    }
}
