# Soccer Ethiopia API
[![](https://jitpack.io/v/brookmg/Soccer-Ethiopia-API.svg)](https://jitpack.io/#brookmg/Soccer-Ethiopia-API)	[![CircleCI](https://circleci.com/gh/brookmg/Soccer-Ethiopia-API/tree/master.svg?style=svg)](https://circleci.com/gh/brookmg/Soccer-Ethiopia-API/tree/master)

This is an Android api that serve the latest Ethiopian premier league standing data

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
    implementation 'com.github.brookmg:Soccer-Ethiopia-API:soccerethiopiaapi:0.3.0'
}
```

Samples
-------

* And simply fetch the latest data from your activity or fragment like:
```java
new SoccerEthiopiaApi(this).getLatestTeamRanking(ranking -> {
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
new SoccerEthiopiaApi(this).getLeagueSchedule(scheduleItems -> {
	for (LeagueScheduleItem item : scheduleItems) {
		Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + 
		" | " + item.getGameDate() + " | " + item.getGameStatus());
	}
}, error -> Log.e("Error_League" , error));
```

* Or for a specific game week (in this case the 5th game week):
```java
new SoccerEthiopiaApi(this).getLeagueSchedule( 5, scheduleItems -> {
	for (LeagueScheduleItem item : scheduleItems) {
		Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + 
		" | " + item.getGameDate() + " | " + item.getGameStatus());
	}
}, error -> Log.e("Error_League" , error));
```

* You can fetch last week's, this week's and next week's league schedule too.
```java
    new SoccerEthiopiaApi(this).getThisWeekLeagueSchedule(  //THIS WEEK'S
        scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_this_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        },
        error -> Log.e("Error_League" , error)
    );

    new SoccerEthiopiaApi(this).getLastWeekLeagueSchedule(  //LAST WEEK'S
        scheduleItems -> {
            for (LeagueScheduleItem item : scheduleItems) {
                Log.v("data_last_week" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
            }
        },
        error -> Log.e("Error_League" , error)
    );

    new SoccerEthiopiaApi(this).getNextWeekLeagueSchedule(  //NEXT WEEK'S
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
   new SoccerEthiopiaApi(this).getTeamDetail(Constants.ADAMA_KETEMA , team -> Log.v("data_team_detail" ,
        team.toString()),
        error -> Log.e("Error_Team" , error)
   );
```

* For getting the latest top players in the league
```java
	new SoccerEthiopiaApi(this).getTopPlayers(
		players -> Log.v("players" , Arrays.toString(players.toArray())),
        error -> Log.e("players_error", error));
```

* To get the top players and then to get player detail
```java
	SoccerEthiopiaApi apiEntry = new SoccerEthiopiaApi(this);
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

## Features in this lib:
- [x] Latest teams' standing data
- [x] League schedule
- [x] Team details
- [x] Player details
- [x] Top players list
- [ ] News

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
