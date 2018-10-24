package com.example.leonardoallen.strafe.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leonardoallen.strafe.EntryDetailQuery
import com.example.leonardoallen.strafe.R
import kotlinx.android.synthetic.main.activity_entry_detail.*


class EntryDetailActivity : AppCompatActivity(), EntryDetailContract.View {

    private var repoFullName: String? = null

    private val presenter: EntryDetailPresenter by lazy {
        EntryDetailPresenter(this)
    }

    companion object {
        private const val ARG_REPOSITORY_FULL_NAME = "arg_repo_full_name"

        fun newIntent(context: Context, repositoryFullName: String): Intent {
            val intent = Intent(context, EntryDetailActivity::class.java)
            intent.putExtra(ARG_REPOSITORY_FULL_NAME, repositoryFullName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_detail)
        repoFullName = intent.getStringExtra(ARG_REPOSITORY_FULL_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.fetchRepositoryDetails(repoFullName!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setEntryData(data: EntryDetailQuery.Data) {
        content.visibility = View.VISIBLE
        loading.visibility = View.GONE

        val entry = data.entry()
        if (entry != null) {
            name.text = entry.repository().full_name()
            description.text = entry.repository().description()
            postedBy.text = resources.getString(R.string.posted_by, entry.postedBy().login())
        } else {
            Toast.makeText(this, "No entry found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}
