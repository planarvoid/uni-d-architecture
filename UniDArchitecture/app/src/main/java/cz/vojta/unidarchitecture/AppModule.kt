package cz.vojta.unidarchitecture

import cz.vojta.unidarchitecture.tracks.TracksApiService
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

const val NEW_THREAD: String = "NEW_THREAD"
const val UI_THREAD: String = "UI_THREAD"

@Module
class AppModule(internal var mApplication: MyApp) {

    @Provides
    @Singleton
    internal fun providesMyApp(): MyApp {
        return mApplication
    }

    @Provides
    internal fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.soundcloud.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    internal fun providesTrackApiService(retrofit: Retrofit): TracksApiService {
        return retrofit.create(TracksApiService::class.java)
    }

    @Provides
    @Named(NEW_THREAD)
    internal fun scheduler(): Scheduler {
        return Schedulers.newThread()
    }

    @Provides
    @Named(UI_THREAD)
    internal fun uiThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}

