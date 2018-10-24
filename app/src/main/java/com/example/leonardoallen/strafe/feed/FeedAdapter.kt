package com.example.leonardoallen.strafe.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leonardoallen.strafe.FeedQuery
import com.example.leonardoallen.strafe.GitHubNavigator
import com.example.leonardoallen.strafe.R
import java.util.*


class FeedAdapter(private val navigator: GitHubNavigator): RecyclerView.Adapter<FeedAdapter.FeedHolder>() {

    private var feed: MutableList<FeedQuery.FeedEntry> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FeedHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_github_entry, parent, false)

        return FeedHolder(itemView)
    }

    fun setFeed(feed: MutableList<FeedQuery.FeedEntry>) {
        this.feed = feed
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return feed.size
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        val feedEntry = this.feed[position]
        holder.setFeedItem(feedEntry, navigator)
    }

    class FeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val repositoryTitle: TextView
        private val feedEntryContainer: View

        init {
            repositoryTitle = itemView.findViewById(R.id.tvRepositoryName)
            feedEntryContainer = itemView.findViewById(R.id.feedEntryContainer)
        }

        fun setFeedItem(feedItem: FeedQuery.FeedEntry, navigator: GitHubNavigator) {
            val repositoryFragment = feedItem.repository().fragments().repositoryFragment()
            repositoryTitle.text = repositoryFragment!!.full_name()
            feedEntryContainer.setOnClickListener { navigator.startGitHuntActivity(repositoryFragment.full_name()) }

        }
    }
}