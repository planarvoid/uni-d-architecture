package cz.vojta.unidarchitecture

import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Observable
import javax.inject.Inject


class MainViewModel(application: MyApp) : AndroidViewModel(application) {
    @Inject lateinit var domain: MainDomain

    init {
        application.appComponent.inject(this)
    }

    fun attachView(view: MainView) {
        Observable.merge(view.load(), view.refresh()).flatMap { domain.load("key") }
    }

    fun detachView() {
    }
}

interface MainView {
    fun setRefreshing(refreshing: Boolean)
    fun setLoading(loading: Boolean)
    fun setError(error: ViewError)
    fun setData(data: List<ListItem>)

    fun load() : Observable<Params>
    fun refresh() : Observable<Params>

    fun enterScreen() : Observable<Enter>
}
