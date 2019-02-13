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
import io.brookmg.soccerethiopiaapi.data.Player;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;
import io.brookmg.soccerethiopiaapi.utils.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static io.brookmg.soccerethiopiaapi.utils.Utils.getTeamFromTeamName;

/**
 * Created by BrookMG on 2/11/2019 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
public class TopPlayersFetch {

    public interface OnPlayersListReceived {
        void onReady(ArrayList<Player> players);
    }

    public interface OnPlayersListFetched {
        void onResponse(String response);
    }

    public static void fetchTopPlayers (RequestQueue queue, OnPlayersListFetched fetched, StandingFetch.OnError onError) {
        queue.add(new CachedStringRequest(Request.Method.GET , Constants.TOP_PLAYERS_BASE_URL, fetched::onResponse, volleyError -> onError.onError(volleyError.getMessage())));
    }

    public static void processFetchedTopPlayersList (String response, OnPlayersListReceived listReceived, StandingFetch.OnError onError) {
        ArrayList<Player> players = new ArrayList<>();
        Document $ = Jsoup.parse(response);
        Elements playersTables = $.getElementsByClass("sp-player-list");
        for (Element table : playersTables) {
            Elements rows = table.getElementsByTag("tr");
            rows.remove(0); //the top is just labels
            for (Element row : rows) {
                int playerRank = Integer.parseInt(row.getElementsByClass("data-rank").get(0).text());
                String playerName = row.getElementsByClass("data-name").get(0).text();
                String countryCode3 = row.getElementsByClass("data-name").get(0).getElementsByClass("player-flag").get(0).getElementsByTag("img").get(0).attr("alt");
                String playerLink = row.getElementsByClass("data-name").get(0).getElementsByTag("a").get(0).attr("href");
                String teamName = row.getElementsByClass("data-team").get(0).text();
                Integer playerGoals = Integer.valueOf(row.getElementsByClass("data-goals").get(0).text());
                try {
                    Player player = new Player(playerName, playerLink, playerRank, playerGoals, -1, countryCode3, null, (teamName.equals("-")) ? null : getTeamFromTeamName(teamName));
                    players.add(player);
                } catch (TeamNotFoundException e) {
                    onError.onError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        if (players.size() > 79) players = new ArrayList<>(players.subList(0 , 79));    //items after the index 78 are repeated
        listReceived.onReady(players);
    }

    public static void getTopPlayersList (RequestQueue queue, OnPlayersListReceived listReceived, StandingFetch.OnError onError) {
        fetchTopPlayers(queue, response -> processFetchedTopPlayersList(response, listReceived, onError), onError);
    }

}
