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

    public interface OnNewsItemProcessed {
        void onFinish(NewsItem item);
    }

    /**
     * A method to fetch the raw data containing list of news items from the website
     * @param queue - the queue where the request will be placed
     * @param callback - the callback method called when the response is received
     * @param error - the callback method that's going to be called if error occurs
     */
    private static void fetchLatestNews( RequestQueue queue, OnRawNewsDataReceived callback, OnError error) {
        queue.add(new CachedStringRequest(Request.Method.GET, BASE_URL, callback::onReceived, volleyError -> error.onError(volleyError.getMessage())));
    }

    /**
     * A method to fetch the raw data containing the content of the news item provided from the website
     * @param queue - the queue where the request will be placed
     * @param newsItem - the news item that the content fetch will belong to
     * @param callback - the callback method called when the response is received
     * @param error - the callback method that's going to be called if error occurs
     */
    private static void fetchSingleNewsItem(RequestQueue queue, NewsItem newsItem, OnRawNewsDataReceived callback, OnError error) {
        queue.add(new CachedStringRequest(Request.Method.GET , BASE_URL + "/football/" + newsItem.getNewsId(), callback::onReceived, volleyError -> error.onError(volleyError.getMessage())));
    }

    /**
     * A method to parse the raw data from the website to give out list of {@link NewsItem}
     * @param response - the raw data that came from the website
     * @param callback - the callback method that's going to be called after the processing is done
     * @param error - the callback method for handling errors that might occur
     */
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
                String authorLink = item.getElementsByClass("slide-content").get(0).getElementsByClass("author").get(0).getElementsByTag("a").get(0).attr("href");
                String authorName = item.getElementsByClass("slide-content").get(0).getElementsByClass("author").get(0).getElementsByTag("a").get(0).text();
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
                        authorLink,
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
                String authorLink = item.getElementsByClass("article-content").get(0).getElementsByClass("author").get(0)
                        .getElementsByTag("a").get(0).attr("href");
                String authorName = item.getElementsByClass("article-content").get(0).getElementsByClass("author").get(0)
                        .getElementsByTag("a").get(0).text();
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
                            authorLink,
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
                            authorLink,
                            authorName,
                            tags,
                            convertedDatetime
                    );
                    returnable.add(news);
                }

            }

            callback.onFinish(returnable);
        } catch (Exception e) {
            error.onError(e.getMessage() != null ? e.getMessage() : "Error while processing raw news data");
        }
    }

    private static void processFetchedNewsItem (String response, NewsItem item, OnNewsItemProcessed onNewsItemProcessed, OnError error) {
        try {
            Document $ = Jsoup.parse(response);
            Element mainContentDiv = $.getElementsByClass("entry-content").get(0);
            Elements allParagraph = mainContentDiv.getElementsByTag("p");
            StringBuilder contentBuilder = new StringBuilder();
            for (Element paragraph : allParagraph) contentBuilder.append("\t").append(paragraph.text()).append("\n");
            item.setNewsContent(contentBuilder.toString());
            onNewsItemProcessed.onFinish(item);
        } catch (Exception e) {
            e.printStackTrace();
            error.onError(e.getMessage() != null ? e.getMessage() : "Error parsing the news item (" + item.getNewsId() + ")");
        }
    }

    /**
     * public facing method for accessing news items fetching functionality. Basically fetches the raw data from the website
     * process the results and call the callback method with the processed output.
     * @param queue - the queue where the request will be placed in
     * @param callback - the callback method for handling the result. which is list of {@link NewsItem}
     * @param error - the callback method for handling any error
     */
    public static void getLatestNews(RequestQueue queue, OnNewsDataProcessed callback, OnError error) {
        fetchLatestNews(queue, response -> processFetchedNews(response, callback, error), error);
    }

    /**
     * public facing method for accessing single news item content fetching functionality. Basically fetches the raw data from
     * the website process the results and call the callback method with the processed output.
     * @param queue - the queue where the request will be placed in
     * @param item - the news item which contains the id of the news
     * @param callback - the callback method for handling the result. which is list of {@link NewsItem}
     * @param error - the callback method for handling any error
     */
    public static void getNewsItem(RequestQueue queue, NewsItem item, OnNewsItemProcessed callback, OnError error) {
        fetchSingleNewsItem(queue, item, response -> processFetchedNewsItem(response, item, callback, error), error);
    }

}
