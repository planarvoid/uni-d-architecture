package cz.vojta.unidarchitecture

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable


class MainActivity : AppCompatActivity(), MainView {
    override fun load(): Observable<Params> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refresh(): Observable<Params> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enterScreen(): Observable<Enter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRefreshing(refreshing: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLoading(loading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setError(error: ViewError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setData(data: List<ListItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewModelProviders.of(this).get(MainViewModel::class.java!!).attachView(this)
    }

    override fun onDestroy() {
        ViewModelProviders.of(this).get(MainViewModel::class.java!!).detachView()
        super.onDestroy()
    }
}
