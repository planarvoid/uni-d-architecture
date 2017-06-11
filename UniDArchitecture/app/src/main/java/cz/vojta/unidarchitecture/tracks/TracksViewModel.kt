package cz.vojta.unidarchitecture.tracks

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import cz.vojta.unidarchitecture.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject


class TracksViewModel(application: Application) : AndroidViewModel(application) {
    @Inject lateinit var domain: TracksDomain
    @Inject lateinit var scheduler: Scheduler

    init {
        if (application is MyApp) {
            application.appComponent.inject(this)
        }
    }

    fun attachView(view: MainView) {
        Observable.merge(view.load(), view.refresh()).flatMap { domain.tracks("key") }
                .scan(Model()) {
                    model, partialState ->
                    model.with(partialState)
                }
                .subscribeOn(scheduler)
                .observeOn(mainThread())
                .subscribe({ (loading, refreshing, error, data) ->
                    view.setData(data)
                    view.setRefreshing(refreshing)
                    view.setLoading(loading)
                    view.setError(error)
                })
    }

    fun detachView() {
    }
}

interface MainView {
    fun setRefreshing(refreshing: Boolean)
    fun setLoading(loading: Boolean)
    fun setError(error: Throwable?)
    fun setData(data: List<Track>?)

    fun load(): Observable<Params>
    fun refresh(): Observable<Params>

    fun enterScreen(): Observable<Enter>
}
