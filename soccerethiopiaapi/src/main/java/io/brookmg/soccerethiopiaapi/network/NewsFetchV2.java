/*
 * Copyright (C) 2019 Brook Mezgebu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.brookmg.soccerethiopiaapi.network;

import androidx.test.annotation.Beta;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.brookmg.soccerethiopiaapi.data.NewsItemV2;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import io.brookmg.soccerethiopiaapi.utils.ThreadPoolProvider;

import static io.brookmg.soccerethiopiaapi.utils.Constants.AUTHOR_URL;
import static io.brookmg.soccerethiopiaapi.utils.Constants.BASE_URL;
import static io.brookmg.soccerethiopiaapi.utils.Constants.CATEGORY_URL;
import static io.brookmg.soccerethiopiaapi.utils.Constants.MEDIA_URL;
import static io.brookmg.soccerethiopiaapi.utils.Constants.NEWS_URL;

/**
 * Created by BrookMG on 8/5/2019 in io.brookmg.soccerethiopiaapi.network
 * inside the project Kuwas .
 */
@Beta
public class NewsFetchV2 {

    public interface OnRawNewsDataReceived {
        void onReceived(String response);
    }

    public interface OnNewsDataProcessed {
        void onFinish(List<NewsItemV2> items);
    }

    public interface OnNewsItemV2Processed {
        void onFinish(NewsItemV2 item);
    }

    /**
     * A method to fetch the raw data containing list of news items from the website
     * @param queue - the queue where the request will be placed
     * @param cache - whether cached data will be returned or not
     * @param callback - the callback method called when the response is received
     * @param error - the callback method that's going to be called if error occurs
     */
    private static void fetchLatestNews(RequestQueue queue, boolean cache, OnRawNewsDataReceived callback, OnError error) {
        if (cache) queue.add(new CachedStringRequest(Request.Method.GET, NEWS_URL, callback::onReceived, volleyError -> error.onError(volleyError.getMessage())));
        else queue.add(new StringRequest(Request.Method.GET, NEWS_URL, callback::onReceived, volleyError -> error.onError(volleyError.getMessage())));
    }

    /**
     * A method to fetch the raw data containing the content of the news item provided from the website
     * @param queue - the queue where the request will be placed
     * @param newsItemV2 - the news item that the content fetch will belong to
     * @param callback - the callback method called when the response is received
     * @param error - the callback method that's going to be called if error occurs
     */
    private static void fetchSingleNewsItemV2(RequestQueue queue, boolean cache, NewsItemV2 newsItemV2, OnRawNewsDataReceived callback, OnError error) {
        if (cache) queue.add(new CachedStringRequest(Request.Method.GET , BASE_URL + "/football/" + newsItemV2.getNewsId(), callback::onReceived, volleyError -> error.onError(volleyError.getMessage() != null ? volleyError.getMessage() : "Error while fetching news.")));
        else queue.add(new StringRequest(Request.Method.GET , BASE_URL + "/football/" + newsItemV2.getNewsId(), callback::onReceived, volleyError -> error.onError(volleyError.getMessage() != null ? volleyError.getMessage() : "Error while fetching news.")));
    }

    /**
     * A method to parse the raw data from the website to give out list of {@link NewsItemV2}
     * @param response - the raw data that came from the website
     * @param callback - the callback method that's going to be called after the processing is done
     * @param error - the callback method for handling errors that might occur
     */
    private static void processFetchedNews(String response, OnNewsDataProcessed callback, OnError error) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            List<NewsItemV2> returnable = Arrays.asList(new GsonBuilder().create().fromJson(response, NewsItemV2[].class));
            for (NewsItemV2 item : returnable) item.setNewsPublishedOnDate(format.parse(item.getNewsPublishedOn()));
            callback.onFinish(returnable);
        } catch (Exception e) {
            error.onError(e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    public static void getAuthorName (RequestQueue queue, int authorId, OnRawNewsDataReceived onAuthorReceived) {
        queue.add(new CachedStringRequest(Request.Method.GET ,
                AUTHOR_URL + "/" + authorId,
                author -> onAuthorReceived.onReceived(new JsonParser().parse(author).getAsJsonObject().get("name").getAsString()),
                error -> onAuthorReceived.onReceived("Unknown") ,
                5 * 24 * 60 * 60 * 1000, //( These information rarely change )
                10 * 24 * 60 * 60 * 1000)); //( These information rarely change )
    }

    public static void getCategoryName (RequestQueue queue, int categoryId, OnRawNewsDataReceived onCategoryReceived) {
        queue.add(new CachedStringRequest(Request.Method.GET ,
                CATEGORY_URL + "/" + categoryId,
                author -> onCategoryReceived.onReceived(new JsonParser().parse(author).getAsJsonObject().get("name").getAsString()),
                error -> onCategoryReceived.onReceived("Unknown") ,
                5 * 24 * 60 * 60 * 1000,    // 5 days ( These information rarely change )
                10 * 24 * 60 * 60 * 1000));         // 10 days ( These information rarely change )
    }

    public static void getImageMediaLink (RequestQueue queue, int mediaId, OnRawNewsDataReceived onCategoryReceived) {
        queue.add(new CachedStringRequest(Request.Method.GET ,
                MEDIA_URL + "/" + mediaId,
                author -> onCategoryReceived.onReceived(new JsonParser().parse(author).getAsJsonObject()
                        .get("guid").getAsJsonObject().get("rendered").getAsString()),
                error -> onCategoryReceived.onReceived("Unknown") ,
                5 * 24 * 60 * 60 * 1000,    // 5 days ( These information rarely change )
                10 * 24 * 60 * 60 * 1000));         // 10 days ( These information rarely change )
    }


    /**
     * public facing method for accessing news items fetching functionality. Basically fetches the raw data from the website
     * process the results and call the callback method with the processed output.
     * @param queue - the queue where the request will be placed in
     * @param cache - if cached data will be returned or not
     * @param callback - the callback method for handling the result. which is list of {@link NewsItemV2}
     * @param error - the callback method for handling any error
     */
    public static void getLatestNews(RequestQueue queue, boolean cache, OnNewsDataProcessed callback, OnError error) {
        fetchLatestNews(queue, cache, response -> ThreadPoolProvider.getInstance().execute(() -> processFetchedNews(response, callback, error)), error);
    }
}
