package com.example.leonardoallen.strafe.feed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leonardoallen.strafe.*
import com.example.leonardoallen.strafe.detail.EntryDetailActivity
import kotlinx.android.synthetic.main.activity_feed.*


class FeedActivity : AppCompatActivity(), GitHubNavigator, FeedContract.View {

    private var strafeApplication: StrafeApplication? = null
    private var feedAdapter: FeedAdapter? = null

    private val presenter: FeedPresenter by lazy {
        FeedPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        strafeApplication = application as StrafeApplication

        feedAdapter = FeedAdapter(this)
        rvFeedList.adapter = feedAdapter
        rvFeedList.layoutManager = LinearLayoutManager(this)

        presenter.fetchFeed()
    }

    override fun setFeed(feed: MutableList<FeedQuery.FeedEntry>?) {
        feedAdapter?.setFeed(feed!!)
        loadingBar.visibility = View.GONE
        contentHolder.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun startGitHuntActivity(repoFullName: String) {
        val intent = EntryDetailActivity.newIntent(this, repoFullName)
        startActivity(intent)
    }
}
