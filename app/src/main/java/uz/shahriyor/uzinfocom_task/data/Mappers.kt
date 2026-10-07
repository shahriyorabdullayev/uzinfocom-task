package uz.shahriyor.uzinfocom_task.data

import uz.shahriyor.uzinfocom_task.domain.Poi
import uz.shahriyor.uzinfocom_task.domain.Point
import uz.shahriyor.uzinfocom_task.domain.Track

fun Poi.toEntity() = PoiEntity(
    id = id,
    name = name,
    category = category,
    lat = lat,
    lng = lng,
    rating = rating,
    address = address,
    openNow = openNow
)

fun PoiEntity.toDomain() = Poi(
    id = id,
    name = name,
    category = category,
    lat = lat,
    lng = lng,
    rating = rating,
    address = address,
    openNow = openNow
)

fun Track.toEntities() = points.map {
    TrackEntity(
        vehicleId = vehicleId,
        t = it.time,
        lat = it.lat,
        lng = it.lng,
        accuracy = it.accuracy,
        speed = it.speed
    )
}

fun TrackEntity.toPoint() = Point(
    time = t,
    lat = lat,
    lng = lng,
    accuracy = accuracy,
    speed = speed
)
