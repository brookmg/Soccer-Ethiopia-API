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
import io.brookmg.soccerethiopiaapi.data.RankItem;
import io.brookmg.soccerethiopiaapi.utils.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by BrookMG on 12/19/2018 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings("unused")
public class StandingFetch {

    /**
     * Interface to serve as a callback function for {@link StandingFetch#fetchLatestStandingData(RequestQueue, OnRawStandingDataFetched, OnError)}
     */
    public interface OnRawStandingDataFetched {
        void onResponse(String response);
    }

    /**
     * Interface to serve as a callback function for {@link StandingFetch#processFetchedStandingHTML(String, OnStandingDataProcessed, OnError)}
     */
    public interface OnStandingDataProcessed {
        void onFinish(ArrayList<RankItem> ranking);
    }

    /**
     * Interface for errors
     */
    public interface OnError {
        void onError(String error);
    }

    /**
     * A function to fetch the latest standing status of football teams from online
     * @param queue - The Volley queue to work on
     * @param callback - The callback to call when response is returned
     * @param onError - callback function for error handling
     */
    public static void fetchLatestStandingData(RequestQueue queue, OnRawStandingDataFetched callback, OnError onError) {
        queue.add(new StringRequest(Request.Method.GET , Constants.CLUB_STANDING_BASE_URL, callback::onResponse , error -> onError.onError(error.toString())));
    }

    /**
     * A function to process the data returned from the website
     * @param responseFromSite - Raw data
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processFetchedStandingHTML (String responseFromSite , OnStandingDataProcessed callback, OnError onError) {
        if (responseFromSite.startsWith("[ERROR!]")) {
            onError.onError("error in response.");
            return;
        }

        Document $ = Jsoup.parse(responseFromSite);
        Element mainTable = $.getElementsByClass("sp-league-table").get(0);
        Elements tableRows = mainTable.getElementsByTag("tr");
        tableRows.remove(0);    //The first row is just a header

        ArrayList<RankItem> ranking = new ArrayList<>();

        try {
            for (Element item : tableRows) {
                ranking.add(new RankItem(
                        Integer.parseInt(item.getElementsByTag("td").get(0).text()),
                        item.getElementsByTag("td").get(1).getElementsByTag("a").get(0)
                                .getElementsByTag("img").get(0)
                                .attributes().get("src"),
                        item.getElementsByTag("td").get(1).text(),
                        Integer.parseInt(item.getElementsByTag("td").get(9).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(2).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(3).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(4).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(5).text())
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError.onError(e.toString());
        }
        callback.onFinish(ranking);

    }

    /**
     * A function to process the data returned from the website
     * @param responseFromSite - Raw data
     * @param moreDetailed - whether the returned data contains more details (like goalScored , goalAgainst & goalDifference) or not
     * @param callback - callback function to call when all is done
     * @param onError - callback function for error handling
     */
    public static void processFetchedStandingHTML (String responseFromSite , boolean moreDetailed, OnStandingDataProcessed callback, OnError onError) {
        if (responseFromSite.startsWith("[ERROR!]")) {
            onError.onError("error in response.");
            return;
        }

        if (!moreDetailed) {
            processFetchedStandingHTML(responseFromSite, callback, onError);
            return;
        }

        Document $ = Jsoup.parse(responseFromSite);
        Element mainTable = $.getElementsByClass("sp-league-table").get(0);
        Elements tableRows = mainTable.getElementsByTag("tr");
        tableRows.remove(0);    //The first row is just a header

        ArrayList<RankItem> ranking = new ArrayList<>();

        try {
            for (Element item : tableRows) {
                ranking.add(new RankItem(
                        Integer.parseInt(item.getElementsByTag("td").get(0).text()),
                        item.getElementsByTag("td").get(1).getElementsByTag("a").get(0)
                                .getElementsByTag("img").get(0)
                                .attributes().get("src"),
                        item.getElementsByTag("td").get(1).text(),
                        Integer.parseInt(item.getElementsByTag("td").get(9).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(2).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(3).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(4).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(5).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(6).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(7).text()),
                        Integer.parseInt(item.getElementsByTag("td").get(8).text())
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError.onError(e.toString());
        }
        callback.onFinish(ranking);
    }

}
