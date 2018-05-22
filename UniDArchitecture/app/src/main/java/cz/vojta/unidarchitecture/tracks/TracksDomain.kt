package cz.vojta.unidarchitecture.tracks

import io.reactivex.Observable
import io.reactivex.functions.Function
import javax.inject.Inject

class TracksDomain
@Inject constructor(val tracksApiService: TracksApiService) {
    fun tracks(key: String): Observable<List<Track>> {
        return dataObservable()
    }

    private fun dataObservable(): Observable<List<Track>> {
        val cliendId = "1af2900a66bfead15fac97d66b8796a1"
        return tracksApiService.tracks(3, cliendId = cliendId).toObservable()
    }

    fun nextPage(s: String): Observable<List<Track>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}



