package cz.vojta.unidarchitecture

import android.app.Application

class MyApp : Application() {
    val appComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}

