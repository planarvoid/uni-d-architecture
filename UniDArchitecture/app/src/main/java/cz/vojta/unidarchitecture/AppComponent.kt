package cz.vojta.unidarchitecture

import cz.vojta.unidarchitecture.tracks.TracksViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(activity: TracksViewModel)
}

