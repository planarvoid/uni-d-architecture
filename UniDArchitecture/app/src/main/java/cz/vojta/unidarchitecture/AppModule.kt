package cz.vojta.unidarchitecture

import cz.vojta.unidarchitecture.tracks.TracksApiService
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


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
    internal fun scheduler(): Scheduler {
        return Schedulers.newThread()
    }
}

