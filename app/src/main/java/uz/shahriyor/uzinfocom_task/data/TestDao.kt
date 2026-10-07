package uz.shahriyor.uzinfocom_task.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {

    @Query("SELECT * FROM poi")
    fun getPois(): Flow<List<PoiEntity>>

    @Query("SELECT * FROM track ORDER BY t")
    fun getTrack(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPois(pois: List<PoiEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(points: List<TrackEntity>)

    @Query("SELECT COUNT(*) FROM poi")
    suspend fun poiCount(): Int

    @Query("SELECT COUNT(*) FROM track")
    suspend fun trackCount(): Int
}
