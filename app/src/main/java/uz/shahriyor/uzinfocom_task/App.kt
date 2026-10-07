package uz.shahriyor.uzinfocom_task

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import uz.shahriyor.uzinfocom_task.di.dataModule
import uz.shahriyor.uzinfocom_task.di.domainModule
import uz.shahriyor.uzinfocom_task.di.presentationModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val apiKey = BuildConfig.YANDEX_MAPKIT_API_KEY
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(this)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}
