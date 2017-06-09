package cz.vojta.unidarchitecture

import io.reactivex.Observable
import javax.inject.Inject

class MainDomain @Inject constructor () {
    fun load(key: String) : Observable<LoadResult> {
        val just: Observable<LoadResult> = Observable.just(Data())
        return just.startWith(Loading())
    }
}

interface LoadResult

class Loading : LoadResult
class Error(throwable: Throwable) : LoadResult
class Data : LoadResult



