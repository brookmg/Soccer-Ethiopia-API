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

import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import com.android.volley.toolbox.Volley;
import io.brookmg.soccerethiopiaapi.data.NewsItem;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by BrookMG on 4/26/2019 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
@RunWith(MockitoJUnitRunner.class)
public class NewsFetchTest {

    @Mock
    NewsFetch.OnNewsDataProcessed newsDataProcessed;

    @Mock
    OnError error;

    private void errorOccured(String err) {
        Log.e("ERROR" , err);
    }

    private void onResultReturned(ArrayList<NewsItem> items) {
        Assert.assertTrue(items.size() > 0);
    }

    @Test
    public void fetchLatestNews_givenOnNewsProcessedCallback() {

//        doAnswer(answerVoid(this::onResultReturned)).when(newsDataProcessed).onFinish(Mockito.isA(ArrayList.class));
//        doAnswer(answerVoid(this::errorOccured)).when(error).onError(Mockito.isA(String.class));

        NewsFetch.getLatestNews(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                Volley.newRequestQueue(InstrumentationRegistry.getInstrumentation().getTargetContext()),
                newsDataProcessed,
                error
        );

        verify(newsDataProcessed).onFinish(any(ArrayList.class));
        verifyNoMoreInteractions(error);
    }
}