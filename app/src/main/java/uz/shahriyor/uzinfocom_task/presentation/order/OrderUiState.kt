package uz.shahriyor.uzinfocom_task.presentation.order

import uz.shahriyor.uzinfocom_task.domain.Point

data class OrderUiState(
    val isPlaying: Boolean = false,
    val speedMultiplier: Int = 1,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val car: CarState? = null,
    val traveledPath: List<Point> = emptyList(),
    val isSignalLost: Boolean = false,
    val followCar: Boolean = true
)

data class CarState(
    val lat: Double,
    val lng: Double,
    val bearing: Float,
    val speedMps: Double,
    val accuracyMeters: Double
)
