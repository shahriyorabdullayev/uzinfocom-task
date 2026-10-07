package uz.shahriyor.uzinfocom_task.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.shahriyor.uzinfocom_task.data.PoiEntity
import uz.shahriyor.uzinfocom_task.data.TestDao
import uz.shahriyor.uzinfocom_task.data.TrackEntity

@Database(
    entities = [PoiEntity::class, TrackEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao
}