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

package io.brookmg.soccerethiopiaapi.utils;

import io.brookmg.soccerethiopiaapi.data.Team;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by BrookMG on 12/19/2018 in io.brookmg.soccerethiopiaapi.utils
 * inside the project SoccerEthiopia .
 */
@SuppressWarnings("unused")
public class Constants {

    public static final String CLUB_STANDING_BASE_URL = "http://www.soccerethiopia.net/football/table/2018-19-premier-league-standing";
    public static final String PREMIER_LEAGUE_SCHEDULE_BASE_URL = "http://www.soccerethiopia.net/ethpl-2011";
    public enum TEAMS_ID {  ETHIOPIA_BUNA , KIDUS_GORJIS , HAWASSA_KETEMA , SIDAMA_BUNA ,
                            WELWALO , FASIL_KENEMA , BAHIR_DAR_KENEMA , MEKELE_70_ENDERTA ,
                            ADAMA_KETEMA , WELAYETA_DICHA , DIRE_DAWA_KETEMA , MEKELAKEYA ,
                            JIMMA_ABA_JIFFAR , SEHUL_SHERE , DEBUB_POLICE , DEDEBIT }

    /* Current League Session Teams */
    public static final Team ETHIOPIA_BUNA = new Team(TEAMS_ID.ETHIOPIA_BUNA , "ኢትዮጵያ ቡና" , "http://www.soccerethiopia.net/wp-content/uploads/2016/02/Bunna-128x128.png", 1968, "Addis Abeba");
    public static final Team KIDUS_GORJIS = new Team(TEAMS_ID.KIDUS_GORJIS ,"ቅዱስ ጊዮርጊስ", "http://www.soccerethiopia.net/wp-content/uploads/2015/10/KG-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team HAWASSA_KETEMA = new Team(TEAMS_ID.HAWASSA_KETEMA, "ሀዋሳ ከተማ", "http://www.soccerethiopia.net/wp-content/uploads/2016/05/Hawassa-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team SIDAMA_BUNA = new Team(TEAMS_ID.SIDAMA_BUNA , "ሲዳማ ቡና", "http://www.soccerethiopia.net/wp-content/uploads/2016/05/Sidama-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team WELWALO = new Team(TEAMS_ID.WELWALO , "ወልዋሎ ዓ.ዩ.", "http://www.soccerethiopia.net/wp-content/uploads/2016/11/Welwalo-128x128.png" , -1 , "", new ArrayList<>(Arrays.asList("ወልዋሎ ዓ.ዩ." , "ወልዋሎ ዓ/ዩ" , "ወልዋሎ ዓ .ዩ"))); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team FASIL_KENEMA = new Team(TEAMS_ID.FASIL_KENEMA , "ፋሲል ከነማ", "http://www.soccerethiopia.net/wp-content/uploads/2016/05/Fasil-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team BAHIR_DAR_KENEMA = new Team(TEAMS_ID.BAHIR_DAR_KENEMA ,"ባህር ዳር ከተማ", "http://www.soccerethiopia.net/wp-content/uploads/2016/11/BDK-123x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team MEKELE_70_ENDERTA = new Team(TEAMS_ID.MEKELE_70_ENDERTA , "መቐለ 70 እንደርታ", "http://www.soccerethiopia.net/wp-content/uploads/2016/11/MK-105x128.png" , -1 , "" , new ArrayList<>(Arrays.asList("መቐለ 70", "መቐለ 70 እንደርታ" , "መቐለ 70 እ." , "መቐለ ሠ.እ."))); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team ADAMA_KETEMA = new Team(TEAMS_ID.ADAMA_KETEMA , "አዳማ ከተማ", "http://www.soccerethiopia.net/wp-content/uploads/2016/05/Adama-1-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team WELAYETA_DICHA = new Team(TEAMS_ID.WELAYETA_DICHA , "ወላይታ ድቻ", "http://www.soccerethiopia.net/wp-content/uploads/2016/05/Dicha-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team DIRE_DAWA_KETEMA = new Team(TEAMS_ID.DIRE_DAWA_KETEMA , "ድሬዳዋ ከተማ", "http://www.soccerethiopia.net/wp-content/uploads/2016/05/DDK-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team MEKELAKEYA = new Team(TEAMS_ID.MEKELAKEYA , "መከላከያ", "http://www.soccerethiopia.net/wp-content/uploads/2016/02/Mekelakeya-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team JIMMA_ABA_JIFFAR = new Team(TEAMS_ID.JIMMA_ABA_JIFFAR , "ጅማ አባ ጅፋር", "http://www.soccerethiopia.net/wp-content/uploads/2016/11/JAJ-1-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team SEHUL_SHERE = new Team(TEAMS_ID.SEHUL_SHERE , "ስሑል ሽረ", "http://www.soccerethiopia.net/wp-content/uploads/2016/11/Shire-128x118.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team DEBUB_POLICE = new Team(TEAMS_ID.DEBUB_POLICE ,"ደቡብ ፖሊስ", "http://www.soccerethiopia.net/wp-content/uploads/2016/11/dP-125x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!
    public static final Team DEDEBIT = new Team(TEAMS_ID.DEDEBIT , "ደደቢት", "http://www.soccerethiopia.net/wp-content/uploads/2016/02/Dedebit-128x128.png" , -1 , ""); // FIXME: 1/19/2019 CORRECT DETAILS HERE!

    public static final ArrayList<Team> teams = new ArrayList<>(Arrays.asList(Constants.ETHIOPIA_BUNA , Constants.ADAMA_KETEMA , Constants.BAHIR_DAR_KENEMA,
            Constants.DEBUB_POLICE, Constants.DEDEBIT, Constants.DIRE_DAWA_KETEMA, Constants.FASIL_KENEMA, Constants.HAWASSA_KETEMA,
            Constants.JIMMA_ABA_JIFFAR, Constants.KIDUS_GORJIS, Constants.MEKELAKEYA, Constants.MEKELE_70_ENDERTA, Constants.SEHUL_SHERE,
            Constants.SIDAMA_BUNA, Constants.WELAYETA_DICHA, Constants.WELWALO));

}
