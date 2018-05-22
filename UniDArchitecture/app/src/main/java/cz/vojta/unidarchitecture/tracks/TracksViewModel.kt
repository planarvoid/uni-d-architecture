package cz.vojta.unidarchitecture.tracks

import cz.vojta.unidarchitecture.LoadNext
import cz.vojta.unidarchitecture.Params
import cz.vojta.unidarchitecture.Refresh
import cz.vojta.unidarchitecture.UniDViewModel
import io.reactivex.Observable
import javax.inject.Inject


class TracksViewModel : UniDViewModel<List<Track>>() {
    @Inject
    lateinit var domain: TracksDomain

    override fun load(params: Params): Observable<List<Track>> {
        return domain.tracks("first page")
    }

    override fun refresh(refreshParams: Refresh): Observable<List<Track>> {
        return domain.tracks("first page")
    }

    override fun nextPage(nextPageParams: LoadNext): Observable<List<Track>> {
        return domain.nextPage("next page")
    }

    override fun combineData(oldData: List<Track>, newData: List<Track>): List<Track> {
        return oldData + newData
    }
}

interface MainView : UniDViewModel.View<List<Track>> {
}