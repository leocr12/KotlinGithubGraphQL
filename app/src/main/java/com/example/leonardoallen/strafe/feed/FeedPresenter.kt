package com.example.leonardoallen.strafe.feed

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloCallback
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.cache.normalized.CacheControl
import com.apollographql.apollo.exception.ApolloException
import com.example.leonardoallen.strafe.FeedQuery
import com.example.leonardoallen.strafe.StrafeApplication
import com.example.leonardoallen.strafe.type.FeedType

class FeedPresenter(private val view: FeedContract.View): FeedContract.Presenter {

    private val FEED_SIZE = 5
    private var apolloClient: ApolloClient = StrafeApplication().apolloClient()
    private var githubFeedCall: ApolloCall<FeedQuery.Data>? = null
    private var uiHandler = Handler(Looper.getMainLooper())

    fun fetchFeed() {
        val feedQuery = FeedQuery.builder()
            .limit(FEED_SIZE)
            .type(FeedType.HOT)
            .build()
        githubFeedCall = apolloClient
            .query(feedQuery)
            ?.cacheControl(CacheControl.NETWORK_FIRST)
        githubFeedCall?.enqueue(dataCallback)
    }

    private val dataCallback = ApolloCallback<FeedQuery.Data>(object : ApolloCall.Callback<FeedQuery.Data>() {
        override fun onResponse(response: Response<FeedQuery.Data>) {
            view.setFeed(response.data()?.feedEntries())
        }

        override fun onFailure(e: ApolloException) {
            Log.e("Feed error: ", e.message, e)
        }
    }, uiHandler)

    override fun onDestroy() {
        if (githubFeedCall != null) {
            githubFeedCall?.cancel()
        }
    }
}