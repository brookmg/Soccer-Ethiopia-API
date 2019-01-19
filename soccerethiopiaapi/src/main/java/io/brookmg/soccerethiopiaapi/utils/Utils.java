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

package io.brookmg.soccerethiopiaapi.utils;

import io.brookmg.soccerethiopiaapi.data.Team;
import io.brookmg.soccerethiopiaapi.errors.TeamNotFoundException;

import static io.brookmg.soccerethiopiaapi.utils.Constants.teams;

/**
 * Created by BrookMG on 1/19/2019 in io.brookmg.soccerethiopiaapi.utils
 * inside the project SoccerEthiopia .
 */
public class Utils {

    public static Team getTeamFromTeamName (String teamName) throws TeamNotFoundException {
        for (Team team : teams) {
            if (team.getTeamFullName().equals(teamName)) return team;
        }
        throw new TeamNotFoundException("Team " + teamName + " is not found in our current data-set.");
    }

}
