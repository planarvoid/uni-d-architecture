package cz.vojta.unidarchitecture

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(internal var mApplication: MyApp) {

    @Provides
    @Singleton
    internal fun providesMyApp(): MyApp {
        return mApplication
    }
}

