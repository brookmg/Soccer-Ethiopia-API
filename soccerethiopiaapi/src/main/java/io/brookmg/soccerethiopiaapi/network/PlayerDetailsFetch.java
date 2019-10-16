/*
 * Copyright (C) 2019 Brook Mezgebu
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
import io.brookmg.soccerethiopiaapi.data.Player;
import io.brookmg.soccerethiopiaapi.data.Team;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;
import io.brookmg.soccerethiopiaapi.utils.ThreadPoolProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.brookmg.soccerethiopiaapi.utils.Utils.getTeamFromTeamName;

/**
 * Created by BrookMG on 2/12/2019 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
public class PlayerDetailsFetch {

    public interface OnPlayerDetailFetched {
        void onFetched(String response);
    }

    public interface OnPlayerDetailProcessed {
        void onReady(Player player);
    }

    /**
     * A method to fetch the raw data of the player detail from website
     * @param queue - the queue in which the request should belong to
     * @param cache - whether cached data will be returned or not
     * @param player - the player we want to find the detail for
     * @param callback - callback to handle the response of the request
     * @param onError - callback to handle errors
     * @throws IllegalArgumentException - if the player argument was NULL or if it doesn't have a valid playerLink property
     */
    private static void fetchPlayerDetail(RequestQueue queue, boolean cache, Player player, OnPlayerDetailFetched callback, OnError onError) throws IllegalArgumentException{
        if (player == null) throw new IllegalArgumentException("player argument can not be null");
        if (player.getPlayerLink() == null || player.getPlayerLink().isEmpty()) throw new IllegalArgumentException("supplied player should have atleast the link for his detail");

        if (cache) queue.add(new CachedStringRequest(Request.Method.GET, player.getPlayerLink(), callback::onFetched, volleyError -> onError.onError(volleyError.getMessage() != null ? volleyError.getMessage() : "Error while fetching player detail.")));
        else queue.add(new StringRequest(Request.Method.GET, player.getPlayerLink(), callback::onFetched, volleyError -> onError.onError(volleyError.getMessage() != null ? volleyError.getMessage() : "Error while fetching player detail.")));
    }

    /**
     * A method to process the fetched response from the website
     * @param response - the response from the site
     * @param player - the player where the details belong to
     * @param processed - callback to handle the processed player details
     * @param onError - callback to handle errors
     */
    private static void processFetchedPlayerDetail(String response, Player player, OnPlayerDetailProcessed processed, OnError onError) {
        Document $ = Jsoup.parse(response);
        Elements details = $.getElementsByTag("dd");
        Elements detailTitles = $.getElementsByTag("dt");

        for (int detailIterator = 0; detailIterator < detailTitles.size(); detailIterator++) {
            String typeOfDetail = detailTitles.get(detailIterator).text();
            if (typeOfDetail.equalsIgnoreCase("#")) {
                player.setNumber(details.get(detailIterator).text().isEmpty() ? 0 : Integer.parseInt(details.get(detailIterator).text()));
            } else if (typeOfDetail.replace(" ", "").equalsIgnoreCase("Name")
                    || typeOfDetail.replace(" ", "").equalsIgnoreCase("ስም")) {
                player.setFullName(details.get(detailIterator).text());
            } else if (typeOfDetail.replace(" ", "").equalsIgnoreCase("ዜግነት")) {
                player.setCountryCode(details.get(detailIterator).getElementsByTag("img").get(0).attr("alt"));
            } else if (typeOfDetail.replace(" ", "").equalsIgnoreCase("የመጫወቻቦታ")) {
                player.setPlayerPosition(details.get(detailIterator).text());
            } else if (typeOfDetail.replace(" ", "").equalsIgnoreCase("አሁንያለበትክለብ")) {
                player.setCurrentTeam(getTeamFromTeamName(details.get(detailIterator).text()));
            } else if (typeOfDetail.replace(" ", "").equalsIgnoreCase("የቀድሞክለቦች")) {
                String[] teamsNameStrings = details.get(detailIterator).text().split(",");
                ArrayList<Team> teams = new ArrayList<>();
                for (String team : teamsNameStrings) teams.add(getTeamFromTeamName(team));
                player.setPreviousTeams(teams);
            }
        }

        ThreadPoolProvider.getInstance().executeOnMainThread(() -> processed.onReady(player));
    }

    /**
     * A method to serve as the entry point for <h3>Get Player Detail</h3> functionality
     * @param queue - the queue in which the request should belong to
     * @param cache - whether cached data will be returned or not
     * @param player - the player we want to find the detail for
     * @param processed - callback to handle the processed player details
     * @param onError - callback to handle errors
     */
    public static void getPlayerDetail (RequestQueue queue, boolean cache, Player player, OnPlayerDetailProcessed processed, OnError onError) {
        fetchPlayerDetail(queue, cache, player, response -> ThreadPoolProvider.getInstance().execute(() -> processFetchedPlayerDetail(response, player, processed, onError)), onError);
    }

}
