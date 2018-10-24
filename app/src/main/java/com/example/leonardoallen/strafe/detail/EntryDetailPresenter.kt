package com.example.leonardoallen.strafe.detail

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.cache.normalized.CacheControl
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.leonardoallen.strafe.EntryDetailQuery
import com.example.leonardoallen.strafe.StrafeApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class EntryDetailPresenter(private val view: EntryDetailContract.View):
    EntryDetailContract.Presenter {

    private var apolloClient: ApolloClient = StrafeApplication().apolloClient()
    private val disposables = CompositeDisposable()

    fun fetchRepositoryDetails(repoFullName: String) {
        val entryDetailQuery = apolloClient
            .query(EntryDetailQuery(repoFullName))
            ?.cacheControl(CacheControl.CACHE_FIRST)

        disposables.add(
            Rx2Apollo.from<EntryDetailQuery.Data>(entryDetailQuery!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<EntryDetailQuery.Data>>() {
                    override fun onSuccess(dataResponse: Response<EntryDetailQuery.Data>) {
                        view.setEntryData(dataResponse.data()!!)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Entry error", e.message, e)
                    }
                })
        )
    }

    override fun onDestroy() {
        disposables.dispose()
    }

}