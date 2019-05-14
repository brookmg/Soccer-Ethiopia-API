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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import io.brookmg.soccerethiopiaapi.data.Player;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

        if (cache) queue.add(new CachedStringRequest(Request.Method.GET, player.getPlayerLink(), callback::onFetched, volleyError -> onError.onError(volleyError.getMessage())));
        else queue.add(new StringRequest(Request.Method.GET, player.getPlayerLink(), callback::onFetched, volleyError -> onError.onError(volleyError.getMessage())));
    }

    /**
     * A method to process the fetched response from the website
     * @param response - the response from the site
     * @param player - the player where the details belong to
     * @param processed - callback to handle the processed player details
     * @param onError - callback to handle errors
     */
    private static void processFetchedPlayerDetail(String response, Player player, OnPlayerDetailProcessed processed, OnError onError) {
        try {
            Document $ = Jsoup.parse(response);
            Elements details = $.getElementsByTag("dd");
            player.setNumber(Integer.parseInt(details.get(0).text()));
            player.setFullName(details.get(1).text());
            player.setCountryCode(details.get(2).getElementsByTag("img").get(0).attr("alt"));
            player.setPlayerPosition(details.get(3).text());
            player.setCurrentTeam(getTeamFromTeamName(details.get(5).text()));
            processed.onReady(player);
        } catch (TeamNotFoundException e) {
            onError.onError(e.getMessage());
        }
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
        fetchPlayerDetail(queue, cache, player, response -> processFetchedPlayerDetail(response, player, processed, onError), onError);
    }

}
