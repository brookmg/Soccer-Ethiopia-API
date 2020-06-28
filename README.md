## This project is no longer maintained! Checkout [the node-js implementation](https://github.com/brookmg/soccer-ethiopia-api-node).

<p align="center">
	<img src="https://github.com/brookmg/Soccer-Ethiopia-API/blob/master/soccer_ethiopia_api.png?raw=true" alt="Soccer Ethiopia" /><br>
	<h1 align="center"> Soccer Ethiopia API </h1>
	<p align="center">
		<a href="https://jitpack.io/#brookmg/Soccer-Ethiopia-API"><img src="https://jitpack.io/v/brookmg/Soccer-Ethiopia-API.svg" alt="Current Version" /></a>
		<a href="https://circleci.com/gh/brookmg/Soccer-Ethiopia-API/tree/master"><img src="https://circleci.com/gh/brookmg/Soccer-Ethiopia-API/tree/master.svg?style=svg" alt="CircleCI" /></a>
		<a href="https://app.codacy.com/app/brookmg/Soccer-Ethiopia-API?utm_source=github.com&utm_medium=referral&utm_content=brookmg/Soccer-Ethiopia-API&utm_campaign=Badge_Grade_Dashboard"><img src="https://api.codacy.com/project/badge/Grade/9a865b7dc8124bed9d1476e6ed331a2a" alt="Codacy Badge" /></a></p><h4 align="center"> This is an Android api that serve the latest Ethiopian premier league standing data </h4></p>

#### The data is fetched from [Soccer Ethiopia](http://soccerethiopia.net). And this is an unoffical api.

follow the following steps to add the dependency to your app:

* make sure to add jitpack to your repositories

```gradle 
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

* implement this lib

```gradle 
    dependencies {
        implementation 'com.github.brookmg:Soccer-Ethiopia-API:0.6.0'
    }
```

Samples
-------

* Initialising the library `With Caching`
```java
    SoccerEthiopiaApi apiEntry = new SoccerEthiopiaApi(getApplicationContext()); //recommended to use application context
```

* Initialising the library `Without Caching`
```java
    SoccerEthiopiaApi apiEntry = new SoccerEthiopiaApi(getApplicationContext(), false); //recommended to use application context
```

* And simply fetch the latest data from your activity or fragment like:
```java
    apiEntry.getLatestTeamRanking(ranking -> {
            for (RankItem item : ranking) {
                Log.v("data" , item.getTeam().getTeamFullName() + ", " + item.getTeam().getTeamLogo() + ", " + item.getRank()
                        + ", " + item.getPlayedGames() + ", " + item.getWonGames() + ", " + item.getDrawGames() 
                        + ", " + item.getLostGames()
                );
            }
        }, 
        error -> Log.e("Error" , error)
    );
```

* Also you can get the league schedule data like:
```java
    apiEntry.getLeagueSchedule(scheduleItems -> {
        for (LeagueScheduleItem item : scheduleItems) {
            Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + 
            " | " + item.getGameDate() + " | " + item.getGameStatus());
        }
    }, error -> Log.e("Error_League" , error));
```

* Or for a specific game week (in this case the 5th game week):
```java
    apiEntry.getLeagueSchedule( 5, scheduleItems -> {
        for (LeagueScheduleItem item : scheduleItems) {
            Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + 
            " | " + item.getGameDate() + " | " + item.getGameStatus());
        }
    }, error -> Log.e("Error_League" , error));
```

* You can fetch last week's, this week's and next week's league schedule too.
```java
    apiEntry.getThisWeekLeagueSchedule(  //THIS WEEK'S
        scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_this_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        },
        error -> Log.e("Error_League" , error)
    );

    apiEntry.getLastWeekLeagueSchedule(  //LAST WEEK'S
        scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_last_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        },
        error -> Log.e("Error_League" , error)
    );

    apiEntry.getNextWeekLeagueSchedule(  //NEXT WEEK'S
        scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_next_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        },
        error -> Log.e("Error_League" , error)
    );
```

* You can also get detail about a specific team like the following.
```java
   apiEntry.getTeamDetail(Constants.ADAMA_KETEMA , team -> Log.v("data_team_detail" ,
        team.toString()),
        error -> Log.e("Error_Team" , error)
   );
```

* For getting the latest top players in the league
```java
    apiEntry.getTopPlayers(
        players -> Log.v("players" , Arrays.toString(players.toArray())),
        error -> Log.e("players_error", error)
    );
```

* To get the top players and then to get player detail
```java
    apiEntry.getTopPlayers(
        players -> {
            if (players.size() > 0) {
                apiEntry.getPlayerDetail(	//This part is used to get the player's detail
                    players.get(0),
                    player -> {
                        if (!player.getCurrentTeam().isComplete()) {
                            apiEntry.getTeamDetail(player.getCurrentTeam(), player::setCurrentTeam, error -> {});
                        }
                        Log.v("player_detailed", player.toString());
                    },
                    error -> Log.e("player_detailed" , error)
                );
            }
        },
        error -> Log.e("players_error", error)
    );
``` 

* To fetch latest sport related news using this api
```java
    apiEntry.getLatestNews(news -> {
        for (NewsItem item : news) Log.v("news_fetch", item.toString());
    }, error -> Log.e("news_fetch", error));
```

* To fetch the content of a specific news item
```java
    apiEntry.getLatestNews(news -> 
        apiEntry.getNewsItemContent(
                news.get(0),
                newsWithContent -> Log.v("news_item", newsWithContent.toString()),
                error -> Log.e("news_item", error)
        ),
        error -> Log.e("news_fetch", error)
    );
```

## Features in this lib:
- [x] Latest teams' standing data
- [x] League schedule
- [x] Team details
- [x] Player details
- [x] Top players list
- [x] News with content
- [x] Do parsing on a different thread
- [ ] Make the api lifecycle aware

#### make sure you have enabled java8 in your project
 
```gradle
    android {
        ...
        
        compileOptions {
            sourceCompatibility = '1.8'
            targetCompatibility = '1.8'
        }
    }
```

## License

```
Copyright (C) 2019 Brook Mezgebu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
