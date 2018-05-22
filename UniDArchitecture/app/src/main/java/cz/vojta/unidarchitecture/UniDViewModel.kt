package cz.vojta.unidarchitecture

import android.arch.lifecycle.ViewModel
import android.util.Log
import cz.vojta.unidarchitecture.tracks.Model
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

abstract class UniDViewModel<T> : ViewModel() {
    @Inject
    @field:Named(NEW_THREAD)
    lateinit var scheduler: Scheduler
    @Inject
    @field:Named(UI_THREAD)
    lateinit var uiScheduler: Scheduler
    protected val disposable = CompositeDisposable()

    private val viewModelDisposable = CompositeDisposable()
    private val sourceDisposable = CompositeDisposable()
    private val viewAction: PublishSubject<ViewAction> = PublishSubject.create()
    private val loader = viewAction.map {
        when (it) {
            is Params -> {
                val load = load(it).map<LoadResult<T>> { Data(it) }
                        .startWith(Loading())
                        .onErrorReturn { Error(it) }
                        .replay()
                sourceDisposable.clear()
                sourceDisposable.add(load.connect())
                it to load
            }
            is Refresh -> {
                val refresh = refresh(it).map<LoadResult<T>> { Data(it) }
                        .startWith(Refreshing())
                        .onErrorReturn { Error(it) }
                        .replay()
                sourceDisposable.clear()
                sourceDisposable.add(refresh.connect())
                it to refresh
            }
            is LoadNext -> {
                val nextPage = nextPage(it).map<LoadResult<T>> { Data(it) }
                        .startWith(LoadingNextPage())
                        .onErrorReturn { Error(it) }
                        .replay()
                sourceDisposable.add(nextPage.connect())
                it to nextPage
            }
        }
    }
            .scan(listOf<ConnectableObservable<LoadResult<T>>>()) { observables, (action, observable) ->
                when (action) {
                    is Params -> listOf(observable)
                    is Refresh -> listOf(observable)
                    is LoadNext -> observables + observable
                }
            }
            .flatMap { Observable.combineLatest(it) { t -> t.map { it as LoadResult<T> } } }
            .scan(Model<T>(), { accumulatedModel, loadResults ->
                val fold = loadResults.fold(Model<T>()) { model, partialState ->
                    model.apply(partialState)
                }
                accumulatedModel.update(fold)
            })
            .replay()

    init {
        viewModelDisposable.add(loader.connect())
    }

    fun attachView(view: View<T>) {
        disposable.addAll(
                Observable.merge(view.firstLoad(), view.refresh(), view.nextPage())
                        .subscribe { viewAction.onNext(it) },
                loader.subscribeOn(scheduler)
                        .observeOn(uiScheduler)
                        .doOnNext { System.out.println("VM: $it") }
                        .subscribe(view))
    }

    fun detachView() {
        disposable.clear()
        sourceDisposable.clear()
    }

    private fun Model<T>.update(newmodel: Model<T>): Model<T> {
        return if (newmodel.data != null) newmodel else newmodel.copy(data = data)
    }

    private fun Model<T>.apply(partialResult: LoadResult<T>): Model<T> {
        return when (partialResult) {
            is Loading -> this.copy(
                    loading = true,
                    loadingNextPage = false,
                    refreshing = false,
                    error = null
            )
            is LoadingNextPage -> this.copy(loading = true, loadingNextPage = true, error = null)
            is Refreshing -> this.copy(refreshing = true, loading = false, error = null)
            is Error -> this.copy(loading = false,
                    refreshing = false,
                    error = partialResult.throwable)
            is Data -> Model(data = combineNullableData(data, partialResult.data))
        }
    }

    abstract fun combineData(oldData: T, newData: T): T

    private fun combineNullableData(oldData: T?, newData: T?): T? {
        return if (oldData != null && newData != null) {
            combineData(oldData, newData)
        } else newData ?: oldData
    }

    abstract fun load(params: Params): Observable<T>
    abstract fun refresh(refreshParams: Refresh): Observable<T>
    abstract fun nextPage(nextPageParams: LoadNext): Observable<T>

    interface View<T> : Consumer<Model<T>> {
        fun firstLoad(): Observable<Params>
        fun refresh(): Observable<Refresh>
        fun nextPage(): Observable<LoadNext>
    }
}


sealed class LoadResult<T>
class Loading<T> : LoadResult<T>()
class LoadingNextPage<T> : LoadResult<T>()
class Refreshing<T> : LoadResult<T>()
data class Error<T>(val throwable: Throwable) : LoadResult<T>()
data class Data<T>(val data: T) : LoadResult<T>()