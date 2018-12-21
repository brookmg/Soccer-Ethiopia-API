# Soccer Ethiopia API
[![](https://jitpack.io/v/brookmg/Soccer-Ethiopia-API.svg)](https://jitpack.io/#brookmg/Soccer-Ethiopia-API)

This is an Android api that serve the latest Ethiopian premier league standing data

follow the following steps to add the dependency to your app:

* make sure to add jitpack to your repositories

``` 
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

* implement this lib

``` 
	dependencies {
	        implementation 'com.github.brookmg:Soccer-Ethiopia-API:0.1.0'
	}
```

* And simply fetch the latest data from your activity or fragment like:

```
	new SoccerEthiopiaApi(this).getLatestTeamRanking(ranking -> {
            for (RankItem item : ranking) {
                Log.v("data" , item.getTeamName() + ", " + item.getTeamIcon() + ", " + item.getRank()
                        + ", " + item.getPlayedGames() + ", " + item.getWonGames() + ", " + item.getDrawGames() 
                        + ", " + item.getLostGames()
                );
            }
        }, error -> Log.e("Error" , error));
```

# make sure you have enabled java8 in your project 
```
android {
	...
	
    compileOptions {
        sourceCompatibility = '1.8'
        targetCompatibility = '1.8'
    }
}
```