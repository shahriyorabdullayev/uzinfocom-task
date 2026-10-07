package uz.shahriyor.uzinfocom_task.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.shahriyor.uzinfocom_task.data.JsonDataSeeder
import uz.shahriyor.uzinfocom_task.data.repository.MapRepositoryImpl
import uz.shahriyor.uzinfocom_task.data.TestDao
import uz.shahriyor.uzinfocom_task.data.db.AppDatabase
import uz.shahriyor.uzinfocom_task.domain.repository.MapRepository

val dataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "test.db")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single<TestDao> { get<AppDatabase>().testDao() }

    single { JsonDataSeeder(androidContext(), get()) }

    single<MapRepository> { MapRepositoryImpl(get()) }
}
