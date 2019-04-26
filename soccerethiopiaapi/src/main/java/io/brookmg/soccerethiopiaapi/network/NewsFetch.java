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
import com.android.volley.toolbox.StringRequest;
import io.brookmg.soccerethiopiaapi.data.NewsItem;
import io.brookmg.soccerethiopiaapi.errors.OnError;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private static void fetchLatestNews( RequestQueue queue, OnRawNewsDataReceived callback, OnError error) {
        queue.add(new CachedStringRequest(Request.Method.GET, BASE_URL, callback::onReceived, volleyError -> error.onError(volleyError.getMessage())));
    }

    private static void processFetchedNews(String response, OnNewsDataProcessed callback, OnError error) {
        try {
            ArrayList<NewsItem> returnable = new ArrayList<>();
            Document $ = Jsoup.parse(response);
            Elements singleSlide = $.getElementsByClass("single-slide");
            Elements singleArticle = $.getElementsByClass("single-article clearfix");

            for (Element item : singleSlide) {
                String title = item.getElementsByClass("slide-content").get(0).getElementsByTag("h3").get(0).text();
                String image = item.getElementsByTag("figure").get(0).getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
                String link = item.getElementsByTag("figure").get(0).getElementsByTag("a").get(0).attr("href");
                String authorName = item.getElementsByClass("slide-content").get(0).getElementsByClass("author").get(0).getElementsByTag("a").get(0).attr("href");
                String datetime = item.getElementsByClass("slide-content").get(0).getElementsByClass("published").get(0).attr("datetime");

                ArrayList<String> tags = new ArrayList<>();
                Elements tagHolders = item.getElementsByClass("slide-content").get(0).getElementsByClass("above-entry-meta").get(0).getElementsByClass("cat-links").get(0).getElementsByTag("a");

                for (Element tagItem : tagHolders) {
                    tags.add(tagItem.text());
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                Date convertedDatetime = format.parse(datetime);

                NewsItem news = new NewsItem(
                        Integer.parseInt(link.replace("http://www.soccerethiopia.net/football/", "")),
                        image,
                        title,
                        authorName,
                        tags,
                        convertedDatetime
                );
                returnable.add(news);
            }

            for (Element item : singleArticle) {
                String title = item.getElementsByClass("article-content").get(0).getElementsByTag("h3").get(0).text();
                String image = item.getElementsByTag("figure").get(0).getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
                String link = item.getElementsByTag("figure").get(0).getElementsByTag("a").get(0).attr("href");
                String authorName = item.getElementsByClass("article-content").get(0).getElementsByClass("author").get(0)
                        .getElementsByTag("a").get(0).attr("href");
                String datetime = item.getElementsByClass("article-content").get(0).getElementsByClass("published").get(0)
                        .attr("datetime");

                ArrayList<String> tags = new ArrayList<>();
                Elements tagHolders = item.getElementsByClass("article-content").get(0).getElementsByClass("above-entry-meta").get(0).getElementsByClass("cat-links").get(0).getElementsByTag("a");

                for (Element tagItem : tagHolders) {
                    tags.add(tagItem.text());
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                Date convertedDatetime = format.parse(datetime);

                if (item.getElementsByClass("article-content").get(0).getElementsByClass("entry-content").size() > 0) {
                    //This news has a content attached
                    NewsItem news = new NewsItem(
                            Integer.parseInt(link.replace("http://www.soccerethiopia.net/football/", "")),
                            image,
                            title,
                            authorName,
                            tags,
                            convertedDatetime,
                            item.getElementsByClass("article-content").get(0).getElementsByClass("entry-content").get(0).text()
                    );
                    returnable.add(news);
                } else {
                    NewsItem news = new NewsItem(
                            Integer.parseInt(link.replace("http://www.soccerethiopia.net/football/", "")),
                            image,
                            title,
                            authorName,
                            tags,
                            convertedDatetime
                    );
                    returnable.add(news);
                }

            }

            callback.onFinish(returnable);
        } catch (Exception e) {
            error.onError(e.getMessage() != null ? e.getMessage() : "Error while fetching news");
        }
    }

    public static void getLatestNews(RequestQueue queue, OnNewsDataProcessed callback, OnError error) {
        fetchLatestNews(queue, response -> processFetchedNews(response, callback, error), error);
    }

}
