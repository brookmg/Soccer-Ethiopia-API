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
import io.brookmg.soccerethiopiaapi.data.Team;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;
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

    public interface OnTeamDetailReady {
        void ready(Team detail);
    }

    public static void getCompleteDetail (Team of, RequestQueue queue, OnTeamDetailReady onTeamDetailReady, StandingFetch.OnError onError) throws IllegalArgumentException {
        if (of == null) throw new IllegalArgumentException("Provided team data can't be null");
        if (of.getTeamLink() == null || of.getTeamLink().isEmpty()) throw new IllegalArgumentException("Provided team data should have link to the team detail");

        queue.add(new CachedStringRequest(Request.Method.GET, of.getTeamLink(), response -> processTeamDetail (response, of, onTeamDetailReady, onError), error -> onError.onError(error.getMessage())));
    }

    public static void getCompleteDetail (String of, RequestQueue queue, OnTeamDetailReady onTeamDetailReady, StandingFetch.OnError onError) throws TeamNotFoundException{
        getCompleteDetail(getTeamFromTeamName(of), queue, onTeamDetailReady, onError);
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
            Elements baseToWorkOn = actualTableNeeded.getElementsByTag("tr");

            for (Element row : baseToWorkOn) {
                if (row.getElementsByTag("td").size() == 3) {
                    //This looks fruitful
                    extractDataFromTableData (row.getElementsByTag("td") , incompleteTeamDetail);
                }
            }

            onTeamDetailReady.ready(incompleteTeamDetail);  //which is now complete

        }

    }

    private static void extractDataFromTableData(Elements tds, Team workOn) {
        String trimmedString = tds.get(0).text().replace(" " , "");
        switch (trimmedString){
            case "ሙሉስም": {
                workOn.setTeamFullName(tds.get(2).text());
                break;
            }

            case "ተመሰረተ": {
                workOn.setInitYear(Integer.parseInt(tds.get(2).text()));
                break;
            }

            case "መቀመጫከተማ": {
                workOn.setFromCity(tds.get(2).text());
                break;
            }

            case "ቀደምትስያሜዎች": {
                workOn.setPreviousNames(new ArrayList<>(Arrays.asList(tds.get(2).html().split("<br>"))));
                break;
            }

            case "ስታድየም": {
                workOn.setStadium(tds.get(2).text());
                break;
            }

            case "ፕሬዝዳንት": {
                workOn.setPresident(tds.get(2).text());
                break;
            }

            case "ም/ፕሬዝዳንት": {
                workOn.setVicePresident(tds.get(2).text());
                break;
            }

            case "ስራአስኪያጅ": {
                workOn.setManager(tds.get(2).text());
                break;
            }

            case "ዋናአሰልጣኝ": {
                workOn.setMainCoach(tds.get(2).text());
                break;
            }

            case "ረዳትአሰልጣኝ": {
                workOn.setViceCoach(tds.get(2).text());
                break;
            }

            case "ቴክኒክዳ.": {
                workOn.setTechniqueDirector(tds.get(2).text());
                break;
            }

            case "የግብጠባቂዎች": {
                workOn.setGoalKeeper(tds.get(2).text());
                break;
            }

            case "ቡድንመሪ": {
                workOn.setTeamAlpha(tds.get(2).text());
                break;
            }

            case "ወጌሻ": {
                workOn.setTeamNurse(tds.get(2).text());
                break;
            }
        }
    }

}
