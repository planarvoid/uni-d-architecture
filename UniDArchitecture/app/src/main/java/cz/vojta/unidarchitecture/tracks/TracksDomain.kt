package cz.vojta.unidarchitecture.tracks

import io.reactivex.Observable
import io.reactivex.functions.Function
import javax.inject.Inject

class TracksDomain @Inject constructor(val tracksApiService: TracksApiService) {
    fun tracks(key: String): Observable<LoadResult> {
        return dataObservable().onErrorReturn { Error(it) }.startWith(Loading())
    }

    private fun dataObservable(): Observable<LoadResult> {
        val cliendId = "1af2900a66bfead15fac97d66b8796a1"
        return tracksApiService.tracks(3, cliendId = cliendId).map { Data(it) as LoadResult }.toObservable()
    }
}

sealed class LoadResult
data class Loading(val refreshing: Boolean = false) : LoadResult()
data class Error(val throwable: Throwable) : LoadResult()
data class Data(val tracks: List<Track>) : LoadResult()



