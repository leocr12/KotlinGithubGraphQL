package com.example.leonardoallen.strafe.feed

import com.example.leonardoallen.strafe.FeedQuery

interface FeedContract {

    interface View {
        fun setFeed(feed: MutableList<FeedQuery.FeedEntry>?)
    }

    interface Presenter {
        //fun fetchFeed()
        fun onDestroy()
    }
}