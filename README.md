# Soccer Ethiopia API
[![](https://jitpack.io/v/brookmg/Soccer-Ethiopia-API.svg)](https://jitpack.io/#brookmg/Soccer-Ethiopia-API)

This is an Android api that serve the latest Ethiopian premier league standing data

## The data is fetched from [Soccer Ethiopia](http://soccerethiopia.net). And this is an unoffical api.

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
    implementation 'com.github.brookmg:Soccer-Ethiopia-API:0.2.0'
}
```

* And simply fetch the latest data from your activity or fragment like:

```java
new SoccerEthiopiaApi(this).getLatestTeamRanking(ranking -> {
        for (RankItem item : ranking) {
            Log.v("data" , item.getTeamName() + ", " + item.getTeamIcon() + ", " + item.getRank()
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
		Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
	}
}, error -> Log.e("Error_League" , error));
```

* Or for a specific game week (in this case the 5th game week):
```java
new SoccerEthiopiaApi(this).getLeagueSchedule( 5, scheduleItems -> {
	for (LeagueScheduleItem item : scheduleItems) {
		Log.v("data_league" , item.getGameWeek() + " | " + item.getGameDetail() + " | " + item.getGameDate() + " | " + item.getGameStatus());
	}
}, error -> Log.e("Error_League" , error));
```

## Features in this lib:
- [x] Latest teams' standing data
- [ ] Top players list
- [x] League schedule

## make sure you have enabled java8 in your project
 
```gradle
android {
	...
	
    compileOptions {
        sourceCompatibility = '1.8'
        targetCompatibility = '1.8'
    }
}
```