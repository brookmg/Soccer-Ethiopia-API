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

import android.content.Context;
import io.brookmg.soccerethiopiaapi.data.Player;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;
import io.brookmg.soccerethiopiaapi.utils.DummyResponses;
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

    private static void fetchPlayerDetail(Context context, Player player, OnPlayerDetailFetched callback, StandingFetch.OnError onError) throws IllegalArgumentException{
        if (player == null) throw new IllegalArgumentException("player argument can not be null");
        if (player.getPlayerLink() == null || player.getPlayerLink().isEmpty()) throw new IllegalArgumentException("supplied player should have atleast the link for his detail");

        callback.onFetched(DummyResponses.getPlayerDetailResponse(context));
    }

    private static void processFetchedPlayerDetail(String response, Player player, OnPlayerDetailProcessed processed, StandingFetch.OnError onError) {
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

    public static void getPlayerDetail (Context context, Player player, OnPlayerDetailProcessed processed, StandingFetch.OnError onError) {
        fetchPlayerDetail(context, player, response -> processFetchedPlayerDetail(response, player, processed, onError), onError);
    }

}
