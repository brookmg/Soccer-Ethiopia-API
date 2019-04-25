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
import io.brookmg.soccerethiopiaapi.data.NewsItem;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import static io.brookmg.soccerethiopiaapi.utils.Constants.BASE_URL;

/**
 * Created by BrookMG on 4/24/2019 in io.brookmg.soccerethiopiaapi.network
 * inside the project SoccerEthiopia .
 */
public class NewsFetch {

    public interface OnRawNewsDataReceived {
        void onReceived(String response);
    }

    public interface OnNewsDataProcessed {
        void onFinish(ArrayList<NewsItem> items);
    }

    private static void fetchLatestNews(RequestQueue queue, OnRawNewsDataReceived callback, OnError error) {
        queue.add(new CachedStringRequest(Request.Method.GET, BASE_URL, callback::onReceived, volleyError -> error.onError(volleyError.getMessage())));
    }

    private static void processFetchedNews(String response, OnNewsDataProcessed callback, OnError error) {

    }

    public static void getLatestNews(RequestQueue queue, OnNewsDataProcessed callback, OnError error) {
        fetchLatestNews(queue, response -> processFetchedNews(response, callback, error), error);
    }

}
