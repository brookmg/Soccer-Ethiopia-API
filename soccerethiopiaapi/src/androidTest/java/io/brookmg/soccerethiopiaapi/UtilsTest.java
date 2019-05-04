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

package io.brookmg.soccerethiopiaapi;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import io.brookmg.soccerethiopiaapi.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by BrookMG on 2/16/2019 in io.brookmg.soccerethiopiaapi
 * inside the project SoccerEthiopia .
 *
 * Instrumented Test class for Utility class functions
 */
@RunWith(AndroidJUnit4.class)
public class UtilsTest {

    @Test
    public void getCountryName_isCorrect() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String returnedString = Utils.getCountryNameFromISO3(appContext,"eth");
        assertEquals("Ethiopia" , returnedString);
    }

    @Test
    public void getCountryISO3166_1Alpha2_isCorrect() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String returnedString = Utils.getCountryISO2FromISO3(appContext,"eth");
        assertEquals("ET" , returnedString);
    }

}
