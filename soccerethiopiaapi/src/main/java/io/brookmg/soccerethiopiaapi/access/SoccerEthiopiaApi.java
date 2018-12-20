package io.brookmg.soccerethiopiaapi.access;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import io.brookmg.soccerethiopiaapi.network.StandingFetch;

/**
 * Created by BrookMG on 12/20/2018 in io.brookmg.soccerethiopiaapi.access
 * inside the project SoccerEthiopia .
 */
public class SoccerEthiopiaApi {

    private RequestQueue mainRequestQueue;

    /**
     * Main Constructor for Soccer Ethiopia API
     * @param context - used for creating the main request queue
     */
    public SoccerEthiopiaApi(Context context) {
        mainRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Overloaded constructor in-case the user wants to use fragment as parameter
     * @param fragment - #fragment.getActivity() will be used as a context
     */
    public SoccerEthiopiaApi(Fragment fragment) {
        this(fragment.getActivity());
    }

    /**
     * Main Function to get the latest team ranking
     * @param processed - a callback to handle the processed array-list
     * @param error - a callback to handle any errors
     */
    public void getLatestTeamRanking (StandingFetch.OnStandingDataProcessed processed, StandingFetch.OnError error) {
        StandingFetch.fetchLatestStandingData(mainRequestQueue,
                response -> StandingFetch.processFetchedStandingHTML(response , processed, error),
                error);
    }

}
