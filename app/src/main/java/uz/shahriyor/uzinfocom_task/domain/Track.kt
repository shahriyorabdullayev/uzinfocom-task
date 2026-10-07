package uz.shahriyor.uzinfocom_task.domain

data class Track(
    val vehicleId: String,
    val points: List<Point>
)

data class Point(
    val time: Long,
    val lat: Double,
    val lng: Double,
    val accuracy: Double,
    val speed: Double
)
