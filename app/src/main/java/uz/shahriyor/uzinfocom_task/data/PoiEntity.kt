package uz.shahriyor.uzinfocom_task.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poi")
data class PoiEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val rating: Double?,
    val address: String,
    val openNow: Boolean
)