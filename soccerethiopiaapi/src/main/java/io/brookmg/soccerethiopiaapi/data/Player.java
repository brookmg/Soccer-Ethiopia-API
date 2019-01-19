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

package io.brookmg.soccerethiopiaapi.data;

import java.util.ArrayList;

/**
 * Created by BrookMG on 1/19/2019 in io.brookmg.soccerethiopiaapi.data
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings("unused")
public class Player {

    private String fullName;
    private Integer number;
    private String countryCode;
    private String playerPosition;
    private Team currentTeam;
    private ArrayList<Team> previousTeams = new ArrayList<>();

    public Player(String fullName, Integer number, String countryCode, String playerPosition, Team currentTeam) {
        this.fullName = fullName;
        this.number = number;
        this.countryCode = countryCode;
        this.playerPosition = playerPosition;
        this.currentTeam = currentTeam;
    }

    public Player(String fullName, Integer number, String countryCode, String playerPosition, Team currentTeam, ArrayList<Team> previousTeams) {
        this.fullName = fullName;
        this.number = number;
        this.countryCode = countryCode;
        this.playerPosition = playerPosition;
        this.currentTeam = currentTeam;
        this.previousTeams = previousTeams;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(String playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    public ArrayList<Team> getPreviousTeams() {
        return previousTeams;
    }

    public void setPreviousTeams(ArrayList<Team> previousTeams) {
        this.previousTeams = previousTeams;
    }
}
