package cz.vojta.unidarchitecture.tracks

import android.arch.lifecycle.ViewModelProviders
import cz.vojta.unidarchitecture.LoadNext
import cz.vojta.unidarchitecture.MyApp
import cz.vojta.unidarchitecture.Params
import cz.vojta.unidarchitecture.Refresh
import io.reactivex.Observable


class TracksActivity : android.support.v7.app.AppCompatActivity(), MainView {
    private lateinit var viewModel: TracksViewModel
    override fun nextPage(): Observable<LoadNext> = Observable.empty<LoadNext>()

    override fun accept(t: Model<List<Track>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun firstLoad(): Observable<Params> = Observable.just(Params())

    override fun refresh(): Observable<Refresh> {
        return Observable.empty()
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cz.vojta.unidarchitecture.R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(TracksViewModel::class.java)

        if (application is MyApp) {
            (application as MyApp).appComponent.inject(viewModel)
        }
        viewModel.attachView(this)
    }

    override fun onDestroy() {
        viewModel.detachView()
        super.onDestroy()
    }
}
