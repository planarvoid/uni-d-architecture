package cz.vojta.unidarchitecture

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.whenever
import cz.vojta.unidarchitecture.tracks.Model
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class UniDViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    @Mock
    private lateinit var view: UniDViewModel.View<Item>
    private lateinit var viewModel: TestViewModel
    private lateinit var refreshSubject: PublishSubject<Item>
    private lateinit var nextPageSubject: PublishSubject<Item>
    private lateinit var loadSubject: PublishSubject<Item>
    private lateinit var loadTrigger: PublishSubject<Params>
    private lateinit var refreshTrigger: PublishSubject<Refresh>
    private lateinit var nextTrigger: PublishSubject<LoadNext>

    @Before
    fun setUp() {
        refreshSubject = PublishSubject.create<Item>()
        nextPageSubject = PublishSubject.create<Item>()
        loadSubject = PublishSubject.create<Item>()

        viewModel = TestViewModel(loadSubject, refreshSubject, nextPageSubject)

        loadTrigger = PublishSubject.create<Params>()
        refreshTrigger = PublishSubject.create<Refresh>()
        nextTrigger = PublishSubject.create<LoadNext>()
        whenever(view.firstLoad()).thenReturn(loadTrigger)
        whenever(view.refresh()).thenReturn(refreshTrigger)
        whenever(view.nextPage()).thenReturn(nextTrigger)
    }

    @Test
    fun `starts and loads data`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val data = Item(1)
        loadSubject.onNext(data)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(loading = false, data = data))
    }

    @Test
    fun `starts and emits error`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val error = IOException("connection failed")
        loadSubject.onError(error)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(loading = false, error = error))
    }

    @Test
    fun `starts and reemits data on change`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val data = Item(1)
        loadSubject.onNext(data)

        val updatedData = data.copy(id = 2)
        loadSubject.onNext(updatedData)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(loading = false, data = data))
        inOrder.verify(view).accept(Model(loading = false, data = updatedData))
    }

    @Test
    fun `refreshes data`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val data = Item(1)
        loadSubject.onNext(data)

        refreshTrigger.onNext(Refresh())

        val refreshedData = Item(2)
        refreshSubject.onNext(refreshedData)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(loading = false, data = data))
        inOrder.verify(view).accept(Model(refreshing = true, data = data))
        inOrder.verify(view).accept(Model(refreshing = false, data = refreshedData))
    }

    @Test
    fun `refreshes data on error`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val loadError = IOException("failure")
        loadSubject.onError(loadError)

        refreshTrigger.onNext(Refresh())

        val refreshedData = Item(2)
        refreshSubject.onNext(refreshedData)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(loading = false, error = loadError))
        inOrder.verify(view).accept(Model(refreshing = true))
        inOrder.verify(view).accept(Model(refreshing = false, data = refreshedData))
    }

    @Test
    fun `keeps loaded data on on refresh error`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val data = Item(1)
        loadSubject.onNext(data)

        refreshTrigger.onNext(Refresh())

        val refreshError = IOException("failure")
        refreshSubject.onError(refreshError)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(loading = false, data = data))
        inOrder.verify(view).accept(Model(refreshing = true, data = data))
        inOrder.verify(view).accept(Model(refreshing = false, data = data, error = refreshError))
    }

    @Test
    fun `loads next page`() {
        viewModel.attachView(view)

        val params = Params()
        loadTrigger.onNext(params)

        val firstPageCount = Item(1, 2)
        val data = firstPageCount
        loadSubject.onNext(data)

        nextTrigger.onNext(LoadNext())

        val secondPageCount = 3
        val nextPage = Item(2, secondPageCount)
        nextPageSubject.onNext(nextPage)

        val inOrder = inOrder(view)
        inOrder.verify(view).accept(Model())
        inOrder.verify(view).accept(Model(loading = true))
        inOrder.verify(view).accept(Model(data = data))
        inOrder.verify(view).accept(Model(loading = true, loadingNextPage = true, data = data))
        val totalCount = 5
        inOrder.verify(view).accept(Model(data = Item(2, totalCount)))
    }

    class TestViewModel(
            private val loadSubject: PublishSubject<Item>,
            private val refreshSubject: PublishSubject<Item>,
            private val nextPageSubject: PublishSubject<Item>
    ) : UniDViewModel<Item>() {
        override fun combineData(oldData: Item, newData: Item): Item {
            return newData.copy(itemCount = oldData.itemCount + newData.itemCount)
        }

        init {
            this.scheduler = Schedulers.trampoline()
            this.uiScheduler = Schedulers.trampoline()
        }
        override fun refresh(refreshParams: Refresh) = refreshSubject

        override fun nextPage(nextPageParams: LoadNext) = nextPageSubject

        override fun load(params: Params) = loadSubject
    }

    data class Item(val id: Int, val itemCount: Int = 0)
}