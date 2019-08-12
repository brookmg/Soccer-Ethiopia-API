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

package io.brookmg.soccerethiopiaapi.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by BrookMG on 8/5/2019 in io.brookmg.soccerethiopiaapi.data
 * inside the project Kuwas .
 */
public class NewsItemV2 implements Serializable {

    @SerializedName("id") private int newsId;
    @SerializedName("featured_media") private Integer newsMediaId;
    @SerializedName("title") private Rendered newsTitle;
    @SerializedName("author") private Integer authorId;
    @SerializedName("categories") private ArrayList<Integer> newsTags;
    @SerializedName("date") private String newsPublishedOn;
    @SerializedName("content") private Rendered newsContent;
    private Date newsPublishedOnDate;

    public static class Rendered implements Serializable {
        @SerializedName("rendered") String rendered;

        public Rendered(String rendered) {
            this.rendered = rendered;
        }

        public String getRendered() {
            return rendered;
        }
    }

    public NewsItemV2(int newsId, Integer newsMediaId, Rendered newsTitle, Integer authorId, ArrayList<Integer> newsTags, String newsPublishedOn, Rendered newsContent) {
        this.newsId = newsId;
        this.newsMediaId = newsMediaId;
        this.newsTitle = newsTitle;
        this.authorId = authorId;
        this.newsTags = newsTags;
        this.newsPublishedOn = newsPublishedOn;
        this.newsContent = newsContent;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public Integer getNewsMediaId() {
        return newsMediaId;
    }

    public void setNewsMediaId(Integer newsMediaId) {
        this.newsMediaId = newsMediaId;
    }

    public Rendered getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(Rendered newsTitle) {
        this.newsTitle = newsTitle;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public ArrayList<Integer> getNewsTags() {
        return newsTags;
    }

    public void setNewsTags(ArrayList<Integer> newsTags) {
        this.newsTags = newsTags;
    }

    public String getNewsPublishedOn() {
        return newsPublishedOn;
    }

    public void setNewsPublishedOn(String newsPublishedOn) {
        this.newsPublishedOn = newsPublishedOn;
    }

    public Rendered getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(Rendered newsContent) {
        this.newsContent = newsContent;
    }

    public Date getNewsPublishedOnDate() {
        return newsPublishedOnDate;
    }

    public void setNewsPublishedOnDate(Date newsPublishedOnDate) {
        this.newsPublishedOnDate = newsPublishedOnDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "[ID: " + newsId + ", Image: " + newsMediaId + ", Title: " + newsTitle +
                ", Author: " + authorId + ", Published: " +
                newsPublishedOn + ", Content: " + newsContent +
                ", Tags: " + Arrays.toString(newsTags.toArray()) + "]";
    }

}
