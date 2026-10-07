package uz.shahriyor.uzinfocom_task.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS_METERS = 6_371_000.0

fun distanceMeters(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
    val dLat = Math.toRadians(toLat - fromLat)
    val dLng = Math.toRadians(toLng - fromLng)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(fromLat)) * cos(Math.toRadians(toLat)) * sin(dLng / 2) * sin(dLng / 2)
    return 2 * EARTH_RADIUS_METERS * atan2(sqrt(a), sqrt(1 - a))
}

fun bearingDegrees(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Float {
    val lat1 = Math.toRadians(fromLat)
    val lat2 = Math.toRadians(toLat)
    val dLng = Math.toRadians(toLng - fromLng)
    val y = sin(dLng) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLng)
    return ((Math.toDegrees(atan2(y, x)) + 360) % 360).toFloat()
}

fun interpolate(from: Double, to: Double, fraction: Double): Double = from + (to - from) * fraction

fun interpolateAngle(from: Float, to: Float, fraction: Float): Float {
    val delta = ((to - from + 540f) % 360f) - 180f
    return (from + delta * fraction + 360f) % 360f
}

fun List<Point>.filterAccurate(maxAccuracyMeters: Double): List<Point> =
    filter { it.accuracy <= maxAccuracyMeters }
