package cz.vojta.unidarchitecture.tracks

import android.util.Log
import cz.vojta.unidarchitecture.Enter
import cz.vojta.unidarchitecture.Params
import io.reactivex.Observable


class TracksActivity : android.support.v7.app.AppCompatActivity(), MainView {
    override fun setRefreshing(refreshing: Boolean) {
        Log.d("REFRESHING", refreshing.toString())
    }

    override fun setLoading(loading: Boolean) {
        Log.d("LOADING", loading.toString())
    }

    override fun setError(error: Throwable?) {
        Log.d("ERROR", error?.toString() ?: "null")
        error?.printStackTrace()
    }

    override fun setData(data: List<Track>?) {
        Log.d("DATA", data?.toString() ?: "null")
    }

    override fun load() = Observable.just(Params())!!

    override fun refresh(): io.reactivex.Observable<Params> {
        return Observable.empty()
    }

    override fun enterScreen(): io.reactivex.Observable<Enter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cz.vojta.unidarchitecture.R.layout.activity_main)
        android.arch.lifecycle.ViewModelProviders.of(this).get(TracksViewModel::class.java!!).attachView(this)
    }

    override fun onDestroy() {
        android.arch.lifecycle.ViewModelProviders.of(this).get(TracksViewModel::class.java!!).detachView()
        super.onDestroy()
    }
}
