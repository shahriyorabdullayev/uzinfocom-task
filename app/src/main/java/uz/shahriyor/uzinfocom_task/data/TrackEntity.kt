package uz.shahriyor.uzinfocom_task.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vehicleId: String,
    val t: Long,
    val lat: Double,
    val lng: Double,
    val accuracy: Double,
    val speed: Double
)
