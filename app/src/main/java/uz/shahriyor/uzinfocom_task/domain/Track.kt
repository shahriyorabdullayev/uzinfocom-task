package uz.shahriyor.uzinfocom_task.domain

import com.google.gson.annotations.SerializedName

data class Track(
    val vehicleId: String,
    val points: List<Point>
)

data class Point(
    @SerializedName("t")
    val time: Long,
    val lat: Double,
    val lng: Double,
    val accuracy: Double,
    val speed: Double
)
