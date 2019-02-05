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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import io.brookmg.soccerethiopiaapi.data.Team;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;
import io.brookmg.soccerethiopiaapi.utils.DummyResponses;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;

import static io.brookmg.soccerethiopiaapi.utils.Utils.getTeamFromTeamName;

/**
 * Created by BrookMG on 1/31/2019 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
public class TeamDetailsFetch {

    /**
     * Interface to serve as a callback function for {@link StandingFetch#fetchLatestStandingData(RequestQueue, StandingFetch.OnRawStandingDataFetched, StandingFetch.OnError)}
     */
    public interface OnRawTeamDataFetched {
        void onResponse(String response);
    }

    public interface OnTeamDetailReady {
        void ready(Team detail);
    }

    public static void getCompleteDetail (Context context, Team of, RequestQueue queue, OnTeamDetailReady onTeamDetailReady, StandingFetch.OnError onError) throws IllegalArgumentException {
        if (of == null) throw new IllegalArgumentException("Provided team data can't be null");
        if (of.getTeamLink() == null || of.getTeamLink().isEmpty()) throw new IllegalArgumentException("Provided team data should have link to the team detail");
    }

    public static void getCompleteDetail (Context context, String of, RequestQueue queue, OnTeamDetailReady onTeamDetailReady, StandingFetch.OnError onError) throws TeamNotFoundException{
        getCompleteDetail(context, getTeamFromTeamName(of), queue, onTeamDetailReady, onError);
    }

    private static void processTeamDetail (String usingResponse, Team incompleteTeamDetail, OnTeamDetailReady onTeamDetailReady, StandingFetch.OnError onError) {
        Document $ = Jsoup.parse(usingResponse);
        Elements tables = $.getElementsByClass("tablepress-id-006");
        Element actualTableNeeded = null;

        //eliminate other stuff
        for (Element tableItem : tables) {
            if (tableItem.getElementsByTag("tr").get(0).text().equals("ፕሮፋይል")) {
                actualTableNeeded = tableItem;
                break;
            }
        }

        if (actualTableNeeded == null) {
            onError.onError("Problem while parsing the web data");
        } else {
            String teamFullName = actualTableNeeded.getElementsByTag("tr").get(1).getElementsByTag("td").get(2).text();
            Integer started = Integer.parseInt(actualTableNeeded.getElementsByTag("tr").get(2).getElementsByTag("td").get(2).text());
            String fromCity = actualTableNeeded.getElementsByTag("tr").get(3).getElementsByTag("td").get(2).text();
            ArrayList<String> previousNames = new ArrayList<>(Arrays.asList(actualTableNeeded.getElementsByTag("tr").get(4).getElementsByTag("td").get(2).text().split("\n")));
            String stadium = actualTableNeeded.getElementsByTag("tr").get(5).getElementsByTag("td").get(2).text();

            String president = actualTableNeeded.getElementsByTag("tr").get(7).getElementsByTag("td").get(2).text();
            String vicePresident = actualTableNeeded.getElementsByTag("tr").get(8).getElementsByTag("td").get(2).text();
            String manager = actualTableNeeded.getElementsByTag("tr").get(9).getElementsByTag("td").get(2).text();

            String mainCoach = actualTableNeeded.getElementsByTag("tr").get(11).getElementsByTag("td").get(2).text();
            String viceCoach = actualTableNeeded.getElementsByTag("tr").get(12).getElementsByTag("td").get(2).text();
            String techniq = actualTableNeeded.getElementsByTag("tr").get(13).getElementsByTag("td").get(2).text();
            String goalKeeper = actualTableNeeded.getElementsByTag("tr").get(14).getElementsByTag("td").get(2).text();
            String teamLeader = actualTableNeeded.getElementsByTag("tr").get(15).getElementsByTag("td").get(2).text();
            String nurse = actualTableNeeded.getElementsByTag("tr").get(16).getElementsByTag("td").get(2).text();

            incompleteTeamDetail.setFromCity(fromCity);
            incompleteTeamDetail.setGoalKeeper(goalKeeper);
            incompleteTeamDetail.setInitYear(started);
            incompleteTeamDetail.setMainCoach(mainCoach);
            incompleteTeamDetail.setManager(manager);
            incompleteTeamDetail.setPresident(president);
            incompleteTeamDetail.setPreviousNames(previousNames);
            incompleteTeamDetail.setViceCoach(viceCoach);
            incompleteTeamDetail.setVicePresident(vicePresident);
            incompleteTeamDetail.setTechniqueDirector(techniq);
            incompleteTeamDetail.setTeamAlpha(teamLeader);
            incompleteTeamDetail.setTeamNurse(nurse);
            incompleteTeamDetail.setStadium(stadium);
            incompleteTeamDetail.setTeamFullName(teamFullName);

            onTeamDetailReady.ready(incompleteTeamDetail);  //which is now complete

        }


}
