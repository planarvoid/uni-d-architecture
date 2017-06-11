package cz.vojta.unidarchitecture

import android.app.Application

class MyApp() : Application() {
    val appComponent: AppComponent by lazy {

        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}

